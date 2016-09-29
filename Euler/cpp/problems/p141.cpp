/************************************************************************* 
 *                               p141.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-29                                                     *
 *************************************************************************/

#include "problem.h"
#include "rational.h"
#include <iostream>

using namespace std;

typedef unsigned long long ull;

ull pps(ull sqrt){
    ull num = sqrt*sqrt;
    ull d = 1;
    for(ull d=1; d<sqrt; ++d){
        // r is always less than d...
        // q is always > d
        // dd == rq
        ull q=num/d, r=num%d;
        if(d*d == r*q){
            cout << num << ", " << r << ", " << d << ", " << q << endl;
            return num;
        }
    }
    return 0;
}

void solv(int argc, char** argv){
    ull base=1, tot=0;
    //while(base*base < 1000000000000){
    while(base*base < 100000){
        tot += pps(base);
        if(base%10000 == 0) cout << base << endl;
        base++;
    }
    cout << tot << endl;
}

REGISTER_SOLVER_MAIN( solv );
