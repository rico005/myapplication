package com.example.fangsheng.myapplication.image;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import com.example.fangsheng.myapplication.R;

public class ImageCutActivity extends AppCompatActivity {

    private ImageView mImg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_cut);

        imageCut();
    }

    private void imageCut(){
        mImg = findViewById(R.id.cut_iv);
        mImg.setImageBitmap(matrix2());
    }

    private Bitmap cutRightTop() {
        Bitmap origialBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.original);
        Bitmap cutBitmap = Bitmap.createBitmap(origialBitmap.getWidth() / 2,
            origialBitmap.getHeight() / 2, Config.ARGB_8888);
        Canvas canvas = new Canvas(cutBitmap);
        Rect desRect = new Rect(0, 0, origialBitmap.getWidth() / 2, origialBitmap.getHeight() / 2);
        Rect srcRect = new Rect(origialBitmap.getWidth() / 2, 0, origialBitmap.getWidth(),
            origialBitmap.getHeight() / 2);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return cutBitmap;
    }

    private Bitmap rotateAndTranslate(Bitmap bitmap){
        Matrix matrix  = new Matrix();
        //以bitmap的中心为轴旋转45度
        matrix.postRotate(45, bitmap.getWidth()/2, bitmap.getHeight()/2);
        int translateX = 350;
        int translateY = 300;
        matrix.postTranslate(translateX, translateY);
        Bitmap rotateImg = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        Canvas canvas = new Canvas(rotateImg);
        canvas.drawBitmap(bitmap, matrix, null);
        return rotateImg;
    }

    private Bitmap matrix1(){
        Bitmap origialBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.original);
        Matrix matrix  = new Matrix();
        matrix.postRotate(45);
        /** 查看下面调用的{@link Bitmap.createbitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean)}的源码，
         * 里面有一句canvas.translate(-deviceR.left, -deviceR.top);
         * 造成的效果是无论通过Rotate还是Translate造成left和top的偏移都会被抵消掉，因此以下的语句中的(x,y)入参在这里都是不起作用的；
         * matrix.postRotate(45, -350, -350);
         * matrix.postTranslate(150, 150);
         */
        Bitmap matrixImg = Bitmap.createBitmap(origialBitmap, origialBitmap.getWidth() / 2, 0, origialBitmap.getWidth() / 2,
            origialBitmap.getHeight() / 2, matrix, true);
        //这里不设会导致背景变黑
        matrixImg.setHasAlpha(true);
        return matrixImg;
    }

    /**
     * 这里将切图和matrix放在一起；
     * 参考了{@link Bitmap.createbitmap(android.graphics.Bitmap, int, int, int, int, android.graphics.Matrix, boolean)}的实现；
     * 切图通过{@link Bitmap.createBitmap()}实现；
     * matrix应用通过canvas.drawBitmap()实现，其中最终宽高转换用到了matrix.mapRect()，应用matrix用到了canvas.concat()；
     * @return
     */
    private Bitmap matrix2(){
        Bitmap origialBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.original);
        RectF desRect = new RectF(0, 0, origialBitmap.getWidth() / 2, origialBitmap.getHeight() / 2);
        //指定坐标，标出原图中切出来的区域
        Rect srcRect = new Rect(origialBitmap.getWidth() / 2, 0, origialBitmap.getWidth(),
            origialBitmap.getHeight() / 2);

        Matrix matrix  = new Matrix();
        matrix.postRotate(45, srcRect.width()/2, srcRect.height()/2);
        //将matrix的变换造成的宽高变化计算出来
        RectF desRectF = new RectF();
        matrix.mapRect(desRectF, desRect);
        //使用计算出来的最终宽高来生成结果图片，避免宽高不够
        Bitmap desBitmap = Bitmap.createBitmap(Math.round(desRectF.width()),
            Math.round(desRectF.height()), Config.ARGB_8888);

        Canvas canvas = new Canvas(desBitmap);
        //起点偏移，让内容能够显示全(如果之前使用matrix进行平移，此处会造成matrix的平移被抵消)
        canvas.translate(-desRectF.left, -desRectF.top);
        canvas.concat(matrix);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return desBitmap;
    }
}
