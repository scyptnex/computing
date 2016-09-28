/************************************************************************* 
 *                              rational.h                               *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#ifndef __RATIONAL_H__
#define __RATIONAL_H__

#include <iostream>

template<typename T>
constexpr T gcd(T a, T b){
    return b == 0 ? a : gcd(b, a%b);
}

template<typename T>
struct rational {
    // members
    T num;
    T den;
    // constructors
    rational() : num(0), den(1) {}
    rational(T _num) : num(_num), den(1) {}
    rational(T _num, T _den) : num(_num), den(_den) { simplify(); }
    // arithmetic operators
    rational<T> operator+(const rational& rhs) const { return {num*rhs.den + den*rhs.num, den*rhs.den}; }
    rational<T> operator-(const rational& rhs) const { return {num*rhs.den - den*rhs.num, den*rhs.den}; }
    rational<T> operator*(const rational& rhs) const { return {num*rhs.num, den*rhs.den}; }
    rational<T> operator/(const rational& rhs) const { return {num*rhs.den, den*rhs.num}; }
    rational<T> operator-() const { return {-num, den}; }
    rational<T> scale(T factor) const { return {num*factor, den}; }
    // comparisons
    bool operator==(const rational& rhs) const { return num*rhs.den == den*rhs.num; }//some would say two multiplications is inefficient...
    bool operator<( const rational& rhs) const { return num*rhs.den < den*rhs.num; }
    bool operator>( const rational& rhs) const { return num*rhs.den > den*rhs.num; }
    bool operator!=(const rational& rhs) const { return !(*this == rhs); }
    bool operator<=(const rational& rhs) const { return rhs > *this; }
    bool operator>=(const rational& rhs) const { return rhs < *this; }
private:
    void simplify(){
        T g = gcd(num, den);
        num = num/g;
        den = den/g;
    }
};

template<typename T>
std::ostream& operator<<(std::ostream& in, const rational<T>& r){
    return in << r.num << "/" << r.den;
}

#endif /* __RATIONAL_H__ */
