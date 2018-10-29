package com.example.fangsheng.myapplication.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import com.example.fangsheng.myapplication.image.affine.AffineUtils;
import com.example.fangsheng.myapplication.image.affine.model.DisplayInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class ImageAffineTransform {

    private ImageView mImageView;
    private Context mContext;
    private Handler mHandler = new Handler(Looper.getMainLooper());

    private String mUrl;
    private float[] mSrc;
    private Matrix mMatrix;
    private DisplayInfo mDisplayInfo;

    public ImageAffineTransform(Context context, String url, float[] src, Matrix matrix, ImageView imageView){
        mUrl = url;
        mSrc = src;
        mMatrix = matrix;
        mContext = context;
        mImageView = imageView;
    }

    public ImageAffineTransform(Context context, String url, ImageView imageView){
        mUrl = url;
        mContext = context;
        mImageView = imageView;
    }

    private Target mTarget = new Target() {
        @Override
        public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    onHandleTransform(bitmap);
                }
            });
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
        }
    };

    public void transform(){
        Picasso.with(mContext).load(mUrl).into(mTarget);
    }

    public void onHandleTransform(Bitmap bitmap){
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mImageView.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }

        mImageView.setImageBitmap(AffineUtils.affineTransformSeparate(bitmap, mSrc, mMatrix, Color.GREEN, Color.BLUE));
    }

    public void onHandleTransform(Bitmap bitmap, DisplayInfo displayInfo){
        mDisplayInfo = displayInfo;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mImageView.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mImageView.setImageBitmap(AffineUtils.affineTransform(bitmap, displayInfo));
    }

    public void onHandleTransformTest(Bitmap bitmap, DisplayInfo displayInfo){
        mDisplayInfo = displayInfo;
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            mImageView.setLayerType(LAYER_TYPE_SOFTWARE, null);
        }
        mImageView.setImageBitmap(AffineUtils.affineTransformTest(bitmap, displayInfo));
    }

    private Bitmap matrix(Bitmap bitmap, float[] src, Matrix matrix){
        Bitmap origialBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(origialBitmap);
        canvas.save();
        canvas.concat(matrix);
        canvas.restore();

        Rect desRectF = AffineUtils.caculateRect(AffineUtils.mapPoints(src, matrix));
        Bitmap desBitmap = Bitmap.createBitmap(Math.round(desRectF.width()),
            Math.round(desRectF.height()), Config.ARGB_8888);
        Canvas canvas1 = new Canvas(desBitmap);
        RectF desRect = new RectF(0, 0, desRectF.width(), desRectF.height());
        canvas1.drawBitmap(origialBitmap, desRectF, desRect, null);
        return desBitmap;
    }
}
