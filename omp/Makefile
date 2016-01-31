SRCS=$(wildcard *.c)
EXES=$(SRCS:.c=)

all: $(EXES)

%: %.c
	gcc -fopenmp -o $@ $<

clean:
	rm -f $(EXES)
