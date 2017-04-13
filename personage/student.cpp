#include <sstream>

#include "student.h"

namespace Week4 {

Student::Student(const std::string &givenName,
                 const std::string &surname,
                 const Date &date,
                 const std::string &degree) :
    Person(givenName, surname, date),
    m_degree(new std::string(degree)),
    m_recordAccessed(0)
{}

Student::~Student(){
    delete m_degree;
}

std::string Student::getRecord() const {
    std::stringstream ss;
    ss << "Name: " << getFirstName() << " " << getSurname() << std::endl
          << "Date Of Birth: " << m_dateOfBirth.dateAsString() << std::endl
          << "Degree: " << *m_degree;
    return ss.str();
}

}
