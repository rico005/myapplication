package com.example.fangsheng.myapplication.image.affine.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 放射变换的矩阵参数
 */
public class FaiAffine extends ArrayList<List<Float>> {

    public List<Float> getMatrixRow(int index) throws IndexOutOfBoundsException {
        if (index >= size()){
            throw new IndexOutOfBoundsException("index=" + index + ", but size=" + size());
        }else {
            return get(index);
        }
    }
}
