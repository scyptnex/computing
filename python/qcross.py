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

"""
/-\
| |
\_/

\ /
 X 
/ \
"""

def subRepl(sub, pos, source):
    return source[:pos] + sub + source[pos+len(sub):]

def printClassic(row, col, nought, brd):
    prt = ["/-\\", "| |", "\\-/"] if nought else ["\\ /", " X ", "/ \\"]
    brd[4*row + 0] = subRepl(prt[0], 3+9*col, brd[4*row + 0])
    brd[4*row + 1] = subRepl(prt[1], 3+9*col, brd[4*row + 1])
    brd[4*row + 2] = subRepl(prt[2], 3+9*col, brd[4*row + 2])

def printQuantum(row, col, nought, idx, brd):
    prt = "o" if nought else "x"
    brd[4*row + idx//3] = subRepl(prt + str(idx), 9*col + 3*(idx%3), brd[4*row + idx//3])

def printBoard():
    brd = [ ("-"*8 + "+")*2 + "-"*8 if (i%4 == 3) else (" "*8 + "|")*2 + " "*8 for i in xrange(0,11)]
    for line in brd: print line
    printClassic(0, 0, True, brd)
    printClassic(0, 2, False, brd)
    printQuantum(1, 1, False, 3, brd)
    printQuantum(1, 1, True, 2, brd)
    print
    for line in brd: print line


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
