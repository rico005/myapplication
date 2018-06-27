package com.example.fangsheng.myapplication.baidupic;

public interface GetResultListener<T, C> {

    void onSuccess(T data, C context);

    void onError(String errorCode, String errorMsg, C context);
}