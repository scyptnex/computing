"""
+-------------------------------------------------------------------------+
|                                 misc.py                                 |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

def is_pandigital_str(s):
    """returns true if the string contains all the digits 1-9"""
    tot = 0
    zer = ord('0')
    for c in [c for c in s if c.isdigit()]:
        tot |= (1<<(ord(c) - zer))
    return tot == (1<<10)-2


