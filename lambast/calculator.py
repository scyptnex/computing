#! /usr/bin/env python

"""
+-------------------------------------------------------------------------+
|                              calculator.py                              |
|                                                                         |
| Author: Nic H.                                                          |
| Date: 2016-Jul-27                                                       |
|                                                                         |
| A lambda calculator                                                     |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import sys
import os
import getopt
import antlr4
from untyped_expressionLexer import untyped_expressionLexer
from untyped_expressionParser import untyped_expressionParser
from untyped_expressionVisitor import untyped_expressionVisitor

#=======#
# Terms #
#=======#
class Var:
    def __init__(self, n):
        self.name = n
    def __str__(self):
        return self.name

class Exp:
    def __init__(self, h, b):
        if not isinstance(h, Var):
            raise
        self.head = h
        self.body = b
    def __str__(self):
        return "(\\" + str(self.head) + "." + str(self.body) + ")"

class App:
    def __init__(self, l, r):
        self.lhs = l
        self.rhs = r
    def __str__(self):
        return "(" + str(self.lhs) + " " + str(self.rhs) + ")"

def lambdaString(l):
    #if isinstance(l, Var):
    #    return str(l)
    #elif isinstance(l, Exp):
    #    sub = lambdaString(l.body)
    #    if sub[0] == '\\':
    #        return '\\' + lambdaString(l.head) + " " + sub[1:]
    #    else:
    #        return '\\' + lambdaString(l.head) + "." + sub
    #else:
    #    r = lambdaString(l.rhs)
    #    if isinstance(l.rhs, App):
    #        return lambdaString(l.lhs) + " (" + r + ")"
    #    else:
    #        return lambdaString(l.lhs) + " " + r
    return str(l)

#==============#
# Construction #
#==============#
class Constructor( untyped_expressionVisitor, antlr4.error.ErrorListener.ErrorListener ):

    def readExpr(self, text):
        inp = antlr4.InputStream(text)
        lexer = untyped_expressionLexer(inp)
        lexer.removeErrorListeners()
        lexer.addErrorListener(self)
        stream = antlr4.CommonTokenStream(lexer)
        parser = untyped_expressionParser(stream)
        parser._listeners = [self]
        return self.visit(parser.start())

    #== Visitors ==#

    def visitNonempty(self, ctx):
        return self.visit(ctx.expr())

    def visitEmpty(self, ctx):
        return None

    def visitUnit(self, ctx):
        return self.visit(ctx.term())

    def visitApplication(self, ctx):
        return App(self.visit(ctx.lhs), self.visit(ctx.rhs)) 

    def visitVariable(self, ctx):
        return self.visit(ctx.var())

    def visitFunction(self, ctx):
        cur = self.visit(ctx.term())
        for v in ctx.var()[::-1]:
            cur = Exp(self.visit(v), cur)
        return cur

    def visitSubexpression(self, ctx):
        return self.visit(ctx.expr())

    def visitVar(self, ctx):
        return Var(ctx.getText())

    #== Errors ==#

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        raise Exception(str(line) + ":" + str(column) + ": syntax ERROR, " + str(msg))

    def reportAmbiguity(self, recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs):
        sys.exit()

    def reportAttemptingFullContext(self, recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs):
        sys.exit()

    def reportContextSensitivity(self, recognizer, dfa, startIndex, stopIndex, prediction, configs):
        sys.exit()

#=========================#
# Analysis/Transformation #
#=========================#
class Analyser:

    def __init__(self, verb, defs):
        self.verbose = verb
        self.defs = defs
        self.lmb = None

    def bound_free(self, term):
        if isinstance(term, Var):
            return (set(), set([term.name]))
        elif isinstance(term, Exp):
            (hb, hf) = bound_free(term.head)
            (bb, bf) = bound_free(term.body)
            return (bb | hf, bf - hf)
        else:
            (lb, lf) = bound_free(term.lhs)
            (rb, rf) = bound_free(term.rhs)
            return (lb|rb, lf|rf) # this only works if lhs and rhs dont share variable names

    def name_of(self, count, is_free):
        if is_free:
            return "f%d" % (count - 11) if count > 10 else chr(ord('a')+count)
        return "b%d" % (count - 11) if count > 10 else chr(ord('z')-count)

    def substitute(self, term=None):
        if not term:
            if self.verbose: print "substitution:"
            self.lmb = self.substitute(self.lmb)
            if self.verbose: print "  ", lambdaString(self.lmb)
            return
        if isinstance(term, Var):
            if self.defs.has_key(term.name):
                if(self.verbose): print "    ", term.name, lambdaString(self.defs[term.name])
                return self.defs[term.name]
            return term
        elif isinstance(term, Exp):
            return Exp(self.substitute(term.head), self.substitute(term.body))
        else:
            return App(self.substitute(term.lhs), self.substitute(term.rhs))


    def alpha(self, term=None,names=None,bound=0,free=0):
        if not term:
            if self.verbose: print "renaming:"
            (self.lmb, b, f) = self.alpha(self.lmb,{})
            if self.verbose: print "  ", lambdaString(self.lmb)
            return
        if isinstance(term, Var):
            if names.has_key(term.name):
                return (Var(names[term.name]), bound, free)
            else:
                return (Var(self.name_of(free, True)), bound, free+1)
        elif isinstance(term, Exp):
            names[term.head.name] = self.name_of(bound, False)
            (l1, b1, f1) = self.alpha(term.head, names, bound+1, free)
            (l2, b2, f2) = self.alpha(term.body, names, b1, f1)
            return (Exp(l1, l2), b2, f2)
        else:
            (l1, b1, f1) = self.alpha(term.lhs, names, bound, free)
            (l2, b2, f2) = self.alpha(term.rhs, names, b1, f1)
            return (App(l1, l2), b2, f2)

    def beta(self, term=None, subVar=None, subExp=None):
        if not term:
            if self.verbose: print "application:"
            self.lmb = self.beta(self.lmb, None, None)
            if self.verbose: print "  ", lambdaString(self.lmb)
            return
        if subVar:
            if isinstance(term, Var):
                return subExp if term.name == subVar else term
            elif isinstance(term, Exp):
                return Exp(self.beta(term.head, subVar, subExp), self.beta(term.body, subVar, subExp))
            else:
                return App(self.beta(term.lhs, subVar, subExp), self.beta(term.rhs, subVar, subExp))
        else:
            if isinstance(term, Var):
                return term
            elif isinstance(term, Exp):
                return Exp(term.head, self.beta(term.body, None, None))
            else:
                l = self.beta(term.lhs, None, None)
                r = self.beta(term.rhs, None, None)
                if isinstance(l, Exp):
                    if self.verbose: print "    ", l.head.name, "=>", r, ":",
                    ret = self.beta(l.body, l.head.name, r)
                    if self.verbose: print lambdaString(ret)
                    return self.beta(ret, None, None)
                else:
                    return App(l, r)

    def simplify(self, lambd):
        self.lmb = lambd
        if self.verbose: print "simplifying:\n  %s" %str(self.lmb)
        self.substitute()
        self.alpha()
        self.beta()
        return self.lmb

#============#
# Calculator #
#============#
class Calculator:

    def __init__(self):
        self.defs = {}
        self.buf = ""
        self.vrb = False
        self.nam = ""
        self.asg = False

    def _help(self):
        if self.vrb:
            print "Variables:"
            for (k, v) in self.defs.items():
                print "    " + k + " = " + str(v)
        else:
            print "Commands:"
            print "    ?                            display this help message"
            print "    @?                           list the defined variables"
            print "    <lambda expression> ?        compute the lambda expression"
            print "    <lambda expression> @?       verbosely compute the lambda expression"
            print "    <lambda expression> = <var>  define a variable"
            print "    <lambda expression> @= <var> show working when defining a variable"

    def _execute(self):
        if len(self.buf.strip()) > 0:
            lmb = Constructor().readExpr(self.buf)
            lmb = Analyser(self.vrb, self.defs).simplify(lmb)
            if self.asg :
                self.defs[self.nam] = lmb
            else:
                print lmb
        else:
            self._help()
        defs = self.defs
        self.__init__()
        self.defs = defs

    def use(self, text):
        for char in text:
            if self.asg:
                if char in " \t\n":
                    if len(self.nam) > 0 :
                        self._execute()
                else:
                    self.nam += char
            elif char == '?':
                self._execute()
            elif char == '@':
                self.vrb = True
            elif char == '=':
                self.asg = True
            else:
                self.buf += char

if __name__ == "__main__":
    calc = Calculator()
    interpret = False
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hi", ["help", "interpreter"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o in ("-i", "--interpreter"):
            interpret = True
    for f in args:
        if not os.path.exists(f) or not os.path.isfile(f):
            print "Argument \"" + str(f) + "\" is not a file."
            sys.exit(1)
    for fn in args:
        with open(fn, 'r') as fi:
                for line in fi:
                    calc.use(line)
    if interpret or len(args) == 0:
        print "type \"?\" for help."
        line = sys.stdin.readline()
        while line:
            calc.use(line)
            line = sys.stdin.readline()

