#include "JavaNative.h"
#include <string.h>

JNIEXPORT jint JNICALL Java_JavaNative_intMethod
 (JNIEnv *env, jobject obj, jint num) {
  return num * num;
}

JNIEXPORT jboolean JNICALL Java_JavaNative_booleanMethod
  (JNIEnv *env, jobject obj, jboolean boolean) {
  return !boolean;
}

JNIEXPORT jstring JNICALL Java_JavaNative_stringMethod
  (JNIEnv *env, jobject obj, jstring string) {
    const char *str = env->GetStringUTFChars(string, 0);
    char cap[128];
    int idx = 0;
    while(str[idx] != '\0' && idx < 127){
        if(str[idx] <= 'z' && str[idx] >= 'a'){
            cap[idx] = str[idx] - 32;
        } else {
            cap[idx] = str[idx];
        }
        idx++;
    }
    cap[idx] = '\0';
    env->ReleaseStringUTFChars(string, str);
    return env->NewStringUTF(cap);
}

JNIEXPORT jint JNICALL Java_JavaNative_intArrayMethod
  (JNIEnv *env, jobject obj, jintArray array) {
    int i, sum = 0;
    jsize len = env->GetArrayLength(array);
    jint *body = env->GetIntArrayElements(array, 0);
    for (i=0; i<len; i++)
    {	sum += body[i];
    }
    env->ReleaseIntArrayElements(array, body, 0);
    return sum;
}

