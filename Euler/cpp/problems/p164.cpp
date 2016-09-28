/************************************************************************* 
 *                               p164.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#include "problem.h"
#include <iostream>

using namespace std;

long table[24][10][10];

long get(int, int, int);

long calc(int x, int a, int b){
    if(x <= 3) return std::max(9-a-b, 0);
    long count = 0;
    for(int n=0; n<=9-a-b; ++n){
        count += get(x-1, n, a);
    }
    return count;
}

long get(int x, int a, int b){
    if(table[x][a][b] == -1){
        table[x][a][b] = calc(x, a, b);
    }
    return table[x][a][b];
}

void p164(int argc, char** argv){
    for(int x=0; x<24; x++){
        for(int a=0; a<10; a++){
            for(int b=0; b<10; b++){
                table[x][a][b] = -1;
            }
        }
    }
    cout << get(22,0,0) << endl;
}

REGISTER_SOLVER_MAIN( p164 );
