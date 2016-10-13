
add_executable(${BAR} bar)
target_compile_features(${BAR} PUBLIC cxx_auto_type)

add_test(NAME alpha COMMAND ${BAR} "a" "b" "c d e")
