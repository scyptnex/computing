/************************************************************************* 
 *                               p165.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#include "problem.h"
#include "geom.h"
#include <iostream>
#include <vector>

using namespace std;

typedef line<double> ln;

double cross(vec3<double> a, vec3<double> b){
    return a.x*b.y - a.y*b.x;
}

bool intersects(ln a, ln b){
    double adXbd = cross(a.dir, b.dir);
    if(adXbd == 0) return false;
    double t = cross((b.pos - a.pos), b.dir) / adXbd;
    double u = cross((a.pos - b.pos), a.dir) / (-adXbd);
    return t > 0 && t < 1 && u > 0 && u < 1;
}

vector<double> bbs(int size){
    vector<double> ret(size);
    long cur = 290797;
    for(unsigned i=0; i<size; i++){
        cur = (cur*cur)%50515093;
        ret[i] = (double)(cur%500);
    }
    return ret;
}

unsigned COUNT = 5000;

void p165(int argc, char** argv){
    ln l1 = ln::from_points({27,44}, {12,32});
    ln l2 = ln::from_points({46,53}, {17,62});
    ln l3 = ln::from_points({46,70}, {22,40});
    std::cout << intersects(l1, l2) << endl;
    std::cout << intersects(l1, l3) << endl;
    std::cout << intersects(l2, l3) << endl;
    auto nums = bbs(COUNT*4);
    vector<line<double>> lines(COUNT);
    for(unsigned i=0; i<COUNT; i++){
        lines[i] = {{nums[4*i], nums[4*i+1], 0}, {nums[4*i+2]-nums[4*i], nums[4*i+3]-nums[4*i+1], 0}};
    }
    unsigned total = 0;
    //TODO distinct points, not just points
    for(unsigned i=0; i<COUNT; i++){
        for(unsigned j=i+1; j<COUNT; j++){
            if(intersects(lines[i], lines[j])) total++;
        }
    }
    std::cout << total << endl;
}

REGISTER_SOLVER_MAIN( p165 );
