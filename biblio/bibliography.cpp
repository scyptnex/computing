/************************************************************************* 
 *                           bibliography.cpp                            *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-17                                                     *
 *************************************************************************/

#include <algorithm>

#include "bibliography.h"

using namespace bib;

std::string trim(const std::string& str, const std::string& whitespace = " \t\n") {
    const auto strBegin = str.find_first_not_of(whitespace);
    if (strBegin == std::string::npos) return ""; // no content
    const auto strEnd = str.find_last_not_of(whitespace);
    const auto strRange = strEnd - strBegin + 1;
    return str.substr(strBegin, strRange);
}

std::string element::clean_string(const std::string& s){
    std::string ret = trim(s);
    auto length = ret.size();
    if (ret[0] == '"' && ret[length-1] == '"') {
        ret = ret.substr(1, length-2);
    } else if (ret[0] == '{' && ret[length-1] == '}' ) {
        ret = ret.substr(1, length-2);
    }
    return ret;
}

void element::sanitise(){
    name = clean_string(name);
    value = clean_string(value);
}

void entry::sanitise() {
    publication_type = element::clean_string(publication_type);
    std::transform(publication_type.begin(), publication_type.end(), publication_type.begin(), toupper);
    name = element::clean_string(name);
    for(auto& e : elements){
        e.sanitise();
    }
}

void bib::bibliography::sanitise() {
    for(auto& e : entries){
        e.sanitise();
    }
}

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
