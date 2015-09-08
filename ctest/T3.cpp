
#include <cstdio>

template<unsigned I> struct F{
    static const unsigned fx = F<I-1>::fx + F<I-2>::fx;
};

template<> struct F<1>{
    static const unsigned fx = 1;
};

template<> struct F<0>{
    static const unsigned fx = 1;
};

int main(){
    printf("%d\n", F<10>::fx);
    return 0;
}
