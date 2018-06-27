package com.example.fangsheng.myapplication.decorate.decorator;

import android.util.Log;

/**
 * Created by fangsheng on 2016/12/1.
 */

public class TestBaseDecorator extends BaseDecorator{

    @Override
    public void init(Object context) {

    }

    public String decorate(){
        Log.d("TestBaseDecorator", "TestBaseDecorator");
        return "TestBaseDecorator";
    }
}
