#include <jni.h>
#include <stdio.h>

JNIEXPORT jstring JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_helloJni(JNIEnv *env, jobject instance) {

    // TODO
//    const char *returnValue = "hello JNI";
    jstring returnValue = "hello JNI1";

    return (*env)->NewStringUTF(env, returnValue);
}

JNIEXPORT jint JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_sum(JNIEnv *env, jobject instance, jintArray arr_) {
    jint *arr = (*env)->GetIntArrayElements(env, arr_, NULL);

    if (arr == NULL){
        return 0;
    }

    int result = 0;
    int len = (*env)->GetArrayLength(env, arr_);
    for (int i = 0; i < len; i++){
        result += arr[i];
    }

    (*env)->ReleaseIntArrayElements(env, arr_, arr, 0);
    return result;
}

JNIEXPORT void JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_changeValue(JNIEnv *env, jobject instance,
                                                                             jobject obj) {

    jclass classObj = (*env)->GetObjectClass(env, obj);
    jfieldID ageField = (*env)->GetFieldID(env, classObj, "age", "I");
    jint oldAge = (*env)->GetIntField(env, obj, ageField);
    (*env)->SetIntField(env, obj, ageField, 111);

    (*env)->DeleteLocalRef(env, classObj);
}

JNIEXPORT void JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_callJavaMethod(JNIEnv *env, jobject instance,
                                                                                jobject obj) {

    jclass classObj = (*env)->GetObjectClass(env, obj);
    jmethodID printMethodID = (*env)->GetMethodID(env, classObj, "printAge", "()V");
    (*env)->CallVoidMethod(env, obj, printMethodID);

    (*env)->DeleteLocalRef(env, classObj);
}

JNIEXPORT void JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_changeName(JNIEnv *env, jobject instance,
                                                                            jobject obj) {

    jclass classObj = (*env)->GetObjectClass(env, obj);
    jfieldID nameField = (*env)->GetStaticFieldID(env, classObj, "name", "Ljava/lang/String;");
    jobject oldName = (*env)->GetStaticObjectField(env, classObj, nameField);
    jstring newName = (*env)->NewStringUTF(env, "JniJavaObjNew");
    (*env)->SetStaticObjectField(env, classObj, nameField, newName);

    (*env)->DeleteLocalRef(env, classObj);
    (*env)->DeleteLocalRef(env, oldName);
    (*env)->DeleteLocalRef(env, newName);
}

JNIEXPORT void JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_callJavaStaticMethod(JNIEnv *env, jobject instance,
                                                                                      jobject obj) {

    jclass classObj = (*env)->GetObjectClass(env, obj);
    jmethodID printMethodID = (*env)->GetStaticMethodID(env, classObj, "printName", "()V");
    (*env)->CallStaticVoidMethod(env, classObj, printMethodID);

    (*env)->DeleteLocalRef(env, classObj);
}

JNIEXPORT jobject JNICALL
Java_com_example_fangsheng_myapplication_jnidemo_JniDemoActivity_newJavaObj(JNIEnv *env, jobject instance) {
    jobject result = NULL;
    jclass classObj = (*env)->FindClass(env, "com/example/fangsheng/myapplication/jnidemo/JniJavaObj");
    if (classObj != NULL){
        jmethodID initMethod = (*env)->GetMethodID(env, classObj, "<init>", "(ILjava/lang/String;)V");
        jstring name = (*env)->NewStringUTF(env, "JniJavaObj");
        result = (*env)->NewObject(env, classObj, initMethod, 2, name);

        (*env)->DeleteLocalRef(env, name);
    }

    (*env)->DeleteLocalRef(env, classObj);

    return result;
}