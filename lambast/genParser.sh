#! /usr/bin/env bash

#=========================================================================#
#                              genParser.sh                               #
#                                                                         #
# Author: Nic H.                                                          #
# Date: 2016-Jul-27                                                       #
#                                                                         #
# creates the parser by finding the antlr jar and executing it            #
#=========================================================================#

set -e
set -u

function usage(){
    grep "^#.*#$" $0
}

ANTLR=`find ~/.gradle -iname "antlr4*.jar" | grep "org\.antlr" | grep -v "runtime" | grep -v "sources" | head -n 1`

for FI in $(dirname $0)/*.g4; do
    COM="java -jar $ANTLR -visitor -no-listener -o $(dirname $FI) -Dlanguage=Python2 $FI"
    echo $COM
    $COM
done
