#include "StringStackQueue.h"

//
// General
//

bool StringStackQueue::empty() const {
    return adaptee.empty();
}

size_t StringStackQueue::size() const {
    return adaptee.size();
}

//
// Stack
//

const std::string& StringStackQueue::peek() const {
    return adaptee.back();
}

void StringStackQueue::push(const std::string& s) {
    adaptee.push_back(s);
}

std::string StringStackQueue::pop() {
    if(empty()) return "";
    std::string ret = peek();
    adaptee.pop_back();
    return ret;
}

StringStack& StringStackQueue::operator=(const StringStack& other) {
    adaptee.clear();
    StringStack& o = const_cast<StringStack&>(other);
    while(!o.empty()) adaptee.push_front(o.pop());
    for(const std::string& s : adaptee) o.push(s);
    return *this;
}

//
// Queue
//

const std::string& StringStackQueue::front() const {
    return adaptee.front();
}

void StringStackQueue::enqueue(const std::string& s) {
    push(s);
}

std::string StringStackQueue::deqeue() {
    if(empty()) return "";
    std::string ret = front();
    adaptee.pop_front();
    return ret;
}

StringQueue& StringStackQueue::operator=(const StringQueue& other) {
    adaptee.clear();
    StringQueue& o = const_cast<StringQueue&>(other);
    while(!o.empty()) push(o.deqeue());
    for(const std::string& s : adaptee) o.enqueue(s);
    return *this;
}
