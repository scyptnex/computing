cmake_minimum_required (VERSION 2.6)
project (CTest)
# add the executable
set(CMAKE_C_FLAGS "-fopenmp")
add_executable(pascall pascall.cc)
add_executable(test main.cpp TestClass.cpp)
add_executable(hello omp.c)
