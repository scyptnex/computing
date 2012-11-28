/******************************************************
File Name
[matrixmul.cu]
Synopsis
[This file defines the main function to do
matrix-matrixmultiplication.]
Description []
*******************************************************/
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
// Included C libraries
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <math.h>
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
// Included CUDA libraries
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
#include <cutil.h>
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
// Included helper functions
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
#include "assist.h"
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
// Included host matrix-matrix multiplication function prototype
//––––––––––––––––––––––––––––––––––––––––––––––––––––––
#include "matrixmul.h"
int
main(int argc, char** argv)
{
bool if_quiet = false;
unsigned int timer_compute = 0;
int i, j;
char * matrix_id = NULL, * input_fn = NULL, * gold_fn = NULL;
int Mw = 0, Mh = 0, Nw = 0, Nh = 0, Pw = 0, Ph = 0;
if (argc == 2) {
matrix_id = strdup(argv[1]);
} else {
fprintf(stderr, "Error: Wrong input parameter numbers.\n");
fprintf(stderr, "Usage:\n"
"$> ./lab1.1-matrixmul <8, 128, 512, 3072, 4096>\n"
"Examples:\n"
" $> ./lab1.1-matrixmul 128\n"
);
exit(1);
}
Mw = Mh = Nw = Nh = Pw = Ph = atoi(matrix_id);
input_fn = (char *) malloc(30*sizeof(char));
gold_fn = (char *) malloc(30*sizeof(char));
sprintf(input_fn, "matrix_%s.bin", matrix_id);
sprintf(gold_fn, "matrix_%s.gold", matrix_id);
if (Pw*Ph > 15*15) {
if_quiet = true; // If not display matrix contents
}
printf("Input matrix size: %d by %d\n", Mw, Mh);
//––––––––––––––––––––––––––––––––––––––––––––––––––––
// Setup host side
//––––––––––––––––––––––––––––––––––––––––––––––––––––
printf("Setup host side environment:\n");
// allocate host memory for matrices M and N
printf(" Allocate host memory for matrices M and N.\n");
printf(" M: %d x %d\n", Mw, Mh);
printf(" N: %d x %d\n", Nw, Nh);
unsigned int size_M = Mw * Mh;
unsigned int mem_size_M = sizeof(float) * size_M;
float* hostM = (float*) malloc(mem_size_M);
unsigned int size_N = Nw * (Nh);
unsigned int mem_size_N = sizeof(float) * size_N;
float* hostN = (float*) malloc(mem_size_N);
// allocate memory for the result on host side
printf(" Allocate memory for the result on host side.\n");
unsigned int size_P = Pw * Ph;
unsigned int mem_size_P = sizeof(float) * size_P;
float* hostP = (float*) malloc(mem_size_P);
// Initialize the input matrices.
printf(" Generate input matrix data for matrix M and N.\n");
GenMatrixFile(input_fn, Pw, Ph, if_quiet);
unsigned int * matrix = ReadMatrixFile(input_fn, Pw, Ph, true);
for (i = 0; i < Mw; i++)
for (j = 0; j < Nw; j++)
hostM[i * Mw + j] = hostN[i * Mw + j] = (float)matrix[i*Mw + j];
free(matrix); matrix = NULL;
//========================================
// Do matrix-matrix multiplication
//========================================
printf(" Computing matrix multiplication M x N:\n");
if (Pw*Ph > 512*512) {
printf(" (It takes time since matrix is larger than 512by512.\n");
}
CUT_SAFE_CALL(cutCreateTimer(&timer_compute));
CUT_SAFE_CALL(cutStartTimer(timer_compute));
float* reference = (float*) malloc(mem_size_P);
computeGold(reference, hostM, hostN, Mh, Mw, Nw);
CUT_SAFE_CALL(cutStopTimer(timer_compute));
printf(" CPU Processing time : %f (ms)\n",
cutGetTimerValue(timer_compute));
CUT_SAFE_CALL(cutDeleteTimer(timer_compute));
printf(" Matrix data checksum : %g\n", CheckSum(reference,
Mw, Nw));
if (!if_quiet) {
printf(" Matrix data contents :\n");
printf(" ");
}
matrix = (unsigned int *) malloc(Pw * Ph * sizeof(unsigned int));
for (i = 0; i < Ph; i++) {
for (j = 0; j < Pw; j++) {
matrix[i*Pw + j] = (unsigned int) reference[i*Pw + j];
if (!if_quiet) printf("%u ", matrix[i*Pw + j]);
}
if (!if_quiet) printf("\n ");
}
if (!if_quiet) printf("\n");
WriteMatrixFile(gold_fn, matrix, Pw, Ph, 1);
free(matrix); matrix = NULL;
free(reference);
// clean up memory
free(hostM); free(hostN); free(hostP);
free(input_fn); free(gold_fn);
return 0;
}


