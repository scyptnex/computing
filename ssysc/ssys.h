#ifndef _SSYS_H_
#define _SSYS_H_

#define BUFFER_SIZE 1024
#define bool int
#define true 1
#define false 0

int toS64(char* in, int length, char* out, int outMaxLength, char* pass);
int fromS64(char* in, int length, char* out, int outMaxLength, char* pass);
int s64(char* in, int length, char* out, int outMaxLength, char* pass, bool encrypt);

int min(int a, int b);

#endif //_SSYS_H_
