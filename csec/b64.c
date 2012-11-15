
#include <string.h>

#include <openssl/sha.h>
#include <openssl/hmac.h>
#include <openssl/evp.h>
#include <openssl/bio.h>
#include <openssl/buffer.h>

char *base64(const unsigned char *input, int length);

int main(int argc, char **argv)
{
  char b64s[256];
  char *output = base64("YOYO!", sizeof("YOYO!"));
  printf("Base64: *%s*\n", output);
  free(output);
}

char *base64(const unsigned char *input, int length)
{
  BIO *bmem, *b64, *bboth;
  BUF_MEM *bptr;

  b64 = BIO_new(BIO_f_base64());
  bmem = BIO_new(BIO_s_mem());
  bboth = BIO_push(b64, bmem);
  BIO_write(bboth, input, length);
  BIO_flush(bboth);
  BIO_get_mem_ptr(bboth, &bptr);

  char *buff = (char *)malloc(bptr->length);
  memcpy(buff, bptr->data, bptr->length-1);
  buff[bptr->length-1] = 0;

  BIO_free_all(b64);
  BIO_free_all(bmem);
  BIO_free_all(bboth);

  return buff;
}

