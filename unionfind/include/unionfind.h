/* 
 *                               unionfind.h
 * 
 * Author: Nic H.
 * Date: 2016-Apr-01
 */

#ifndef __UNIONFIND_H__
#define __UNIONFIND_H__

#include <iostream>
#include <vector>

struct VUF{
    typedef std::size_t Ty;
    std::vector<Ty> parents;
    std::vector<Ty> ranks;

    void un(Ty a, Ty b){
        extend(std::max(a, b));
        un_r(a, b);
    }

    Ty fi(Ty i){
        extend(i);
        return fi_r(i);
    }

private:
    void extend(Ty n){
        auto cur = parents.size();
        if(n < cur) return;
        ranks.resize(n+1, 1);
        parents.resize(n+1);
        for(auto i=cur; i<=n; i++) parents[i] = i;
    }
    void un_r(Ty x, Ty y){
        Ty xr = fi(x);
        Ty yr = fi(y);
        if(xr == yr) return;
        if(ranks[xr] < ranks[yr]){
            parents[xr] = yr;
        } else if (ranks[yr] < ranks[xr]) {
            parents[yr] = xr;
        } else {
            parents[yr] = xr;
            ranks[xr]++;
        }
    }

    Ty fi_r(Ty i){
        if(parents[i] != i){
            parents[i] = fi_r(parents[i]);
        }
        return parents[i];
    }
};

std::ostream& operator<<(std::ostream& os, const VUF& uf){
    for(unsigned i=0; i<uf.parents.size(); i++){
        os << i << ": " << uf.parents[i] << " [" << uf.ranks[i] << "]" << std::endl;
    }
    return os;
}

#endif /* __UNIONFIND_H__ */
