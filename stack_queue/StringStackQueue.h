#ifndef STRINGSTACKQUEUE_H
#define STRINGSTACKQUEUE_H

#include "StringQueue.h"
#include "StringStack.h"

#include <list>

class StringStackQueue : public StringStack, public StringQueue {

public:
    // General
    bool empty() const;
    size_t size() const;

    // Stack
    const std::string& peek() const;
    void push(const std::string& s);
    std::string pop();
    StringStack& operator=(const StringStack& other);

    // Queue
    const std::string& front() const;
    void enqueue(const std::string& string);
    std::string deqeue();
    StringQueue& operator=(const StringQueue& other);

private:
    // Adapter
    std::list<std::string> adaptee;
};

#endif // STRINGSTACKQUEUE_H
