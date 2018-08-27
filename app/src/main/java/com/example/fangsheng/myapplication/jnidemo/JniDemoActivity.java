package com.example.fangsheng.myapplication.jnidemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import com.example.fangsheng.myapplication.R;

public class JniDemoActivity extends AppCompatActivity {

    private TextView mContentTV;

    static {
        System.loadLibrary("jniDemo");//加载so文件，不要带上前缀lib和后缀.so
        System.loadLibrary("jniDemoCpp");
    }
    public native String helloJni();//定义本地方法接口，这个方法类似虚方法，实现是用c或者c++实现的

    public native int sum(int[] arr);

    //设置对象的非静态属性值
    public native void changeValue(JniJavaObj obj);

    //调用对象的非静态方法
    public native void callJavaMethod(JniJavaObj obj);

    //设置对象的静态属性值
    public native void changeName(JniJavaObj obj);

    //调用对象的静态方法
    public native void callJavaStaticMethod(JniJavaObj obj);

    //构造新java对象
    public native JniJavaObj newJavaObj();

    public native String helloJniCpp();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jnidemo);

        mContentTV = findViewById(R.id.content_tv);

        JniJavaObj javaObj = new JniJavaObj();
        callJavaMethod(javaObj);
        changeValue(javaObj);

        callJavaStaticMethod(javaObj);
        changeName(javaObj);

        String content = helloJni()
            + "\n" + "sum : " + sum(new int[]{1,2,3})
            + "\n" + "age : " + javaObj.age
            + "\n" + "name : " + JniJavaObj.name
            + "\n" + helloJniCpp();
        mContentTV.setText(content);

        JniJavaObj newOne = newJavaObj();
        Log.i("JniJavaObj", "newOne hashCode : " + newOne.hashCode() + " | age : " + newOne.age);
    }
}
