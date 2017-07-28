#! /usr/bin/env bash

#=========================================================================#
#                              timetable.sh                               #
#                                                                         #
# Author: nic                                                             #
# Date: 2017-Jul-28                                                       #
#=========================================================================#

set -e # error on non-zero exit
set -u # undefined variables are an error

function usage(){
    grep "^#.*#$" $0
}

function errxit(){
    [ $# -gt 0 ] && echo "Error: $@" >&2
    exit 1
}

function pause(){
    echo
    echo "-- Press enter to continue, ctrl+c to quit --"
    read
}

function ttdates(){
    grep "$DRE" "$TT"
}

function ttshifts(){
    grep "$NM" "$TT"
}

function ttlist(){
    paste -d ":" <(ttdates | tr '\t' '\n') <(ttshifts | tr '\t' '\n') | \
        grep "^$DRE" | grep -v "OFF$" | grep -v ":$"
}

function ttformatted(){
    ttlist | cut -d":" -f 1 | while read DAY; do
        date "+%B %_d," -d "$DAY";
    done | paste -d " " - <(ttlist | cut -d":" -f 2)
}

function ttcal(){
    echo "Processing \"$1\""
    gcalcli --calendar "$CAL" quick "$1"
}

CAL="S&M&N"
DRE="[0-3]*[0-9]-[A-Z][a-z][a-z]"
NM="Mindy Da"

read -p "If you would like to delete an old timetable, type \"D\" now: " DELETING
if [ "$DELETING" == "D" ]; then
    cat <<EOF
When you added this calendar, you were asked to put in some text to
identify entries made for this timetable. To delete those entries
we need to know what the text at the end of the block is.
So if your entries look like this:

    August 23, N [rofl]

then your text is "rofl".

EOF
    read -p "What was the text you used to add the timetable? " ID
    echo "Deleting calendar items containing [$ID]"
    gcalcli --iamaexpert delete "[$ID]"
else
    cat <<EOF
Welcome to the automatic calendar updater.

Please do the following:
 - Copy the timetable page from the Excel file (ctrl+a, ctrl+c)
   - i.e. from your windows machine :P
 - Paste the timetable below here (right click, paste)
 - press ctrl+d AFTER you have pasted the data

If you want to exit press ctrl+c
EOF
    TT=$(mktemp ./TIMETABLE-XXXX.tsv)
    touch "$TT"
    trap "rm $TT" EXIT
    
    cat > "$TT"
    
    echo
    echo --------------------------------------------------------
    echo You have pasted $(wc -l < "$TT") lines of data
    
    [ $(ttdates | wc -l) == 1 ] || errxit "Could not find the line with the dates on it, there should be only one line with e.g. \"23-Aug\" on it"
    [ $(ttshifts | wc -l) == 1 ] || errxit "Could not find the line for user \"$NM\", their name should appear in the table"
    
    echo Check to see if the following list looks reasonable
    echo The first few entries are:
    echo
    ttformatted | head
    pause
    
    cat <<EOF
----------------------------------------------------------------------
We need to have a bit of text now that will be put next to each shift.
This text is used to delete the entries en masse if e.g. you get a new
roster for the same time period

For example, if you choose the text "cHeEsE" then your shifts will
appear like this:

    August 23, N [cHeEsE]

You should choose something that is short (< 10 chars) and unlikely to
appear in the name for another entry in the calendar.

EOF
    echo A good example would be '"'AUTO-$(date "+%d-%m")'"'
    read -p "What text would you like: " ID
    echo
    
    ttformatted | sed "s/$/ [$ID]/" | while read LINE; do ttcal "$LINE"; done
fi

echo "Your next few weeks look like this:"
gcalcli --calendar "$CAL" calw 4
