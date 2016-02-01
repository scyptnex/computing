SRCS=$(wildcard *.c)
EXES=$(SRCS:.c=)

all: $(EXES)

%: %.c
	gcc -Wall -fopenmp -o $@ $<

clean:
	rm -f $(EXES)
