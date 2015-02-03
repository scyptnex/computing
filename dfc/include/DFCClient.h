/* 
 *                               DFCClient.h
 * 
 * Author: Nic H.
 * Date: 2015-Feb-02
 */

#ifndef __D_F_C_CLIENT_H__
#define __D_F_C_CLIENT_H__

#include <iostream>

class DFCClient final {

// SINGLETOND PATTERN
private:
    DFCClient();
public:
    static DFCClient& getInstance(){
        static DFCClient instance;
        return instance;
    }
    DFCClient(const DFCClient& dfcc) = delete;
    void operator=(const DFCClient& dfcc) = delete;
//END SINGLETON PATTERN

public:
    virtual ~DFCClient();
    void dump() const{std::cout << *this;}
//friends
    friend std::ostream& operator<<(std::ostream&, const DFCClient&);
};

std::ostream& operator<<(std::ostream&, const DFCClient&);

#endif /* __D_F_C_CLIENT_H__ */
