#include <stdlib.h>
#include <stdio.h>
#include <pthread.h>
#include <assert.h>

#define bool int
#define false (0)
#define true (!false)

// simplifies malloc
#define MALLOC(a) (a *)malloc(sizeof(a))

// Index of x/y coordinate
#define x (0)
#define y (1)

// Defines size of a block
#define BLOCK_DIM_X (3)
#define BLOCK_DIM_Y (2)

// Defines size of the grid, i.e., how many blocks
#define GRID_DIM_X (5)
#define GRID_DIM_Y (7)

// Defines the number of threads in the grid
#define GRID_SIZE (BLOCK_DIM_X * BLOCK_DIM_Y * GRID_DIM_X * GRID_DIM_Y)

// execution environment for the kernel
typedef struct exec_env {
   int threadIdx[2];  // thread location 
   int blockIdx[2];
   int blockDim[2];
   int gridDim[2];

   int rows;
   int cols;

   float *A,*B;       // parameters for the thread
   float *C;
} exec_env;

// kernel 
void *kernel(void *arg)
{
    exec_env *env = (exec_env *) arg;

    // compute number of threads in a block
    int sz = env->blockDim[x] * env->blockDim[y]; 

    // compute the index of the first thread in the block
    int k = sz * (env->blockIdx[y]*env->gridDim[x] + env->blockIdx[x]);

    // compute the index of a thread inside a block
    k = k + env->threadIdx[y]*env->blockDim[x] + env->threadIdx[x]; 

    // check whether it is in range
    assert(k >= 0 && k < GRID_SIZE && "Wrong index computation"); 

    // print coordinates in block and grid and computed index
    /*printf("tx:%d ty:%d bx:%d by:%d idx:%d\n",env->threadIdx[x],
                                              env->threadIdx[y],
                                              env->blockIdx[x],
                                              env->blockIdx[y], k);*/
	int myRow = k/(env->cols);
	int myCol = k%(env->cols);
	if(myRow < env->rows && myCol < env->cols){
#ifdef ADDITION
		//matrix addition
		env->C[k] = env->A[k] + env->B[k];
#else
		//matrix multiplication
		float sum = 0;
		int i;
		for(i=0; i<env->rows; i++){
			int aidx = env->cols*myRow + i;
			int bidx = env->cols*i + myCol;
			sum = sum + (env->A[aidx] * env->B[bidx]);
		}
		env->C[k] = sum;
#endif
	}
	else env->C[k] = 0;

    // free execution environment (not needed anymore)
    free(env);

    return NULL;
}


//file format
//ROWS(both matrices)
//COLUMNS(both matrices)
//R0C0
//R0C1
//...
//R1C0
//R1C1
//...
//(repeat for matrix 2)
bool speedRead(FILE* fil, float* mat, int size){
	int i;
	for(i=0; i<size; i++){
		if(fscanf(fil, "%f", &(mat[i])) == EOF) return false;
	}
	return true;
}
bool readin(char* fileLoc, float* a, float* b, int maxsize, int* rowRef, int* colRef){
	FILE* fil;
	bool ret = false;
	int size;

	fil = fopen(fileLoc, "r");
	if(!fil) return false;
	fscanf(fil, "%d", rowRef);
	fscanf(fil, "%d", colRef);
	size=(*rowRef)*(*colRef);
	if(size <= maxsize){
		if(speedRead(fil, a, size) && speedRead(fil, b, size)) ret = true;
	}
	else printf("A %d x %d matrix is too big for bernhard's lousy program\n", *rowRef, *colRef);
	fclose(fil);
	return ret;
}

void printMat(float* mat, int rows, int cols){
	int r, c;
	for(r=0; r<rows; r++){
		for(c=0; c<cols; c++){
			printf("%f ", mat[r*cols + c]);
		}
		printf("\n");
	}
}

// main function
int main(int argc, char **argv)
{
	float A[GRID_SIZE], B[GRID_SIZE], C[GRID_SIZE];
	pthread_t threads[GRID_SIZE];
	int rows, cols;

	if(argc != 2){
		printf("Error, must be run with matrix file\n");
		return 1;
	}
	else if(!readin(argv[1], A, B, GRID_SIZE, &rows, &cols)){
		printf("failed to read in file %s\n", argv[1]);
		return 1;
	}
	printf("Matrix A\n");
	printMat(A, rows, cols);
	printf("\nMatrix B\n");
	printMat(B, rows, cols);

   int i=0, bx, by, tx, ty; 

   // Step 1: create execution environment for threads and create thread
   for (bx=0;bx<GRID_DIM_X;bx++) {
      for (by=0;by<GRID_DIM_Y;by++) {
         for (tx=0;tx<BLOCK_DIM_X;tx++) {
            for (ty=0;ty<BLOCK_DIM_Y;ty++) { 

               exec_env *e = MALLOC(exec_env);
               assert(e != NULL && "memory exhausted"); 

               e->threadIdx[x]=tx;
               e->threadIdx[y]=ty;

               e->blockIdx[x]=bx;
               e->blockIdx[y]=by; 

               e->blockDim[x]=BLOCK_DIM_X;
               e->blockDim[y]=BLOCK_DIM_Y;

               e->gridDim[x]=GRID_DIM_X;
               e->gridDim[y]=GRID_DIM_Y;
		e->rows = rows;
		e->cols = cols;

               // set parameters
               e->A = A; 
               e->B = B; 
               e->C = C;

               // create thread
               pthread_create(&threads[i++],NULL,kernel,(void *)e); 
            }
         }
      }
   }

   // Step 2: wait for completion of all threads
   for (i=0;i<GRID_SIZE;i++) { 
      pthread_join(threads[i], NULL); 
   }
      
	printf("==========\nRESULT (C)\n==========\n");
	printMat(C, rows, cols);
 
   return 0;
}
