/************************************************************************* 
 *                               iters.cpp                               *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-28                                                     *
 *************************************************************************/

#include <cstdio>

struct nil {};

template<typename First, typename Second> struct pair {};
template<typename Cur, typename Rest> using tchain = pair<Cur, Rest>;

template<unsigned I> struct unsigned_t {};

template<typename, template<typename> class> struct foreach;
template<typename Cur, typename Rest, template<typename> class Callback> struct foreach<tchain<Cur, Rest>, Callback> {
    static inline void go(){
        Callback<Cur>::go();
        foreach<Rest, Callback>::go();
    }
};
template<template<typename> class Callback> struct foreach<nil, Callback> {
    static inline void go(){}
};

template<unsigned From, unsigned To> struct range {
    typedef tchain<unsigned_t<From>, typename range<From+1, To>::type> type;
};
template<unsigned X> struct range<X, X>{
    typedef nil type;
};

template <typename> struct foo;
template<unsigned I>
struct foo<unsigned_t<I>> {
    static inline void go() {
        printf("%d\n", I);
    }
};

int main(){
    //meta_iterator<5,50,foo>::go();
    foreach<tchain<unsigned_t<42>, tchain<unsigned_t<99>, nil>>, foo>::go();
    foreach<range<5,50>::type, foo>::go();

    return 0;
}
