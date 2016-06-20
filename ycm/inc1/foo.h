/************************************************************************* 
 *                                 foo.h                                 *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-20                                                     *
 *************************************************************************/

#ifndef __FOO_H__
#define __FOO_H__

#include <string>

struct foo {
    static std::string f1() { return "hi there f1"; }
};

#endif /* __FOO_H__ */
