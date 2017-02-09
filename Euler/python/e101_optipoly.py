"""
+-------------------------------------------------------------------------+
|                            e101_optipoly.py                             |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-09                                                       |
+-------------------------------------------------------------------------+
"""

def get_term(n):
    return 1 - n + n**2 - n**3 + n**4 - n**5 + n**6 - n**7 + n**8 - n**9 + n**10

def make_det(leng):
    return ([[(i+1)**j for j in xrange(0, leng)] for i in xrange(0, leng)], [get_term(i+1) for i in xrange(0, leng)])

def find_coeff(det):
    (coeffs, solu) = det
    leng = len(solu)
    for take in xrange(0, leng-1):
        for give in xrange(take+1, leng):
            factor = coeffs[give][take]/coeffs[take][take]
            coeffs[give] = [coeffs[give][i]-factor*coeffs[take][i] for i in xrange(0, leng)]
            solu[give] = solu[give]-factor*solu[take]
    for tk in xrange(1, leng):
        take = leng-tk
        solu[take] = solu[take]/coeffs[take][take]
        coeffs[take][take] = 1
        for give in xrange(0, take):
            factor = coeffs[give][take]/coeffs[take][take]
            coeffs[give] = [coeffs[give][i]-factor*coeffs[take][i] for i in xrange(0, leng)]
            solu[give] = solu[give]-factor*solu[take]
    return solu

def get_predicted(idx, solu):
    tot=0
    for i, x in enumerate(solu):
        tot += x*(idx**i)
    return tot

def get_fit(solu):
    for i in xrange(1, len(solu)+3):
        pred = get_predicted(i, solu)
        if get_term(i) != pred:
            return pred
    return 0

sm=0
for i in xrange(1, 20):
    sm += get_fit(find_coeff(make_det(i)))
print sm

