
#include <cstdio>

template<unsigned I> unsigned fibo(){
    return fibo<I-1>() + fibo<I-2>();
}

template<> unsigned fibo<1>(){
    return 1;
}
template<> unsigned fibo<0>(){
    return 1;
}

int main(){
    printf("%d\n", fibo<10>());
    return 0;
}
