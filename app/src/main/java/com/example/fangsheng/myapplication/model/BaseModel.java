package com.example.fangsheng.myapplication.model;

import com.example.fangsheng.myapplication.orm.field.DataType;
import com.example.fangsheng.myapplication.orm.field.DatabaseField;

import java.io.Serializable;

/**
 * 存储的base类，包含了必须的字段
 */
public class BaseModel implements Serializable{

    protected static final long serialVersionUID = 1L;

    @DatabaseField(generatedId = true)
    protected long id; // 数据库索引字段
    @DatabaseField(columnName = "create_time")
    protected long createTime = 0; //创建时间
    @DatabaseField(columnName = "modify_time")
    protected long modifyTime = 0; //修改时间
    protected boolean modifyTimeChange;

    @DatabaseField(columnName = "server_version")
    protected long serverVersion = 0; //服务端版本号(服务端数据修改时间)
    protected boolean serverVersionChange;
    @DatabaseField(width = 128)
    protected String owner; //当前登录用户的淘宝nick
    @DatabaseField(columnName = "owner_id", width = 128, uniqueCombo = true)
    protected String ownerId; //当前登录用户的userId
    
    // 扩展字段
    @DatabaseField(dataType = DataType.LONG_STRING)
    protected String col1 = "";
    @DatabaseField(dataType = DataType.LONG_STRING)
    protected String col2 = "";
    
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getCreateTime() {
        return createTime;
    }
    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
    public long getModifyTime() {
        return modifyTime;
    }
    public boolean isModifyTimeChange() {
        return modifyTimeChange;
    }
    public void setModifyTime(long modifyTime) {
        this.modifyTime = modifyTime;
        this.modifyTimeChange = true;
    }
    public boolean isServerVersionChange(){
        return serverVersionChange;
    }
    public long getServerVersion() {
        return serverVersion;
    }
    public void setServerVersion(long serverVersion) {
        this.serverVersion = serverVersion;
        this.serverVersionChange = true;
    }

    public String getOwner() {
        return owner;
    }
    public void setOwner(String owner) {
        this.owner = owner;
    }
    public String getCol1() {
        return col1;
    }
    public void setCol1(String col1) {
        this.col1 = col1;
    }
    public String getCol2() {
        return col2;
    }
    public void setCol2(String col2) {
        this.col2 = col2;
    }
    public String getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }
    
    public String toString() {
        return "BaseModel [id=" + id + ", owner=" + owner + ", createTime=" 
                + createTime + ", headImg="  + ", col1=" + col1 + ", col2="
                + col2 + "]";
    }
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;

		result = prime * result + ((owner == null) ? 0 : owner.hashCode());
		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseModel other = (BaseModel) obj;

		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		if (ownerId == null) {
			if (other.ownerId != null)
				return false;
		} else if (!ownerId.equals(other.ownerId))
			return false;
		return true;
	}
    
    
}
