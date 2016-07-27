import sys
from antlr4 import *
from untypedLexer import untypedLexer
from untypedParser import untypedParser

def main(argv):
    input = FileStream(argv[1])
    lexer = untypedLexer(input)
    stream = CommonTokenStream(lexer)
    parser = untypedParser(stream)
    tree = parser.program()
    print(tree.toStringTree(recog=parser))

if __name__ == '__main__':
    main(sys.argv)
