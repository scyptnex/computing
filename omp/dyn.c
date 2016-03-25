#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <omp.h>

int main(int argc, char** argv){
    if(argc != 2){
        printf("usage: dyn <number of entries>");
        return 1;
    }
    int amt = atoi(argv[1]);
    printf("splitting up %d entries\n", amt);
    int* owner = malloc(sizeof(int)*amt);
    if(owner){
#pragma omp parallel
        {
#pragma omp for schedule(guided)
            for(int i=0; i<amt; i++) owner[i] = omp_get_thread_num();
        }
        for(int i=0; i<amt; i++){
            printf("%d\t- %d\n", i, owner[i]);
        }
        free(owner);
    }
    return 0;
}
