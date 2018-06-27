package com.example.fangsheng.myapplication.floatview;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.example.fangsheng.myapplication.R;

/**
 * Created by fangsheng on 2017/2/21.
 */

public class FloatView{
    private final static int 				BANNER_HEIGHT			= 35;
    private static final int 				ANIMATION_DURATION      = 5000;

    private Context mContext;
    private View contentView;
    private RelativeLayout 					mParentLayout;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mWindowManagerParams;

    public FloatView(Context context) {
        init(context);
    }

    public void init(Context context){
        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        contentView = LayoutInflater.from(mContext).inflate(R.layout.float_view, null);
    }

    public static final class Messages {
        private static final int DISPLAY_BANNER = 0x445354;
        private static final int REMOVE_BANNER = 0x525354;
    }

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case Messages.DISPLAY_BANNER:
                    displayBanner();
                    break;
                case Messages.REMOVE_BANNER:
//                    removeBanner();
                    break;
                default: {
                    super.handleMessage(message);
                    break;
                }
            }
        }
    };

    public void showFloat(){
        mWindowManagerParams = new WindowManager.LayoutParams();
        // mWindowManagerParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.height =  dip2px(mContext, BANNER_HEIGHT);
        mWindowManagerParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowManagerParams.format = PixelFormat.TRANSLUCENT;


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //解决Android 7.1.1起不能再用Toast的问题（先解决crash）
            if(Build.VERSION.SDK_INT > 24){
                mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            }else{
                mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_TOAST;
            }
        } else {
            mWindowManagerParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        mWindowManagerParams.gravity = Gravity.TOP|Gravity.RIGHT;
        mWindowManagerParams.x = dip2px(mContext, 105);
        Log.d("floatView", "x=" + mWindowManagerParams.x);
        mWindowManagerParams.y = dip2px(mContext, 5);
        mWindowManagerParams.flags  = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mHandler.sendEmptyMessage(Messages.DISPLAY_BANNER);
    }

    protected void displayBanner() {

        if(mWindowManager != null && contentView != null) {
//            mParentLayout = new RelativeLayout(mContext);
//            mParentLayout.setBackgroundColor(Color.RED);
//            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//            params.rightMargin= dip2px(mContext, 40);
//            mParentLayout.addView(contentView,params);
            mWindowManager.addView(contentView, mWindowManagerParams);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
//                    contentView.findViewById(R.id.online_banner_text).setVisibility(View.GONE);
//                    initAnimation().start();
                    contentView.findViewById(R.id.online_banner_text).setVisibility(View.GONE);
                }
            }, 2000);
        }
    }

    private ValueAnimator initAnimation() {

        ValueAnimator animator = ValueAnimator.ofInt(dip2px(mContext, 40), 0).setDuration(ANIMATION_DURATION);
        ValueAnimator.setFrameDelay(1);
        Log.d("floatView",ValueAnimator.getFrameDelay()+"");
        animator.setInterpolator(new LinearInterpolator());


        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(final ValueAnimator valueAnimator) {
                if (null != contentView) {
//                    contentView.getLayoutParams().width= (Integer) valueAnimator.getAnimatedValue();
                    Log.d("floatView", "valueAnimator=" + (Integer) valueAnimator.getAnimatedValue());
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            contentView.findViewById(R.id.online_banner_text).getLayoutParams().width = (Integer) valueAnimator.getAnimatedValue();
                            contentView.getParent().requestLayout();
                        }
                    });


                }
            }
        });
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationCancel(Animator arg0) {
            }

            @Override
            public void onAnimationEnd(Animator arg0) {
                ViewGroup.LayoutParams params =contentView.getLayoutParams();
                params.width = dip2px(mContext, BANNER_HEIGHT);
                contentView.setLayoutParams(params);
//                contentView.findViewById(R.id.online_banner_text).setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animator arg0) {

            }

            @Override
            public void onAnimationStart(Animator arg0) {

            }
        });

        return animator;
    }

    public static int dip2px(Context context, float dpValue) {
        DisplayMetrics temp = context.getApplicationContext().getResources().getDisplayMetrics();
        final float scale = temp.density;
        return (int) (dpValue * scale + 0.5f);
    }
}
