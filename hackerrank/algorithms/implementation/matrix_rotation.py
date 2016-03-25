#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                          matrix_rotation.py                           |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-19                                                     |
 +----------------------------------------------------------------------'''

(m, n, rot) = map(int, raw_input().split(" "))
mat = [map(int, raw_input().split(" ")) for i in xrange(0, m)]
rails = [[mat[rail][c] for c in xrange(rail, n-rail)]+[mat[r][n-rail-1] for r in xrange(rail+1, m-rail)]+[mat[m-rail-1][c] for c in xrange(n-rail-2, rail, -1)]+[mat[r][rail] for r in xrange(m-rail-1, rail, -1)] for rail in xrange(0, min(m/2, n/2))]
rots = map(lambda a: a[rot%len(a):]+a[:rot%len(a)], rails)
out = [[0 for j in xrange(0, n)] for i in xrange(0, m)]
for (ri, rail) in enumerate(rots):
    idx=0
    for c in xrange(ri, n-ri):
        out[ri][c] = rail[idx]
        idx+=1
    for r in xrange(ri+1, m-ri-1):
        out[r][n-ri-1] = rail[idx]
        idx+=1
    for c in xrange(n-ri-1, ri, -1):
        out[m-ri-1][c] = rail[idx]
        idx+=1
    for r in xrange(m-ri-1, ri, -1):
        out[r][ri] = rail[idx]
        idx+=1
print "\n".join([" ".join(map(str, l)) for l in out])

