debug:
	bison -t -d *.ypp
	flex -d *.l
	g++ --std=c++11 -o debug *.cpp lex.yy.c

clean:
	rm -f *.tab.cpp *.tab.hpp stack.hh lex.yy.c a.out debug release

