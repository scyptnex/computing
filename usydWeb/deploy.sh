#! /usr/bin/env bash

#-------------------------------------------------------------------------#
#                                deploy.sh                                #
#                                                                         #
# Author: Nic H.                                                          #
# Date: 2015-May-20                                                       #
#                                                                         #
# Deploy the usydweb website to lib/html directory                        #
#-------------------------------------------------------------------------#

SRC="$(dirname $0)/in"
DST="$HOME/lib/html"

function usage(){
    grep "^#.*#$" $0
}

function write_nav(){
    local SOUR="$1"
    local CURD="$2"
    local CURF="$3"
    local TABS="$4"
    local TARG="$5"
    echo "$TABS<ul>" >> $TARG
    for FIL in `ls $SOUR`; do
        local PAT="${SOUR}$FIL"
        if [ -d $PAT ]; then
            echo "$TABS<li>$FIL</li>" >> $TARG
            write_nav "$PAT/" $CURD $CURF "  $TABS" $TARG
        elif [[ $FIL =~ ^.*\.mdown$ ]]; then
            echo "$TABS<li>$FIL</li>" | sed 's/.mdown</</' >> $TARG
        fi
    done
    echo "$TABS</ul>" >> $TARG
}

function generate(){
    local S="$1"
    local D="$2"
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
            TITLE=`echo "Nic H. $FIL" | sed 's/.mdown$//'`
            echo -e "<!DOCTYPE html>\n<html>\n  <head>" > $TARG
            echo -e "    <title>$TITLE</title>" >> $TARG
            echo -e "  </head>\n  <body>\n    <div class=\"nav\">" >> $TARG
            write_nav "$SRC/" $S $FIL "      " $TARG
            echo -e "    </div>\n    <div class=\"content\">" >> $TARG
            perl $(dirname $0 )/Markdown.pl $PAT | sed "s/^/      /" >> $TARG
            echo -e "    </div>\n  </body>\n</html>" >> $TARG
            chmod a+r $TARG
        fi
    done
}

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
