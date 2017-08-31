/************************************************************************* 
 *                               p109.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-29                                                     *
 *************************************************************************/

#include "problem.h"
#include "prime.h"
#include <algorithm>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

constexpr unsigned long tenpow(unsigned power){
    return power == 0 ? 1 : 10*tenpow(power-1);
}

const unsigned DIGIS = 10;

unsigned long m[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
unsigned long n[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
unsigned long s[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
unsigned long lg = 0;
unsigned long GRAD = tenpow(8);

void process(unsigned long l){
    unsigned long c[10] = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    unsigned long ls = l;
    if(l < tenpow(DIGIS-1)) return;
    if(l > lg){
        std::cout << l << std::endl;
        lg += GRAD;
    }
    for(int i=0; i<DIGIS; i++){
        c[l%10]++;
        l = l/10;
    }
    for(int i=0; i<10; i++){
        if(c[i] > m[i]){
            m[i] = c[i];
            n[i] = 1;
            s[i] = ls;
        } else if(c[i] == m[i]){
            n[i]++;
            s[i] += ls;
        }
    }
}

void solv(int argc, char** argv){
    prime_sieve ps(tenpow(DIGIS));
    ps.process(process);
    unsigned long ts = 0;
    for(int i=0; i<10; i++){
        std::cout << i << "\t" << m[i] << "\t" << n[i] << "\t" << s[i] << std::endl;
        ts += s[i];
    }
    std::cout << ts << std::endl;
}

REGISTER_SOLVER_MAIN( solv );
