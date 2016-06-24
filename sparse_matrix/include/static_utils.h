/************************************************************************* 
 *                            static_utils.h                             *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-24                                                     *
 *                                                                       *
 * static/constexpr utilities used in the library.                       *
 *************************************************************************/

#ifndef __STATIC_UTILS_H__
#define __STATIC_UTILS_H__

namespace sparse_matrix {

    class static_utils {

        private:
            static constexpr unsigned _square_root(unsigned target, unsigned low, unsigned high){
                return low == high ? low : ((target / mid(low, high) < mid(low, high)) ? _square_root(target, low, mid(low, high) - 1) : _square_root(target, mid(low, high), high));
            }
        public:
            static constexpr unsigned mid(unsigned a, unsigned b){
                return (a+b+1)/2;
            }
            static constexpr unsigned square_root(unsigned i){
                return _square_root(i, 0, i/2+1);
            }

    };

} // end namespace sparse_matrix

#endif /* __STATIC_UTILS_H__ */
