
#include <string>
#include <iostream>

#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>
#include <string.h>
#include <stdio.h>

#include "secure.hh"

#define BUFFER_SIZE 1024

using namespace std;

string base64(string input){
	BIO *bmem, *b64;
	BUF_MEM *bptr;
	b64 = BIO_new(BIO_f_base64());
	bmem = BIO_new(BIO_s_mem());
	b64 = BIO_push(b64, bmem);
	BIO_write(b64, input.c_str(), input.length());
	BIO_flush(b64);
	BIO_get_mem_ptr(b64, &bptr);
	string ret(bptr->data, bptr->length-1);
	BIO_free_all(b64);
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
	int readin;
	char buffer[BUFFER_SIZE];
	if(!enc) input = recode(input, enc);
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
	//stream the BIOs
	while(true){
		readin = BIO_read(bin, buffer, BUFFER_SIZE);
		if(readin <= 0) break;
		else{
			BIO_write(bout, buffer, readin);
		}
	}
	//clean up
	BIO_get_mem_ptr(bout, &bptr);
	string ret(bptr->data, bptr->length-1);
	BIO_free_all(bin);
	BIO_free_all(bout);
	if(enc) ret = recode(ret, enc);
	return ret;
}

