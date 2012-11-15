cmake_minimum_required (VERSION 2.6)
project (EncTest)

find_package(OpenSSL)

set(CMAKE_C_FLAGS "-g -Wall")

add_executable(encx enc.c apps.c)
target_link_libraries(encx ${OPENSSL_LIBRARIES})


add_executable(b64 b64.c)
target_link_libraries(b64 ${OPENSSL_LIBRARIES})

