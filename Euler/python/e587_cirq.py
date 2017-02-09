"""
+-------------------------------------------------------------------------+
|                              e587_cirq.py                               |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Jan-25                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import sys
import getopt
import math
from fractions import Fraction

class coord:

    def __init__(self):
        self.whole=Fraction()
        self.scoef=Fraction()
        self.surd=0

    def __str__(self):
        return "%d/%d - %d(%d)^0.5/%d" % (self.whole.numerator, self.whole.denominator, self.scoef.numerator, self.surd, self.scoef.denominator)

    def realise(self):
        return (self.whole.numerator*self.scoef.denominator - self.scoef.numerator*self.whole.denominator*(self.surd**0.5))/(self.whole.denominator*self.scoef.denominator)

def getY(n):
    ret = coord()
    ret.whole = Fraction(n+1, 2*n*n+2)
    ret.scoef = Fraction(1, 2*n*n+2)
    ret.surd = 2*n
    return ret

def scale(s, c):
    ret = coord()
    ret.whole = c.whole * s
    ret.scoef = c.scoef * s
    ret.surd = c.surd
    return ret

def add(a, c):
    ret = coord()
    ret.whole = c.whole + a
    ret.scoef = c.scoef
    ret.surd = c.surd
    return ret

class e587_cirq:
    
    def __init__(self):
        for n in xrange(1, 50000):
            y = getY(n)
            tnm = add(Fraction(1,2), scale(-n, y))
            tdn = add(Fraction(1,2), scale(-1, y))
            theta = math.atan(tnm.realise()/tdn.realise())
            pre = add(1, scale(2-2*n, y))
            a8 = pre.realise() - theta
            prop = (2*a8) / (4-math.pi)
            if prop < 0.001:
                print n, "=", prop
                break

if __name__ == "__main__":
    e587_cirq()
