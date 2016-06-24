/************************************************************************* 
 *                               matrix.h                                *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-23                                                     *
 *                                                                       *
 * Convenience class for matrices.                                       *
 *************************************************************************/

#ifndef __MATRIX_H__
#define __MATRIX_H__

namespace sparse_matrix {

    template<unsigned S, typename T>
    struct matrix {
        typedef T this_t;
        static const unsigned side_length = S;
    
        virtual void set(unsigned, unsigned) = 0;
        virtual void unset(unsigned, unsigned) = 0;
        virtual bool get(unsigned, unsigned) const = 0;
    
        virtual T operator*(const T& other) const {
            T ret;
            for(int r=0; r<S; r++){
                for(int x=0; x<S; x++){
                    for(int c=0; c<S; c++){
                        if(get(r, c) && other.get(c, x)) ret.set(r, x);
                    }
                }
            }
            return ret;
        }
    
        template<typename OUT> void draw(OUT& out) const {
            for(int r=0; r<S; ++r){
                if(r != 0) out << "\n";
                for(int c=0; c<S; ++c){
                    if(c != 0) out << " ";
                    out << (get(r, c) ? "1" : "0");
                }
            }
        }
    };

} // end namespace

#endif /* __MATRIX_H__ */
