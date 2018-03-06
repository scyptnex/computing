#! /usr/bin/env bash

#=========================================================================#
#                                pdfly.sh                                 #
#                                                                         #
# Author: nic                                                             #
# Date: 2018-Mar-05                                                       #
#                                                                         #
# Interactive tool for modifying PDF files. This tool modifies files that #
# you put on the desktop, so if it cant find your file you might need to  #
# rename it to have a .pdf (lowercase) extension.                         #
#                                                                         #
# Options:                                                                #
#   -h           Display this help message                                #
#=========================================================================#

set -e # error on non-zero exit
set -u # undefined variables are an error

function usage(){
    grep "^#.*#$" $0
}

function errxit(){
    [ $# -gt 0 ] && echo "Error: $@" >&2
    echo "Re-run with -h for help" >&2
    exit 1
}

function choose_pdf(){
    cat >&2 << EOF

Please select one of the following .pdf files:
EOF
    select FILE in *.pdf; do
        case "$FILE" in
            *.pdf )
                echo $FILE
                break
                ;;
            *)
                errxit "No file selected"
                ;;
        esac
    done
}

while getopts "h" opt; do
    case $opt in
        h)
            usage
            exit 0
            ;;
        \?)
            errxit Unrecognised command
            ;;
    esac
done
shift $(($OPTIND -1))

[ $# -eq 1 ] || errxit "Please specify the working directory"

usage
pushd "$1" > /dev/null 2>/dev/null
trap "popd > /dev/null 2>/dev/null" EXIT

CHOICE=`choose_pdf "$1"`
echo = $CHOICE = 

