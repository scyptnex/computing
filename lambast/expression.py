"""
+-------------------------------------------------------------------------+
|                              expression.py                              |
|                                                                         |
| Author: Nic H.                                                          |
| Date: 2016-Jul-27                                                       |
|                                                                         |
| Defines the structure of a lambda expression and provides miscelaneous  |
| utilities to modify it                                                  |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

# parsing
import antlr4
from untyped_expressionLexer import untyped_expressionLexer
from untyped_expressionParser import untyped_expressionParser
import untyped_expressionVisitor

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

class _parse_visitor( untyped_expressionVisitor.untyped_expressionVisitor ):

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

class _custom_error_handler( antlr4.error.ErrorListener.ErrorListener ):

    def syntaxError(self, recognizer, offendingSymbol, line, column, msg, e):
        raise Exception(str(line) + ":" + str(column) + ": syntax ERROR, " + str(msg))

    def reportAmbiguity(self, recognizer, dfa, startIndex, stopIndex, exact, ambigAlts, configs):
        sys.exit()

    def reportAttemptingFullContext(self, recognizer, dfa, startIndex, stopIndex, conflictingAlts, configs):
        sys.exit()

    def reportContextSensitivity(self, recognizer, dfa, startIndex, stopIndex, prediction, configs):
        sys.exit()

def readExpr(expr_string):
    errs = _custom_error_handler()
    inp = antlr4.InputStream(expr_string)
    lexer = untyped_expressionLexer(inp)
    lexer.removeErrorListeners()
    lexer.addErrorListener(errs)
    stream = antlr4.CommonTokenStream(lexer)
    parser = untyped_expressionParser(stream)
    parser._listeners = [errs]
    return _parse_visitor().visit(parser.start())

if __name__ == "__main__":
    import sys
    line = sys.stdin.readline()
    while line:
        print readExpr(line)
        line = sys.stdin.readline()

