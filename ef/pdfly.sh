#! /usr/bin/env bash

#=========================================================================#
#                                pdfly.sh                                 #
#                                                                         #
# Author: nic                                                             #
# Date: 2018-Mar-05                                                       #
#                                                                         #
# Interactive tool for modifying PDF files.                               #
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

function choix(){
    CHOICES=`mktemp`
    cat > $CHOICES
    echo >&2
    cat "$CHOICES" | awk '{print NR") "$0}' | column >&2
    echo "Please choose from the above options:" >&2
    read -p "Please choose from the above options: " CHOICE >&2
    sed -n "$CHOICE"p "$CHOICES"
    rm $CHOICES
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

CHOICE=`ls "$1" | grep -i ".*\.pdf" | choix`
echo = $CHOICE =
