/* 
 *                              DFCServer.cpp
 * 
 * Author: Nic H.
 * Date: 2015-Feb-02
 */

#include "DFCServer.h"

#include <thread>

using namespace std;

void DFCServer::spawnLocalPlayer(int localClientPlayerID){
    cout << "I AM A THREAD LPC: " << localClientPlayerID << endl;
}

//Default Constructor
DFCServer::DFCServer() {
    //TODO implementation
}

//Default Destructor
DFCServer::~DFCServer() {
    //TODO implementation
}

/*
 * localClientPlayerID, player ID of the local client's player, -ive means no local player
 */
void DFCServer::run(int localClientPlayerID) {
    cout << "lol" << endl;
    thread * localClientThread = nullptr;
    if(localClientPlayerID >= 0){
        localClientThread = new thread(&DFCServer::spawnLocalPlayer, this, localClientPlayerID);
    }
    if(localClientThread != nullptr){
        localClientThread->join();
    }
}

//Stream output
ostream& operator<<(ostream& os, const DFCServer& obj) {
    return os << "DFCServer@" << &obj;
}
