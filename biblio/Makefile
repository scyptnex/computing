debug:
	bison -o parser.cpp parser.y
	flex -o scanner.cpp scanner.l
	g++ --std=c++11 -o debug *.cpp

clean:
	rm -f grammar_*.*pp stack.hh lex.yy.c a.out debug release
	rm -f parser.cpp parser.hpp scanner.cpp stack.hh

