/************************************************************************* 
 *                               p114.cpp                                *
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


void solv(int argc, char** argv){
    vector<num> rc({0, 0, 0, 1});
    vector<num> bc({0, 1, 1, 1});
    for(int i=4; i<=50; i++){
        num r = rc[i-1] + bc[i-3];
        num b = rc[i-1] + bc[i-1];
        std::cout << i << " " << r << " - " << b << std::endl;
        rc.push_back(r);
        bc.push_back(b);
    }
    std::cout << rc.back() + bc.back() << std::endl;
}

REGISTER_SOLVER_MAIN( solv );
