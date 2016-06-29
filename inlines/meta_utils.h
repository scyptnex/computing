/************************************************************************* 
 *                             meta_utils.h                              *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-29                                                     *
 *                                                                       *
 * Meta template utilities for function-style programming the compiler,  *
 * this library guarantees inline support, but my encoding is slightly   *
 * wacky.                                                                *
 *************************************************************************/

#ifndef __META_UTILS_H__
#define __META_UTILS_H__

namespace meta {

    /********* 
     * Types *
     *********/
    struct nil {};

    template<typename First, typename Second> struct pair {};
    template<typename Cur, typename Rest> using tlist = pair<Cur, Rest>;

    typedef long numeric;
    template<numeric> struct numeric_t {};

    typedef bool boolean;
    template<boolean> struct boolean_t {};

    /************** 
     * Arithmetic *
     **************/
    class arithmertic {
        private:
            static constexpr numeric _square_root(numeric target, numeric low, numeric high){
                return low == high ? low : ((target / mid(low, high) < mid(low, high)) ? _square_root(target, low, mid(low, high) - 1) : _square_root(target, mid(low, high), high));
            }
        public:
            static constexpr numeric mid(numeric a, numeric b){
                return (a+b+1)/2;
            }
            static constexpr numeric square_root(numeric i){
                return _square_root(i, 0, i/2+1);
            }

    };


    /******** 
     * Size *
     ********/
    template<typename> struct size;
    template<typename T, typename Rest> struct size<tlist<T, Rest>> {
        static const unsigned value = size<Rest>::value + 1;
    };
    template<> struct size<nil> {
        static const unsigned value = 0;
    };

    /*************** 
     * Concatenate *
     ***************/
    template<typename, typename> struct cat;
    template<typename Cur, typename Rest, typename Other> struct cat<tlist<Cur, Rest>, Other> {
        typedef tlist<Cur, typename cat<Rest, Other>::type> type;
    };
    template<typename Other> struct cat<nil, Other> {
        typedef Other type;
    };

    /************ 
     * For Each *
     ************/
    template<typename, template<typename> class> struct foreach;
    template<typename Cur, typename Rest, template<typename> class Callback> struct foreach<tlist<Cur, Rest>, Callback> {
        template <typename...As>
            static inline void go(As&...as){
                Callback<Cur>::go(as...);
                foreach<Rest, Callback>::template go<As...>(as...);
            }
    };
    template<template<typename> class Callback> struct foreach<nil, Callback> {
        template <typename...As>
            static inline void go(As...as){}
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
        typedef tlist<numeric_t<From>, typename range<From+1, To>::type> type;
    };
    template<unsigned X> struct range<X, X>{
        typedef nil type;
    };

    /***************** 
     * Cross Product *
     *****************/
    template<typename L, typename R> struct cross {
        private:
            static const unsigned lsize = size<L>::value;
            static const unsigned rsize = size<R>::value;
            template<typename T> using curry_extend = extend<T, rsize>;
            typedef typename flatten<typename map<L, curry_extend>::type>::type xl;
            typedef typename flatten<typename extend<R, lsize>::type>::type xr;
        public:
            typedef typename zip<xl, xr>::type type;
    };

} // end namespace meta

#endif /* __META_UTILS_H__ */
