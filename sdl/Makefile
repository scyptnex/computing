LIBS=-lSDL -lSDL_image -lGL -lGLU
SOURCES=$(wildcard *.cpp)
EXECS=$(subst .cpp,.exe,${SOURCES})

all: ${EXECS}

%.exe: %.cpp
	g++ $< -o $(subst .cpp,.exe,$<) ${LIBS}

clean:
	rm -f ${EXECS} *~
