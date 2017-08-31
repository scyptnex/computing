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

typedef std::pair<long, std::string> mv;

std::ostream& operator<<(std::ostream& os, const std::tuple<mv, mv, mv>& turn){
    os << get<0>(turn).second << " " << get<1>(turn).second << " " << get<2>(turn).second << "=" << (get<0>(turn).first + get<1>(turn).first + get<2>(turn).first);
}

bool filterer(const tuple<mv, mv, mv>& e){
    long a = get<0>(e).first;
    long b = get<1>(e).first;
    long c = get<2>(e).first;
    const string& as = get<0>(e).second;
    const string& bs = get<1>(e).second;
    const string& cs = get<2>(e).second;
    long sum = a+b+c;

    if(sum == 0 || c == 0) return true;
    if(b == 0 && a != 0) return true;
    if(cs[0] != 'D') return true;
    if(bs < as) return true;
    return sum >= 100;
}

void solv(int argc, char** argv){
    cout << "hello world" << std::endl;
    std::vector<mv> scores;
    std::string mult[] = {"S", "D", "T"};
    for(int i=1; i<=20; i++){
        for(int m=1; m<=3; m++){
            scores.push_back(std::make_pair<long, std::string>(i*m, mult[m-1] + std::to_string(i)));
        }
    }
    scores.push_back(std::make_pair(25, "SB"));
    scores.push_back(std::make_pair(50, "DB"));
    scores.push_back(std::make_pair(0, "--"));
    std::vector<std::tuple<mv, mv, mv>> combos;
    for(const mv& m1 : scores){
        for(const mv& m2: scores){
            for(const mv& m3: scores){
                combos.push_back(std::make_tuple(m1, m2, m3));
            }
        }
    }
    combos.erase(std::remove_if(combos.begin(), combos.end(), filterer), combos.end());
    for(const auto& c : combos){
        std::cout << c << std::endl;
    }
    std::cout << std::endl << combos.size() << std::endl;
}

REGISTER_SOLVER_MAIN( solv );
