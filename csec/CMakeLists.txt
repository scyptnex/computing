cmake_minimum_required (VERSION 2.6)
project (EncTest)

find_package(OpenSSL)

add_executable(encx enc.c apps.c)
target_link_libraries(encx ${OPENSSL_LIBRARIES})

