
add_executable(${BAR} bar)
target_compile_features(${BAR} PUBLIC cxx_auto_type)

add_test(NAME alpha COMMAND ${BAR} "a" "b" "c d e")

set(OLD ${CMAKE_BUILD_TYPE})
set(CMAKE_BUILD_TYPE Release)
add_executable(fact_rel opti)
set(CMAKE_BUILD_TYPE Debug)
add_executable(fact_dbg opti)
set(CMAKE_BUILD_TYPE ${OLD})

