
#include <iostream>


typedef std::uint16_t T;

const unsigned SIZE = 4;

const T MASK = (1<<SIZE)-1;

inline constexpr unsigned shift(unsigned r, unsigned c){
    return SIZE*r + c;
}

template<unsigned Shift> inline void print_inner(const T& val) {
    std::cout << (((val >> Shift) & 1) ? "1" : "0") << " ";
}

template<unsigned Ls, unsigned Rs, unsigned Os> inline void mult_simple_inner(const T& l, const T& r, T& out) {
    out |= ((l >> Ls)&(r >> Rs)&1) << Os;
}

template<unsigned Lm, unsigned Rm, unsigned Os> inline void mult_smart_inner(const T& l, const T& r, T& out) {
    //std::cout << std::hex << (l&Lm) << " " << (r&Rm) << " " << std::endl;
    if((l>>Lm)&(r>>Rm)&MASK) out |= 1 << Os;
}

template<unsigned Fr, unsigned To> inline void transpose_inner(const T& from, T& to) {
    to |= ((from>>Fr)&1)<<To;
}

template<unsigned A, unsigned B, unsigned Cur> struct ternary {
    static inline void mult_simple(const T& l, const T& r, T& out) {
        mult_simple_inner<shift(SIZE-A, SIZE-Cur), shift(SIZE-Cur, SIZE-B), shift(SIZE-A, SIZE-B)>(l, r, out);
        ternary<A, B, Cur-1>::mult_simple(l, r, out);
    }
};

template<unsigned A, unsigned B> struct ternary<A, B, 0> {
    static inline void mult_simple(const T& l, const T& r, T& out) {}
};

template<unsigned A, unsigned Cur> struct binary {
    static inline void print(const T& val) {
        print_inner<shift(SIZE-A, SIZE-Cur)>(val);
        binary<A, Cur-1>::print(val);
    }
    static inline void mult_simple(const T& l, const T& r, T& out) {
        ternary<A, Cur, SIZE>::mult_simple(l, r, out);
        binary<A, Cur-1>::mult_simple(l, r, out);
    }
    static inline void mult_smart(const T& l, const T& r, T& out) {
        mult_smart_inner<(SIZE*(SIZE-A)), (SIZE*(SIZE-Cur)), shift(SIZE-A, SIZE-Cur)>(l, r, out);
        binary<A, Cur-1>::mult_smart(l, r, out);
    }
    static inline void transpose(const T& from, T& to) {
        transpose_inner<shift(SIZE-A, SIZE-Cur), shift(SIZE-Cur, SIZE-A)>(from, to);
        binary<A, Cur-1>::transpose(from, to);
    }
};

template<unsigned A> struct binary<A, 0> {
    static inline void print(const T& val) {}
    static inline void mult_simple(const T& l, const T& r, T& out) {}
    static inline void mult_smart(const T& l, const T& r, T& out) {}
    static inline void transpose(const T& from, T& to) {}
};

template<unsigned Cur> struct unary {
    static inline void print(const T& val) {
        binary<Cur, SIZE>::print(val);
        std::cout << "\n";
        unary<Cur-1>::print(val);
    }
    static inline void mult_simple(const T& l, const T& r, T& out) {
        binary<Cur, SIZE>::mult_simple(l, r, out);
        unary<Cur-1>::mult_simple(l, r, out);
    }
    static inline void mult_smart(const T& l, const T& r, T& out) {
        binary<Cur, SIZE>::mult_smart(l, r, out);
        unary<Cur-1>::mult_smart(l, r, out);
    }
    static inline void transpose(const T& from, T& to) {
        binary<Cur, SIZE>::transpose(from, to);
        unary<Cur-1>::transpose(from, to);
    }
};

template<> struct unary<0> {
    static inline void print(const T& val) {}
    static inline void mult_simple(const T& l, const T& r, T& out) {}
    static inline void mult_smart(const T& l, const T& r, T& out) {}
    static inline void transpose(const T& from, T& to) {}
};

void show(const T& val){
    std::cout << std::hex << val << std::endl;
    unary<SIZE>::print(val);
}

T mult(const T& a, const T& b){
    T ret = 0;
    unary<SIZE>::mult_simple(a, b, ret);
    return ret;
}

T mult2(const T& a, const T& b){
    T ret = 0;
    T transp = 0;
    unary<SIZE>::transpose(b, transp);
    //std::cout << "---\n";
    //show((T)transp);
    //std::cout << "---\n";
    unary<SIZE>::mult_smart(a, transp, ret);
    return ret;
}

T read(){
    T val;
    std::cin >> std::hex >> val;
    return val;
}

int main(int argc, char** argv){
    if(argc > 1){
        for(unsigned x=0; x<(1 << (SIZE*SIZE)); ++x){
            for(unsigned y=0; y<(1 << (SIZE*SIZE)); ++y){
                T m1 = mult((T)x, (T)y);
                T m2 = mult2((T)x, (T)y);
                if(m1 != m2){
                    show((T)x);
                    show((T)y);
                    show(m1);
                    show(m2);
                    return 1;
                }
            }
        }
    } else {
        T a = read();
        T b = read();
        show(a);
        show(b);
        T c = mult(a, b);
        show(c);
        T d = mult2(a, b);
        show(d);
    }
}
