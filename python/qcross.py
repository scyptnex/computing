#! /usr/bin/env python

'''----------------------------------------------------------------------+ 
 |                               qcross.py                               |
 |                                                                       |
 | Author: Nic H.                                                        |
 | Date: 2015-Mar-18                                                     |
 |                                                                       |
 | Quantum Noughts and Crosses                                           |
 +----------------------------------------------------------------------'''

__doc__ = """Quantum Noughts and Crosses

The fun way to tic-tac and toe

Options:
    -h --help        Print help message
    -s --state <arg> Initialises the game to state <arg>
    -m --moves <arg> Make <arg> moves before play"""

import sys
import getopt

def printBoard():
    for i in xrange(0,11):
        if (i % 4 == 3):
            print ("-"*8 + "+")*2 + "-"*8
        else:
            print (" "*8 + "|")*2

def qcross():
    try:
        opts, args = getopt.getopt(sys.argv[1:], "hs:m:", ["help","state=","moves="])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o in ("-s", "--state"):
            print "STATE ", a
        else:
            print "unrecognised option ", o
            sys.exit(2)
    printBoard()

if __name__ == "__main__":
    qcross()
