#include <iostream>
#include <functional>
#include <tuple>

template<typename S, unsigned I, typename...Ts>
struct read_block_helper{
    static bool go(S& src, std::tuple<Ts...>& tpl){
        src >> std::get<sizeof...(Ts)-I>(tpl);
        return read_block_helper<S, I-1, Ts...>::go(src, tpl);
    }
    static void print(S& out, const std::tuple<Ts...>& tpl){
        out << std::get<sizeof...(Ts)-I>(tpl);
        if(I != 1) out << ",";
        read_block_helper<S, I-1, Ts...>::print(out, tpl);
    }
};

template<typename S, typename...Ts>
struct read_block_helper<S, 0, Ts...>{
    static bool go(S& src, std::tuple<Ts...>& tpl){ return true; }
    static void print(S& src, const std::tuple<Ts...>& tpl) {}
};

template<typename S, typename...Ts> bool read_block(S& src, std::tuple<Ts...>& tpl){
    read_block_helper<S, sizeof...(Ts), Ts...>::go(src, tpl);
}

template<typename S, typename...Ts> bool print_tuple(S& out, const std::tuple<Ts...>& tpl){
    out << "(";
    read_block_helper<S, sizeof...(Ts), Ts...>::print(out, tpl);
    out << ")";
}

template<typename S, typename...Ts> S& operator<<(S& in, const std::tuple<Ts...>& tpl){
    print_tuple(in, tpl);
    return in;
}

template<int ...>
struct seq { };

template<int N, int ...S>
struct gens : gens<N-1, N-1, S...> { };

template<int ...S>
struct gens<0, S...> {
    typedef seq<S...> type;
};

template<typename F, typename T, int...S> auto forward_call_helper(F fnc, T tpl, seq<S...>){
    return fnc(std::get<S>(tpl)...);
}

template<typename F, typename...Ts> auto forward_call(F fnc, std::tuple<Ts...> tpl) {
    return forward_call_helper(fnc, tpl, typename gens<sizeof...(Ts)>::type());
}

std::string solve(int a, int m, int sl, int sh, int fl, int fh){
    if(sl >= fl && sh <= fh) return "empty";
    if(a == 0) return "impossible";
    int max_mults = hf - fl;
    int adds = (fl - sl - 1)/a + 1;
    if (sh + a*adds > fh) return "impossible";
    for(int len=1; len<24; len++){
    }
    return "hi";
}

int main(){
    std::tuple<int, int, int, int, int, int> b;
    int i=0;
    while(true){
        read_block(std::cin, b);
        if(
                std::get<0>(b) == 0 &&
                std::get<1>(b) == 0 &&
                std::get<2>(b) == 0 &&
                std::get<3>(b) == 0 &&
                std::get<4>(b) == 0 &&
                std::get<5>(b) == 0) {
            return 0;
        }
        std::cout << "Case " << ++i << ": " << forward_call(solve, b) << "|" << b << std::endl;
    }
    return 0;
}
