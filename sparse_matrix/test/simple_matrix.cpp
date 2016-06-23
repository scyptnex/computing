
#include <sstream>
#include <iostream>

#include <boost/test/unit_test.hpp>

#include "simple_matrix.h"

BOOST_AUTO_TEST_SUITE( test_simple_matrix )

    BOOST_AUTO_TEST_CASE ( f2 ){
        simple_matrix<2,2> sm;
        BOOST_CHECK(sm.rows == 2);
        BOOST_CHECK(sm.cols == 2);
        std::stringstream ss1;
        sm.draw(ss1);
        BOOST_CHECK(ss1.str() == "0 0\n0 0");
        sm.set(0,0);
        sm.set(1,0);
        BOOST_CHECK(sm.get(0,0));
        BOOST_CHECK(!sm.get(0,1));
        BOOST_CHECK(sm.get(1,0));
        BOOST_CHECK(!sm.get(1,1));
        std::stringstream ss2;
        sm.draw(ss2);
        BOOST_CHECK(ss2.str() == "1 0\n1 0");
    }

BOOST_AUTO_TEST_SUITE_END()
