package com.example.fangsheng.myapplication.image;

import com.alibaba.fastjson.JSONObject;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.example.fangsheng.myapplication.R;
import com.example.fangsheng.myapplication.image.affine.model.SKCAttribute;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Picasso.LoadedFrom;
import com.squareup.picasso.Target;

public class ImagePopActivity extends AppCompatActivity {

    private ImageView mImg1;
    private ImageView mImg2;
    private ImageView mImgTest;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_pop);

        mImg1 = findViewById(R.id.pop_iv1);
        mImg2 = findViewById(R.id.pop_iv2);
        mImgTest = findViewById(R.id.pop_test);

        //doTransform(AffineConstants.url4, AffineConstants.src4, AffineConstants.matrix4, mImg1);
        //doTransform(AffineConstants.url3, AffineConstants.src3, AffineConstants.matrix3, mImg2);

        doTransform(AffineConstants.skcAttr5, mImg1);
        doTransform(AffineConstants.skcAttr6, mImg2);
        //doTransformTest(AffineConstants.skcAttr5, mImgTest);
    }

    private void doTransform(final String url, final float[] src, final Matrix matrix, final ImageView imageView){
        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                new ImageAffineTransform(ImagePopActivity.this, url, src, matrix, imageView).onHandleTransform(bitmap);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void doTransform(String displayJson, final ImageView imageView){
        final SKCAttribute skcAttribute = JSONObject.parseObject(displayJson, SKCAttribute.class);
        final String url = "http://gd1.alicdn.com/imgextra/" + skcAttribute.url;
        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                new ImageAffineTransform(ImagePopActivity.this, url,imageView).onHandleTransform(bitmap, skcAttribute.disp_info);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }

    private void doTransformTest(String displayJson, final ImageView imageView){
        final SKCAttribute skcAttribute = JSONObject.parseObject(displayJson, SKCAttribute.class);
        final String url = "http://gd1.alicdn.com/imgextra/" + skcAttribute.url;
        Picasso.with(this).load(url).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, LoadedFrom from) {
                new ImageAffineTransform(ImagePopActivity.this, url,imageView).onHandleTransformTest(bitmap, skcAttribute.disp_info);
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });
    }
}
