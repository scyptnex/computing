/************************************************************************* 
 *                               p109.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-29                                                     *
 *************************************************************************/

#include "problem.h"
#include <iostream>
#include <string>
#include <vector>

using namespace std;

void solv(int argc, char** argv){
    cout << "hello world" << std::endl;
    std::vector<std::pair<long, std::string>> scores;
    std::string mult[] = {"S", "D", "T"};
    for(int i=1; i<=20; i++){
        for(int m=1; m<=3; m++){
            scores.push_back(std::make_pair<long, std::string>(i*m, mult[m-1] + std::to_string(i)));
        }
    }
    scores.push_back(std::make_pair(25, "SB"));
    scores.push_back(std::make_pair(50, "DB"));
    for(const auto& s: scores){
        std::cout << s.first << " - " << s.second << std::endl;
    }

}

REGISTER_SOLVER_MAIN( solv );
