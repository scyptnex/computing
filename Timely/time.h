#ifndef TIME_H
#define TIME_H

#include <string>

namespace week06 {

class Time
{
public:
    Time(); // this was private on the website, but actually it should be public
    Time(int hours, int minutes, int seconds);
    Time(const Time&); // copy constructor

    ~Time() = default;

    double numberOfHours() const;
    double numberOfMinutes() const;
    int numberOfSeconds() const;
    std::string getTimeAsString() const;

    Time& operator=(const Time&);
    Time operator+(const Time&) const;
    Time operator-(const Time&) const;
    bool operator==(const Time &time) const;
    bool operator!=(const Time &time) const;
    bool operator<(const Time &time) const;
    bool operator>(const Time &time) const;
    bool operator<=(const Time &time) const;
    bool operator>=(const Time &time) const;

private:
    Time(int totalSeconds); // this constructor is used internally, but since it manipulates seconds directly it should not be publicly visible
    int totalSeconds;
};

Time operator+(const Time& lhs, int numberOfSeconds);

} // end namespace week06

#endif // TIME_H
