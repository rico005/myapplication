package com.example.fangsheng.myapplication.image.affine;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.support.annotation.ColorInt;
import android.util.Log;
import com.example.fangsheng.myapplication.image.affine.model.DisplayInfo;
import com.example.fangsheng.myapplication.image.affine.model.FaiPoint;

public class AffineUtils {

    private static final String TAG = "ImageUtils";

    public static Bitmap affineTransform(Bitmap bitmap, DisplayInfo displayInfo){
        if (displayInfo == null
            || displayInfo.getOrigin_corners() == null
            || displayInfo.getAffine_matrix() == null){
            return null;
        }

        float[] src = new float[8];
        for (int i=0; i < displayInfo.getOrigin_corners().size(); i++){
            FaiPoint point = displayInfo.getOrigin_corners().get(i);
            src[i*2] = point.getX();
            src[i*2+1] = point.getY();
        }

        Matrix matrix = new Matrix();
        List<Float> row1 = displayInfo.getAffine_matrix().getMatrixRow(0);
        List<Float> row2 = displayInfo.getAffine_matrix().getMatrixRow(1);
        float[] values = new float[9];
        for (int i=0; i < 9; i++){
            if (i < 3){
                values[i] = row1.get(i);
            }else if (i < 6){
                values[i] = row2.get(i-3);
            }else if (i == 8){
                values[i] = 1;
            }else {
                values[i] = 0;
            }
        }
        matrix.setValues(values);

        return affineTransformMix(bitmap, src, matrix, displayInfo.getLeft_color().getColor(), displayInfo.getRight_color().getColor());
    }

    /**
     * 使用一个canvas完成了裁剪和matrix变换
     * @param bitmap
     * @param srcPoint
     * @param matrix
     * @return
     */
    public static Bitmap affineTransformMix(Bitmap bitmap, float[] srcPoint, Matrix matrix, @ColorInt int leftColor, @ColorInt int rightColor){
        /*
        名词解释：
            不规则矩形：通过四个坐标点划出的区域，可能不是平行位置的矩形，图片要显示到窗口中，需要变成规则的矩形；
            规则矩形：位置摆放是平行的，可以通过左顶点坐标和width、height来标识的矩形区域，可以直接在窗口中显示；
        */

        long startTime = System.currentTimeMillis();

        Bitmap origialBitmap = bitmap;
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.reset();

        //根据四个点的参数，划出原始图中需要抠出来的path区域
        Path path = new Path();
        path.reset();
        path.moveTo(srcPoint[0], srcPoint[1]);
        path.lineTo(srcPoint[2], srcPoint[3]);
        path.lineTo(srcPoint[4], srcPoint[5]);
        path.lineTo(srcPoint[6], srcPoint[7]);
        path.close();

        //从四个点的坐标构成的不规则矩形中，计算出规则的矩形的区域，方便后面drawBitmap()的时候用来标出绘制区域
        Rect srcRect_raw = caculateRect(srcPoint);
        RectF srcRectF_raw = caculateRectF(srcPoint);
        //根据srcRectF的宽高，决定处理完的图片安放的位置区域，提供给后面drawBitmap()作为入参（因为后面canvas会做matrix变换，drawBitmap函数的入参标识的都是变换之前的参数）
        RectF desRectF_raw = new RectF(0, 0, srcRectF_raw.width(), srcRectF_raw.height());
        //通过matrix变换，将原始四个点坐标进行变换，并计算出规则的矩形区域，这就是最终变换后结果图片的区域
        RectF desRectF = caculateRectF(mapPoints(srcPoint, matrix));

        //使用计算出来的最终宽高来生成结果图片，这里就已经决定了最终处理完的图片的大小
        Bitmap desBitmap = Bitmap.createBitmap(Math.round(desRectF.width()),
            Math.round(desRectF.height()), Config.ARGB_8888);
        desBitmap.setDensity(origialBitmap.getDensity());
        desBitmap.setHasAlpha(origialBitmap.hasAlpha());
        //desBitmap.setPremultiplied(origialBitmap.mRequestPremultiplied);
        Canvas canvas = new Canvas(desBitmap);

        //背景色填充，避免出现透明的区域（path圈出的区域，通过变换后最终呈现到规则的矩形中，可能边缘角有部分透明区域）
        fillColor(canvas, Math.round(desRectF.width()), Math.round(desRectF.height()), leftColor, rightColor);

        //根据原始四个点坐标计算出的规则矩形区域，计算变换后的规则矩形区域
        RectF srcRectF_mtx = caculateRectF(mapPoints(srcRectF_raw, matrix));
        //通过和desRectF取差值，得到左右微调的平移参数（通过原始四个坐标点得出规则矩形，再做变换得出的规则矩形区域，和直接使用四个原始坐标点变换后再得出的规则矩形区域范围可能不一样，因此需要做差值微调）
        float leftT = srcRectF_mtx.left - desRectF.left;
        float topT = srcRectF_mtx.top - desRectF.top;
        //通过对desRectF_raw做变换，得到变换后的图片安放位置区域（canvas经过变换之后，坐标系有了变化，可能造成图片移出窗口区域显示不全了，因此这里可以通过desRectF_mtx的数值来进行调整）
        RectF desRectF_mtx = new RectF();
        matrix.mapRect(desRectF_mtx, desRectF_raw);
        //坐标偏移微调，让内容能够显示全(如果之前使用matrix进行平移，此处会造成matrix的平移被抵消)
        canvas.translate(-desRectF_mtx.left + leftT, -desRectF_mtx.top + topT);

        //做matrix变换后，其实只是canvas的坐标系发生了变化，因此所有输入的坐标点参数不用变化，draw到屏幕区域的位置自然就会变化了
        //这也是为什么后面drawBitmap()函数的入参都是原坐标系值的原因
        canvas.concat(matrix);

        //因为前面图层的背景被fillColor填充了，造成后续使用PorterDuff.Mode.SRC_IN模式的时候，Src与Dst交接的区域就变成fillColor填充的区域了
        //为了让后续drawPath能正确标出Dst区域，因此使用saveLayer来新建透明图层，保证只有drawPath区域有内容，提供给PorterDuff.Mode.SRC_IN模式使用
        //新建图层的宽高规格使用srcRectF_raw原坐标点的值
        if (Build.VERSION.SDK_INT < VERSION_CODES.LOLLIPOP){
            canvas.saveLayer(0, 0, srcRectF_raw.width(), srcRectF_raw.height(), paint, Canvas.ALL_SAVE_FLAG);
        }else {
            canvas.saveLayer(0, 0, srcRectF_raw.width(), srcRectF_raw.height(), paint);
        }

        //原始的srcRectF_raw区域，在最终安放的显示区域被调整到desRectF_raw，其中desRectF_raw的顶点坐标为0，相当于srcRectF_raw被平移了，因此这里的原始path值也需要做相应调整
        float leftP = srcRectF_raw.left;
        float topP = srcRectF_raw.top;
        Matrix matrix_path = new Matrix();
        matrix_path.postTranslate(-leftP, -topP);
        path.transform(matrix_path);
        // 绘制Dst，用于和srcRect_raw区域做交集，达到抠图显示的效果（其中paint需要设定渐变色，否则原图填不满path区域的剩余部分会变黑色）
        canvas.drawPath(path, configPaint(paint, desRectF_raw.width(), desRectF_raw.height(), leftColor, rightColor));
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置转换模式（显示Scr与Dst交接的区域）

        //绘制Src区域，其中Xfermode造成的抠图效果、根据srcRect_raw的裁剪操作和matrix变换的结果呈现都在这里会体现
        canvas.drawBitmap(origialBitmap, srcRect_raw, desRectF_raw, paint);
        paint.setXfermode(null);
        canvas.restore();

        long endTime = System.currentTimeMillis();
        Log.d(TAG, "affineTransformMix time:" + String.valueOf(endTime-startTime));

        return desBitmap;
    }

    /**
     * 先使用一个canvas将图处理成path区域有内容，其它部分透明，
     * 再使用一个canvas进行裁剪和matrix变换
     * @param bitmap
     * @param src
     * @param matrix
     * @return
     */
    public static Bitmap affineTransformSeparate(Bitmap bitmap, float[] src, Matrix matrix, @ColorInt int leftColor, @ColorInt int rightColor){
        Bitmap origialBitmap = clipPath(bitmap, src);

        //指定坐标，标出原图中切出来的区域
        Rect srcRect = caculateRect(src);
        RectF srcRectF = caculateRectF(src);
        RectF desRect = new RectF(0, 0, srcRectF.width(), srcRectF.height());

        //将matrix的变换造成的宽高变化计算出来
        RectF desRectF = caculateRectF(mapPoints(src, matrix));
        RectF desRectF2 = caculateRectF(mapPoints(srcRectF,matrix));
        RectF desRectF1 = new RectF();
        matrix.mapRect(desRectF1, desRect);
        //使用计算出来的最终宽高来生成结果图片，避免宽高不够
        Bitmap desBitmap = Bitmap.createBitmap(Math.round(desRectF.width()),
            Math.round(desRectF.height()), Config.ARGB_8888);

        desBitmap.setDensity(origialBitmap.getDensity());
        desBitmap.setHasAlpha(origialBitmap.hasAlpha());
        //bitmap.setPremultiplied(origialBitmap.mRequestPremultiplied);

        float left = desRectF2.left-desRectF.left;
        float top = desRectF2.top-desRectF.top;

        Canvas canvas = new Canvas(desBitmap);
        fillColor(canvas, Math.round(desRectF.width()), Math.round(desRectF.height()), leftColor, rightColor);
        //起点偏移，让内容能够显示全(如果之前使用matrix进行平移，此处会造成matrix的平移被抵消)
        canvas.translate(-desRectF1.left+left, -desRectF1.top+top);
        canvas.concat(matrix);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return desBitmap;
    }

    public static Bitmap affineTransformTest(Bitmap bitmap, DisplayInfo displayInfo){
        if (displayInfo == null
            || displayInfo.getOrigin_corners() == null
            || displayInfo.getAffine_matrix() == null){
            return null;
        }

        float[] src = new float[8];
        for (int i=0; i < displayInfo.getOrigin_corners().size(); i++){
            FaiPoint point = displayInfo.getOrigin_corners().get(i);
            src[i*2] = point.getX();
            src[i*2+1] = point.getY();
        }

        Matrix matrix = new Matrix();
        List<Float> row1 = displayInfo.getAffine_matrix().getMatrixRow(0);
        List<Float> row2 = displayInfo.getAffine_matrix().getMatrixRow(1);
        float[] values = new float[9];
        for (int i=0; i < 9; i++){
            if (i < 3){
                values[i] = row1.get(i);
            }else if (i < 6){
                values[i] = row2.get(i-3);
            }else if (i == 8){
                values[i] = 1;
            }else {
                values[i] = 0;
            }
        }
        matrix.setValues(values);

        return affineTransformTest(bitmap, src, matrix, displayInfo.getLeft_color().getColor(), displayInfo.getRight_color().getColor());
    }

    public static Bitmap affineTransformTest(Bitmap origialBitmap, float[] srcPoint, Matrix matrix, @ColorInt int leftColor, @ColorInt int rightColor){
        //指定坐标，标出原图中切出来的区域
        Rect srcRect = caculateRect(srcPoint);
        RectF srcRectF = caculateRectF(srcPoint);
        RectF desRect = new RectF(0, 0, srcRectF.width(), srcRectF.height());

        //将matrix的变换造成的宽高变化计算出来
        RectF desRectF = caculateRectF(mapPoints(srcPoint, matrix));
        RectF desRectF2 = caculateRectF(mapPoints(srcRectF,matrix));
        RectF desRectF1 = new RectF();
        matrix.mapRect(desRectF1, desRect);
        //使用计算出来的最终宽高来生成结果图片，避免宽高不够
        Bitmap desBitmap = Bitmap.createBitmap(Math.round(desRectF.width()),
            Math.round(desRectF.height()), Config.ARGB_8888);

        desBitmap.setDensity(origialBitmap.getDensity());
        desBitmap.setHasAlpha(origialBitmap.hasAlpha());
        //bitmap.setPremultiplied(origialBitmap.mRequestPremultiplied);

        float left = desRectF2.left-desRectF.left;
        float top = desRectF2.top-desRectF.top;

        Canvas canvas = new Canvas(desBitmap);
        fillColor(canvas, Math.round(desRectF.width()), Math.round(desRectF.height()), leftColor, rightColor);
        //起点偏移，让内容能够显示全(如果之前使用matrix进行平移，此处会造成matrix的平移被抵消)
        canvas.translate(-desRectF1.left+left, -desRectF1.top+top);
        canvas.concat(matrix);
        canvas.drawBitmap(origialBitmap, srcRect, desRect, null);
        return desBitmap;
    }

        private static Bitmap clipPath(Bitmap bitmap, float[] src){
        Paint mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Path mPath = new Path();

        int left = 0;
        int top = 0;
        int right = bitmap.getWidth();
        int bottom = bitmap.getHeight();
        Rect srcRect = new Rect(left, top, right, bottom);
        RectF dsc = new RectF(left, top, right, bottom);

        Bitmap desBitmap = Bitmap.createBitmap(Math.round(bitmap.getWidth()),
            Math.round(bitmap.getHeight()), Config.ARGB_8888);
        Canvas canvas = new Canvas(desBitmap);

        mPaint.reset();
        mPath.reset();
        mPath.moveTo(src[0], src[1]);
        mPath.lineTo(src[2], src[3]);
        mPath.lineTo(src[4], src[5]);
        mPath.lineTo(src[6], src[7]);
        mPath.close();
        canvas.drawPath(mPath, mPaint);// 绘制Dst
        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));// 设置转换模式（显示Scr与Dst交接的区域）
        canvas.drawBitmap(bitmap, srcRect, dsc, mPaint);// 绘制Src
        return desBitmap;
    }

    /**
     * 绘制水平方向leftColor到rightColor的渐变背景
     * 从横向1/4处开始渐变，到3/4处结束渐变，避免渐变很剧烈的情况下，边缘显示不了本色
     * @param canvas
     * @param width
     * @param height
     * @param leftColor
     * @param rightColor
     */
    private static void fillColor(Canvas canvas, int width, int height, @ColorInt int leftColor, @ColorInt int rightColor){
        Paint paint = new Paint();
        LinearGradient linearGradient = new LinearGradient(width/4, 0, width*3/4, 0, leftColor, rightColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawRect(0, 0, width, height, paint);
    }

    /**
     * 配置paint为渐变色绘制
     * @param paint
     * @param width
     * @param height
     * @param leftColor
     * @param rightColor
     * @return
     */
    private static Paint configPaint(Paint paint, float width, float height, @ColorInt int leftColor, @ColorInt int rightColor){
        LinearGradient linearGradient = new LinearGradient(width/4, 0, width*3/4, 0, leftColor, rightColor, Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        return paint;
    }

    public static float[] mapPoints(float[] src, Matrix matrix){
        float[] dest = new float[8];
        matrix.mapPoints(dest, src);
        Log.i(TAG, String.valueOf(dest[0]));
        return dest;
    }

    public static float[] mapPoints(RectF rectF, Matrix matrix){
        float[] rectFP = new float[]{rectF.left, rectF.top,
            rectF.left+rectF.width(), rectF.top,
            rectF.left, rectF.top+rectF.height(),
            rectF.left+rectF.width(), rectF.top+rectF.height()};
        return mapPoints(rectFP, matrix);
    }

    public static RectF caculateRectF(float[] points){
        float left = points[0];
        float right = points[0];
        float top = points[1];
        float bottom = points[1];
        List<Float> xList = new ArrayList<>();
        List<Float> yList = new ArrayList<>();
        for (int index = 0; index < points.length; index++){
            if (index%2==0){
                xList.add(points[index]);
            }else {
                yList.add(points[index]);
            }
        }
        for (float point : xList){
            if (left > point){
                left = point;
            }
            if(right < point){
                right = point;
            }
        }
        for (float point : yList){
            if (top > point){
                top = point;
            }
            if (bottom < point){
                bottom = point;
            }
        }
        return new RectF(left, top, right, bottom);
    }

    public static Rect caculateRect(float[] points){
        RectF rectF = caculateRectF(points);
        return new Rect((int)rectF.left, (int)rectF.top, (int)rectF.right, (int)rectF.bottom);
    }

}
