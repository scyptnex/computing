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

def contains_origin(a, b, c):
    return abs(geom.angle_between(a, b)) + abs(geom.angle_between(b, c)) >= math.pi

print contains_origin([-340,495], [-153,-910], [835,-947])
print contains_origin([-175,41], [-421,-714], [574,-645])

for ln in filer.stream_delimited_lines("E102.txt", ","):
    print ln
