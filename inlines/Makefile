
TARGETS=$(patsubst %.cpp,%.x,$(wildcard *.cpp))
ASSEMBL=$(patsubst %.x,%.s,$(TARGETS))

all: $(ASSEMBL) $(TARGETS)

%.x: %.s
	$(CXX) -o $@ $<

%.s: %.cpp $(wildcard *.h)
	$(CXX) --std=c++11 -O3 -S $<
	$(CXX) --std=c++11 -O0 -S -o $<.0.s $<

clean:
	rm -f $(TARGETS) $(ASSEMBL) $(patsubst %.cpp,%.cpp.0.s,$(wildcard *.cpp))
