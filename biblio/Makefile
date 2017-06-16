debug:
	bison -o grammar_parse.cpp *.y
	flex -o grammar_scan.cpp *.l
	g++ --std=c++11 -o debug *.cpp

clean:
	rm -f grammar_*.*pp stack.hh lex.yy.c a.out debug release

