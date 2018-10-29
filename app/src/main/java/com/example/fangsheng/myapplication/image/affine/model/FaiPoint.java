package com.example.fangsheng.myapplication.image.affine.model;

import java.util.ArrayList;

public class FaiPoint extends ArrayList<Float> {

    public float getX() throws IllegalArgumentException {
        if (size() >= 2){
            return get(0);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=2, real size=" + size());
        }
    }

    public float getY() throws IllegalArgumentException {
        if (size() >= 2){
            return get(1);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=2, real size=" + size());
        }
    }
}
