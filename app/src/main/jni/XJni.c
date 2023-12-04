#include <jni.h>

JNIEXPORT jstring JNICALL
Java_com_example_fangsheng_myapplication_jni_XJni_getStr(JNIEnv *env, jobject thiz, jstring s_) {
    // TODO: implement getStr()
    const char *s = (*env)->GetStringUTFChars(env, s_, 0);

    // TODO

//    (*env)->ReleaseStringUTFChars(env, s_, s);

    return (*env)->NewStringUTF(env, s);
}