package com.example.fangsheng.myapplication.remote;

public class RemoteResult {

    public String resultBody;

    public int resultCode;

    @Override
    public String toString() {
        return resultCode + " : " + resultBody;
    }
}
