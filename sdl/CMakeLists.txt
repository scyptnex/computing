cmake_minimum_required(VERSION 2.6)
project(sdl)

set(CMAKE_C_FLAGS "-Wall")

set(PROGRAMS lesson01 lesson20)

find_package(SDL)
find_package(SDL_image)

foreach(program ${PROGRAMS})
  add_executable(${program} ${program}.cpp)
  target_link_libraries(${program} ${SDL_LIBRARY} ${SDLIMAGE_LIBRARY})
endforeach(program)

