/******************************************************
File Name [matrixmul_gold.cpp]
Synopsis [This file defines the gold-version matrix-matrix
multiplication.]
Description []
*******************************************************/
#include <stdio.h>
#include "matrixmul.h"

void
computeGold(
float* P, // Resultant matrix data
const float* M, // Matrix M
const float* N, // Matrix N
int Mh, // Matrix M height
int Mw, // Matrix M width
int Nw) // Matrix N width
{
int i, j, k;
float sum, a, b;
for (i = 0; i < Mh; i++)
for (j = 0; j < Nw; j++)
{
sum = 0;
for (k = 0; k < Mw; k++)
{
a = M[i * Mw + k];
b = N[k * Nw + j];
//printf ("A[%d] * B[%d]\n", i * Mw + k, k * Nw + j);
sum += a * b;
}
P[i * Nw + j] = (float)sum;
}
}

