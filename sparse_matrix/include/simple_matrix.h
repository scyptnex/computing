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

namespace sparse_matrix {

template<unsigned S>
class simple_matrix {

    public:
        const static unsigned side_length = S;
        static inline void multiply(const simple_matrix<S>& left, const simple_matrix<S>& right, simple_matrix<S>& out) {
            for(int r=0; r<side_length; r++){
                for(int x=0; x<side_length; x++){
                    for(int c=0; c<side_length; c++){
                        if(left.get(r, c) && right.get(c, x)) out.set(r, x);
                    }
                }
            }
        }

    private:
        std::vector<bool> mat;

    public:
        simple_matrix() : mat(S*S, false) {}
        void set(unsigned r, unsigned c) { mat[c*S + r] = true; }
        void unset(unsigned r, unsigned c) { mat[c*S + r] = false; }
        bool get(unsigned r, unsigned c) const { return mat[c*S + r]; }
        simple_matrix<S> operator*(const simple_matrix<S>& other) const {
            simple_matrix<S> ret;
            multiply(*this, other, ret);
            return ret;
        }

        template<typename OUT> void draw(OUT& out) const {
            matrix_utils<simple_matrix<S>>::draw(*this, out);
        }
};

} // end namespace

#endif /* __SIMPLE_MATRIX_H__ */
