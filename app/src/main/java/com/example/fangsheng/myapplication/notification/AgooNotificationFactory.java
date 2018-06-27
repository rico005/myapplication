package com.example.fangsheng.myapplication.notification;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AgooNotificationFactory {
	
	private static final String TAG = "AgooNotficationFactory";

	public static AgooNotification createAgooNotification(Intent intent, Intent param, Context context, int type){
//		if(intent == null){
//			Log.i(TAG, "showPersonalMsg intent==null");
//			return null;
//		}
		final MsgNotficationDTO data = new MsgNotficationDTO("test", "test", "text", "", "https://img.alicdn.com/bao/uploaded/i4/TB1NaWIJFXXXXXQXpXXXXXXXXXX_!!0-item_pic.jpg_200x200q90.jpg"
												, "", 0,0,0,type);//ConvertUtil.convertBasicMsg(intent);
		final Bundle extras = null; //= ConvertUtil.convertMsgExtras(intent);

		Log.e("agoo_push", "agoo_arrive_biz_id, messageId=" + (extras != null ? extras.getString("id") : ""));
		if(data == null){
			return null;
		}

//		if ((TextUtils.isEmpty(data.notification)
//				|| TextUtils.equals(data.notification, "-1"))) {
//			return isShow;
//		}

		try {
			Log.i(TAG, "AgooNotificationFactory view_type=" + data.view_type + ",data.personalImgUrl=" + data.personalImgUrl);
			switch ( data.view_type ) {
				case 1:
					Log.d(TAG, "网络图片自定义通知...");
					return new AgooPersonalNotification(context, data, extras, param);
//				case 2:
//					Log.d(TAG, "样式切换自定义通知...子类型 1");
//					return new AgCustomNotification(1, context, data, extras, param);
//				case 3:
//					Log.d(TAG, "样式切换自定义通知...子类型 2");
//					return new AgCustomNotification(2, context, data, extras, param);
//				case 4:
//					Log.d(TAG, "大图切换自定义通知...");
//					return new AgBigPictureNotification(context, data, extras, param);
				default:
					Log.d(TAG, "系统通知...");
					return new AgooSystemNotification(context, data, extras, param);
			}
		} catch ( Throwable e ) {
			Log.e(TAG, Log.getStackTraceString(e));
		}

		return null;
	}
}
