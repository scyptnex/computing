#include <iostream>
#include <string.h>
#include <stdio.h>

#include "secure.hh"

using namespace std;

int main(int argc, char** argv){
	char* pass = "hi";
	for(int i=1; i<argc; i++){
		string enc = passcode(argv[i], pass, true);
		cout << argv[i] << " --> " << enc << " --> " << passcode(enc, pass, false) << endl;
	}
	return 0;
}
