"""
+-------------------------------------------------------------------------+
|                               lambast.py                                |
|                                                                         |
| Author: Nic H.                                                          |
| Date: 2016-Jul-25                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import sys
import getopt

from expression import *

def lmbda(head, body):
    if not isinstance(head, list):
        return lmbda([head], body)
    elif len(head) > 0:
        return Exp(Var(head[0]), lmbda(head[1:], body))
    elif not isinstance(body, list):
        return lmbda([], [body])
    elif len(body) > 1:
        return App(lmbda([], body[:-1]), lmbda([], body[-1]))
    else:
        return Var(body[0]) if isinstance(body[0], str) else body[0]

def _is_same(a, b, cura, curb):
    if isinstance(a, Var):
        if not isinstance(b, Var):
            return False
        elif cura.has_key(a.name) and curb.has_key(b.name):
            return cura[a.name] == curb[b.name]
        elif not cura.has_key(a.name) and not curb.has_key(b.name):
            cura[a.name] = len(cura)
            curb[b.name] = len(curb)
            return True
        else:
            return False
    elif isinstance(a, Exp):
        if not isinstance(b, Exp):
            return False
        else:
            return _is_same(a.head, b.head, cura, curb) and _is_same(a.body, b.body, cura, curb)
    else:
        if not isinstance(b, App):
            return False
        else:
            return _is_same(a.lhs, b.lhs, cura, curb) and _is_same(a.rhs, b.rhs, cura, curb)

def is_same(a, b):
    return _is_same(a, b, {}, {})


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

def alpha(term, old, new):
    if isinstance(term, Var):
        return Var(new if term.name == old else term.name)
    elif isinstance(term, Exp):
        return Exp(alpha(term.head, old, new), alpha(term.body, old, new))
    else:
        return App(alpha(term.lhs, old, new), alpha(term.rhs, old, new))

def find_alpha(term):
    if isinstance(term, Exp):
        return Exp(term.head, find_alpha(term.body))
    elif isinstance(term, App):
        l = find_alpha(term.lhs)
        r = find_alpha(term.rhs)
        (lb, lf) = bound_free(l)
        (rb, rf) = bound_free(r)
        for b in (lb & rb):
            nm = 1
            while b + str(nm) in lb or b + str(nm) in rb:
                nm += 1
            l = alpha(l, b, b+str(nm))
        return App(l, r)
    return term

def beta(term, v, f):
    if isinstance(term, Var):
        return f if term.name == v else term
    elif isinstance(term, Exp):
        return Exp(term.head, beta(term.body, v, f))
    else:
        return App(beta(term.lhs, v, f), beta(term.rhs, v, f))

def find_beta(term):
    if isinstance(term, Var):
        return term
    elif isinstance(term, Exp):
        return Exp(term.head, find_beta(term.body))
    elif isinstance(term.lhs, Exp):
        return beta(term.lhs.body, term.lhs.head.name, term.rhs)
    else:
        return App(find_beta(term.lhs), find_beta(term.rhs))

def describe(term, indent=0):
    print "  "*indent,
    if isinstance(term, Var):
        print term.name
    elif isinstance(term, Exp):
        print "\\", term.head.name, "."
        describe(term.body, indent+1)
    else:
        print "APP"
        describe(term.lhs, indent+1)
        describe(term.rhs, indent+1)

def simplify(term, verb=False):
    old = ""
    while str(term) != old:
        old = str(term)
        if(verb): print " - " + old
        term = find_alpha(term)
        if str(term) != old:
            continue
        term = find_beta(term)
    return term

def lambast():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "h", ["help"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)

    tr = readExpr("\\x y . x")
    fa = readExpr("\\x y . y")
    no = readExpr("\\f a b .(f b a)")

    print tr, fa
    print no

    notfalse = simplify(App(no, fa), True)
    print notfalse
    print is_same(tr, fa)
    print is_same(tr, tr)
    print is_same(tr, notfalse)

    print

    zero = fa
    print "ZERO=", zero
    succ = lmbda(["x", "y", "z"], ["y", lmbda([], ["x", "y", "z"])])
    print "SUCC=",succ
    one = simplify(App(succ, zero), True)
    print "ONE =", one
    # print
    # two = simplify(App(succ, one), True)
    # print two
    # print
    # two2 = simplify(App(App(succ, succ), zero), True)
    # print two2
    # print
    # two2 = simplify(App(succ, App(succ, zero)), True)
    # print two2
    print
    two = simplify(App(succ, App(succ, zero)))
    print two

    add = lmbda(["n", "f", "x"], ["f", lmbda([], ["n", "x"])])
    print add
    addTwo = simplify(App(add, two), True)
    print
    four = simplify(App(addTwo, two), True)
    fourp = lmbda([], [succ, succ, zero])
    print fourp

    print
    print
    nums = [simplify(reduce(lambda a, b : App(succ, a), range(0, i), zero)) for i in range(0,20)]
    for n in nums:
        print str(n)
    CHK=readExpr("\\m n f x .(m f(n f x))")
    ADD=readExpr("\\n f x . (f(n f x))")
    MUL=readExpr("\\m n f . (m(n f))")
    adr = lambda x, y : simplify(App(App(CHK, x), y))
    print str(adr(nums[0], nums[0]))
    print str(adr(nums[0], nums[1]))
    print str(adr(nums[1], nums[0]))
    print str(adr(nums[1], nums[1]))
    print str(adr(nums[3], nums[4]))
    print str(adr(nums[6], nums[2]))
    print str(adr(nums[4], nums[4]))

if __name__ == "__main__":
    lambast()
