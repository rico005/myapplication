package com.example.fangsheng.myapplication.image.affine.model;

import java.util.List;

/**
 * 图像变换的参数
 */
public class DisplayInfo {

    /** 需要抠图的矩形四个角的坐标 */
    private List<FaiPoint> origin_corners;

    /** 展现到界面上容器中的位置，目前只在上层weex展现用，这里可以不关心 */
    private FaiLTWH present_ltwh;

    /** 放射变换的矩阵参数(只有前两列，因为最后一列值固定为[0,0,1]) */
    private FaiAffine affine_matrix;

    /** 抠图之后超出原图范围的空白处的填充色 */
    private FaiBorderColor left_color;

    private FaiBorderColor right_color;

    public List<FaiPoint> getOrigin_corners() {
        return origin_corners;
    }

    public void setOrigin_corners(List<FaiPoint> origin_corners) {
        this.origin_corners = origin_corners;
    }

    public FaiLTWH getPresent_ltwh() {
        return present_ltwh;
    }

    public void setPresent_ltwh(FaiLTWH present_ltwh) {
        this.present_ltwh = present_ltwh;
    }

    public FaiAffine getAffine_matrix() {
        return affine_matrix;
    }

    public void setAffine_matrix(FaiAffine affine_matrix) {
        this.affine_matrix = affine_matrix;
    }

    public FaiBorderColor getLeft_color() {
        return left_color;
    }

    public void setLeft_color(FaiBorderColor left_color) {
        this.left_color = left_color;
    }

    public FaiBorderColor getRight_color() {
        return right_color;
    }

    public void setRight_color(FaiBorderColor right_color) {
        this.right_color = right_color;
    }
}
