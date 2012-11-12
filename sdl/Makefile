SOURCES=$(wildcard *.cpp)
EXECS=$(subst .cpp,.exe,${SOURCES})

all: ${EXECS}

%.exe: %.cpp
	g++ $< -o $(subst .cpp,.exe,$<) -lSDL -lSDL_image

clean:
	rm -f ${EXECS} *~
