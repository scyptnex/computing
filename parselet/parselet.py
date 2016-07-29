"""
+-------------------------------------------------------------------------+
|                               parselet.py                               |
|                                                                         |
| Author: Nic H.                                                          |
| Date: 2016-Jul-29                                                       |
|                                                                         |
| simple implementation of an arithmetic expression parser                |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import re

NUMS = re.compile("^[0-9]*$")

class Scannie:

    def __init__(self, expr):
        self.s = expr
        self.i = 0
        self.skip()

    def skip(self):
        """advance the index past whitespace (which is irrelevant)"""
        while self.s[self.i:self.i+1].isspace():
            self.i += 1

    def go(self):
        """determine the result of the full expression"""
        ret = self.expression()
        if not self.ended():
            raise Exception("Unrecognised trailing characters")
        return ret

    def ended(self):
        """returns true when there are no more tokens to scan"""
        return self.i >= len(self.s)

    def get(self, advance):
        """Returns the next part of the input, usually a single character, but for numbers returns the stream of numbers"""
        if self.ended():
            if advance:
                raise Exception("Unexpected end of input")
            else:
                return None
        else:
            start = self.i
            end = start+1
            while end < len(self.s) and NUMS.match(self.s[start:end+1]):
                end += 1
            if advance:
                self.i = end
                self.skip()
            return self.s[start:end]
   
    def unit(self):
        """unit -> "-" unit | [0-9]* | "(" expr ")" """
        nxt = self.get(True)
        if nxt == "-":
            return -1 * self.unit()
        elif nxt == "(":
            ret = self.expression()
            if self.get(True) == ")":
                return ret
            else:
                raise Exception("Syntax error")
        elif NUMS.match(nxt):
            return int(nxt)
        else:
            raise Exception("Unexpected syntax: %s" % nxt)


    def term(self):
        """term -> unit (("*" | "/") term)"""
        u1 = self.unit()
        nxt = self.get(False)
        if nxt == "*":
            self.get(True)
            return u1 * self.term()
        elif nxt == "/":
            self.get(True)
            return u1 / self.term()
        else:
            return u1

    def expression(self):
        """expression -> term (("+" | "-") expression)?"""
        t1 = self.term()
        nxt = self.get(False)
        if nxt == "+":
            self.get(True)
            return t1 + self.expression()
        elif nxt == "-":
            self.get(True)
            return t1 - self.expression()
        else:
            return t1

def solve(exp):
    return Scannie(exp).go()

def parselet():
    print("type an arithmetic expression:")
    expr = input()
    print("Expression: %s" % expr)
    print("    actual: %d" % solve(expr))
    print("  expected: %d" % eval(expr))

if __name__ == "__main__":
    parselet()
