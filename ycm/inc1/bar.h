/************************************************************************* 
 *                                 bar.h                                 *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-20                                                     *
 *************************************************************************/

#ifndef __BAR_H__
#define __BAR_H__

#include "foo.h"

struct bar {
    static std::string b1() { return "BAR[" + foo::f1() + "]"; }
};

#endif /* __BAR_H__ */
