#include <stdio.h>
#include <omp.h>

static long num_steps=1000000000;

int main(){
    double tstart=omp_get_wtime();
    double tend;
    double pi = 0;
    double step = 1.0/(double) num_steps;
    double sum = 0;
#pragma omp parallel for reduction (+:sum) schedule(auto)
    for(int i=0; i<num_steps; ++i){
        double x = (i+0.5)*step;
        sum+=(4.0/(1.0+x*x));
    }
    pi = step*sum;
    tend = omp_get_wtime();
    printf("pi=%.10f\n", pi);
    printf(" t=%f\n", (tend - tstart));
    return 0;
}
