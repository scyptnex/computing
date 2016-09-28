/************************************************************************* 
 *                               p164.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#include "problem.h"
#include <iostream>

void p164(int argc, char** argv){
    std::cout << argc << " I AM 164 HEAR ME ROAR args:" << std::endl;
    for(unsigned i=0; i<argc; i++){
        std::cout << "  - " << argv[i] << std::endl;
    }
}

REGISTER_SOLVER_MAIN( p164 );
