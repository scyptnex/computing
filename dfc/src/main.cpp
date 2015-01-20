
#include "GameComponent.h"

#include <iostream>

using namespace std;

int main() {
    cout << "Hello world" << endl;
    dfc::GameComponent gc = dfc::GameComponent(0, "something", 12345);
    cout << "gc " << gc.getName() << endl;
    return 0;
}
