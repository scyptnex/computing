#ifndef _SECURE_HH_
#define _SECURE_HH_

#include <string>
#include <openssl/bio.h>
#include <openssl/evp.h>

#define KEY_LENGTH 256
#define IV_LENGTH 128
#define SALT_LENGTH 64
#define CIPHER EVP_aes_256_cbc()
#define DIGEST EVP_md5()

class PassCode
{
	public:
		PassCode();
		PassCode(const unsigned char* password);
		~PassCode();
		void describe();
	protected:
	private:
		size_t keyLength, ivLength;
		const EVP_CIPHER* cipher;
		const unsigned char* pass;
		unsigned char* key;
		unsigned char* iv;
		void init();
};

std::string passcode(std::string input, char* pass, bool enc);

BIO* passCipher(char* pass, bool enc);

#endif //_SECURE_HH_
