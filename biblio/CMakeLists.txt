cmake_minimum_required(VERSION 3.1.0 FATAL_ERROR)
project(bibliog CXX)

set( CMAKE_EXPORT_COMPILE_COMMANDS 1 )

# Flex/Bison
find_package(BISON)
find_package(FLEX)
BISON_TARGET(bibParser parser.y ${CMAKE_CURRENT_BINARY_DIR}/parser.cpp COMPILE_FLAGS "--debug -d" )
FLEX_TARGET(bibScanner scanner.l  ${CMAKE_CURRENT_BINARY_DIR}/scanner.cpp COMPILE_FLAGS "-d")
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
target_compile_features(bibliog PRIVATE cxx_lambdas)
target_compile_features(bibliog PRIVATE cxx_lambda_init_captures)
target_compile_features(bibliog PRIVATE cxx_generic_lambdas)

