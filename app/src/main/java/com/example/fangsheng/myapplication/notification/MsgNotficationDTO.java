package com.example.fangsheng.myapplication.notification;

import android.os.Parcel;
import android.os.Parcelable;

public class MsgNotficationDTO implements Parcelable {
	
	//弹窗标题
	public String title;
	//弹窗ticker
	public String ticker;
	//弹窗内容
	public String text;
	//跳转url
	public String url;
	//个性化图片url
	public String personalImgUrl;
	//是否系统弹窗
	public String notification;
	//弹窗声音
	public int sound;
	//是否亮屏
	public int popup;
	//弹窗id
	public int notificationId;
	
	//个性化弹窗模版参数
	public int view_type = 0;
	

	public MsgNotficationDTO(String title, String ticker, String text, String url, String personalImgUrl, String notification, int sound, int popup, int notificationId, int view_type) {
		this.title = title;
		this.ticker = ticker;
		this.text = text;
		this.url = url;
		this.personalImgUrl = personalImgUrl;
		this.notification = notification;
		this.sound = sound;
		this.popup = popup;
		this.notificationId = notificationId;
		this.view_type = view_type;
	}

	public MsgNotficationDTO(Parcel source) {
		this.title = source.readString();
		this.ticker = source.readString();
		this.text = source.readString();
		this.url = source.readString();
		this.personalImgUrl = source.readString();
		this.notification = source.readString();
		this.sound = source.readInt();
		this.popup = source.readInt();
		this.notificationId = source.readInt();
		this.view_type = source.readInt();
	}


	@Override
	public int describeContents () {
		return 0;
	}

	@Override
	public void writeToParcel (Parcel arg0, int arg1) {
		arg0.writeString(title);
		arg0.writeString(ticker);
		arg0.writeString(text);
		arg0.writeString(url);
		arg0.writeString(personalImgUrl);
		arg0.writeString(notification);
		arg0.writeInt(sound);
		arg0.writeInt(popup);
		arg0.writeInt(notificationId);
		arg0.writeInt(view_type);
	}
	
	//实例化静态内部对象CREATOR实现接口Parcelable.Creator
	public static final Creator< MsgNotficationDTO > CREATOR = new Creator< MsgNotficationDTO >() {

		@Override
		public MsgNotficationDTO[] newArray (int size) {
			return new MsgNotficationDTO[ size ];
		}

		//将Parcel对象反序列化为ParcelableDate
		@Override
		public MsgNotficationDTO createFromParcel (Parcel source) {
			return new MsgNotficationDTO(source);
		}

	};

}
