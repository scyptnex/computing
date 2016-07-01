#============================================================================#
#                                  Testing                                   #
#                                                                            #
# Author: nic                                                                #
# Date: 2016-Jun-23                                                          #
#============================================================================#

add_executable(regression regression.cpp)
target_compile_features(regression PRIVATE ${needed_features})
