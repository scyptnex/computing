#! /usr/bin/env bash

#-------------------------------------------------------------------------#
#                               backdoor.sh                               #
#                                                                         #
# Author: Nic H.                                                          #
# Date: 2016-Mar-11                                                       #
#-------------------------------------------------------------------------#

set -e
set -u

JAVA='
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;


public class Backdoor {
	
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException{
		if(args.length == 0){
			System.err.println("Point me to the decrypted alpha.txt");
			return;
		}
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(args[0]));
		ArrayList<String> name = (ArrayList<String>)ois.readObject();
		ArrayList<String> tags = (ArrayList<String>)ois.readObject();
		ArrayList<String> date = (ArrayList<String>)ois.readObject();
		ArrayList<Long> size = (ArrayList<Long>)ois.readObject();
		ois.close();
		for(int i=0; i<name.size(); i++){
			System.out.println(i + "\t" + name.get(i) + "\t" + tags.get(i) + "\t" + date.get(i));
		}
	}

}
'

[ -f "$1/store/zzAlpha.dat" ] || (echo "must have $$1/store/zzAlpha.dat" >&2 && exit 1)
echo "Whats the password"
read PASS
PHEX=`echo -n "$PASS" | sha256sum | sed -e 's/[ \t].*$//'`
KHEX=${PHEX:0:32}
IVHEX=${PHEX:32:32}
openssl enc -aes-128-cbc -d -K $KHEX -iv $IVHEX -in "$1/store/zzAlpha.dat" -out backdoor.dat
echo $JAVA > Backdoor.java
javac Backdoor.java
java Backdoor backdoor.dat | tee backdoor.tsv
rm Backdoor.java Backdoor.class backdoor.dat
echo "openssl enc -aes-128-cbc -d -K $KHEX -iv $IVHEX -in \"$1/store/<file>\" -out <out>"
