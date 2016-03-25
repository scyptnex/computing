#include <stdio.h>
#include <omp.h>


int main(){
    double tstart=omp_get_wtime();
    double tend;
    double pi = 0;
#pragma omp parallel
    {
        long num_steps=1000000000;
        int i;
        int tid=omp_get_thread_num();
        int tnum=omp_get_num_threads();
        double x;
        double sum = 0;
        double step = 1.0/(double)num_steps;
        for(i=tid; i<num_steps; i+=tnum) {
            x=(i+0.5)*step;
            sum=sum+4.0/(1.0+x*x);
        }
#pragma omp critical
        pi += (sum*step);
    }
    tend = omp_get_wtime();
    printf("pi=%.10f\n", pi);
    printf(" t=%f\n", (tend - tstart));
    return 0;
}
