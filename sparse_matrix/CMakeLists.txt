#==
# CMAKE META
#==
cmake_minimum_required(VERSION 3.1.0 FATAL_ERROR) # this version is needed for compiler-support checking

project(SPARSEMAT)

set( CMAKE_EXPORT_COMPILE_COMMANDS 1 )

set(needed_features cxx_constexpr cxx_auto_type cxx_static_assert cxx_nullptr)

enable_testing()

#==
# INCLUDES
#==
include_directories(include/)

#==
# TESTS
#==
add_subdirectory(test)

#==
# COMPILER FLAGS
#==
# find_package(OpenMP)
# if (OPENMP_FOUND)
#     set (CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} ${OpenMP_CXX_FLAGS}")
# endif()
# 
# find_package(Boost)
# if (Boost_FOUND)
#     include_directories(${Boost_INCLUDE_DIRS})
# endif()


#==
# EXECUTABLES
#==
