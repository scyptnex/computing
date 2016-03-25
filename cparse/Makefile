SRC_DIR=src
INCLUDES=-I include/
BIN_DIR=bin
BUILD_DIR=build

DEP_DIR=$(BUILD_DIR)/deps
OBJ_DIR=$(BUILD_DIR)/objs
CXXFLAGS=$(INCLUDES) -std=c++11 -O3

SRCS=$(wildcard $(SRC_DIR)/*.cpp)
TARGETS=$(patsubst $(SRC_DIR)/%.cpp,%,$(SRCS))
OBJS=$(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SRCS))
DEPS=$(patsubst $(SRC_DIR)/%.cpp,$(DEP_DIR)/%.P,$(SRCS))
BINS=$(patsubst %,$(BIN_DIR)/%,$(TARGETS))

OBJ_MAINS=$(patsubst %,$(OBJ_DIR)/%.o,$(TARGETS))
OBJ_NONMAINS=$(filter-out $(OBJ_MAINS), $(OBJS))

all: $(OBJS) $(BINS)

$(BIN_DIR)/%: $(OBJ_DIR)/%.o $(OBJ_NONMAINS)
	@[ -d $(BIN_DIR) ] || mkdir -p $(BIN_DIR)
	$(CXX) $(CXXFLAGS) -o $@ $^

$(OBJ_DIR)%.o : $(SRC_DIR)/%.cpp
	@[ -d $(DEP_DIR) ] || mkdir -p $(DEP_DIR) $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -MMD -c -o $@ $<
	@cp $(OBJ_DIR)/$*.d $(DEP_DIR)/$*.P; sed -e 's/#.*//' -e 's/^[^:]*: *//' -e 's/ *\\$$//' -e '/^$$/ d' -e 's/$$/ :/' < $(OBJ_DIR)/$*.d >> $(DEP_DIR)/$*.P; rm -f $(OBJ_DIR)$*.d

clean:
	rm -rf $(DEP_DIR) $(OBJ_DIR) $(BUILD_DIR) $(BIN_DIR)

-include $(DEPS)
