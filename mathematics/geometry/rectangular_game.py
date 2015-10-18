#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                          rectangular_game.py                          |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Oct-18                                                     |
 +----------------------------------------------------------------------'''

print reduce(lambda a, b: a*b, reduce(lambda c, a: [min(c[0], a[0]), min(c[1], a[1])], [[int(j) for j in raw_input().split(" ")] for i in xrange(0, int(raw_input()))]))
