/************************************************************************* 
 *                           bibliography.cpp                            *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-17                                                     *
 *************************************************************************/

#include "bibliography.h"

using namespace bib;

std::ostream& operator<<(std::ostream& o, const element& e){
    return o << e.name << "={" << e.value << "}";
}

std::ostream& operator<<(std::ostream& o, const entry& e){
    o << "@" << e.publication_type << "{" << e.name;
    for(const element& el : e.elements) o << "," << el;
    return o << "}";
}
std::ostream& operator<<(std::ostream& o, const bibliography& b){
    for(const entry& en : b.entries) o << en;
    return o;
}
