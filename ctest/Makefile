TARGS=test.exe pascall.exe simple.exe

all: $(TARGS)

test.exe: main.cpp TestClass.cpp TestClass.h
	g++ -Wall -W -pedantic main.cpp TestClass.cpp -o test.exe

pascall.exe: pascall.cc
	g++ pascall.cc -o pascall.exe

simple.exe: simple.c
	gcc `pkg-config --cflags glfw3` -o $@ $< `pkg-config --static --libs glfw3` 

clean:
	rm -f $(TARGS)
	rm -f *~
