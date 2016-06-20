#==
# CMAKE META
#==
cmake_minimum_required(VERSION 3.1.0 FATAL_ERROR) # this version is needed for compiler-support checking

set(CMAKE_BUILD_TYPE Release) # for the O3 flag
#set(CMAKE_BUILD_TYPE Debug)

set( CMAKE_EXPORT_COMPILE_COMMANDS 1 )

#==
# INCLUDES
#==
include_directories(inc1/)
include_directories(inc2/)

#==
# COMPILER REQS
#==
set(needed_features cxx_constexpr cxx_auto_type cxx_static_assert cxx_nullptr)

#==
# EXECUTABLES
#==
add_executable(foo foo.cpp)
add_executable(bar bar.cpp)

target_compile_features(foo PRIVATE ${needed_features})
target_compile_features(bar PRIVATE ${needed_features})
