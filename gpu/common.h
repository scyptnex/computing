#ifndef COMMON_H
#define COMMON_H

float *
create_matrix_h(unsigned int w, unsigned int h);

void
fill_matrix_h(float *const m, unsigned int w, unsigned int h, float *const values, unsigned int nvalues);

void
print_matrix_h(float *const m, unsigned int w, unsigned int h, FILE *f);

float *
create_matrix_d(unsigned int w, unsigned int h);


#endif
