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
#include "meta_utils.h"

namespace sparse_matrix {

    

    template<typename T>
    class dense_bit_matrix {

        public:
            static const unsigned available_bits = sizeof(T)*8;
            static const T UNIT = 1;
            static const unsigned side_length = meta::arithmertic::square_root(available_bits);

            static inline void multiply(const dense_bit_matrix<T>& l, const dense_bit_matrix<T>& r, dense_bit_matrix<T>& out){
                T transp = 0;
                typedef typename meta::range<0,dense_bit_matrix<T>::side_length>::type t_iter;
                typedef typename meta::cross<t_iter, t_iter>::type t_rcs;
                meta::foreach<typename meta::map<t_rcs, transp_help>::type, transp_help>::go(r.data, transp);
                meta::foreach<typename meta::map<t_rcs, mult_map>::type, mult_use>::go(l.data, transp, out.data);
            }

        private:
            T data;
            static inline constexpr T shift(unsigned r, unsigned c) { return r*side_length + c; }
            
            // template meta-helpers for multiplication
            static const T MASK = (UNIT<<side_length)-UNIT;
            template<typename> struct transp_help;
            template<meta::numeric R, meta::numeric C> struct transp_help<meta::pair<meta::numeric_t<R>, meta::numeric_t<C>>> {
                typedef meta::pair<meta::numeric_t<shift(R, C)>, meta::numeric_t<shift(C, R)>> type;
                static inline void go(const T& from, T& to){
                    to |= ((from>>R)&UNIT)<<C;
                };
            };
            template<typename> struct mult_map;
            template<meta::numeric I, meta::numeric J> struct mult_map<meta::pair<meta::numeric_t<I>, meta::numeric_t<J>>> {
                typedef meta::pair<meta::pair<meta::numeric_t<dense_bit_matrix<T>::side_length*I>, meta::numeric_t<dense_bit_matrix<T>::side_length*J>>, meta::numeric_t<shift(I, J)>> type;
            };
            template<typename> struct mult_use;
            template<meta::numeric As, meta::numeric Bs, meta::numeric O> struct mult_use<meta::pair<meta::pair<meta::numeric_t<As>, meta::numeric_t<Bs>>, meta::numeric_t<O>>> {
                static inline void go(const T& l, const T& r, T& out){
                    if((l>>As)&(r>>Bs)&MASK) out |= UNIT << O;
                }
            };

        public:

            dense_bit_matrix() : data() {
                std::memset(&data, 0, sizeof(T));
            }

            void set(unsigned r, unsigned c) {
                data |= UNIT << shift(r,c);
            }

            void unset(unsigned r, unsigned c) {
                data &= ~(UNIT << shift(r,c));
            }

            bool get(unsigned r, unsigned c) const {
                return UNIT&(data >> shift(r,c));
            } 

            dense_bit_matrix<T> operator*(const dense_bit_matrix<T>& other) const {
                dense_bit_matrix<T> ret;
                multiply(*this, other, ret);
                return ret;
            }

            template<typename OUT> void draw(OUT& out) const {
                matrix_utils<dense_bit_matrix<T>>::draw(*this, out);
            }
    };

} // end namespace

#endif /* __DENSE_BIT_MATRIX_H__ */
