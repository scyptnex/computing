cmake_minimum_required (VERSION 2.6)
project (CTest)
# add the executable
add_executable(pascall pascall.cc)
add_executable(test main.cpp TestClass.cpp)
