#include <boost/test/unit_test.hpp>

BOOST_AUTO_TEST_SUITE( bar )

    BOOST_AUTO_TEST_CASE ( b1 ){
        BOOST_CHECK(true);
    }

    BOOST_AUTO_TEST_CASE ( b2 ){
        BOOST_CHECK('a' + 1 == 'b');
    }

BOOST_AUTO_TEST_SUITE_END()
