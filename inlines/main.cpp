
#include <iostream>

#include "meta_utils.h"

using namespace meta;

typedef std::uint16_t T;

const unsigned SIZE = arithmertic::square_root(sizeof(T)*8);

const T MASK = (1<<SIZE)-1;

inline constexpr unsigned shift(unsigned r, unsigned c){
    return SIZE*r + c;
}

typedef range<0, SIZE>::type t_lst;
typedef cross<t_lst, t_lst>::type t_rcs;

template<typename> struct show_h;
template<numeric Shift> struct show_h<numeric_t<Shift>>{
    static inline void go(const T& val){
        std::cout << (((val >> Shift) & 1) ? "1" : "0") << " ";
    };
};
template<> struct show_h<nil>{
    static inline void go(const T&){
        std::cout << "\n";
    };
};
template<typename> struct show_map;
template<numeric R, numeric C> struct show_map<pair<numeric_t<R>, numeric_t<C>>> {
    typedef tlist<numeric_t<shift(R, C)>, nil> type;
};
template<numeric R> struct show_map<pair<numeric_t<R>, numeric_t<SIZE-1>>> {
    typedef tlist<numeric_t<shift(R, SIZE-1)>, tlist<nil, nil>> type;
};
void show(const T& val){
    std::cout << std::hex << val << std::endl;
    //typedef map<t_rcs, show_map>::type t_ord;
    //foreach<flatten<t_ord>::type, show_h>::go(val);
}

template<typename> struct transp_help;
template<numeric R, numeric C> struct transp_help<pair<numeric_t<R>, numeric_t<C>>> {
    typedef pair<numeric_t<shift(R, C)>, numeric_t<shift(C, R)>> type;
    static inline void go(const T& from, T& to){
        to |= ((from>>R)&1)<<C;
    };
};
template<typename> struct mult_map;
template<numeric I, numeric J> struct mult_map<pair<numeric_t<I>, numeric_t<J>>> {
    typedef pair<pair<numeric_t<SIZE*I>, numeric_t<SIZE*J>>, numeric_t<shift(I, J)>> type;
};
template<typename> struct mult_use;
template<numeric As, numeric Bs, numeric O> struct mult_use<pair<pair<numeric_t<As>, numeric_t<Bs>>, numeric_t<O>>> {
    static inline void go(const T& l, const T& r, T& out){
        if((l>>As)&(r>>Bs)&MASK) out |= 1 << O;
    }
};
T mult2(const T& a, const T& b){
    T ret = 0;
    T transp = 0;
    foreach<map<t_rcs, transp_help>::type, transp_help>::go(b, transp);
    foreach<map<t_rcs, mult_map>::type, mult_use>::go(a, transp, ret);
    return ret;
}

T read(){
    T val;
    std::cin >> std::hex >> val;
    return val;
}

int main(int, char**){
    T a = read();
    T b = read();
    show(a);
    show(b);
    //T c = mult(a, b);
    //show(c);
    T d = mult2(a, b);
    show(d);
}
