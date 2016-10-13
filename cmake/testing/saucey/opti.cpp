#include <iostream>

unsigned long facto(unsigned long depth){
    if(depth > 0) return depth*facto(depth-1);
    else return 1;
}

int main(){
    std::cout << facto(8) << std::endl;
}
