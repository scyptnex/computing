/************************************************************************* 
 *                                bar.cpp                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Oct-13                                                     *
 *************************************************************************/

#include <iostream>
#include <string>
#include <vector>

using namespace std;

int main(int argc, char** argv){
    vector<string> args(argv+1, argv+argc);
    for(const auto& s : args){
        cout << s << endl;
    }
}
