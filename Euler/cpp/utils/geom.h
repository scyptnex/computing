/************************************************************************* 
 *                                geom.h                                 *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#ifndef __GEOM_H__
#define __GEOM_H__

#include <iostream>

template<typename T> struct vec3{
    T x;
    T y;
    T z;
    vec3() : x(0), y(0), z(0) {}
    vec3(T _x, T _y) : x(_x), y(_y), z(0) {}
    vec3(T _x, T _y, T _z) : x(_x), y(_y), z(_z) {}
};
template<typename T> std::ostream& operator<<(std::ostream& in, const vec3<T>& v){
    return in << "(" << v.x << "," << v.y << "," << v.z << ")";
}
template<typename T> vec3<T> operator-(const vec3<T>& l, const vec3<T>& r){
    return {l.x-r.x, l.y-r.y, l.z-r.z};
}

template<typename T> struct line{
    vec3<T> pos;
    vec3<T> dir;
    line() : pos(), dir() {}
    line(vec3<T> _pos, vec3<T> _dir) : pos(_pos), dir(_dir) {}
    static inline line<T> from_points(vec3<T> a, vec3<T> b){
        return {a, {b.x-a.x, b.y-a.y, b.z-a.z}};
    }
};
template<typename T> std::ostream& operator<<(std::ostream& in, const line<T>& l){
    return in << l.pos << "--" << l.dir;
}

#endif /* __GEOM_H__ */
