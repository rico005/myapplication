package com.example.fangsheng.myapplication.notification;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.fangsheng.myapplication.R;

import java.text.SimpleDateFormat;

import static android.util.Log.d;

public class AgooPersonalNotification extends AgooNotification{
	
	private static final String TAG = "AgooPerNotification";

	public AgooPersonalNotification(Context context, MsgNotficationDTO msgData, Bundle extras, Intent param){
		super(context, msgData, extras, param);
	}

	@Override
	public void performNotify() {
		showPersonalMsg();
	}

	public void showPersonalMsg(){
		if(mMsgData == null){
			Log.e(TAG, "showPersonalMsg is error,msgData==null");
			return;
		}
		try {
			d(TAG, "弹窗开始!");
			int requestCode = notificationRandom.nextInt();

			mBuilder.setTicker(mMsgData.ticker);
			mBuilder.setContentTitle("");
			mBuilder.setContentText("");
			if (Build.VERSION.SDK_INT < 21){
				mBuilder.setSmallIcon(R.drawable.notify_small_icon);
			}else {
				mBuilder.setSmallIcon(R.drawable.tao_mag_icon_white);
			}
			mBuilder.setWhen(System.currentTimeMillis());

//			Log.e(TAG,
//					"onNotification clickIntent=" + TaobaoConstants.AGOO_COMMAND_MESSAGE_READED);
			setVibrateSound(mBuilder, mParam);
			//优先使用外部传入的requestCode值
//			if (mParam != null){
//				requestCode = mParam.getIntExtra(NotifyConstants
//						.NOTIFY_CONTENT_INTENT_REQUEST_CODE_KEY, requestCode);
//			}
//			AgooNotifyReporter.assembleUserCommand(mBuilder, mContext, mExtras, requestCode, mParam);

			mBuilder.setAutoCancel(true);
			Notification defaultNotification = mBuilder.getNotification();
			String packageName = mContext.getPackageName();
			int viewType = mMsgData.view_type;
			if(viewType != 0){
				switch (viewType) {
				case 1:
					defaultNotification = createDeafultView(mMsgData, defaultNotification, packageName);
					break;
				}
			}
			int notifyId = requestCode;
//			if (mParam != null){ //消息盒子定制的notifyId
//				notifyId = mParam.getIntExtra(NotifyConstants.NOTIFY_ID, requestCode);
//			}
//			ImageTools.updateNotifyIconByUrl(mContext, mMsgData.personalImgUrl, AgooNotificationManger.instance().getNotifyManager(), defaultNotification, notifyId, mExtras,
//					new ImageTools.IUpdateIconListener() {
//						@Override
//						public void onSucceed() {
//							reportNotify();
//						}
//
//						@Override
//						public void onFailed() {
//							reportNotify();
//						}
//					}
//			);

			RemoteViews contentView = null;
			contentView = defaultNotification.contentView;
			contentView.setImageViewResource(R.id.notificationImage, R.drawable.msg_notify_icon_dark);
			defaultNotification.contentView = contentView;
//			defaultNotification.flags = Notification.FLAG_ONGOING_EVENT;
			AgooNotificationManger.instance(mContext).getNotifyManager().notify(notifyId, defaultNotification);

		} catch (Throwable e) {
			Log.e(TAG, Log.getStackTraceString(e));
		}
		
		
	}

	/**
	 * 封装模版1的view
	 *
	 * @param msgData
	 * @param defaultNotification
	 * @param packageName
	 */
	private Notification createDeafultView(final MsgNotficationDTO msgData,
			Notification defaultNotification, String packageName) {
		RemoteViews contentView = new RemoteViews(packageName, R.layout.personal_msg_default);
		Log.d(TAG, "contentView end...getPackageName()="+packageName);
		contentView.setTextViewText(R.id.notificationTitle, msgData.title);
//		if ( !TextUtils.isEmpty(BrandUtil.getInstance().getTitleColor()) )
//			contentView.setTextColor(R.id.notificationTitle, Color.parseColor(BrandUtil.getInstance().getTitleColor()));
		contentView.setTextViewText(R.id.notificationText, msgData.text);
//		if ( !TextUtils.isEmpty(BrandUtil.getInstance().getContentColor()) )
//			contentView.setTextColor(R.id.notificationText, Color.parseColor(BrandUtil.getInstance().getContentColor()));
		String timeString = time2String(System.currentTimeMillis());
		if(!TextUtils.isEmpty(timeString)){
			contentView.setTextViewText(R.id.custom_time, timeString.split("-")[3]);
//			if ( !TextUtils.isEmpty(BrandUtil.getInstance().getTitleColor()) )
//				contentView.setTextColor(R.id.custom_time,Color.parseColor(BrandUtil.getInstance().getTitleColor()));
		}
		if(Build.VERSION.SDK_INT >= 16){
			defaultNotification.bigContentView = contentView;
		}
        defaultNotification.contentView = contentView;
        d(TAG, "Build.VERSION.SDK_INT="+Build.VERSION.SDK_INT+",notification1="+defaultNotification+",contentView="+contentView);
		return defaultNotification;
	}
	
	 private final static String time2String(long time) {
	        try {
	            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH:mm");
	            return formatter.format(time);
	        } catch (Throwable e) {
	            return "";
	        }
	    }

	private void setVibrateSound (NotificationCompat.Builder mBuilder, Intent param) {
//		SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext.getApplicationContext());
//		if ( null == param ) {
//			// 铃声状态
//			if ( settings.getBoolean(NotifyConstants.RINGON, true) )
//				mBuilder.setSound(Uri.parse("android.resource://"
//						+ mContext.getPackageName() + "/" + R.raw.wangwangsent));
//
//			// 震动状态
//			if ( isVibrate() && settings.getBoolean(NotifyConstants.ISVIBRATIONON, false) )
////				mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//				mBuilder.setVibrate(VIBRATE);
//		} else {
//			String sound = param.getStringExtra(NotifyConstants.NOTIFY_SOUND_KEY);
//			String vibrate = param.getStringExtra(NotifyConstants.NOTIFY_VIBRATE_KEY);
//			if ( ! TextUtils.isEmpty(sound) )
//				mBuilder.setSound(Uri.parse(sound));
//			else if ( settings.getBoolean(NotifyConstants.RINGON, true) )
//				mBuilder.setSound(Uri.parse("android.resource://"
//						+ Globals.getApplication().getPackageName() + "/" + R.raw.wangwangsent));
//			if ( ! TextUtils.isEmpty(vibrate) )
//				mBuilder.setDefaults(Integer.parseInt(vibrate));
//			else if ( isVibrate() && settings.getBoolean(NotifyConstants.ISVIBRATIONON, false) )
////				mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);
//				mBuilder.setVibrate(VIBRATE);
//
//		}

	}

	public static boolean isVibrate() {
		AudioManager mAudioManager = (AudioManager) mContext.getSystemService(
				Context.AUDIO_SERVICE);

		switch (mAudioManager.getRingerMode()) {
			case AudioManager.RINGER_MODE_NORMAL:
				//正常模式下默认振动
				return true;
			case AudioManager.RINGER_MODE_SILENT:
				return false;
			case AudioManager.RINGER_MODE_VIBRATE:
				return true;
			default:
				return true;
		}
	}
}
