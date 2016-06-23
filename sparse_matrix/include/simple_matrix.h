/************************************************************************* 
 *                            simple_matrix.h                            *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-23                                                     *
 *                                                                       *
 * A simple vector<bool> reference implementation for the matrix         *
 *************************************************************************/

#ifndef __SIMPLE_MATRIX_H__
#define __SIMPLE_MATRIX_H__

#include <vector>

#include "matrix.h"

template<unsigned R, unsigned C>
class simple_matrix : public matrix<R, C, simple_matrix> {

    private:
        std::vector<bool> mat;

    public:
        simple_matrix() : mat(R*C, false) {}
        void set(unsigned r, unsigned c) { mat[c*R + r] = true; }
        void unset(unsigned r, unsigned c) { mat[c*R + r] = false; }
        bool get(unsigned r, unsigned c) const { return mat[c*R + r]; }
        template<unsigned X>
        simple_matrix<R,X> operator*(const simple_matrix<C, X>& other){
            simple_matrix<R, X> ret;
            for(int r=0; r<R; r++){
                for(int x=0; x<X; x++){
                    for(int c=0; c<C; c++){
                        if(get(r, c) && other.get(c, x)) ret.set(r, x);
                    }
                }
            }
            return ret;
        }
};

#endif /* __SIMPLE_MATRIX_H__ */
