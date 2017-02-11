"""
+-------------------------------------------------------------------------+
|                           e106_specialmeta.py                           |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

def is_inter(ia, ib):
    if ia[0] < ib[0]:
        tmp = ia
        ia = ib
        ib = tmp
    for i, x in enumerate(ia):
        if x < ib[i]:
            return True
    return False


def thingo(l):
    count = 0
    for part in xrange(1, 3**l):
        p = part
        ca = 0
        cb = 0
        ia = []
        ib = []
        for i in xrange(l):
            if p%3 == 1:
                ca += 1
                ia.append(i)
            elif p%3 == 2:
                if ca == 0:
                    break
                cb += 1
                ib.append(i)
            p = p//3
        if ca == 0 or cb == 0:
            continue
        if ca == cb and ca > 1 and is_inter(ia, ib):
            #print ptn
            count += 1
    print count

print [0,1,2,3,4,5,6,7][:3:-1]

thingo(4)
thingo(7)
thingo(12)
