SRCS = src/eg1.cc src/eg2.cc

all: $(SRCS:.cc-.P)

%.P : %.cc
	g++ -MM -MF $@ $<
	#@sed 's/\($*\)\.o[ :]*/\1.o $@ : /g' < $*.d > $@; rm -f $*.d; [ -s $@ ] || rm -f $@

# include $(SRCS:.c=.P)
