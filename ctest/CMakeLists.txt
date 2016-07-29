cmake_minimum_required (VERSION 3.2)
set(CMAKE_EXPORT_COMPILE_COMMANDS ON)
project (CTest)
# add the executable
set(CMAKE_C_FLAGS "-fopenmp")
add_executable(pascall pascall.cc)
add_executable(xtest main.cpp TestClass.cpp)
add_executable(hello omp.c)

find_package( Boost REQUIRED COMPONENTS program_options )
INCLUDE_DIRECTORIES( ${Boost_INCLUDE_DIR} )
add_executable(command_line cl.cpp clmain.cpp)
TARGET_LINK_LIBRARIES( command_line ${Boost_LIBRARIES})
