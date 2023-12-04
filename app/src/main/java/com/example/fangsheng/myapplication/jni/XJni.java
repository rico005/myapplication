package com.example.fangsheng.myapplication.jni;

public class XJni {

    static {
        System.loadLibrary("xjni");
    }

    public native String getStr(String s);
}
