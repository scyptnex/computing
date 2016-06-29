
TARGETS=$(patsubst %.cpp,%.x,$(wildcard *.cpp))
ASSEMBL=$(patsubst %.x,%.s,$(TARGETS))

all: $(ASSEMBL) $(TARGETS)

%.x: %.s
	$(CXX) -o $@ $<

%.s: %.cpp $(wildcard *.h)
	$(CXX) --std=c++11 -O3 -S $<

clean:
	rm -f $(TARGETS) $(ASSEMBL)
