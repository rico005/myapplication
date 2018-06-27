package com.example.fangsheng.myapplication;

/**
 * Created by fangsheng on 2016/12/15.
 */

public enum GroupUserIdentityModel {
    guest("0"),
    admin("1"),
    owner("2"),
    active("3");

    private String code;

    private GroupUserIdentityModel(String code){
        this.code = code;
    }

    public String code() {
        return this.code;
    }

    public String toString(){
        return this.code.toString();
    }
}
