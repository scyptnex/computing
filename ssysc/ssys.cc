#include <iostream>
#include <string.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/buffer.h>
#include <stdio.h>

#include "secure.hh"

using namespace std;

int usage(){
	cout << "Usage: ssys <file> <pass> [-d]" << endl;
	cout << "\t eg ssys secret.txt badpassword" << endl;
	cout << "\t    ssys v74m-kQ.. badpassword -d" << endl;
	return 1;
}

int success(const char* file, PassCode pc, bool enc){
	string outname = pc.codeString(file, enc);
	if(outname.length() == 0) return 1;
	string ret = pc.codeFile(file, enc);
	if(ret.length() > 0){
		cout << "Completed " << ret << endl;
		return 0;
	}
	return 1;
}

int main(int argc, char** argv){
	if(argc != 4 && argc != 3) return usage();
	return success(argv[1], PassCode((const unsigned char*)argv[2]), (argc == 3));
}
