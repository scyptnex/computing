cmake_minimum_required(VERSION 2.6)
project(crypto)

find_package(OpenSSL)

include_directories("${PROJECT_BINARY_DIR}")

add_executable(test test.cc cipher.cc)
target_link_libraries(test ${OPENSSL_LIBRARIES})

add_executable(ct ct.cc cipher.cc)
target_link_libraries(ct ${OPENSSL_LIBRARIES})
