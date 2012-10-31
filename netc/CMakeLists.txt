cmake_minimum_required(VERSION 2.6)
project(netc)

set(PROGRAMS showip server client)

foreach(program ${PROGRAMS})
  add_executable(${program} ${program}.c)
endforeach(program)

