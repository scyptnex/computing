#!/bin/bash
#Thank you Cambridge (unix.com user, not the place)

AWK=awk
[ -x /bin/nawk ] && AWK=nawk
ECHO="echo"
ECHO_N="echo -n"

tty_save=$(stty -g)

function Get_odx
{
    od -t o1 | $AWK '{ for (i=2; i<=NF; i++)
                        printf("%s%s", i==2 ? "" : " ", $i)
                        exit }'
}

# Grab terminal capabilities
tty_cuu1=$(tput cuu1 2>&1 | Get_odx)            # up arrow
tty_kcuu1=$(tput kcuu1 2>&1 | Get_odx)
tty_cud1=$(tput cud1 2>&1 | Get_odx)            # down arrow
tty_kcud1=$(tput kcud1 2>&1 | Get_odx)
tty_cub1=$(tput cub1 2>&1 | Get_odx)            # left arrow
tty_kcub1=$(tput kcud1 2>&1 | Get_odx)
tty_cuf1=$(tput cuf1 2>&1 | Get_odx)            # right arrow
tty_kcuf1=$(tput kcud1 2>&1 | Get_odx)
tty_ent=$($ECHO | Get_odx)                      # Enter key
tty_kent=$(tput kent 2>&1 | Get_odx)
tty_bs=$($ECHO_N "\b" | Get_odx)                # Backspace key
tty_kbs=$(tput kbs 2>&1 | Get_odx)
tty_spa=$($ECHO_N " " | Get_odx)

# Some terminals (e.g. PuTTY) send the wrong code for certain arrow keys
if [ "$tty_cuu1" = "033 133 101" -o "$tty_kcuu1" = "033 133 101" ]; then
    tty_cudx="033 133 102"
    tty_cufx="033 133 103"
    tty_cubx="033 133 104"
fi

stty cs8 -icanon -echo min 10 time 1
stty intr '' susp ''

trap "stty $tty_save; exit"  INT HUP TERM

keypress=$(dd bs=10 count=1 2> /dev/null | Get_odx)
$ECHO_N "keypress=\"$keypress\""
case "$keypress" in
    "$tty_ent"|"$tty_kent") $ECHO " -- ENTER";;
    "$tty_bs"|"$tty_kbs") $ECHO " -- BACKSPACE";;
    "$tty_cuu1"|"$tty_kcuu1") $ECHO " -- KEY_UP";;
    "$tty_cud1"|"$tty_kcud1"|"$tty_cudx") $ECHO " -- KEY_DOWN";;
    "$tty_cub1"|"$tty_kcub1"|"$tty_cubx") $ECHO " -- KEY_LEFT";;
    "$tty_cuf1"|"$tty_kcuf1"|"$tty_cufx") $ECHO " -- KEY_RIGHT";;
    "$tty_spa") $ECHO " -- SPACE";;
    *) $ECHO;;
esac

stty $tty_save

