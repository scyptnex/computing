#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>
#include <string.h>
#include <stdio.h>
#include "ssys.h"

char* Base64Encode( char *input, int length)
{
  BIO *bmem, *b64;
  BUF_MEM *bptr;

  b64 = BIO_new(BIO_f_base64());
  bmem = BIO_new(BIO_s_mem());
  b64 = BIO_push(b64, bmem);
  BIO_write(b64, input, length);
  BIO_flush(b64);
  BIO_get_mem_ptr(b64, &bptr);

  char *buff = (char *)malloc(bptr->length);
  memcpy(buff, bptr->data, bptr->length-1);
  buff[bptr->length-1] = 0;

  BIO_free_all(b64);

  return buff;
}

char *Base64Decode( char *input, int length)
{
  BIO *b64, *bmem;

  char *buffer = (char *)malloc(length);
  memset(buffer, 0, length);

  b64 = BIO_new(BIO_f_base64());
  bmem = BIO_new_mem_buf(input, length);
  bmem = BIO_push(b64, bmem);

  BIO_read(bmem, buffer, length);

  BIO_free_all(bmem);

  return buffer;
}

int main(int argc, char** argv){
	int i, len64, lenout;
	char* pass = "hi";
	char* encoded, * decoded;
	char tmp64[BUFFER_SIZE], tmpout[BUFFER_SIZE];
	for(i=1; i<argc; i++){
		encoded = Base64Encode(argv[i], strlen(argv[i]));
		decoded = Base64Decode(encoded, strlen(encoded));
		printf("%s -> %s -> %s\n", argv[i], encoded, decoded);
		free(encoded);
		free(decoded);
		//len64 = toS64(argv[i], strlen(argv[i]), tmp64, BUFFER_SIZE, pass);
		//lenout = fromS64(tmp64, len64, tmpout, BUFFER_SIZE, pass);
		//printf("%s -> %s -> %s\n", argv[i], tmp64, tmpout);
	}
	return 0;
}

int toS64(char* in, int length, char* out, int outMaxLength, char* pass){
	s64(in, length, out, outMaxLength, pass, true);
}
int fromS64(char* in, int length, char* out, int outMaxLength, char* pass){
	s64(in, length, out, outMaxLength, pass, false);
}

void recode(char* input, int length, bool enc){
	int i;
	for(i=0; i<length; i++){
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
}
int s64(char* in, int length, char* out, int outMaxLength, char* pass, bool encrypt){
	BIO *bin, *b64, *benc, *bout;
	BUF_MEM *bptr;
	int readin;
	int outlen;
	unsigned char tmpin[BUFFER_SIZE];
	unsigned char tmpout[BUFFER_SIZE];
	
	//prepare the temp array
	memcpy(tmpin, in, length);
	if(!encrypt) recode(tmpin, length, encrypt);
	//make the BIOs
	bin = BIO_new_mem_buf(tmpin, length);
	b64 = BIO_new(BIO_f_base64());
	bout = BIO_new(BIO_s_mem());
	//push b64 onto in when decrupting and out when decrypting
	if(encrypt){
		bout=BIO_push(b64,bout);
	}
	else{
		bin=BIO_push(b64,bin);
	}
	//push enc onto out always
	//stream the BIOs
	readin = BIO_read(bin, tmpout, BUFFER_SIZE);
	printf("reeeeeding %d\n", readin);
	BIO_write(bout, tmpout, readin);
	BIO_flush(bout);
	//clean up
	BIO_get_mem_ptr(bout, &bptr);
	outlen = min(bptr->length-1, outMaxLength-1);
	memcpy(out, bptr->data, outlen);
	out[outlen] = '\0';
	BIO_free_all(bin);
	BIO_free_all(bout);
	if(encrypt) recode(out, outlen, encrypt);
	return outlen;
}

int min(int a, int b){
	if(a > b) return b;
	return a;
}
