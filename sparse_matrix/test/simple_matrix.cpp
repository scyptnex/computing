#include "matrix_tests.h"
#include "simple_matrix.h"

using namespace sparse_matrix;

BOOST_AUTO_TEST_SUITE( test_simple_matrix )

    BOOST_AUTO_TEST_CASE ( printing ){
        print_test<simple_matrix<2>>();
    }

    BOOST_AUTO_TEST_CASE ( setting ) {
        set_test<simple_matrix<2>>();
    }

    BOOST_AUTO_TEST_CASE ( mult ) {
        mult_test<simple_matrix<2>>();
    }

BOOST_AUTO_TEST_SUITE_END()
