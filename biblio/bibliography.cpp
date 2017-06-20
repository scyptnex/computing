/************************************************************************* 
 *                           bibliography.cpp                            *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Jun-17                                                     *
 *************************************************************************/

#include <algorithm>

#include "bibliography.h"

using namespace bib;

/// https://stackoverflow.com/a/33380725
bool string_ignore_less(const std::string& lhs, const std::string& rhs){
    auto char_iequal = [](const auto& lhs, const auto& rhs){return tolower(lhs) == tolower(rhs);};
    const auto result = std::mismatch(lhs.cbegin(), lhs.cend(), rhs.cbegin(), rhs.cend(), char_iequal);
    return result.second != rhs.cend() && (result.first == lhs.cend() || tolower(*result.first) < tolower(*result.second));
}

bool string_ignore_equal(const std::string& lhs, const std::string& rhs){
    auto char_iequal = [](const auto& lhs, const auto& rhs){return tolower(lhs) == tolower(rhs);};
    const auto result = std::mismatch(lhs.cbegin(), lhs.cend(), rhs.cbegin(), rhs.cend(), char_iequal);
    return result.first == lhs.cend() && result.second == rhs.cend();
}

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
    // if (ret[0] == '"' && ret[length-1] == '"') {
    //     ret = ret.substr(1, length-2);
    return ret;
}

void element::sanitise(){
    name = clean_string(name);
    value = clean_string(value);
    auto length = value.size();
    if (string_ignore_equal(name, "month") && value[0] == '{' && value[length-1] == '}' ) {
         value = trim(value.substr(1, length-2));
    }
}

const std::vector<std::string> bib::element::priorities = std::vector<std::string>({"title", "author", "year", "url", "doi"});
bool element::operator<(const element& other) const {
    for (const auto& s : bib::element::priorities) {
        if (string_ignore_equal(name, s)) return true;
        if (string_ignore_equal(other.name, s)) return false;
    }
    return false; // other elements have no ordering
    //return string_ignore_less(name, other.name);
}

void entry::sanitise() {
    publication_type = element::clean_string(publication_type);
    // std::transform(publication_type.begin(), publication_type.end(), publication_type.begin(), toupper); // TODO when we want uppercase entries
    name = element::clean_string(name);
    for(auto& e : elements){
        e.sanitise();
    }
    std::stable_sort(elements.begin(), elements.end());
}

bool entry::operator<(const entry& other) const{
    return string_ignore_less(name, other.name);
}

void bib::bibliography::sanitise() {
    for(auto& e : entries){
        e.sanitise();
    }
    std::sort(entries.begin(), entries.end());
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
