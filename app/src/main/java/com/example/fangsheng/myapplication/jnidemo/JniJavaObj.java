package com.example.fangsheng.myapplication.jnidemo;

import android.util.Log;

public class JniJavaObj {

    public JniJavaObj(){

    }

    public JniJavaObj(int age, String name){
        this.age = age;
        JniJavaObj.name = name;
    }

    public int age = 1;

    public void printAge(){
        Log.i("JniJavaObj", "age="+age);
    }

    public static String name;

    public static void printName(){
        Log.i("JniJavaObj", "name="+name);
    }
}
