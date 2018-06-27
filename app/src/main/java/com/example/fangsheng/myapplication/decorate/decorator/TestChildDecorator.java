package com.example.fangsheng.myapplication.decorate.decorator;

import android.util.Log;

/**
 * Created by fangsheng on 2016/12/1.
 */

public class TestChildDecorator extends TestBaseDecorator {

    @Override
    public String decorate() {
        Log.d("TestChildDecorator", "TestChildDecorator");
        return "TestChildDecorator";
    }
}
