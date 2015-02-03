/* 
 *                              DFCClient.cpp
 * 
 * Author: Nic H.
 * Date: 2015-Feb-02
 */

#include "DFCClient.h"

using namespace std;

//Default Constructor
DFCClient::DFCClient() {
    //TODO implementation
}

//Default Destructor
DFCClient::~DFCClient() {
    //TODO implementation
}

//Stream output
ostream& operator<<(ostream& os, const DFCClient& obj) {
    return os << "DFCClient@" << &obj;
}
