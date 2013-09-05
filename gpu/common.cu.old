#include <stdio.h>
#include <stdlib.h>
#include "common.h"

float *
create_matrix_h(unsigned int w, unsigned int h) {
  float *m;
  m = (float *) malloc(w * h * sizeof(float));
  if (m == NULL) {
    fprintf(stderr, "Failed to malloc.\n");
    exit(1);
  }
  return m;
}


void
fill_matrix_h(float *const m, unsigned int w, unsigned int h, float *const values, unsigned int nvalues) {
  unsigned int i, j = 0;
  for (i = 0; i != w * h; ++i) {
    m[i] = values[j];
    j = (j + 1) % nvalues;
  }
}


void
print_matrix_h(float *const m, unsigned int w, unsigned int h, FILE *f) {
  unsigned int x, y;
  float *ptr = m;

  for (y = 0; y != h; ++y) {
    for (x = 0; x != w; ++x) 
      fprintf(f, "%.2f ", *ptr++);
    fprintf(f, "\n");
  }
}


float *
create_matrix_d(unsigned int w, unsigned int h) {
  float *m;
  if (cudaMalloc(&m, w * h * sizeof(float)) == cudaErrorMemoryAllocation) {
    fprintf(stderr, "Failed to cudaMalloc.\n");
    exit(1);
  }
  return m;
}

