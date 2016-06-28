/************************************************************************* 
 *                          dense_bit_matrix.h                           *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-23                                                     *
 *                                                                       *
 * Template implementation of a densely-represented row-major bit-matrix *
 * as stored in the bits of the given type.                              *
 *************************************************************************/

#ifndef __DENSE_BIT_MATRIX_H__
#define __DENSE_BIT_MATRIX_H__

#include <cstring>

#include "matrix.h"
#include "static_utils.h"

namespace sparse_matrix {

    template<typename T>
    class dense_bit_matrix : public matrix<static_utils::square_root(sizeof(T)*8), dense_bit_matrix<T>> {

        private:
            T data;
            static inline constexpr unsigned shift(unsigned r, unsigned c) { return c*dense_bit_matrix<T>::side_length + r; }

        public:

            static const unsigned available_bits = sizeof(T)*8;

            dense_bit_matrix() : data() {
                std::memset(&data, 0, sizeof(T));
            }

            void set(unsigned r, unsigned c) {
                data |= 1 << shift(r,c);
            }

            void unset(unsigned r, unsigned c) {
                data &= ~(1 << shift(r,c));
            }

            bool get(unsigned r, unsigned c) const {
                return 1&(data >> shift(r,c));
            } 

            dense_bit_matrix<T> operator*(const dense_bit_matrix<T>& other) const {

                return dense_bit_matrix<T>();
            }
    };

} // end namespace

#endif /* __DENSE_BIT_MATRIX_H__ */
