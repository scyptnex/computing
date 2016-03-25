#include <iostream>

#include "obj.h"

int main(int argc, char** argv){
    Obj o = Obj(argc);
    std::cout << "Obj: " << o.get() << std::endl;
    return 1;
}
