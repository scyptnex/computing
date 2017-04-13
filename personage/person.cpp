#include "person.h"

namespace Week4 {

Person::Person(const std::string& firstName,
               const std::string& surname,
               const Date& dateOfBirth) :
    m_firstName(firstName),
    m_surname(surname),
    m_dateOfBirth(dateOfBirth)
{}

Person::~Person() {
    //do nothing
}

void Person::changeFirstName(const std::string& firstName) {
    m_firstName = firstName;
}

void Person::changeSurname(const std::string& surname) {
    m_surname = surname;
}

const std::string& Person::getFirstName() const {
    return m_firstName;
}

const std::string& Person::getSurname() const {
    return m_surname;
}

int Person::getYearOfBirth() {
    return m_dateOfBirth.getYear();
}

bool Person::isBornInLeapYear() {
    int y = getYearOfBirth();
    return y%400==0 || (y%4==0 && y%100!=0);
}

}
