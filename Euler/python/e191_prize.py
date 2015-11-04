#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                             e191_prize.py                             |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-30                                                     |
 +----------------------------------------------------------------------'''

import itertools

def nonlates():
    y = (1,0,0)
    while True:
        yield y
        y = (y[0]+y[1]+y[2], y[0], y[1])

n=30
gs = [x+y+z for (x, y, z) in itertools.islice(nonlates(), n+1)]
print gs[n] + sum([gs[i]*gs[n-i-1] for i in xrange(0, n)])
