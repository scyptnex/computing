#! /usr/bin/env bash

#-------------------------------------------------------------------------#
#                                deploy.sh                                #
#                                                                         #
# Author: Nic H.                                                          #
# Date: 2015-May-20                                                       #
#                                                                         #
# Deploy the usydweb website to lib/html directory                        #
#-------------------------------------------------------------------------#

function usage(){
    grep "^#.*#$" $0
}

function generate(){
    local S=$1
    local D=$2
    echo generating $S to $D
    for FIL in `ls $S`; do
        local PAT="${S}$FIL"
        if [ -d $PAT ]; then
            local DIR="${D}${FIL}"
            if [ ! -d $DIR ]; then
                mkdir -p $DIR 
                chmod og-rw $DIR
            fi
            chmod a+x $DIR
            generate "$PAT/" "$DIR/"
        elif [[ $FIL =~ ^.*\.mdown$ ]]; then
            local TARG=`echo ${D}${FIL} | sed 's/mdown$/html/'`
            rm -f $TARG
            perl $(dirname $0 )/Markdown.pl $PAT > $TARG
        fi
    done
}

SRC="$(dirname $0)/in"
DST="$HOME/lib/html"
while getopts "d:hs:" opt; do
    case $opt in
        d)
            DST="$OPTARG"
            ;;
        h)
            usage
            exit 0
            ;;
        s)
            SRC="$OPTARG"
            ;;
        \?)
            usage
            exit 1
            ;;
    esac
done

if [ ! -d $SRC ]; then
    echo "Source directory $SRC doesn't exist" >&2
    exit 1
elif [ ! -d $DST ]; then
    echo "Destination directory $DST doesn't exist" >&2
    exit 1
fi

generate "$SRC/" "$DST/"
