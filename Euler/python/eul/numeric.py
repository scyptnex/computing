"""
+-------------------------------------------------------------------------+
|                               numeric.py                                |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

def is_special(s):
    """this was used for several problems, find if every non overlapping subset's sum is nonequal and larger for more elements"""
    for part in xrange(1, 3**len(s)):
        p = part
        sa = 0
        ca = 0
        sb = 0
        cb = 0
        for i, x in enumerate(s):
            if p%3 == 1:
                sa += x
                ca += 1
            elif p%3 == 2:
                sb += x
                cb += 1
            p = p//3
        if ca == 0 or cb == 0:
            continue
        if sa == sb:
            return False
        if ca > cb and sa <= sb:
            return False
        if cb > ca and sb <= sa:
            return False
    return True
