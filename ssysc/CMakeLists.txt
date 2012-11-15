cmake_minimum_required (VERSION 2.6)
project (EncTest)

find_package(OpenSSL)

add_executable(ssys ssys.cc secure.cc)
target_link_libraries(ssys ${OPENSSL_LIBRARIES})
