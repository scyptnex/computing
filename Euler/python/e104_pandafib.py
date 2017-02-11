"""
+-------------------------------------------------------------------------+
|                            e104_pandafib.py                             |
|                                                                         |
| Author: nic                                                             |
| Date: 2017-Feb-11                                                       |
+-------------------------------------------------------------------------+
"""

__doc__ = __doc__.strip()

import math
import eul.misc as misc

def checkr(num):
    lstr = str(num)
    return misc.is_pandigital_str(lstr[:9]) and misc.is_pandigital_str(lstr[-9:])

def checkrb(num):
    b = 0
    for d in xrange(1, 10):
        b |= 1<<(num%10)
        num = num//10
    return b == (1<<10)-2


print misc.is_pandigital_str("123456789")

low = 1
high = 1
f_low = 1

while True:
    if checkrb(low) and checkr(low):
        print f_low#, low
        #going = False
        break
    tmp = high
    high = low + high
    low = tmp
    f_low += 1
    if f_low%10000 == 0:
        print "%d" % (f_low,)

