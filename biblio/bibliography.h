/************************************************************************* 
 *                            bibliography.h                             *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-08                                                     *
 *************************************************************************/

#ifndef __BIBLIOGRAPHY_H__
#define __BIBLIOGRAPHY_H__

#include <iostream>
#include <string>
#include <vector>

namespace bib {

struct element {
    std::string name;
    std::string value;
    element(const std::string& n, const std::string& v) : name(n), value(v) {}
};

struct entry {
    std::string publication_type;
    std::string name;
    std::vector<element> elements;
    entry(const std::string& pt, const std::string& n) : publication_type(pt), name(n) {}
    void add(element&& el){
        elements.push_back(el);
    }
};

struct bibliography {
    std::vector<entry> entries;
};

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

}

#endif /* __BIBLIOGRAPHY_H__ */
