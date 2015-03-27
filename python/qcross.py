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

/-\     \ /
| |  &   X
\_/     / \ 

Options:
    -h --help        Print help message
    -m --moves <arg> Make <arg> moves before play

How to play:
Play or observe quantum noughts-and-crosses pieces into the board.  The
squares are named:
    q w e
    a s d
    z x c
Onscreen prompts are displayed, telling you who is doing what.
When a cycle of quantum pieces is placed, the opposing player must observe
which superposition is collapsed, the 'Classical Winner' is determined by
having a line of three collapsed pieces in any direction.
In the event that one chain of observations causes multiple winners, the player
who placed their pieces earlier is declared the 'Quantum Winner'.
"""

import sys
import getopt
import re
import itertools

def errorPrint(msg):
    """Print "ERROR, <msg>" to stderr"""
    sys.stderr.write("ERROR, " + msg + "\n")

def errorExit(msg):
    """Print <msg> to stderr and exit with code 1"""
    errorPrint(msg)
    sys.exit(1)

def subRepl(sub, pos, source):
    """return a string with the substring <sub> overwritten into <source> at position <pos>"""
    return source[:pos] + sub + source[pos+len(sub):]

def depthFirstDuplicate(edges, known, current, previous):
    """
    Perform bi-directed depth-first search on <edges>, maintaining <known> visited items
    <current> is the vertex i am at, <previous> is the most revent vertex
    """
    if known[current]:
        return True
    known[current] = True
    for oth in edges[current]:
        if oth != previous and depthFirstDuplicate(edges, known, oth, current):
            return True
    return False

class Board:
    """Class holding the state of the quantum board"""

    def __init__(self):
        self.state = [["","",""],["","",""],["","",""]]
        self.moveIdx = 0
        self.moveIsFirst = True
        self.moveIsCollapse = False
        self.moveHistory = ""
        self.quantumVictors = ""
        self.classicVictors = ""

    def __str__(self):
        return ",".join([",".join(a) for a in self.state])

    def isNoughtMove(self):
        return self.moveIdx%2 == 0

    def move(self, square):
        """
        Make a move in <square> on the board
        Moves are qweasdzxc and correspond to squares in reading-order
        If a move is invalid, prints an error and returns without changing the state
        """
        if self.classicVictors:
            errorPrint("The game is already over")
            return
        validMoves = "qweasdzxc"
        if(not square in validMoves):
            errorPrint("Move is not a square: " + square)
            errorPrint("Choose:   q w e")
            errorPrint("          a s d")
            errorPrint("          z x c")
            return
        r = validMoves.index(square)/3
        c = validMoves.index(square)%3
        if(len(self.state[r][c]) == 1):
            errorPrint("Square is taken: (%d,%d)" % (r,c))
            return
        if self.moveIsCollapse:
            r1, c1, r2, c2 = self._getCollapseMoves()
            if not (r,c) in ((r1, c1), (r2, c2)):
                errorPrint("Observing move must be in the opponent's previous square: (%d, %d) or (%d, %d)" % (r1, c1, r2, c2))
                return
            self._observe(self.moveIdx-1, r, c)
            self._finaliseClassic()
            self.moveIsCollapse = False
        else:
            m = ('o' if self.isNoughtMove() else 'x') + str(self.moveIdx)
            if self.moveIsFirst:
                self.state[r][c] += m
                self.moveIsFirst = False
            else:
                if self.state[r][c].find(m) != -1:
                    errorPrint("Quantum piece already present in square: (%d,%d)" % (r,c))
                    return
                self.state[r][c] += m
                self.moveIsFirst = True
                self.moveIdx += 1
                if self._hasCycle():
                    self.moveIsCollapse = True
        self.moveHistory += square

    def getMoveMessage(self):
        """Returns a string which instructs the player(s) on what to do next"""
        curPlayer = "O" if self.moveIdx%2 == 0 else "X"
        if self.moveIsCollapse:
            return curPlayer + ", observe your opponent's %s%d move" % ("x" if self.moveIdx%2 == 0 else "o", self.moveIdx-1)
        elif self.moveIsFirst:
            return curPlayer + ", Make your first %s%d half-move" % ("o" if self.moveIdx%2 == 0 else "x", self.moveIdx)
        else:
            return curPlayer + ", Make your second %s%d half-move" % ("o" if self.moveIdx%2 == 0 else "x", self.moveIdx)

    def prettyPrint(self):
        """Pretty print the board to stdout"""
        brd = [ ("-"*8 + "+")*2 + "-"*8 if (i%4 == 3) else (" "*8 + "|")*2 + " "*8 for i in xrange(0,11)]
        for r,c in itertools.product(xrange(0,3), repeat=2):
            if (len(self.state[r][c]) == 0):
                pass
            elif (self.state[r][c] == "O"):
                self._makeClassic(r, c, True, brd)
            elif (self.state[r][c] == "X"):
                self._makeClassic(r, c, False, brd)
            else:
                for mv in [self.state[r][c][i:i+2] for i in xrange(0,len(self.state[r][c]),2)]:
                    self._makeQuantum(r, c, mv[0] == 'o', int(mv[1]), brd)
        for line in brd: print line

    def _makeClassic(self, row, col, nought, brd):
        """Draw the classical piece onto <brd> at <row>,<col>. If <nought> draw Nought, otherwise draw Cross"""
        prt = ["/-\\", "| |", "\\-/"] if nought else ["\\ /", " X ", "/ \\"]
        brd[4*row + 0] = subRepl(prt[0], 3+9*col, brd[4*row + 0])
        brd[4*row + 1] = subRepl(prt[1], 3+9*col, brd[4*row + 1])
        brd[4*row + 2] = subRepl(prt[2], 3+9*col, brd[4*row + 2])
    
    def _makeQuantum(self, row, col, nought, idx, brd):
        """Draw the quantum piece index <idx> onto <brd> at <row>,<col>. If <nought> draw Nought, otherwise draw Cross"""
        prt = "o" if nought else "x"
        brd[4*row + idx//3] = subRepl(prt + str(idx), 9*col + 3*(idx%3), brd[4*row + idx//3])

    def _getCollapseMoves(self):
        """return the potential moves of the opponent as (r1, c1, r2, c2)"""
        first = ()
        for r,c in itertools.product(xrange(0,3), repeat=2):
            if self.state[r][c].find(str(self.moveIdx-1)) != -1:
                if first:
                    return (first[0], first[1], r, c)
                else:
                    first = (r,c)
        return (-1, -1, -1, -1)

    def _observe(self, idx, row, col):
        """Recursively observe that move <idx> ocurred at <row>,<col>"""
        tmp = self.state[row][col]
        self.state[row][col] = tmp.upper()[tmp.find(str(idx))-1] + str(idx)
        for r,c in itertools.product(xrange(0,3), repeat=2):
            has = self.state[r][c].find(str(idx))
            if has != -1 and ( r != row or c != col):
                self.state[r][c] = self.state[r][c][:has-1] + self.state[r][c][has+1:]
            for prop in [int(tmp[i]) for i in xrange(1, len(tmp), 2)]:
                if prop != idx and self.state[r][c].find(str(prop)) != -1:
                    self._observe(prop, r, c)

    def _finaliseClassic(self):
        """Incrementally add the newly classicalised pieces, checking for a victor along the way"""
        for idx in xrange(0,9):
            for r,c in itertools.product(xrange(0,3), repeat=2):
                if self.state[r][c].find(str(idx)) != -1 and self.state[r][c][0] in ("O", "X"):
                    self.state[r][c] = self.state[r][c][0]
                    self.classicVictors = self._getVictors()
                    if self.classicVictors and not self.quantumVictors:
                        self.quantumVictors = self.classicVictors

    def _getVictors(self):
        statestr =  [self.state[a][b] for b in xrange(3) for a in xrange(3)] + [self.state[b][a] for b in xrange(3) for a in xrange(3)] + [self.state[r][r] for r in xrange(3)] + [self.state[r][2-r] for r in xrange(3)]
        wins = ""
        for piece in ("X", "O"):
            for i in xrange(0, len(statestr), 3):
                if statestr[i] == piece and statestr[i+1] == piece and statestr[i+2] == piece:
                    wins = wins + piece
        return wins


    def _hasCycle(self):
        """return True when the board has a cycle amongst its quantum pieces"""
        edges = [[] for x in range(9)]
        for idx in xrange(9):
            fnd = -1
            for search in xrange(9):
                if self.state[search/3][search%3].find(str(idx)) != -1:
                    if fnd == -1:
                        fnd = search
                    else:
                        edges[search] += [fnd]
                        edges[fnd] += [search]
                        break
        for squ in xrange(9):
            seenSquares = [False for i in xrange(9)]
            if depthFirstDuplicate(edges, seenSquares, squ, -1):
                return True
        return False

def qcross():

    preMoves = []

    board = Board()

    # patt = re.compile("(o|x)[1-8]")
    # for a in sys.argv:
    #     print a, patt.match(a)
    # sys.exit(0)

    try:
        opts, args = getopt.getopt(sys.argv[1:], "hm:", ["help","moves="])
    except getopt.error, msg:
        print msg
        print "for help use --help"
        sys.exit(2)
    for o, a in opts:
        if o in ("-h", "--help"):
            print __doc__
            sys.exit(0)
        elif o in ("-m", "--moves"):
            preMoves = a
        else:
            print "unrecognised option ", o
            sys.exit(2)

    for m in preMoves:
        board.move(m)

    while True:
        board.prettyPrint()
        if board.classicVictors:
            break
        print board.getMoveMessage()
        mov = sys.stdin.readline()
        print
        if mov:
            board.move(mov[0])
        else:
            break
    print "Game completed"
    print "Moves:", board.moveHistory
    print "State:", board
    print "Quantum victors:", board.quantumVictors
    print "Classic victors:", board.classicVictors

if __name__ == "__main__":
    qcross()
