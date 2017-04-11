#include <sstream>

#include "time.h"

namespace week06 {

// Constructors

Time::Time() : totalSeconds(0) {}

Time::Time(int hours, int minutes, int seconds) : totalSeconds(seconds + (minutes + hours*60)*60) {}

Time::Time(const Time & copy) : totalSeconds(copy.totalSeconds) {}

Time::Time(int _totalSeconds) : totalSeconds(_totalSeconds) {}

// Member Functions

double Time::numberOfHours() const {
    return totalSeconds / 3600.0d;
}

double Time::numberOfMinutes() const {
    return totalSeconds / 60.0d;
}

int Time::numberOfSeconds() const {
    return totalSeconds;
}

// Macros are usually bad form, but i'm including this here to remind you what they are
#define prependZeroToStream(num) (num < 10 ? "0" : "") << (num)

std::string Time::getTimeAsString() const {
    std::stringstream ss;
    int hrs = totalSeconds/3600;
    int mins = (totalSeconds%3600)/60;
    int snds = (totalSeconds%60);
    ss << prependZeroToStream(hrs) << ":" << prependZeroToStream(mins) << ":" << prependZeroToStream(snds);
    return ss.str();
}

// Overloaded operators

Time& Time::operator=(const Time& rhs) {
    totalSeconds = rhs.totalSeconds;
    return *this;
}

Time Time::operator+(const Time& rhs) const {
    return Time(totalSeconds + rhs.totalSeconds);
}

Time Time::operator-(const Time& rhs) const {
    return Time(totalSeconds - rhs.totalSeconds);
}

bool Time::operator==(const Time& rhs) const {
    return totalSeconds == rhs.totalSeconds;
}

bool Time::operator>(const Time& rhs) const {
    return totalSeconds > rhs.totalSeconds;
}

bool Time::operator<(const Time& rhs) const {
    return totalSeconds < rhs.totalSeconds;
}

bool Time::operator!=(const Time& rhs) const {
    return !(*this==rhs);
}

bool Time::operator>=(const Time& rhs) const {
    return !(*this<rhs);
}

bool Time::operator<=(const Time& rhs) const {
    return !(*this>rhs);
}

Time operator+(const Time& lhs, int rhs) {
    return Time(lhs.numberOfSeconds()/3600, (lhs.numberOfSeconds()%3600)/60, (lhs.numberOfSeconds()%60) + rhs);
}

} // end namespace week06
