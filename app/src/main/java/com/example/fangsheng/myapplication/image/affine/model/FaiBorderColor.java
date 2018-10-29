package com.example.fangsheng.myapplication.image.affine.model;

import java.util.ArrayList;

import android.graphics.Color;
import android.support.annotation.ColorInt;

public class FaiBorderColor extends ArrayList<Integer> {

    @ColorInt
    public int getColor(){
        if (size() >= 4){ //包含了alpha值的argb
            return Color.argb(getColorRgb(0), getColorRgb(1), getColorRgb(2), getColorRgb(3));
        }else if (size() >= 3){ //不包含alpha值的rgb
            return Color.argb(255, getColorRgb(0), getColorRgb(1), getColorRgb(2));
        }else { //默认值全白
            return Color.argb(255,255,255,255);
        }
    }

    public int getColorRgb(int index){
        if (index >= size()){
            throw new IndexOutOfBoundsException("index=" + index + ", but size=" + size());
        }else {
            return get(index);
        }
    }
}
