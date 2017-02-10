"""
+-------------------------------------------------------------------------+
|                              e102_tris.py                               |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-10                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import math

import eul.geom as geom
import eul.filer as filer

def same_side(p1, p2, a, b):
    """http://blackpawn.com/texts/pointinpoly/"""
    p1 = p1 + [0]
    p2 = p2 + [0]
    a = a + [0]
    b = b + [0]
    cp1 = geom.cross_product(geom.pairwise_sub(b, a), geom.pairwise_sub(p1, a))
    cp2 = geom.cross_product(geom.pairwise_sub(b, a), geom.pairwise_sub(p2, a))
    return geom.dot_product(cp1, cp2) >= 0

def contains_origin(a, b, c):
    """http://blackpawn.com/texts/pointinpoly/"""
    o = [0,0]
    return same_side(o, a, b, c) and same_side(o, b, a, c) and same_side(o, c, a, b)

count = 0
for ln in filer.stream_delimited_lines("E102.txt", ","):
    coords = [int(s) for s in ln]
    if contains_origin(coords[0:2], coords[2:4], coords[4:6]):
        count += 1
print count
