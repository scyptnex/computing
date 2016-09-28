cmake_minimum_required (VERSION 2.6)

include_directories(utils)

add_executable(euler utils/main)
target_link_libraries(euler ${CMAKE_DL_LIBS})
target_compile_features(euler PUBLIC cxx_constexpr cxx_auto_type)

macro(PROBLEM pname)
    message("prob: " ${pname})
    add_library(${pname} MODULE problems/${pname})
    target_compile_features(${pname} PUBLIC cxx_auto_type cxx_constexpr)
endmacro()

PROBLEM(p164)
PROBLEM(p165)

