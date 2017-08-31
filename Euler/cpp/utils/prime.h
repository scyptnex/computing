/************************************************************************* 
 *                                prime.h                                *
 *                                                                       *
 * Author: nic                                                           *
 * Date: 2017-Aug-31                                                     *
 *************************************************************************/

#ifndef __PRIME_H__
#define __PRIME_H__

#include <vector>

class prime_sieve_iterator {
public:
    prime_sieve_iterator(std::vector<bool>::const_iterator, unsigned long max);
    bool operator!=(const prime_sieve_iterator& other) const;
    void operator++();
    unsigned long operator*() const;
private:
    unsigned long nextPrime;
    unsigned long max;
    std::vector<bool>::const_iterator iter;
};

class prime_sieve {
public:
    prime_sieve(unsigned long);
    prime_sieve_iterator begin() const;
    prime_sieve_iterator end() const;
    std::vector<unsigned long> enumerate() const;
    void process(void (*f)(unsigned long)) const;
private:
    unsigned long max;
    std::vector<bool> marks;
};

#endif /* __PRIME_H__ */
