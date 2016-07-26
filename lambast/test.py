import sys
from antlr4 import *
from utlLexer import utlLexer
from utlParser import utlParser

def main(argv):
    input = FileStream(argv[1])
    lexer = utlLexer(input)
    stream = CommonTokenStream(lexer)
    parser = utlParser(stream)
    tree = parser.expr()
    print(tree.toStringTree(recog=parser))

if __name__ == '__main__':
    main(sys.argv)
