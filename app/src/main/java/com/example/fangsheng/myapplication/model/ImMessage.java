package com.example.fangsheng.myapplication.model;


import com.example.fangsheng.myapplication.orm.field.DataType;
import com.example.fangsheng.myapplication.orm.field.DatabaseField;
import com.example.fangsheng.myapplication.orm.table.DatabaseTable;

/**
 * 关于ImMessage 本地存储对象，对应AmpMessage
 * 1.id与code均唯一标识信息
 * 2.id字段为数据库自增字段，一般用不到，与AmpMessage的AutoId无关，
 * 特别的，发送信息的返回值不带id字段，需要使用code去查询数据然后更新
 * 
 * 
 *
 * @create 2014年12月8日 下午6:09:50
 * @author shuzhe.ww
 * @version
 */
@DatabaseTable(tableName = ImMessageKey.TABLE_NAME)
public class ImMessage extends BaseModel{
    
    protected static final long serialVersionUID = 1L;
    @DatabaseField(dataType = DataType.STRING, width = 128, canBeNull = false, columnName = ImMessageKey.C_CODE, uniqueCombo = true)
    private String ccode; //会话code，用来关联conversation和contact或者group
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false, columnName = ImMessageKey.SENDER_ID, uniqueCombo = true)
    private long senderId; //发送者的uid
    @DatabaseField(dataType = DataType.INTEGER, columnName = ImMessageKey.MESSAGE_ID)
    private long messageId = -1; //消息全局的唯一id
    @DatabaseField(dataType = DataType.STRING, width = 10, canBeNull = false, columnName = ImMessageKey.TYPE)
    private String type;
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.CONTENT)
    private String content;
    //文件上传，url变更
    private boolean isContentChange =false;
    @DatabaseField(dataType = DataType.STRING, width = 10, canBeNull = false, columnName = ImMessageKey.CONTENT_TYPE)
    private String contentType;
    
    private boolean isContentTypeChange =false;
    @DatabaseField(dataType = DataType.STRING, width = 10, columnName = ImMessageKey.CONTENT_SUB_TYPE)
    private String subContentType="0";
    
    private boolean isSubContentTypeChange =false;
    
    @DatabaseField(dataType = DataType.INTEGER, canBeNull = false, columnName = ImMessageKey.SEND_TIME, indexName = ImMessageKey.SEND_TIME)
	private long sendTime;
    
    private boolean isSendTimeChanged =false;
    @DatabaseField(dataType = DataType.INTEGER, columnName = ImMessageKey.DURATION)
    private long duration;
    @DatabaseField(dataType = DataType.STRING, width = 128, columnName = ImMessageKey.SYNC_ID)
    private long syncId;
    private boolean syncIdChange = false;
    @DatabaseField(dataType = DataType.STRING, width = 10, canBeNull = false, columnName = ImMessageKey.DIRECTION)
    private String direction;
    @DatabaseField(dataType = DataType.STRING, width = 256, columnName = ImMessageKey.ACTION_URL)
    private String actionUrl;
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.SUMMARY)
    private String  summary;
    @DatabaseField(dataType = DataType.STRING, width = 128, canBeNull = false, columnName = ImMessageKey.CODE, uniqueCombo = true)
    private String code;
    @DatabaseField(dataType = DataType.STRING, width = 10, columnName = ImMessageKey.STATUS)
    private String status;
    private boolean statusChange = false;
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.AVATAR_PATH)
    private String avatarPath;
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.AUDIO_PATH)
    private String audioPath;
    @DatabaseField(dataType = DataType.STRING, width = 128, columnName = ImMessageKey.SENDER_NAME)
    private String senderName; //发送者的 备注名 或者 nick
    @DatabaseField(dataType = DataType.STRING, width = 256, columnName = ImMessageKey.SENDER_HEAD_URL)
    private String senderHeadUrl; //发送者的头像
    @DatabaseField(dataType = DataType.STRING, width = 10, columnName = ImMessageKey.SENDER_GROUP_USER_IDENTITY)
    private String senderGroupUserIdentity; //发送者在群里的身份
    
    //新加字段calllist,存@的人员，服务端设计问题，需要单独存
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.CALL_USER_LIST)
    private String callUserList;
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.MESSAGE_ACTION)
    private String messageAction;
    
	private String ext;//备用字段 数据库名字为col1 存储服务器消息的ext字段

    private String remindType ="A"; //消息临时提醒状态,不入库,传递使用  A：提醒，U：不提醒，S：不提醒且不生成会话(silent)

    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.COL_3)
    private String col3;//备用字段
    @DatabaseField(dataType = DataType.LONG_STRING, columnName = ImMessageKey.COL_4)
    private String col4;//备用字段
    
  
    //查询参数
    private long queryTime;
    private long queryId;
    
    //图片上传进度
    private int progress=100;
    @DatabaseField(dataType = DataType.STRING, width = 10, canBeNull = false, columnName = ImMessageKey.IS_DELETED)
    private String isDeleted = "0";//是否被删除
    private boolean isDeletedChange = false;
    private boolean isMessageIdChange = false;
    private boolean isExtChange =false;

	public String getAudioPath() {
		return audioPath;
	}

	public void setAudioPath(String audioPath) {
		this.audioPath = audioPath;
	}

	public String getCallUserList() {
        return callUserList;
    }

    public void setCallUserList(String callUserList) {
        this.callUserList = callUserList;
    }

    public boolean isMessageIdChange() {
		return isMessageIdChange;
	}

	public long getMessageId() {
		return messageId;
	}

	public void setMessageId(long messageId) {
		isMessageIdChange = true;
		this.messageId = messageId;
	}

	public String getSenderName() {
		return senderName;
	}

	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}

	public String getSenderHeadUrl() {
		return senderHeadUrl;
	}

	public void setSenderHeadUrl(String senderHeadUrl) {
		this.senderHeadUrl = senderHeadUrl;
	}

	public String getSenderGroupUserIdentity() {
		return senderGroupUserIdentity;
	}

	public void setSenderGroupUserIdentity(String senderGroupUserIdentity) {
		this.senderGroupUserIdentity = senderGroupUserIdentity;
	}

	public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public String getCcode() {
        return ccode;
    }

    public void setCcode(String ccode) {
        this.ccode = ccode;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        isContentChange=true;
        this.content = content;
    }
    
    public String getContentType() {
        return contentType;
    }
    

 	
    public void setContentType(String contentType) {
        isContentTypeChange =true;
        this.contentType = contentType;
    }
    
    public boolean isContentTypeChange(){
        return this.isContentTypeChange;
    }
        
 	public String getSubContentType() {
 		return subContentType;
 	}

 	public void setSubContentType(String subContentType) {
 		this.isSubContentTypeChange =true;
 		this.subContentType = subContentType;
 	}
 	
 	public boolean isSubContentTypeChange(){
 		return this.isSubContentTypeChange;
 	}
 	
    public long getSendTime() {
        return sendTime;
    }
    
    public void setSendTime(long sendTime) {
        isSendTimeChanged=true;
        this.sendTime = sendTime;
    }
    
    public long getDuration() {
        return duration;
    }
    
    public void setDuration(long duration) {
        this.duration = duration;
    }
    
    public long getSyncId() {
        return syncId;
    }
    
    public void setSyncId(long syncId) {
        syncIdChange = true;
        this.syncId = syncId;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
    
    public String getActionUrl() {
        return actionUrl;
    }
    
    public void setActionUrl(String actionUrl) {
        this.actionUrl = actionUrl;
    }
    
    public String getSummary() {
        return summary;
    }
    
    public void setSummary(String summary) {
        this.summary = summary;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        statusChange = true;
        this.status = status;
    }
    
    public String getCol1() {
    	
        return ext;
    }
    
    public void setCol1(String ext) {
    	isExtChange =true;
        this.ext = ext;
    }
    
    public boolean isExtChange(){
    	return isExtChange;
    }
    
    public String getCol2() {
        return col2;
    }
    
    public void setCol2(String col2) {
        this.col2 = col2;
    }
    
    public String getCol3() {
        return col3;
    }
    
    public void setCol3(String col3) {
        this.col3 = col3;
    }
    
    public String getCol4() {
        return col4;
    }
    
    public void setCol4(String col4) {
        this.col4 = col4;
    }
    
    public String getIsDeleted() {
        return isDeleted;
    }
    
    public void setIsDeleted(String isDeleted) {
        isDeletedChange = true;
        this.isDeleted = isDeleted;
    }

    
    public boolean isSyncIdChange() {
        return syncIdChange;
    }

    
    public boolean isStatusChange() {
        return statusChange;
    }

    
    public boolean isDeletedChange() {
        return isDeletedChange;
    }

    public long getQueryTime() {
        return queryTime;
    }

    public void setQueryTime(long queryTime) {
        this.queryTime = queryTime;
    }

    public long getQueryId() {
        return queryId;
    }

    public void setQueryId(long queryId) {
        this.queryId = queryId;
    }

    public String getRemindType() {
        return remindType;
    }

    public void setRemindType(String remindType) {
        this.remindType = remindType;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append(ImMessageKey.COL_1+":"+ext+";");
        sb.append(ImMessageKey.COL_2+":"+col2+";");
        sb.append(ImMessageKey.COL_3+":"+col3+";");
        sb.append(ImMessageKey.COL_4+":"+col4+";");
        sb.append(ImMessageKey.CREATE_TIME+":"+this.createTime+";");
        sb.append(ImMessageKey.DIRECTION+":"+this.direction+";");
        sb.append(ImMessageKey.DURATION+":"+this.duration+";");
        sb.append(ImMessageKey.STATUS+":"+this.status+";");
        sb.append(ImMessageKey.SEND_TIME+":"+this.sendTime+";");
        sb.append(ImMessageKey.TYPE+":"+this.type+";");
        sb.append(ImMessageKey.OWNER+":"+this.owner+";");
        sb.append(ImMessageKey.OWNER_ID+":"+this.ownerId+";");
        sb.append(ImMessageKey.CONTENT+":"+this.content+";");
        sb.append(ImMessageKey.CONTENT_TYPE+":"+this.contentType+";");
        sb.append(ImMessageKey.CONTENT_SUB_TYPE+":"+this.subContentType+";");
        sb.append(ImMessageKey.IS_DELETED+":"+this.isDeleted+";");
        sb.append(ImMessageKey.ACTION_URL+":"+this.actionUrl+";");
        sb.append(ImMessageKey.C_CODE+":"+this.ccode+";");
        sb.append(ImMessageKey.SENDER_ID+":"+this.senderId+";");
        sb.append(ImMessageKey.SUMMARY+":"+this.summary+";");
        sb.append(ImMessageKey.SYNC_ID+":"+this.syncId+";");
        sb.append(ImMessageKey.MESSAGE_ID+":"+this.messageId+";");
        sb.append(ImMessageKey.CODE+":"+this.code+";");
        sb.append(ImMessageKey.ID+":"+this.id+";");
        sb.append(ImMessageKey.SENDER_NAME+":"+this.senderName+";");
        sb.append(ImMessageKey.SENDER_HEAD_URL+":"+this.senderHeadUrl+";");
        sb.append(ImMessageKey.SENDER_GROUP_USER_IDENTITY+":"+this.senderGroupUserIdentity+";");
        sb.append(ImMessageKey.MESSAGE_ACTION+":"+this.messageAction+";");
        return sb.toString();
    }
    /**
     * @return the progress
     */
    public int getProgress() {
        return progress;
    }

    /**
     * @param progress the progress to set
     */
    public void setProgress(int progress) {
        this.progress = progress;
    }

    /**
     * 
     * @return the isContentChange
     */
    public boolean isContentChange() {
        return isContentChange;
    }

    /**
     * @param isContentChange the isContentChange to set
     */
    public void setContentChange(boolean isContentChange) {
        this.isContentChange = isContentChange;
    }
    
    /**
     * @return the avatarPath
     */
    public String getAvatarPath() {
        return avatarPath;
    }

    /**
     * @param avatarPath the avatarPath to set
     */
    public void setAvatarPath(String avatarPath) {
        this.avatarPath = avatarPath;
    }


    public String getMessageAction() {
		return messageAction;
	}

	public void setMessageAction(String messageAction) {
		this.messageAction = messageAction;
	}
    
    
    public boolean isSendTimeChanged() {
        return isSendTimeChanged;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ImMessage other = (ImMessage) obj;
		if (code == null)
			return false;
		return code.equals(other.getCode());
    }

}
