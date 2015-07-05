#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                               avger.py                                |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Mar-18                                                     |
 +----------------------------------------------------------------------'''

__doc__ = """avger
This is the Docstring"""

import sys
import getopt

def avger():
    maxKept = 5

    try:
        opts, args = getopt.getopt(sys.argv[1:], "ht:", ["help"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o == "-t":
            maxKept = int(a)
    
    kept = []
    for line in sys.stdin.readlines():
        sortedNums = sorted(map(float, line.split()))[:maxKept]
        print sum(sortedNums)/float(len(sortedNums))

if __name__ == "__main__":
    avger()
