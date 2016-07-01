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

    template<typename Mat>
    struct matrix_utils {
    
        template<typename OUT> static inline void draw(const Mat& mat, OUT& out){
            for(int r=0; r<Mat::side_length; ++r){
                if(r != 0) out << "\n";
                for(int c=0; c<Mat::side_length; ++c){
                    if(c != 0) out << " ";
                    out << (mat.get(r, c) ? "1" : "0");
                }
            }
        }

    };

} // end namespace

#endif /* __MATRIX_H__ */
