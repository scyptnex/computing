#include <cmath>
#include <cstdio>
#include <vector>
#include <iostream>
#include <algorithm>
using namespace std;


int main() {
    unsigned t;
    cin >> t;
    for(unsigned i=0; i<t; i++){
        unsigned long long n;
        cin >> n;
        bool curLouise = true;
        while(n > 1){
            unsigned long long ngpo2 = 1ull << (8*sizeof(unsigned long long) - __builtin_clzll(n) - 1);
            if(n-ngpo2){
                n -= ngpo2;
            } else {
                n /= 2;
            }
            curLouise = !curLouise;
        }
        cout << (curLouise ? "Richard": "Louise") << endl;
        
    }
    return 0;
}
