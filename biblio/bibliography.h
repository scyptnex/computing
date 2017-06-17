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

class parser;

struct element {
    std::string name;
    std::string value;
    element() {}
    element(const std::string& n, const std::string& v) : name(n), value(v) {}
};

struct entry {
    std::string publication_type;
    std::string name;
    std::vector<element> elements;
    entry() {}
    entry(const std::string& pt, const std::string& n) : publication_type(pt), name(n) {}
    void add(element&& el){
        elements.push_back(el);
    }
};

struct bibliography {
    std::vector<entry> entries;
};

} // end namespace bib

std::ostream& operator<<(std::ostream& o, const bib::element& e);
std::ostream& operator<<(std::ostream& o, const bib::entry& e);
std::ostream& operator<<(std::ostream& o, const bib::bibliography& b);

#endif /* __BIBLIOGRAPHY_H__ */
