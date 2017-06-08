/************************************************************************* 
 *                               main.cpp                                *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-07                                                     *
 *************************************************************************/

#include <iostream>
#include <string>

#include "bibliography.h"
#include "parser.tab.hpp"

using namespace bib;

int main(){
    element e("hi", "there");
    element e2("hi", "you are cool");
    entry en("book", "hollingum2015towards");
    en.add(std::move(e));
    en.add(std::move(e2));
    std::cout << en << std::endl;

    //parsing stuff
    extern int yydebug;
    yydebug = 1;
    yyparse();

    return 0;
}

