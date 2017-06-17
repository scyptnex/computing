cmake_minimum_required(VERSION 3.1.0 FATAL_ERROR)
project(bibliog CXX)

# Flex/Bison
find_package(BISON)
find_package(FLEX)
BISON_TARGET(bibParser parser.y ${CMAKE_CURRENT_BINARY_DIR}/parser.cpp)
FLEX_TARGET(bibScanner scanner.l  ${CMAKE_CURRENT_BINARY_DIR}/scanner.cpp)
ADD_FLEX_BISON_DEPENDENCY(bibScanner bibParser)

# Includes
include_directories(${CMAKE_CURRENT_SOURCE_DIR})
include_directories(${CMAKE_CURRENT_BINARY_DIR})

# Executable
add_executable(bibliog
    bibliography.cpp
    main.cpp
    ${BISON_bibParser_OUTPUTS}
    ${FLEX_bibScanner_OUTPUTS}
)
target_compile_features(bibliog PRIVATE cxx_range_for)

