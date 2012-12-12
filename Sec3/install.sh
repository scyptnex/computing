#! /bin/sh

if [ "$1lol" = "lol" ]
then
	echo "No install location given"
	exit
fi
ICON_SRC=icon.png
ICON_TARG=ssys3.ico
MANIFEST=manifest.mf
JARFILE=ssys3.jar
EXE=ssys3.desktop

javac Ssys3.java
echo "Manifest-Version: 1.0\nClass-Path: .\nMain-Class: Ssys3\n" > manifest.mf
jar cmf $MANIFEST $JARFILE *.class
chmod a+x $JARFILE
rm $MANIFEST
srcloc=`pwd`
cd $1
targloc=`pwd`
#echo $srcloc - $targloc
mv $srcloc/$JARFILE $targloc/$JARFILE
cp $srcloc/$ICON_SRC $targloc/$ICON_TARG

echo "[Desktop Entry]" > $EXE
echo "Version=3" >> $EXE
echo "Type=Application" >> $EXE
echo "Terminal=true" >> $EXE
echo "StartupNotify=true" >> $EXE
echo "Icon=$targloc/$ICON_TARG" >> $EXE
echo "Name=Secure System 3" >> $EXE
echo "Comment=The general purpose encrypted file system" >> $EXE
echo "Exec=sh -c \"cd $targloc && java -jar $JARFILE && echo ssys3 terminated && sleep 10\"" >> $EXE
echo "Categories=Application;" >> $EXE
chmod a+x $EXE
