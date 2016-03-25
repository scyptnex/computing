#include <stdio.h>
#include <omp.h>

static long num_steps=1000000000;

int main(){
  double pi = 0;
  double step = 1.0/(double) num_steps;
//  #pragma omp parallel
//  {
//    double t = omp_get_wtime();
//    int id = omp_get_thread_num();
//    int i = 0;
//    int min = id*(num_steps/omp_get_num_threads());
//    int max = min + (num_steps/omp_get_num_threads());
//    if(id == omp_get_num_threads()-1) max = num_steps;
//    double sum = 0.0;
//    double x = 0.0;
//    for(i=min; i<max; i++){
//      x=(i+0.5)*step;
//      sum=sum+4.0/(1.0+x*x);
//    }
//    t = omp_get_wtime() - t;
//    printf("T %d finished in %f with %f\t(%d,%d)\n", id, t, step*sum, min, max);
//    #pragma omp atomic
//    pi+=step*sum;
//  }
double sum = 0;
#pragma omp parallel for reduction (+:sum)
for(int i=0; i<num_steps; ++i){
  double x = (i+0.5)*step;
  sum+=(4.0/(1.0+x*x));
}
pi = step*sum;
  printf("pi = %f\n", pi);

}
