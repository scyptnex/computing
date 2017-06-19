/************************************************************************* 
 *                               main.cpp                                *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-07                                                     *
 *************************************************************************/

#include <iostream>
#include <string>

#include "bibliography.h"
#include "parser.hpp"
#include "scanner.h"

using namespace bib;

void pprint(std::ostream& os, const bib::element& el) {
    os << el.name << "=" << el.value;
}

void pprint(std::ostream& os, const bib::entry& en) {
    os << '@' << en.publication_type << "{" << en.name;
    for(const auto& el : en.elements) {
        os << "," << std::endl << "    ";
        pprint(os, el);
    }
    os << std::endl << "}";
}

int main(int argc, char* argv[]){
    //parsing stuff
    bib::scanner sca;
    bib::bibliography bbl;
    bib::parser psr(sca, bbl);
    if(argc > 1) psr.set_debug_level(1);
    int res = psr.parse();
    bbl.sanitise();
    for(auto& e : bbl.entries){
        pprint(std::cout, e);
        std::cout << std::endl << std::endl;
    }
    return 0;
}

