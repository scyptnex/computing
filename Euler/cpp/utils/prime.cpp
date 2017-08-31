/************************************************************************* 
 *                               prime.cpp                               *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Aug-31                                                     *
 *************************************************************************/

#include "prime.h"

#include <iostream>
#include <chrono>

// n = 2*i+3
// i = (n-3)/2

prime_sieve_iterator::prime_sieve_iterator(std::vector<bool>::const_iterator _it, unsigned long _max) :
    nextPrime(2),
    max(_max),
    iter(_it) {}

typedef std::chrono::steady_clock timer;

prime_sieve::prime_sieve(unsigned long _max) :
    max(_max),
    marks(std::vector<bool>((_max-2)/2, true))
{
    auto true_t = timer::now();
    auto start_t = timer::now();
    for(unsigned long p=3; p*p<max; p+=2){
        if(marks[(p-3)/2]){
            for(unsigned long i=3*p; i<max; i += 2*p){
                marks[(i-3)/2] = false;
            }
        }
        auto cur_t = timer::now();
        if(std::chrono::duration_cast<std::chrono::seconds>(cur_t - start_t).count() >= 10){
            std::cout << "p = " << p << std::endl;
            start_t = cur_t;
        }
    }
    std::cout << "completed primes <" << max << " in " << std::chrono::duration_cast<std::chrono::milliseconds>(timer::now() - true_t).count() <<"ms" << std::endl;
}

prime_sieve_iterator prime_sieve::begin() const {
    return prime_sieve_iterator(marks.begin(), max);
}
prime_sieve_iterator prime_sieve::end() const {
    return prime_sieve_iterator(marks.end(), max);
}

std::vector<unsigned long> prime_sieve::enumerate() const{
    std::vector<unsigned long> vec;
    if(max < 2) return vec;
    vec.push_back(2);
    for(unsigned long i=0; i<marks.size(); i++){
        if(marks[i]) vec.push_back(i*2 + 3);
    }
    return vec;
}

void prime_sieve::process( void (*f)(unsigned long) ) const {
    if(max < 2) return;
    (*f)(2);
    for(unsigned long i=0; i<marks.size(); i++){
        if(marks[i]) (*f)(i*2 + 3);
    }
}
