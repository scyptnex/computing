TARGETS=main codTest
SRC_DIR=src
BIN_DIR=bin
BUILD_DIR=build
INCLUDES=-Iinclude -Iinclude/libtcod -Llib
LIBS=-ltcod -ltcodxx

DEP_DIR=$(BUILD_DIR)/deps
OBJ_DIR=$(BUILD_DIR)/objs

CXXFLAGS=$(INCLUDES) -Wl,-rpath=. -Wall
SRCS=$(wildcard $(SRC_DIR)/*.cpp)
OBJS=$(patsubst $(SRC_DIR)/%.cpp,$(OBJ_DIR)/%.o,$(SRCS))
DEPS=$(patsubst $(SRC_DIR)/%.cpp,$(DEP_DIR)/%.P,$(SRCS))
BINS=$(patsubst %,$(BIN_DIR)/%,$(TARGETS))
SYMLINKS=$(patsubst lib/%.so,$(BIN_DIR)/%.so.1,$(wildcard lib/*.so))

OBJ_MAINS=$(patsubst %,$(OBJ_DIR)/%.o,$(TARGETS))
OBJ_NONMAINS=$(filter-out $(OBJ_MAINS), $(OBJS))

all: $(OBJS) $(BINS) $(SYMLINKS)

$(BIN_DIR)/%.so.1 : lib/%.so
	@[ -d $(BIN_DIR) ] || mkdir -p $(BIN_DIR)
	ln -s `readlink -f $<` $@

$(BIN_DIR)/%: $(OBJ_DIR)/%.o $(OBJ_NONMAINS)
	@[ -d $(BIN_DIR) ] || mkdir -p $(BIN_DIR)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LIBS)

$(OBJ_DIR)%.o : $(SRC_DIR)/%.cpp
	@[ -d $(DEP_DIR) ] || mkdir -p $(DEP_DIR) $(OBJ_DIR)
	$(CXX) $(CXXFLAGS) -MMD -c -o $@ $<
	@cp $(OBJ_DIR)/$*.d $(DEP_DIR)/$*.P; sed -e 's/#.*//' -e 's/^[^:]*: *//' -e 's/ *\\$$//' -e '/^$$/ d' -e 's/$$/ :/' < $(OBJ_DIR)/$*.d >> $(DEP_DIR)/$*.P; rm -f $(OBJ_DIR)$*.d

clean:
	rm -rf $(DEP_DIR) $(OBJ_DIR) $(BUILD_DIR) $(BIN_DIR)

-include $(DEPS)
