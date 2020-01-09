package com.example.fangsheng.myapplication.asynclayout;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.AsyncLayoutInflater;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.fangsheng.myapplication.R;

public class AsyncLayoutActivity extends AppCompatActivity {

    private TextView mContentTV;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //测试异步渲染
        new AsyncLayoutInflater(AsyncLayoutActivity.this).inflate(R.layout.activity_asynclayout, null, new AsyncLayoutInflater.OnInflateFinishedListener(){

            @Override
            public void onInflateFinished(View view, int resid, ViewGroup parent) {
                setContentView(view);
                initView();
            }
        });
    }

    private void initView(){
        mContentTV = findViewById(R.id.content_tv);
        mContentTV.setText("异步渲染完成");
    }
}
