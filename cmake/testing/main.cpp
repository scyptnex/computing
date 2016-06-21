#include <cstdio>
int main(int argc, char** argv){
    printf("%d arguments\n", argc);
    for(int i=0; i<argc; i++){
        printf(" - %s\n", argv[i]);
    }
    if(argc > 1){
        char c = argv[1][0];
        if(c <= '9' && c >= '0'){
            return c - '0';
        } else {
            return 1;
        }
    } else {
        return 0;
    }
}
