BINARY = my_program

PRODUCTION_CFLAGS = 
PRODUCTION_DIR = build
DEVELOPMENT_CFLAGS = -g -G -deviceemu
DEVELOPMENT_DIR = debug

ifeq ($(shell hostname | grep -q '^ucpu' && echo yes || echo no), yes)
	CUDA_DIR = /local/usr/cuda2.3
else 
	CUDA_DIR = /opt/cuda
endif

NVCC = $(CUDA_DIR)/bin/nvcc
NVCC_CFLAGS = -I$(CUDA_DIR)/include --compiler-options -W
NVCC_LDFLAGS = -L$(CUDA_DIR)/lib64

DIRS = $(PRODUCTION_DIR) $(DEVELOPMENT_DIR)
SOURCES = $(shell find . -name "*.cu")
PRODUCTION_OBJECTS  = $(shell find . -name "*.cu" | sed 's@^\./\(.*\)\.cu@$(PRODUCTION_DIR)/\1.o@g')
DEVELOPMENT_OBJECTS = $(shell find . -name "*.cu" | sed 's@^\./\(.*\)\.cu@$(DEVELOPMENT_DIR)/\1.o@g')
PRODUCTION_BINARY = $(PRODUCTION_DIR)/$(BINARY)
DEVELOPMENT_BINARY = $(DEVELOPMENT_DIR)/$(BINARY)

.PHONY: all clean depends production development

# phony targets
all: $(DIRS) $(DEVELOPMENT_BINARY) $(PRODUCTION_BINARY)

production: $(PRODUCTION_DIR) $(PRODUCTION_BINARY)

development: $(DEVELOPMENT_DIR) $(DEVELOPMENT_BINARY)

clean:
	-rm -f $(PRODUCTION_BINARY) $(PRODUCTION_OBJECTS) 
	-rm -f $(DEVELOPMENT_BINARY) $(DEVELOPMENT_OBJECTS)

depends:
	$(NVCC) $(NVCC_CFLAGS) -M $(SOURCES) > Makefile.depends

# real targets
$(PRODUCTION_DIR)/%.o: %.cu
	$(NVCC) $(NVCC_CFLAGS) $(PRODUCTION_CFLAGS) -c -o $@ $<

$(PRODUCTION_BINARY): $(PRODUCTION_OBJECTS)
	$(NVCC) $(NVCC_LDFLAGS) -o $@ $^

$(PRODUCTION_DIR):
	mkdir -p $(PRODUCTION_DIR)

$(DEVELOPMENT_DIR)/%.o: %.cu
	$(NVCC) $(NVCC_CFLAGS) $(DEVELOPMENT_CFLAGS) -c -o $@ $<

$(DEVELOPMENT_BINARY): $(DEVELOPMENT_OBJECTS)
	$(NVCC) $(NVCC_LDFLAGS) -o $@ $^

$(DEVELOPMENT_DIR):
	mkdir -p $(DEVELOPMENT_DIR)

# dependencies
-include Makefile.depends

