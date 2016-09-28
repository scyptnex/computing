/************************************************************************* 
 *                               problem.h                               *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Sep-28                                                     *
 *************************************************************************/

#ifndef __PROBLEM_H__
#define __PROBLEM_H__

#include <memory>

struct problem {
    virtual void solve(int, char**) = 0;
};

struct registrar {
    std::unique_ptr<problem> prob;
};
extern registrar regi;

#define REGISTER_SOLVER_MAIN(PNAME) \
    struct PNAME ## _wrapper : public problem { \
        void solve(int argc, char** argv) { \
            PNAME(argc, argv); \
        } \
    }; \
    struct PNAME ## _registration_proxy { \
        PNAME ## _registration_proxy() { \
            regi.prob = std::unique_ptr<problem>( new PNAME ## _wrapper ); \
        } \
    }; \
    PNAME ## _registration_proxy proxied_ ## PNAME ; \

#endif /* __PROBLEM_H__ */

