main: main.s
	g++ -o main main.s

main.s: main.cpp
	g++ --std=c++11 -O3 -S main.cpp

clean:
	rm -f main.s main
