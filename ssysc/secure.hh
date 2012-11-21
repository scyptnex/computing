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
		BIO* getCipher(bool enc);
		void describe();
		std::string codeString(std::string input, bool enc);
		std::string codeFile(std::string in, bool enc);
	protected:
	private:
		size_t keyLength, ivLength;
		const EVP_CIPHER* cipher;
		const unsigned char* pass;
		unsigned char* key;
		unsigned char* iv;
		void init();
};

#endif //_SECURE_HH_
