/*******************************************************
File Name [matrixmul.h]
Synopsis [This file defines the function prototype of
the gold-versionmatrix-matrix multiplication.]
Description []
*******************************************************/
#ifndef MATRIXMUL_H
#define MATRIXMUL_H
extern "C"
void computeGold(float* P, const float* M, const float* N, int Mh, int Mw, int Nw);
#endif

