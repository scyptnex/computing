all: test.exe pascall.exe

test.exe: main.cpp TestClass.cpp TestClass.h
	g++ -Wall -W -pedantic main.cpp TestClass.cpp -o test.exe

pascall.exe: pascall.cc
	g++ pascall.cc -o pascall.exe

clean:
	rm -f pascall.exe test.exe
	rm -f *~
