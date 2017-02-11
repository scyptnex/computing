"""
+-------------------------------------------------------------------------+
|                           e105_specialsubs.py                           |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import eul.filer as filer
import eul.numeric as numeric

tot = 0
for ln in filer.stream_delimited_lines("E105.txt", ","):
    nums = [int(s) for s in ln]
    if numeric.is_special(nums):
        print nums
        tot += sum(nums)
print tot
