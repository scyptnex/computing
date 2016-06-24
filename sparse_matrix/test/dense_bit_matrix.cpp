/************************************************************************* 
 *                         dense_bit_matrix.cpp                          *
 *                                                                       *
 * Author: Nic H.                                                        *
 * Date: 2016-Jun-24                                                     *
 *************************************************************************/

#include <cstdint>

#include "dense_bit_matrix.h"
#include "matrix_tests.h"

using namespace sparse_matrix;

BOOST_AUTO_TEST_SUITE( test_dense_bit_matrix )

    BOOST_AUTO_TEST_CASE( meta ){
        BOOST_CHECK(dense_bit_matrix<uint64_t>::available_bits == 64);
        BOOST_CHECK(dense_bit_matrix<uint64_t>::side_length == 8);
        BOOST_CHECK(dense_bit_matrix<uint32_t>::available_bits == 32);
        BOOST_CHECK(dense_bit_matrix<uint32_t>::side_length == 5);
    }

    BOOST_AUTO_TEST_CASE ( printing ){
        print_test<dense_bit_matrix<unsigned char>>();
    }

    BOOST_AUTO_TEST_CASE ( set ){
        set_test<dense_bit_matrix<unsigned char>>();
    }
    
    BOOST_AUTO_TEST_CASE ( multiply ){
        mult_test<dense_bit_matrix<unsigned char>>();
    }

BOOST_AUTO_TEST_SUITE_END()
