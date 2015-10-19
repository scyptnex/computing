#include <iostream>
#include <algorithm>
using namespace std;
/*
 * Complete the function below.
 */
int maxXor(int l, int r) {
    int max = l^r;
    for(int a=l; a<=r; a++){
        for(int b=a+1; b<=r; b++){
            max = std::max(max, a^b);
        }
    }
    return max;
}

int main() {
    int res;
    int _l;
    cin >> _l;
    
    int _r;
    cin >> _r;
    
    res = maxXor(_l, _r);
    cout << res;
    
    return 0;
}
