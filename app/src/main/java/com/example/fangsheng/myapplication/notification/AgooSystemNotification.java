package com.example.fangsheng.myapplication.notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.PowerManager;
import android.util.Log;

import com.example.fangsheng.myapplication.R;

public class AgooSystemNotification extends AgooNotification{
	
	private static final String TAG = "SystemNotification";

	private static PowerManager.WakeLock wakeLock = null;

	public AgooSystemNotification(Context context, MsgNotficationDTO msgData, Bundle extras, Intent param){
		super(context, msgData, extras, param);
	}

	@Override
	public void performNotify() {
		onNotification();
	}
	
	private final void onNotification() {
		try {
			final int requestCode = notificationRandom.nextInt();
//			final SharedPreferences prefs = mContext.getSharedPreferences(
//					NotifyConstants.PREFERENCES, Context.MODE_MULTI_PROCESS);

//			Log.e(TAG,
//					"onNotification clickIntent="+TaobaoConstants.AGOO_COMMAND_MESSAGE_READED);
//			AgooNotifyReporter.assembleUserCommand(mBuilder, mContext, mExtras, requestCode, mParam);

//			int icon = prefs.getInt(NotifyConstants.PROPERTY_APP_NOTIFICATION_ICON, -1);
			Bitmap iconBitmap = null;
			try {
//				if (icon < 0) {
//					try {
//						Resources resources = mContext.getResources();
//						icon = resources.getIdentifier("icon", "id", mContext.getPackageName());
//						ALog.e(TAG,
//								"AgooSystemNotification,icon="+icon);
//					} catch (Resources.NotFoundException e) {
//					}
//				} 
//				if (icon < 0) {
//					icon = mContext.getPackageManager().getPackageInfo(
//							mContext.getPackageName(), 0).applicationInfo.icon;
//					ALog.e(TAG,
//							"AgooSystemNotification22222222,icon="+icon);
//				}
				iconBitmap = ((BitmapDrawable)mContext.getResources().getDrawable(R.drawable.icon)).getBitmap();
				if (android.os.Build.VERSION.SDK_INT < 21){
//					mBuilder.setSmallIcon(icon);
					mBuilder.setSmallIcon(R.drawable.tao_mag_icon);
				}else {
					mBuilder.setSmallIcon(R.drawable.tao_mag_icon_white);
				}
//				iconBitmap = BitmapFactory.decodeResource(mContext.getResources(), bigIcon);
				if (iconBitmap != null){
					mBuilder.setLargeIcon(iconBitmap);
					Log.e(TAG,
							"AgooSystemNotification33333,iconBitmap="+iconBitmap);
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
//			if (icon < 0) {
//				ALog.e(TAG,
//						"cann't find icon ic_launcher which is also used for notification.");
//				return;
//			}
			mBuilder.setContentTitle(mMsgData.title).setContentText(mMsgData.text)
					.setTicker(mMsgData.ticker).setAutoCancel(true);
			//设置声音
//			String soundUrl = prefs.getString(
//					NotifyConstants.PROPERTY_APP_NOTIFICATION_CUSTOM_SOUND, null);
//			Log.d(TAG, "Notification,soundUrl is="+soundUrl);
//			if(!TextUtils.isEmpty(soundUrl)){
//				mBuilder.setSound(Uri.parse(soundUrl));
//			}else{
//				SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(Globals
//						.getApplication());
//				if ( settings.getBoolean(NotifyConstants.RINGON, true) )
//					mBuilder.setSound(Uri.parse("android.resource://"
//							+ Globals.getApplication().getPackageName() + "/" + R.raw.wangwangsent));
//				int defaults = 0;
//				if (prefs.getBoolean(
//						NotifyConstants.PROPERTY_APP_NOTIFICATION_VIBRATE, true)
//						|| prefs.getBoolean(NotifyConstants.ISVIBRATIONON, true)) {
//					//defaults |= Notification.DEFAULT_VIBRATE;
//					mBuilder.setVibrate(VIBRATE);
//				}
//
////				if (prefs.getBoolean(
////						NotifyConstants.PROPERTY_APP_NOTIFICATION_SOUND, true)
////						&& mMsgData.sound != -1) {
////					defaults |= Notification.DEFAULT_SOUND;
////				}
////				mBuilder.setDefaults(defaults);
//			}
			Notification notify = mBuilder.build();
			AgooNotificationManger.instance(mContext).getNotifyManager().notify(requestCode, notify);
			reportNotify();
			if(mMsgData.popup == 1){
				AcquireWakeLock(5 * 1000);  
				//释放屏幕常亮锁 
				new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
					public void run() {
						ReleaseWakeLock(); 
					}
				}, 10 * 1000);
			}
		} catch (Throwable t) {
			Log.e(TAG, Log.getStackTraceString(t));
		}
	}
	
	private void AcquireWakeLock(long timeout) {
		if (wakeLock == null) {
			wakeLock = AgooNotificationManger.instance(mContext).getPowerManager().newWakeLock(PowerManager.FULL_WAKE_LOCK
					| PowerManager.ACQUIRE_CAUSES_WAKEUP
					| PowerManager.ON_AFTER_RELEASE, TAG);
			wakeLock.acquire(timeout);
		}
	}
	
	private void ReleaseWakeLock() {
        Log.i(TAG, " ---------------------------------取消点亮");
        if (wakeLock != null && wakeLock.isHeld()) {
        	wakeLock.release();
        	wakeLock = null;
        }
    } 
	

}
