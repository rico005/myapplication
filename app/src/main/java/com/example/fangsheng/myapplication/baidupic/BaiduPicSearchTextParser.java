package com.example.fangsheng.myapplication.baidupic;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.example.fangsheng.myapplication.remote.RemoteBusiness;

/**
 * 参考资料 https://blog.csdn.net/xiligey1/article/details/73321152
 */
public class BaiduPicSearchTextParser implements IPicSearchProcessor {

    /**
     * 根据不同的关键词,获取远端(百度图片)图片列表，并展示在列表页
     * TODO 支持分页
     */
    @Override
    public void fetchRemotePicList(String keywords, GetResultListener<List<String>, Object> listener){
        new RemoteGetTask(keywords, listener).execute();
    }

    public List<String> extractPicUrl(String htmlRaw){
        List<String> picUrls = new ArrayList<>();
        if (TextUtils.isEmpty(htmlRaw)){
            return picUrls;
        }
        Pattern pattern = Pattern.compile(BaiduConstants.baidu_pic_search_regex);
        Matcher matcher = pattern.matcher(htmlRaw);
        while(matcher.find()){
            picUrls.add(matcher.group(1).replace("\\",""));
        }

        return picUrls;
    }

    public static String assembleQueryUrl(String baseUrl, String keywords){
        try {
            String queryWords = URLEncoder.encode(keywords, "utf-8");
            return baseUrl.concat("&" + BaiduConstants.baidu_pic_search_word_key + "=" + queryWords);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public class RemoteGetTask extends AsyncTask<Void, Void, List<String>> {

        private String keywords;
        private GetResultListener<List<String>, Object> listener;

        public RemoteGetTask(String keywords, GetResultListener<List<String>, Object> listener){
            this.keywords = keywords;
            this.listener = listener;
        }

        @Override
        protected List<String> doInBackground(Void... voids) {
            String result = RemoteBusiness.getResultFromUrl(assembleQueryUrl(BaiduConstants.baidu_pic_search_url_base, keywords)).resultBody;
            List<String> urlList = extractPicUrl(result);
            return urlList;
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            if (strings == null || strings.isEmpty()){
                if (listener != null){
                    listener.onError("-1", "数据为空", null);
                }
            }else {
                if (listener != null){
                    listener.onSuccess(strings, null);
                }
            }
        }
    }

}
