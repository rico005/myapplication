package com.example.fangsheng.myapplication.shadow;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.fangsheng.myapplication.R;

public class ShadowImageActivity extends AppCompatActivity {

    private ImageView shadowIV;
    private Button changeStatusBtn;
    private boolean isRaw = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_shadow);

        shadowIV = (ImageView) findViewById(R.id.shadow_iv);
        changeStatusBtn = (Button) findViewById(R.id.change_status_btn);

        changeStatusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeStatus();
            }
        });

        handleShadow();
        changeStatusBtn.setText("切换原图");
        isRaw = false;
    }

    private void handleShadow(){
        Bitmap result = createShadowBitmap(
                ((BitmapDrawable)getResources().getDrawable(R.drawable.fai_shadow)).getBitmap(),
//                ((BitmapDrawable)shadowIV.getDrawable()).getBitmap(),
                getResources().getColor(R.color.grey),
                50, 50, -50);
        shadowIV.setImageBitmap(
                result
        );
    }

    //Paint的setMaskFilter不被GPU支持,为了确保画笔的setMaskFilter能供起效，我们需要禁用掉GPU硬件加速或AndroidManifest.xml文件中设置android:hardwareAccelerated为false
    private Bitmap createShadowBitmap(Bitmap originalBitmap, @ColorInt int color, int shadowWidth, int x, int y) {
        BlurMaskFilter blurFilter = new BlurMaskFilter(shadowWidth, BlurMaskFilter.Blur.NORMAL);
        Paint shadowPaint = new Paint();
        shadowPaint.setColor(color);

        /*
        //Paint的setMaskFilter不被GPU支持,为了确保画笔的setMaskFilter能供起效，我们需要禁用掉GPU硬件加速或AndroidManifest.xml文件中设置android:hardwareAccelerated为false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            //View从API Level 11才加入setLayerType方法
            //设置软件渲染模式绘图
            this.shadowIV.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        */

        shadowPaint.setMaskFilter(blurFilter);

        int[] offsetXY = new int[2];
        Bitmap shadowImage = originalBitmap.extractAlpha(shadowPaint, offsetXY);
        Log.d("ShadowImageActivity", "offsetXY[0]:" + offsetXY[0] + " | offsetXY[1]:" + offsetXY[1]);

        /* Need to convert shadowImage from 8-bit to ARGB here. */
//        Bitmap shadowImage32 = shadowImage.copy(Bitmap.Config.ARGB_8888, true);
        Bitmap shadowImage32 = Bitmap.createBitmap(originalBitmap.getWidth(),
                originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        shadowImage32.setDensity(originalBitmap.getDensity());
        shadowImage32.setHasAlpha(true);

        Canvas c = new Canvas(shadowImage32);
        c.drawBitmap(shadowImage, x+offsetXY[0], y+offsetXY[1], shadowPaint); //shadowImage本身已经是毛边了，再用shadowPaint来drawBitmap，加重模糊的效果
//        c.drawBitmap(originalBitmap, -offsetXY[0], -offsetXY[1], null);
        c.drawBitmap(originalBitmap, 0, 0, null);

        return shadowImage32;
    }

    private void changeStatus(){
        if (isRaw){
            changeStatusBtn.setText("切换原图");
            handleShadow();
        }else {
            changeStatusBtn.setText("切换阴影");
            shadowIV.setImageBitmap(((BitmapDrawable)getResources().getDrawable(R.drawable.fai_shadow)).getBitmap());
        }

        isRaw = !isRaw;
    }
}
