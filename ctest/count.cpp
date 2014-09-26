#include <iostream>
#include <sstream>

using namespace std;

int main(int argc, char** argv){
    if(argc != 2){
        cerr << "bad" << endl;
        return 1;
    }
    istringstream ss(argv[1]);
    unsigned long x;
    if(!(ss >> x)){
        cerr << "bad long" << endl;
        return 1;
    }
    cout << "hi " << x << " - " << __builtin_clzl(x) << endl;
    return 0;
}
