#include <boost/test/unit_test.hpp>

BOOST_AUTO_TEST_SUITE( foo )

    BOOST_AUTO_TEST_CASE ( f1 ){
        BOOST_CHECK(true);
    }

    BOOST_AUTO_TEST_CASE ( f2 ){
        BOOST_CHECK('a' + 1 == 'b');
    }

BOOST_AUTO_TEST_SUITE_END()
