"""
+-------------------------------------------------------------------------+
|                                 geom.py                                 |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-10                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

def pairwise_mult(a, b):
    """return the pair-wise multiplication of two lists of numbers"""
    return [a[i]*b[i] for i in xrange(0, min(len(a), len(b)))]

def dot_product(a,b):
    """return the dot product (sum of pairwise multiplication) of two lists of numbers"""
    return sum(pairwise_mult(a,b))

def magnitude(a):
    """return the length of the vector represented by this list of numbers"""
    return dot_product(a, a)**0.5

def angle_between(a, b):
    """return the angle (radians) between the two vectors"""
    from math import acos
    return acos( dot_product(a, b) / (magnitude(a) * magnitude(b)) )
