#include <iostream>
#include "unionfind.h"

int main(int argc, char** argv){
    VUF field;
    field.fi(0);
    field.fi(7);
    field.un(4, 5);
    field.un(4, 7);
    field.un(2, 4);
    std::cout << field;
    return 0;
}
