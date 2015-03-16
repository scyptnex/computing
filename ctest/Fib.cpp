/* 
 *                                 Fib.cpp
 * 
 * Author: Nic H
 * Date: 2015-Mar-16
 * 
 * Compute fibonacci intelligently
 */

#include <cstdint>
#include <iostream>

using namespace std;

template <uint32_t T> inline long fib_naive(){
    return fib_naive<T-1>() + fib_naive<T-2>();
}

template <> inline long fib_naive<1>(){
    return 1;
}

template <> inline long fib_naive<0>(){
    return 1;
}

int main(){
    long a = fib_naive<50>();
    cout << a << endl;
    return 0;
}

