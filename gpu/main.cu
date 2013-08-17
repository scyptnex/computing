#include <stdio.h>
#include <sys/resource.h>

#define TILE_SIZE 16
#define SIZE (TILE_SIZE * 256)
#define MALLOC_MATRIX(n) (float*)malloc((n)*(n)*sizeof(float))

float* device_malloc(int n){
	float* m;
	if(cudaMalloc(&m, n*n*sizeof(float)) == cudaErrorMemoryAllocation) return NULL;
	return m;
}

__global__ void gpuPower(float* res, float* inp, int n, int power){
	__shared__ float s_res[TILE_SIZE*TILE_SIZE];
	__shared__ float s_inp[TILE_SIZE*TILE_SIZE];
	__shared__ float s_tmp[TILE_SIZE*TILE_SIZE];
	const unsigned int row = blockIdx.y*blockDim.y + threadIdx.y;
	const unsigned int col = blockIdx.x*blockDim.x + threadIdx.x;
	int pow = 0, idx = 0;
	float sum = 0.0f;

	if(row < n && col < n){
		//copy the input to its share
		s_inp[row*n+col] = inp[row*n+col];
		//set the shared result to the identity matrix
		if(row == col) s_res[row*n+col] = 1.0f;
		else s_res[row*n+col]=0.0f;
	}
	__syncthreads();

	for(pow=0; pow<power; pow++){
		//multiply the matrices
		if(row < n && col < n){
			sum = 0.0f;
			for(idx=0; idx<n; idx++){
				sum = sum + s_res[row*n+idx]*s_inp[idx*n+col];
			}
			s_tmp[row*n+col] = sum;
		}
		__syncthreads();
		//copy temp back to result
		if(row < n && col < n) s_res[row*n+col] = s_tmp[row*n+col];
		__syncthreads();
	}
	__syncthreads();

	//copy the result matrix to global res
	if(row < n && col < n) res[row*n+col] = s_res[row*n+col];
	__syncthreads();
}

void power_gpu(float* result, float* input, int n, int power){
	dim3 bdim(TILE_SIZE, TILE_SIZE);
	dim3 gdim(SIZE/TILE_SIZE, SIZE/TILE_SIZE);

	float* d_result = device_malloc(n);
	float* d_input = device_malloc(n);

	cudaMemcpy(d_input, input, n*n*sizeof(float), cudaMemcpyHostToDevice);

	gpuPower<<<gdim, bdim>>>(d_result, d_input, n, power);

	cudaMemcpy(result, d_result, n*n*sizeof(float), cudaMemcpyDeviceToHost);

	cudaFree(d_result);
	cudaFree(d_input);
}

void power_cpu(float* result, float* input, int n, int power){
	int pow;
	int i, k, r, c;
	float* temp = MALLOC_MATRIX(n);

	//set result to be the identity matrix
	for(i=0; i<n*n; i++){
		if(i/n == i%n) result[i] = 1.0f;
		else result[i] = 0.0f;
	}

	for(pow=0; pow<power; pow++){
		//multiply result*input -> temp
		for(r=0; r<n; r++) for(c=0; c<n; c++){
			float sum = 0;
			for(k=0; k<n; k++){
				sum = sum + result[r*n+k]*input[k*n+c];
			}
			temp[r*n+c] = sum;
		}
		//copy temp back to result
		for(i=0; i<n*n; i++){
			result[i] = temp[i];
		}
	}
}

void printMat(float* mat, int n){
	for(int r=0; r<n; r++){
		for(int c=0; c<n; c++){
			if(c != 0) printf(" ");
			printf("%.3f", mat[r*n+c]);
		}
		printf("\n");
	}
}

int main() {
	int n;
	int power;
	float* mat = NULL;
	float* result = NULL;

	scanf("%d", &n);
	scanf("%d", &power);

	if(power < 0 || n < 0){
		printf("error\n");
		return 1;
	}

	mat = MALLOC_MATRIX(n);
	result = MALLOC_MATRIX(n);
	if(!mat || !result){
		printf("error\n");
		return 1;
	}

	for(int i=0; i<(n*n); i++){
		if(!scanf("%f", &(mat[i]))){
			printf("error\n");
			free(mat);
			free(result);
			return 1;
		}
	}

	power_gpu(result, mat, n, power);

	printMat(mat, n);
	printMat(result, n);
	free(mat);
	free(result);
	return 0;
}

