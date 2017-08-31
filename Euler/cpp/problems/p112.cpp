/************************************************************************* 
 *                               p109.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-29                                                     *
 *************************************************************************/

#include "problem.h"
#include <algorithm>
#include <iostream>
#include <string>
#include <vector>

using namespace std;

typedef unsigned long num;

int bouncy(num n){
    bool inc = true;
    bool dec = true;
    num ln = n%10;
    n /= 10;
    while(n != 0){
        num cn = n%10;
        if(cn > ln) inc = false;
        if(cn < ln) dec = false;
        if(!inc and !dec) return 0;
        ln = cn;
        n /= 10;
    }
    if(inc) return 1;
    else return -1;
}

void solv(int argc, char** argv){
    num cur = 100;
    num bncy = 0;
    while(true){
        if(bouncy(cur) == 0) bncy++;
        if(bncy*100 >= 99*cur) break;
        cur++;
    }
    std::cout << bncy << "/" << cur << std::endl;
    std::cout << cur << std::endl;
}

REGISTER_SOLVER_MAIN( solv );
