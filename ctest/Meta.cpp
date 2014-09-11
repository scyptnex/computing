#include <iostream>

static inline void visit(unsigned n) { std::cout << n << "\n"; }

template <int I>
void f(unsigned n) {
	if (n == I) {
		if (I & 0x01) visit(1);
		if (I & 0x02) visit(2);
		if (I & 0x04) visit(3);
		if (I & 0x08) visit(4);
		if (I & 0x10) visit(5);
		if (I & 0x20) visit(6);
		if (I & 0x40) visit(7);
		if (I & 0x80) visit(8);
	} else {
		f<I-1>(n);
	}
}

template <>
void f<0>(unsigned n) { }

int main() {
    unsigned int val;
    std::cout << "insert val: ";
    std::cin >> val;
	f<256>(val);
}

