TARGETS=eg1 eg2
SRC_DIR=src
INCLUDE_DIR=include
BIN_DIR=bin
BUILD_DIR=build

DEP_DIR=$(BUILD_DIR)/deps
OBJ_DIR=$(BUILD_DIR)/objs

CXXFLAGS=-I $(INCLUDE_DIR) -O0
SRCS=$(wildcard $(SRC_DIR)/*.cc)
OBJS=$(patsubst $(SRC_DIR)/%.cc,$(OBJ_DIR)/%.o,$(SRCS))
DEPS=$(patsubst $(SRC_DIR)/%.cc,$(DEP_DIR)/%.P,$(SRCS))
BINS=$(patsubst %,$(BIN_DIR)/%,$(TARGETS))

OBJ_MAINS=$(patsubst %,$(OBJ_DIR)/%.o,$(TARGETS))
OBJ_NONMAINS=$(filter-out $(OBJ_MAINS), $(OBJS))

all: $(OBJS) $(BINS)

$(BIN_DIR)/%: $(OBJ_DIR)/%.o $(OBJ_NONMAINS)
	@[ -d $(BIN_DIR) ] || mkdir -p $(BIN_DIR)
	$(CXX) $(CXXFLAGS) -o $@ $^

$(OBJ_DIR)%.o : $(SRC_DIR)/%.cc
	@[ -d $(DEP_DIR) ] || mkdir -p $(DEP_DIR) $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -MMD -c -o $@ $<
	@cp $(OBJ_DIR)/$*.d $(DEP_DIR)/$*.P; sed -e 's/#.*//' -e 's/^[^:]*: *//' -e 's/ *\\$$//' -e '/^$$/ d' -e 's/$$/ :/' < $(OBJ_DIR)/$*.d >> $(DEP_DIR)/$*.P; rm -f $(OBJ_DIR)$*.d

clean:
	rm -rf $(DEP_DIR) $(OBJ_DIR) $(BUILD_DIR) $(BIN_DIR)

-include $(DEPS)
