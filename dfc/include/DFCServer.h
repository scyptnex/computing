/* 
 *                               DFCServer.h
 * 
 * Author: Nic H.
 * Date: 2015-Feb-02
 */

#ifndef __D_F_C_SERVER_H__
#define __D_F_C_SERVER_H__

#include <iostream>

class DFCServer {
private:
    void spawnLocalPlayer(int localClientPlayerID);
public:
    DFCServer();
    virtual ~DFCServer();
    void run(int localClientPlayerID);
    void dump() const{std::cout << *this;}
//friends
    friend std::ostream& operator<<(std::ostream&, const DFCServer&);
};

std::ostream& operator<<(std::ostream&, const DFCServer&);

#endif /* __D_F_C_SERVER_H__ */
