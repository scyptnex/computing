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

    def bound_free(term):
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

    def _execute(self):
        if len(self.buf.strip()) > 0:
            lmb = Constructor().readExpr(self.buf)
            if self.asg :
                self.defs[self.nam] = lmb
            else:
                print lmb
        else:
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
        if len(args) == 0: print "type \"?\" for help."
        line = sys.stdin.readline()
        while line:
            calc.use(line)
            line = sys.stdin.readline()

