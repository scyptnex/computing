/************************************************************************* 
 *                               p165.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#include "problem.h"
#include "geom.h"
#include "rational.h"
#include <iostream>
#include <set>
#include <vector>

using namespace std;

typedef rational<long long> unit;

typedef line<unit> ln;

unit cross(vec3<unit> a, vec3<unit> b){
    return a.x*b.y - a.y*b.x;
}

set<vec3<unit> > uniques;

bool intersects(ln a, ln b){
    unit adXbd = cross(a.dir, b.dir);
    if(adXbd == 0) return false;
    unit t = cross((b.pos - a.pos), b.dir) / adXbd;
    unit u = cross((a.pos - b.pos), a.dir) / (-adXbd);
    if(t > 0 && t < 1 && u > 0 && u < 1){
        // cout << a << "  AND  " << b << endl;
        // cout << t << ", " << u << endl;
        // cout << (a.pos + a.dir.scale(t)) << endl;
        // cout << (b.pos + b.dir.scale(u)) << endl;
        uniques.insert(a.pos + a.dir.scale(t));
        return true;
    }
    return false;
}

vector<unit> bbs(int size){
    vector<unit> ret(size);
    long cur = 290797;
    for(unsigned i=0; i<size; i++){
        cur = (cur*cur)%50515093;
        ret[i] = (unit)(cur%500);
    }
    return ret;
}

unsigned COUNT = 5000;

void p165(int argc, char** argv){
    auto nums = bbs(COUNT*4);
    vector<line<unit>> lines(COUNT);
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
    std::cout << uniques.size() << std::endl;
}

REGISTER_SOLVER_MAIN( p165 );
