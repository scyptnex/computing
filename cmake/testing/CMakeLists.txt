cmake_minimum_required (VERSION 2.6)


set(BAR bar)
configure_file(${CMAKE_SOURCE_DIR}/testsss/CTestCustom.cmake CTestCustom.cmake)

#=========#
# Testing #
#=========#
enable_testing()
add_custom_target(check COMMAND ${CMAKE_CTEST_COMMAND} DEPENDS foo ${BAR})

macro(PASSING_TEST tname targ)
    message("testing " ${tname} " with \"" ${targ} "\"")
    add_test(NAME ${tname} COMMAND foo ${targ})
endmacro()

macro(FAILING_TEST tname targ)
    PASSING_TEST(${tname} ${targ})
    set_tests_properties(${tname} PROPERTIES WILL_FAIL TRUE)
endmacro()

#=============#
# Executables #
#=============#
add_subdirectory(saucey)

add_executable(foo main)


#=======#
# Tests #
#=======#

PASSING_TEST(blank "")
FAILING_TEST(letter "a")
PASSING_TEST(zero "0")
FAILING_TEST(num "1")
PASSING_TEST(zero2 "01")
FAILING_TEST(num2 "10")

add_custom_target(zomg COMMAND ${BAR} "a" "b" "cd e" DEPENDS ${BAR})
