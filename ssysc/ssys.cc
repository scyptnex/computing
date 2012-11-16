#include <iostream>
#include <string.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/buffer.h>
#include <stdio.h>

#include "secure.hh"

using namespace std;

int main(int argc, char** argv){
	PassCode pc = PassCode((const unsigned char*)"hi");
	pc.describe();
	/**cout << "test\n";
	char* pass = "hi";
	for(int i=1; i<argc; i++){
		cout << argv[i];
		string enc = passcode(argv[i], pass, true);
		cout << " --> " << enc << " --> " << passcode(enc, pass, false) << endl;
	}
	BIO* benc = passCipher(pass, true);
	BIO_free_all(benc);**/
	return 0;
}
