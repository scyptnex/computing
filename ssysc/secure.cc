
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

#define BUFFER_SIZE (8*1024)

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

void recode(string* input, bool enc){
	for(int i=0; i<input->length(); i++){
		if(enc){
			if((*input)[i] == '=') (*input)[i] = '.';
			else if((*input)[i] == '/') (*input)[i] = '_';
			else if((*input)[i] == '+') (*input)[i] = '-';
		}
		else{
			if((*input)[i] == '.') (*input)[i] = '=';
			else if((*input)[i] == '_') (*input)[i] = '/';
			else if((*input)[i] == '-') (*input)[i] = '+';
		}
	}
}

string PassCode::codeFile(string in, bool enc){
	BIO *bin, *bout, *benc;
	int readin;
	unsigned char buffer[BUFFER_SIZE];
	bool writing = true;
	string out = codeString(in, enc);
	bin = BIO_new(BIO_s_file());
	bout = BIO_new(BIO_s_file());
	benc = getCipher(enc);
	if (BIO_read_filename(bin, (char*)in.c_str()) <= 0){
		cerr << "File not found" << endl;
		writing = false;
	}
	if (BIO_write_filename(bout, (char*)out.c_str()) <= 0){
		cerr << "Cannot write file" << endl;
		writing = false;
	}
	bout = BIO_push(benc,bout);
	if(writing){
		while(true){
			readin = BIO_read(bin, buffer, BUFFER_SIZE);
			if(readin <= 0){
				BIO_flush(bout);
				break;
			}
			BIO_write(bout, buffer, readin);
		}
	}
	BIO_free_all(bin);
	BIO_free_all(bout);
	if(writing){
		return out;
	}
	return string();
}

string PassCode::codeString(string input, bool enc){
	BIO *bin, *b64, *benc, *bout;
	BUF_MEM *bptr;
	int readin, amt=1;
	unsigned char buffer[BUFFER_SIZE];
	if(!enc){
		recode(&input, enc);
		input.append("\n");
		amt=0;
	}
	//make the BIOs
	bin = BIO_new_mem_buf((char*)input.c_str(), input.length());
	b64 = BIO_new(BIO_f_base64());
	bout = BIO_new(BIO_s_mem());
	//push b64 onto in when decrupting and out when decrypting
	if(enc){
		bout=BIO_push(b64,bout);
	}
	else{
		bin=BIO_push(b64,bin);
	}
	//push enc onto out always
	benc = getCipher(enc);
	bout = BIO_push(benc, bout);
	//stream the BIOs
	while(true){
		readin = BIO_read(bin, buffer, input.length());
		if(readin <= 0){
			break;
		}
		BIO_write(bout, buffer, readin);
	}
	bool success = BIO_flush(bout);
	//clean up
	BIO_get_mem_ptr(bout, &bptr);
	string ret(bptr->data, bptr->length-amt);
	BIO_free_all(bin);
	BIO_free_all(bout);
	if(!success){
		cerr << "Decryption failure" << endl;
		return string();
	}
	if(enc) recode(&ret, enc);
	return ret;
}

