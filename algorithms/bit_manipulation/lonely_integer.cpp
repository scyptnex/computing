#include <iostream>
using namespace std;

int main() {
unsigned res;
int size;
cin >> size;
for(unsigned i=0; i<size; i++){
unsigned tmp;
cin >> tmp;
res ^= tmp;
}
cout << res;
return 0;
}

