package com.example.fangsheng.myapplication.image.affine.model;

import java.util.ArrayList;

public class FaiLTWH extends ArrayList<Float> {

    public float getLeft() throws IllegalArgumentException {
        if (size() >= 4){
            return get(0);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=4, real size=" + size());
        }
    }

    public float getTop() throws IllegalArgumentException {
        if (size() >= 4){
            return get(1);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=4, real size=" + size());
        }
    }

    public float getWidth() throws IllegalArgumentException {
        if (size() >= 4){
            return get(2);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=4, real size=" + size());
        }
    }

    public float getHeight() throws IllegalArgumentException {
        if (size() >= 4){
            return get(3);
        }else {
            throw new IllegalArgumentException("当前数值内容长度错误: expected size=4, real size=" + size());
        }
    }
}
