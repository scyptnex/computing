/************************************************************************* 
 *                            matrix_tests.h                             *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-24                                                     *
 *                                                                       *
 * The standard suite of tests for matrices                              *
 *************************************************************************/

#ifndef __MATRIX_TESTS_H__
#define __MATRIX_TESTS_H__

#include <sstream>
#include <iostream>

#include <boost/test/unit_test.hpp>

namespace sparse_matrix {

    template<typename T> void print_test(){
        T sm;
        BOOST_CHECK(sm.side_length == 2);
        std::stringstream ss1;
        sm.draw(ss1);
        BOOST_CHECK(ss1.str() == "0 0\n0 0");
        sm.set(0,0);
        sm.set(1,0);
        std::stringstream ss2;
        sm.draw(ss2);
        BOOST_CHECK(ss2.str() == "1 0\n1 0");
    }

    template<typename T> void set_test(){
        T mat;
        BOOST_CHECK(mat.side_length >= 2);
        BOOST_CHECK(!mat.get(0,0));
        BOOST_CHECK(!mat.get(0,1));
        BOOST_CHECK(!mat.get(1,0));
        BOOST_CHECK(!mat.get(1,1));
        mat.set(1,1);
        mat.set(0,0);
        mat.set(1,0);
        mat.unset(1,1);
        BOOST_CHECK(mat.get(0,0));
        BOOST_CHECK(!mat.get(0,1));
        BOOST_CHECK(mat.get(1,0));
        BOOST_CHECK(!mat.get(1,1));
    }

    template<typename T> void mult_test(){
        T m1, m2;
        BOOST_CHECK(T::side_length >= 2);
        m1.set(0,1);
        m2.set(1,0);
        auto m12 = m1*m2;
        BOOST_CHECK( m12.get(0, 0));
        BOOST_CHECK(!m12.get(0, 1));
        BOOST_CHECK(!m12.get(1, 0));
        BOOST_CHECK(!m12.get(1, 1));
        auto m21 = m2*m1;
        BOOST_CHECK(!m21.get(0, 0));
        BOOST_CHECK(!m21.get(0, 1));
        BOOST_CHECK(!m21.get(1, 0));
        BOOST_CHECK( m21.get(1, 1));
    }
}

#endif /* __MATRIX_TESTS_H__ */
