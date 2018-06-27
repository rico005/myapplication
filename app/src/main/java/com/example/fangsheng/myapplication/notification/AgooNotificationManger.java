package com.example.fangsheng.myapplication.notification;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

/**
 * 统一对外接口
 */
public class AgooNotificationManger {

    private static final String TAG = "AgooNotificationManger";

    protected NotificationManager mNotifyManager;
    protected PowerManager mPowerManager;
    private Handler mHandler;
    private Context mContext;


    private static AgooNotificationManger instance = null;


    public static AgooNotificationManger instance(Context context){
        if (instance == null){
            instance = new AgooNotificationManger(context);
        }
        return instance;
    }

    protected AgooNotificationManger(Context context){
        mContext = context;
        mHandler = new Handler(Looper.getMainLooper());
        mNotifyManager = (NotificationManager) context.getSystemService(
                Context.NOTIFICATION_SERVICE);
        mPowerManager = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
    }

    public NotificationManager getNotifyManager(){
        return mNotifyManager;
    }

    public PowerManager getPowerManager(){
        return mPowerManager;
    }

    /**
     * 触发显示通知
     * 外部调用的接口
     */
    public boolean sendNotify(final Intent intent, final Intent param, int type){
        Log.e("agoo_push", "agoo_arrive_biz");

//        String messageId = "";
//        try{
//            messageId = intent.getStringExtra("id");
//        }catch (Exception e){
//            Log.e("agoo_push", Log.getStackTraceString(e));
//        }
//
//        Log.e("agoo_push", "agoo_arrive_biz, messageId=" + messageId);

        final AgooNotification notification = AgooNotificationFactory.createAgooNotification(intent, param, mContext, type);

        if (notification == null){
            return false;
        }

        mHandler.post(new Runnable() {

            @Override
            public void run () {
                try {
                    notification.performNotify();
                }catch (Throwable tr){
                    Log.e(TAG, Log.getStackTraceString(tr));
                }
            }
        });

        return true;
    }

    public void cancelNotify(int NotifyId) {
            Log.d(TAG, "cancelNotify NotifyId=" + NotifyId + ", mNotifyManager=" + mNotifyManager);
        //在某些手机上，notify不会随着主进程的被杀而自动消失，但是mNotifyManager会被置空，
        //造成直接点击notify拉起手淘进入聊天页后，这个函数里的mNotifyManager为空，无法执行cancel
        if (mNotifyManager == null){
            mNotifyManager = (NotificationManager) mContext.getSystemService(
                    Context.NOTIFICATION_SERVICE);
        }

        try {
            if (NotifyId == 0) {
                mNotifyManager.cancelAll();
            } else {
                mNotifyManager.cancel(NotifyId);
            }
        } catch (Exception e) {
            Log.e(TAG, "cacelNotify;" + e.getMessage());
        }
    }

}
