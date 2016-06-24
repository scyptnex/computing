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
class simple_matrix : public matrix<S, simple_matrix<S>> {

    private:
        std::vector<bool> mat;

    public:
        simple_matrix() : mat(S*S, false) {}
        void set(unsigned r, unsigned c) { mat[c*S + r] = true; }
        void unset(unsigned r, unsigned c) { mat[c*S + r] = false; }
        bool get(unsigned r, unsigned c) const { return mat[c*S + r]; }
};

} // end namespace

#endif /* __SIMPLE_MATRIX_H__ */
