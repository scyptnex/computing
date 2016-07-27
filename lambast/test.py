import sys
from antlr4 import *
from untyped_expressionLexer import untyped_expressionLexer
from untyped_expressionParser import untyped_expressionParser

def main(argv):
    input = FileStream(argv[1])
    lexer = untyped_expressionLexer(input)
    stream = CommonTokenStream(lexer)
    parser = untyped_expressionParser(stream)
    tree = parser.expr()
    print(tree.toStringTree(recog=parser))

if __name__ == '__main__':
    main(sys.argv)
