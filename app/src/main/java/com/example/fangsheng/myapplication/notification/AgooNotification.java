package com.example.fangsheng.myapplication.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import java.util.Random;

/**
 * Created by fangsheng on 16/2/26.
 */
public abstract class AgooNotification {

    protected static Random notificationRandom = new Random(100000);
    protected static long[] VIBRATE = new long[]{0,140,80,140};

    protected static Context mContext = null;
    protected NotificationCompat.Builder mBuilder;
    protected MsgNotficationDTO mMsgData;
    protected Bundle mExtras;
    protected Intent mParam;

    protected AgooNotification(Context context, MsgNotficationDTO msgData, Bundle extras, Intent param){
        mContext = context;
        mBuilder = new NotificationCompat.Builder(context.getApplicationContext());
        mMsgData = msgData;
        mExtras = extras;
        mParam = param;
    }

    protected void reportNotify(){
//        try{
//            String messageId = (mExtras != null ? mExtras.getString(TaobaoConstants.MESSAGE_ID) : "");
//            AppMonitor.Counter.commit("accs", "agoo_notify", "", 0);
//            AppMonitor.Counter.commit("accs", "agoo_notify_id", messageId, 0);
//            TBS.Ext.commitEvent("Page_Push", 19999, "agoo_notify_id", null, null, null, "messageId=" + messageId);
//            TLog.loge("agoo_push", "agoo_notify_id, messageId=" + messageId);
//
//            AgooNotifyReporter.notifyMessageWithTrigger(mContext, messageId, "10", null);
//        }catch (Exception e){
//            TaoLog.Loge("AgooNotification", "reportNotify,error="+e);
//        }
    }

    /**
     * 执行展示通知的动作
     */
    public abstract void performNotify();
}
