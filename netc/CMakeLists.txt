cmake_minimum_required(VERSION 2.6)
project(netc)

set(CMAKE_C_FLAGS "-Wall")

set(PROGRAMS showip server client listener talker)

foreach(program ${PROGRAMS})
  add_executable(${program} ${program}.c)
endforeach(program)

