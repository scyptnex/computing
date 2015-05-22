#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                               deploy.py                               |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-May-22                                                     |
 |                                                                       |
 | Deploy the usydweb website to lib/html directory, Python version      |
 +----------------------------------------------------------------------'''

__doc__ = """deploy
This is the Docstring"""

import sys
import os
import getopt
from subprocess import call

def deploy():
    source = "./in"
    dest = os.path.expanduser("~") + "/lib/html"
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hs:d:", ["help"])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o in ("-s"):
            source = a
        elif o in ("-d"):
            dest = a
    print source, dest
    print os.path.isdir(source), os.path.isdir(dest)

if __name__ == "__main__":
    deploy()
