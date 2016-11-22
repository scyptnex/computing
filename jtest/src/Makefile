all:
	javac JavaNative.java
	javah JavaNative
	g++ -I /usr/lib/jvm/java-7-openjdk-amd64/include/ -shared -fPIC JavaNative.cpp -o libSample1.so
	java -Djava.library.path=. JavaNative

clean:
	rm -rf JavaNative.class JavaNative.h libSample1.so
