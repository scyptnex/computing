#include <sstream>

#include "date.h"

namespace Week4 {

Date::Date(int day, int month, int year) :
    m_day(day),
    m_month(month),
    m_year(year)
{}

Date::~Date() {
    //do nothing
}

int Date::getYear() const {
    return m_year;
}

int Date::getMonth() const {
    return m_month;
}

int Date::getDay() const {
    return m_day;
}

/// Helper function used to print a leading zero when the date's
/// value is too small for DD/MM/YYYY format
inline std::string prependZero(int num) {
    return num > 0 ? "" : "0";
}

std::string Date::dateAsString() const {
    std::stringstream ss;
    int d = getDay();
    int m = getMonth();
    int y = getYear();
    ss << prependZero(d/10) << d
       << "/" << prependZero(m/10) << m
       << "/" << prependZero(y/1000) << prependZero(y/100) << prependZero(y/10) << y;
    return ss.str();
}

}
