
#include <string>
#include <string.h>
#include <iostream>
#include <openssl/bio.h>
#include <openssl/err.h>
#include <openssl/evp.h>
#include <openssl/objects.h>
#include <openssl/x509.h>
#include <openssl/rand.h>
#include <openssl/pem.h>
#include <openssl/comp.h>
#include <ctype.h>
#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/buffer.h>

#include "secure.hh"

#define BUFFER_SIZE 1024

using namespace std;

void PassCode::init(){
	int i;
	keyLength = cipher->key_len;
	ivLength = cipher->iv_len;
	key = new unsigned char[keyLength];
	iv = new unsigned char[ivLength];
	EVP_BytesToKey(cipher,DIGEST, NULL, pass, strlen((char*)pass), 1, key, iv);
}

PassCode::PassCode() :
	cipher(CIPHER),
	pass((const unsigned char*)"")
{
	init();
}

PassCode::PassCode(const unsigned char* password) :
	cipher(CIPHER)
{
	pass = password;
	init();
}

PassCode::~PassCode(){
	delete[] key;
	delete[] iv;
}

void PassCode::describe(){
	cout << keyLength << " and " << ivLength << endl;
}

BIO* PassCode::getCipher(bool enc){
	BIO* ret = BIO_new(BIO_f_cipher());
	BIO_set_cipher(ret, cipher, key, iv, (enc ? 1 : 0));
	return ret;
}

string recode(string input, bool enc){
	for(int i=0; i<input.length(); i++){
		if(enc){
			if(input[i] == '=') input[i] = '.';
			else if(input[i] == '/') input[i] = '_';
			else if(input[i] == '+') input[i] = '-';
		}
		else{
			if(input[i] == '.') input[i] = '=';
			else if(input[i] == '_') input[i] = '/';
			else if(input[i] == '-') input[i] = '+';
		}
	}
	return input;
}

string passcode(string input,char* pass, bool enc){
	BIO *bin, *b64, *benc, *bout;
	BUF_MEM *bptr;
	int readin, amt=1;
	unsigned char buffer[BUFFER_SIZE];
	if(!enc){
		input = recode(input, enc);
		input.append("\n");
		amt=0;
	}
	//make the BIOs
	//cout << "make bios: " << input << " : " << input.length() << endl;
	bin = BIO_new_mem_buf((char*)input.c_str(), input.length());
	b64 = BIO_new(BIO_f_base64());
	bout = BIO_new(BIO_s_mem());
	//push b64 onto in when decrupting and out when decrypting
	//cout << "push bios\n";
	if(enc){
		bout=BIO_push(b64,bout);
	}
	else{
		bin=BIO_push(b64,bin);
	}
	//push enc onto out always
	//stream the BIOs
	//cout << "stream bios\n";
	readin = BIO_read(bin, buffer, input.length());
	//cout << "read " << readin << endl;
	//for(int i=0; i<readin; i++){
	//	cout << buffer[i];
	//}
	//cout << endl;
	BIO_write(bout, buffer, readin);
	BIO_flush(bout);
	//clean up
	//cout << "clean bios\n";
	BIO_get_mem_ptr(bout, &bptr);
	string ret(bptr->data, bptr->length-amt);
	BIO_free_all(bin);
	BIO_free_all(bout);
	//cout << "recode " << ret << endl;
	if(enc) ret = recode(ret, enc);
	return ret;
}

