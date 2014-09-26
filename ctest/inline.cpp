#include <iostream>

using namespace std;

inline void prt(bool val){
    if(val){
        cout << "tr" << endl;
    } else {
        cout << "fa" << endl;
    }
}

int main(int argc, char** agrv){
    if(argc >= 2){
        prt(true);
    } else {
        prt(false);
    }
    return 0;
}

