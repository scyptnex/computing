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

template<unsigned R, unsigned C, template<unsigned, unsigned> class T>
struct matrix {
    typedef T<R, C> this_t;
    static const unsigned rows = R;
    static const unsigned cols = C;

    virtual void set(unsigned, unsigned) = 0;
    virtual void unset(unsigned, unsigned) = 0;
    virtual bool get(unsigned, unsigned) const = 0;

    template<typename OUT> void draw(OUT& out) const {
        for(int r=0; r<R; ++r){
            if(r != 0) out << "\n";
            for(int c=0; c<C; ++c){
                if(c != 0) out << " ";
                out << (get(r, c) ? "1" : "0");
            }
        }
    }
};

#endif /* __MATRIX_H__ */
