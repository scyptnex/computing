#include <iostream>

#include "TestClass.h"

using namespace std;

int main()
{
	TestClass* tc = new TestClass(7);
    cout << "Hello world!" << endl;
    tc->speak();
    delete(tc);
    return 0;
}
