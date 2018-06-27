/**
 * taobao_msgcenter Contact.java
 * 
 * File Created at 2014年7月21日 下午4:42:42
 * $Id$
 * 
 * Copyright 2014 Taobao.com Croporation Limited.
 * All rights reserved.
 */
package com.example.fangsheng.myapplication.model;

import com.example.fangsheng.myapplication.orm.field.DataType;
import com.example.fangsheng.myapplication.orm.field.DatabaseField;

/**
 * 
 * @create 2014年7月21日 下午4:42:42
 * @author zhongmu.fangzm
 * @version 联系人实体对象
 */
public class Contact extends BaseModel {
    
    protected static final long serialVersionUID = 1L;

	@DatabaseField(dataType = DataType.STRING, width = 128, canBeNull = false, uniqueCombo = true, index = true)
	private String account;// 联系人账号
	@DatabaseField(index = true)
	private long userId;
	@DatabaseField(columnName = "account_type", canBeNull = false)
	private int accountType = -1;// 联系人账号类型
	@DatabaseField(columnName = "display_name", width = 128)
	private String displayName;// 联系人的备注名称
	@DatabaseField(columnName = "head_img", width = 400)
	private String headImg;
	@DatabaseField(columnName = "cache_time")
	private long cacheTime;// 缓存时间
	@DatabaseField(columnName = "channal_id", uniqueCombo = true)
	private int channelID; // 联系人的通道属性
	@DatabaseField(columnName = "c_code", width = 128, canBeNull = false, uniqueCombo = true, index = true, indexName = "ccodeindex")
	private String ccode; //会话code，用来和conversation关联
	@DatabaseField(columnName = "phone_num", width = 64)
	private String phoneNum;
	@DatabaseField(width = 64)
	private String spells;
	@DatabaseField(defaultValue = "0")
	private int friend = 0;

//	private String actionUrl; // 联系人定义的跳转行为

//	private boolean urlChange = false;
	private boolean cacheTimeChange = false;
	private boolean isContactExtChange =false;
	//可变长扩展字段，现在只有一个服务端返回的ext字段
//	private ContactExtra contactExtra =new ContactExtra();
//
//
//	public ContactExtra getContactExtra() {
//		return contactExtra;
//	}
//
//	public void setContactExtra(ContactExtra contactExtra) {
//		this.contactExtra = contactExtra;
//	}
//
//	public String getExt() {
//		return contactExtra.ext;
//	}
//
//	public void setExt(String ext) {
//		isContactExtChange =true;
//		this.contactExtra.ext = ext;
//	}
//
//	public void setLinkGroup(List<String> linkGroup){
//		isContactExtChange =true;
//		this.contactExtra.linkGroup = linkGroup;
//	}
//
//	public List<String> getLinkGroup(){
//		return contactExtra.linkGroup;
//	}

	
	public boolean isContactExtChange(){
		return isContactExtChange;
	}

	public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

	public int getFriend() {
		return friend;
	}

	public void setFriend(int friend) {
		this.friend = friend;
	}

	public String getPhoneNum() {
		return phoneNum;
	}

	public void setPhoneNum(String phoneNum) {
		this.phoneNum = phoneNum;
	}

	public String getSpells() {
		return spells;
	}

	public void setSpells(String spells) {
		this.spells = spells;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the accountType
	 */
	public int getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType
	 *            the accountType to set
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the displayName
	 */
	public String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName
	 *            the displayName to set
	 */
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the headImg
	 */
	public String getHeadImg() {
		return headImg;
	}

	/**
	 * @param headImg
	 *            the headImg to set
	 */
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}

	/**
	 * @return the cacheTime
	 */
	public long getCacheTime() {
		return cacheTime;
	}

	/**
	 * @param cacheTime
	 *            the cacheTime to set
	 */
	public void setCacheTime(long cacheTime) {
		this.cacheTime = cacheTime;
		cacheTimeChange = true;
	}

	/**
	 * @return the userId
	 */
	public long getUserId() {
		return userId;
	}

	/**
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(long userId) {
		this.userId = userId;
	}

	/**
	 * 判断缓存是否超时
	 * 
	 * @return
	 */
//	public boolean isValidate() {
//	    if(getChannelID()==ChannelType.WX_CHANNEL_ID.getValue()||
//	            getChannelID()==ChannelType.OFFICAL_CHANNEL_ID.getValue()){
//	        if (AmpTimeStampManager.instance().getCurrentTimeStamp()
//	                - this.getCacheTime() > Constants.MESSAGEBOX_ACCOUNTINFO_WX_CACHE_TIMEOUT_TIME) {
//	            return false;
//	        }
//	    }else if(getChannelID()==ChannelType.SELF_CHANNEL_ID.getValue()){
//	        if (AmpTimeStampManager.instance().getCurrentTimeStamp()
//	                - this.getCacheTime() > Constants.MESSAGEBOX_ACCOUNTINFO_SELF_CACHE_TIMEOUT_TIME) {
//	            return false;
//	        }
//	    }else {
//	        if (AmpTimeStampManager.instance().getCurrentTimeStamp()
//	                - this.getCacheTime() > Constants.MESSAGEBOX_ACCOUNTINFO_CACHE_TIMEOUT_TIME) {
//	            return false;
//	        }
//        }
//	    return true;
//	}

	/**
	 * @return the channelID
	 */
	public int getChannelID() {
		return channelID;
	}

	/**
	 * @param channelID
	 *            the channelID to set
	 */
	public void setChannelID(int channelID) {
		this.channelID = channelID;
	}

//	public String getActionUrl() {
//		return actionUrl;
//	}
//
//	public void setActionUrl(String actionUrl) {
//		urlChange = true;
//		this.actionUrl = actionUrl;
//	}

//	public boolean isUrlChange() {
//		return urlChange;
//	}
	
	public boolean isCacheTimeChange(){
		return cacheTimeChange;
	}

	@Override
	public String toString() {
		return "Contact [id=" + id + ", owner=" + owner + ", ownerId=" + ownerId
				+ ", account="+ account + ", userId=" + userId + ", accountType="
				+ accountType + ", displayName=" + displayName
				+ ", createTime=" + createTime + ", headImg=" + headImg
				+ ", cacheTime=" + cacheTime + ", channelID=" + channelID
				+ ", phoneNum=" + phoneNum + ", friend=" + friend + ", spells=" + spells
				+ ", col1=" + col1 + ", col2="
				+ col2 + "]";
	}
	
//	public ContentValues toContentValues(){
//		ContentValues initialValues = new ContentValues();
//		initialValues.put(ContactKey.ACCOUNT, account);
//		initialValues.put(ContactKey.ACCOUNT_TYPE, accountType);
//		initialValues.put(ContactKey.CREATE_TIME, createTime );
//		initialValues.put(ContactKey.MODIFY_TIME, modifyTime );
//		initialValues.put(ContactKey.SERVER_VERSION, serverVersion);
//		initialValues.put(ContactKey.DISPLAY_NAME, displayName );
//		initialValues.put(ContactKey.SPELLS,AmpSdkUtil.convertSimplePinYin(displayName));
//		initialValues.put(ContactKey.HEAD_IMG, headImg );
//		initialValues.put(ContactKey.CACHE_TIME, cacheTime );
//		initialValues.put(ContactKey.USER_ID, userId );
//		initialValues.put(ContactKey.C_CODE, ccode );
//
//		initialValues.put(ContactKey.CHANNAL_ID, channelID );
//		initialValues.put(ContactKey.OWNER, owner );
//		initialValues.put(ContactKey.OWNER_ID, ownerId );
//		initialValues.put(ContactKey.PHONE_NUM, phoneNum );
//		initialValues.put(ContactKey.FRIEND, friend );
//
//		initialValues.put(ContactKey.COL1, JSON.toJSONString(contactExtra ));
//		initialValues.put(ContactKey.COL2, col2 );
//
//		return initialValues;
//	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + accountType;
		result = prime * result + ((ccode == null) ? 0 : ccode.hashCode());
		result = prime * result + channelID;
		result = prime * result + (int) (userId ^ (userId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contact other = (Contact) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (accountType != other.accountType)
			return false;
		if (ccode == null) {
			if (other.ccode != null)
				return false;
		} else if (!ccode.equals(other.ccode))
			return false;
		if (channelID != other.channelID)
			return false;
		if (userId != other.userId)
			return false;
		return true;
	}
	
	
}
