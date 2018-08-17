package com.example.fangsheng.myapplication.baidupic;

import java.util.List;

public interface IPicSearchProcessor {

    void fetchRemotePicList(String keywords, GetResultListener<List<String>, Object> listener);
}
