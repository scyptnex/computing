#! /bin/bash

deepCrypt()
{
	while read line
	do
		if [ -d $line ]
		then
			ls $line | sed "s/^/$line\//g" | deepCrypt $1
		else
			tar -czf $line.tar.gz $line
			echo $1 | openssl enc -aes-256-cbc -nosalt -pass stdin -in $line.tar.gz -out $line.enc
		fi
	done
}

MYNAME=lol.sh
OTHNAME=unlol.sh

read -p "Password: " -s password
echo
workin=`pwd`
ls | grep -v "^$MYNAME\$" | grep -v "^$OTHNAME\$" | deepCrypt $password
