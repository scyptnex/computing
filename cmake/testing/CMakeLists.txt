cmake_minimum_required (VERSION 2.6)

add_executable(foo main)

enable_testing()

add_test(NAME zero  COMMAND foo)
set_tests_properties(zero PROPERTIES WILL_FAIL TRUE)

add_test(NAME one   COMMAND foo a)
add_test(NAME two   COMMAND foo a b)
add_test(NAME three COMMAND foo a b c)

