/************************************************************************* 
 *                               iters.cpp                               *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-28                                                     *
 *************************************************************************/

#include <cstdio>

struct nil {};

template<typename First, typename Second> struct pair {};
template<typename Cur, typename Rest> using tlist = pair<Cur, Rest>;

template<unsigned I> struct unsigned_t {};

/************ 
 * For Each *
 ************/
template<typename, template<typename> class> struct foreach;
template<typename Cur, typename Rest, template<typename> class Callback> struct foreach<tlist<Cur, Rest>, Callback> {
    static inline void go(){
        Callback<Cur>::go();
        foreach<Rest, Callback>::go();
    }
};
template<template<typename> class Callback> struct foreach<nil, Callback> {
    static inline void go(){}
};

/******* 
 * Zip *
 *******/
template<typename, typename> struct zip;
template<typename C1, typename C2, typename R1, typename R2> struct zip<tlist<C1, R1>,tlist<C2, R2>> {
    typedef tlist<pair<C1, C2>, typename zip<R1, R2>::type> type;
};
template<typename L> struct zip<L, nil> {
    typedef nil type;
};
template<typename R> struct zip<nil, R> {
    typedef nil type;
};
template<> struct zip<nil, nil> {
    typedef nil type;
};

/******* 
 * Map *
 *******/
template<typename, template<typename> class> struct map;
template<typename Cur, typename Rest, template<typename> class Mapper> struct map<tlist<Cur, Rest>, Mapper> {
    typedef tlist<typename Mapper<Cur>::type, typename map<Rest, Mapper>::type> type;
};
template<template<typename> class Mapper> struct map<nil, Mapper> {
    typedef nil type;
};

/*********** 
 * Flatten *
 ***********/
template<typename> struct flatten;
template<typename Cur, typename CRest, typename RRest> struct flatten<tlist<tlist<Cur, CRest>, RRest>> {
    typedef tlist<Cur, typename flatten<tlist<CRest, RRest>>::type> type;
};
template<typename Rest> struct flatten<tlist<nil, Rest>> {
    typedef typename flatten<Rest>::type type;
};
template<> struct flatten<nil> {
    typedef nil type;
};

/***************** 
 * Cross Product *
 *****************/
template<typename, typename> struct cross; // TODO

/********** 
 * Extend *
 **********/
template<typename T, unsigned Len> struct extend {
    typedef tlist<T, typename extend<T, Len-1>::type> type;
};
template<typename T> struct extend<T, 0> {
    typedef nil type;
};

/********* 
 * Range *
 *********/
template<unsigned From, unsigned To> struct range {
    typedef tlist<unsigned_t<From>, typename range<From+1, To>::type> type;
};
template<unsigned X> struct range<X, X>{
    typedef nil type;
};

//-------------------------------------------------------------------------

template <typename> struct foo;
template<unsigned I>
struct foo<unsigned_t<I>> {
    static inline void go() {
        printf("%d\n", I);
    }
};

template <typename> struct bar;
template<unsigned I, unsigned J>
struct bar<pair<unsigned_t<I>, unsigned_t<J>>> {
    static inline void go() {
        printf("%d - %d\n", I, J);
    }
};

template<typename> struct maz;
template<unsigned I>
struct maz<unsigned_t<I>> {
    typedef unsigned_t<I*5> type;
};

int main(){
    //meta_iterator<5,50,foo>::go();
    typedef tlist<unsigned_t<1>, nil> ONE;
    typedef tlist<unsigned_t<2>, tlist<unsigned_t<3>, nil>> TWO;
    foreach<TWO, foo>::go();
    foreach<range<5,50>::type, foo>::go();
    foreach<nil, bar>::go();
    foreach<tlist<pair<unsigned_t<99>, unsigned_t<42>>, nil>, bar>::go();
    foreach<zip<ONE, TWO>::type, bar>::go();
    foreach<zip<range<0,10>::type, range<10,20>::type>::type, bar>::go();
    foreach<map<TWO, maz>::type, foo>::go();
    foreach<extend<unsigned_t<6>, 3>::type, foo>::go();
    foreach<flatten<extend<TWO, 3>::type>::type, foo>::go();
    return 0;
}
