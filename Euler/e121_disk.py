#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                             e121_disk.py                              |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-27                                                     |
 +----------------------------------------------------------------------'''

def gcd(a, b):
    if b > a:
        return gcd(b, a)
    if b == 0:
        return a
    return gcd(b, a%b)

def red(n, d):
    return (n/gcd(n, d), d/gcd(n, d))

def sum(na, da, nb, db):
    return red(da*nb + na*db, da*db)

# of drawing exactly b blues and r reds after cur turns
# turn reds blues prob
# 0    1    1     0.5
# 1    2    1     0.33
# 2    3    1     0.25
def p(r, b, cur):
    rprob = (0,1)
    bprob = (0,1)
    if r == 0 and b == 0:
        return (1, 1)
    # prob of drawing a red
    if r > 0:
        (rn, rd) = p(r-1, b, cur+1)
        rprob = ((cur+1)*rn, (cur+2)*rd)
    # prob of blue
    if b > 0:
        (bn, bd) = p(r, b-1, cur+1)
        bprob = (bn, (cur+2)*bd)
    return sum(bprob[0], bprob[1], rprob[0], rprob[1])

def probs(n):
    return reduce(lambda x, y: sum(x[0], x[1], y[0], y[1]), [p(n-i, i, 0) for i in xrange((n/2) +1, n+1)])

def pay(f):
    n = f[0]
    d = f[1]
    return d/n

#for i in xrange(3, 16):
#    print i, probs(i)
print pay(probs(4))
print pay(probs(15))
