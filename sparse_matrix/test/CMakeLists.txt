#============================================================================#
#                                  Testing                                   #
#                                                                            #
# Author: nic                                                                #
# Date: 2016-Jun-23                                                          #
#============================================================================#

# Boost test framework compiled to a library - this is a compilation performance hack
add_library (BoostTest test)

# create a 'make check' target
add_custom_target(check COMMAND ${CMAKE_CTEST_COMMAND})

# Create a unit test with the given name
function(unit_test tname)
    add_executable(${tname} ${tname})
    target_link_libraries(${tname} BoostTest)
    target_compile_features(${tname} PRIVATE ${needed_features})
    add_test(${tname} ${tname})
    add_dependencies(check ${tname})
endfunction()

#============#
# Unit Tests #
#============#
unit_test("foo")
unit_test("bar")
