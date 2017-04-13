#ifndef PERSON_H
#define PERSON_H

#include "date.h"
#include <string>

namespace Week4
{
    class Person
    {
    public:
        Person(const std::string& firstName,
            const std::string& surname,
            const Date& dateOfBirth);
        virtual ~Person();

        void changeFirstName(const std::string& firstName);
        const std::string& getFirstName() const;

        void changeSurname(const std::string& surname);
        const std::string& getSurname() const;

        int getYearOfBirth();
        bool isBornInLeapYear();

        int getCurrentAge();

        virtual std::string getRecord() const = 0;

    protected:
        std::string m_firstName;
        std::string m_surname;
        Date m_dateOfBirth;
    };
}
#endif
