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
	if(argc < 2) return 1;
	pc.codeFile(argv[1], true);
	return 0;
}
