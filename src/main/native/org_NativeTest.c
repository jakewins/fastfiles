#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <jni.h>
#include "org_NativeTest.h"

JNIEXPORT jstring JNICALL Java_org_NativeTest_sayHello (JNIEnv *env, jobject obj)
{
  return (*env)->NewStringUTF(env, "Hello, world!");
}