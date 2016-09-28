/************************************************************************* 
 *                               main.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/
#include "problem.h"
#include <dlfcn.h>
#include <iostream>
#include <string>

using namespace std;

// instantiate the registrar which is used to store the last loaded problem
registrar regi;

//i could recursively call main, but that feels dirty...
int solve_problem(int argc, char** argv){
    // return when we run out of args
    if (argc <= 0) return 0;
    string libname = "./lib" + string(argv[0]) + ".so";
    cout << "Problem " << argv[0] << " - " << libname << endl;
    void* handle = dlopen(libname.c_str(), RTLD_NOW);
    if (!handle) {
        std::cerr << "failure" << std::endl;
        std::cerr << dlerror() << std::endl;
        return 1;
    }
    regi.prob->solve(argc, argv);
    return dlclose(handle);
}

int main(int argc, char** argv){
    if(argc <= 1){
        std::cerr << "Usage:" << std::endl;
        std::cerr << "  euler {<problem name> [problem args]}" << std::endl;
        return 1;
    }
    return solve_problem(argc-1, argv+1);
}
