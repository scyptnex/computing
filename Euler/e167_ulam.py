#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                             e167_ulam.py                              |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-27                                                     |
 +----------------------------------------------------------------------'''

def bf_ulam(a, b, k):
    cur = [a, b]
    while len(cur) < k:
        tmp = sorted([cur[i]+cur[j] for i in xrange(0, len(cur)) for j in xrange(i+1, len(cur))])
        tmp = [i for i in tmp if i > cur[-1]]
        nw = []
        for x in tmp:
            if len(nw) != 0 and nw[-1][0] == x:
                nw[-1] = (nw[-1][0], nw[-1][1]+1)
            else:
                nw += [(x, 1)]
        tmp = [x for x in nw if x[1] == 1]
        cur += [tmp[0][0]]
    return cur


print bf_ulam(2, 5,  25)
print bf_ulam(2, 7,  25)
print bf_ulam(2, 9,  25)
print bf_ulam(2, 11, 25)

print [i for i in bf_ulam(2, 5,  100) if i%2 == 0]
