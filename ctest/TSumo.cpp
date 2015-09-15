#include <cstdio>

template<unsigned...> struct M;
template<unsigned T, unsigned...Ts>
struct M<T, Ts...>{
    static const unsigned val = T*M<Ts...>::val;
};
template<>
struct M<>{
    static const unsigned val = 1;
};

template<typename...> struct A;
template<unsigned...Vs, typename...Rest>
struct A<M<Vs...>, Rest...>{
    static const unsigned val = M<Vs...>::val + A<Rest...>::val;
};
template<>
struct A<>{
    static const unsigned val = 0;
};

int main(){
    printf("%d\n", M<2,2,3>::val);
    printf("%d\n", M<>::val);
    printf("%d\n", A<M<2,2,3>,M<>>::val);
    printf("%d\n", A<>::val);
    return 0;
}
