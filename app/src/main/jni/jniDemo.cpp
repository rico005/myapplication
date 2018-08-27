//
// Created by fangsheng on 2018/8/27.
//

#include <jni.h>
#include <string>
#include "android/log.h"

#define TAG "myDemo-jni" // 这个是自定义的LOG的标识
#define LOGD(...) __android_log_print(ANDROID_LOG_DEBUG,TAG ,__VA_ARGS__) // 定义LOGD类型
#define LOGI(...) __android_log_print(ANDROID_LOG_INFO,TAG ,__VA_ARGS__) // 定义LOGI类型
#define LOGW(...) __android_log_print(ANDROID_LOG_WARN,TAG ,__VA_ARGS__) // 定义LOGW类型
#define LOGE(...) __android_log_print(ANDROID_LOG_ERROR,TAG ,__VA_ARGS__) // 定义LOGE类型
#define LOGF(...) __android_log_print(ANDROID_LOG_FATAL,TAG ,__VA_ARGS__) // 定义LOGF类型

/**
 * JNI 的函数命名规则：JNIEXPORT 返回值 JNICALL Java_全路径类名_方法名_参数签名(JNIEnv* , jclass, 其它参数);
 * 其中第二个参数，当 java native 方法是 static 时，为 jclass，当为非静态方法时，为 jobject，
 * 为了简单起见，下面的例子 JNI 函数都标记extern "C"，函数名就不需要写参数签名了
 * 详见：http://notes.maxwi.com/2016/07/28/cpp-gleaning-extern-c/
 */

extern "C"

JNIEXPORT jstring JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_helloJniCpp(JNIEnv *env, jobject instance) {
    std::string hello = "Hello from C++";
    LOGI("helloJniCpp:'%s'", hello.c_str());
    return env->NewStringUTF(hello.c_str());
}