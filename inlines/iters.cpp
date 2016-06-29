/************************************************************************* 
 *                               iters.cpp                               *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-28                                                     *
 *************************************************************************/

#include <string>
#include <cstdio>
#include "meta_utils.h"

using namespace meta;

template <typename> struct foo;
template<numeric I>
struct foo<numeric_t<I>> {
    static inline void go() {
        printf("%ld\n", I);
    }
};

template <typename> struct bar;
template<numeric I, numeric J>
struct bar<meta::pair<numeric_t<I>, numeric_t<J>>> {
    static inline void go(short& foo, short& bar) {
        printf("%p %p: %ld - %ld\n", &bar, &foo, I, J);
    }
};

template<typename> struct maz;
template<numeric I>
struct maz<numeric_t<I>> {
    typedef numeric_t<I*5> type;
};

int main(){
    short fo = 99;
    short ba = 42;
    printf("%p %p\n", &fo, &ba);
    typedef tlist<numeric_t<1>, nil> ONE;
    typedef tlist<numeric_t<2>, tlist<numeric_t<3>, nil>> TWO;
    // foreach<TWO, foo>::go();
    // foreach<range<5,50>::type, foo>::go();
    foreach<nil, bar>::go(fo, ba);
    foreach<tlist<pair<numeric_t<99>, numeric_t<42>>, nil>, bar>::template go<short&, short&>(fo, ba);
    // foreach<zip<ONE, TWO>::type, bar>::go("zippy");
    foreach<zip<range<0,10>::type, range<10,20>::type>::type, bar>::template go<short&, short&>(fo, ba);
    // foreach<map<TWO, maz>::type, foo>::go();
    // foreach<extend<numeric_t<6>, 3>::type, foo>::go();
    // foreach<flatten<extend<TWO, 3>::type>::type, foo>::go();
    // printf(".%d.\n", size<range<88,99>::type>::value);
    // foreach<cross<range<0,10>::type, range<42,45>::type>::type, bar>::go(fo, ba);
    return 0;
}
