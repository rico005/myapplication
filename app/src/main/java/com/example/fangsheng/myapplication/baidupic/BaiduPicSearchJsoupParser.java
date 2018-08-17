package com.example.fangsheng.myapplication.baidupic;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 本意是想用Jsoup来解析静态html拿到图片地址，但baidu是动态js获取的地址，无法直接解析，
 * 因此还是使用了类似{@link BaiduPicSearchTextParser}里的正则匹配字符串的方式；
 * 疑问：使用Jsoup返回的格式和直接使用{@link HttpURLConnection}返回的格式有细微区别，正则表达式会有区别
 *
 * 参考资料：https://blog.csdn.net/greatkendy123/article/details/51759040
 */
public class BaiduPicSearchJsoupParser implements IPicSearchProcessor {
    private static final String TAG = "BaiduPicSearchJsoupParser";

    @Override
    public void fetchRemotePicList(String keywords, GetResultListener<List<String>, Object> listener) {
        new RemoteGetTask(keywords, listener).execute();
    }

    public List<String> extractPicUrl(String htmlRaw){
        String xmlSource = htmlRaw;//StringEscapeUtils.unescapeHtml3(htmlRaw);
        List<String> picUrls = new ArrayList<>();
        if (TextUtils.isEmpty(htmlRaw)){
            return picUrls;
        }

        Pattern pattern = Pattern.compile(BaiduConstants.baidu_pic_search_regex_jsoup);
        Matcher matcher = pattern.matcher(xmlSource);
        while(matcher.find()){
            picUrls.add(matcher.group(1).replace("\\",""));
        }

        return picUrls;
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
            Document doc = null;
            try {
                doc = Jsoup.connect(BaiduPicSearchTextParser.assembleQueryUrl(BaiduConstants.baidu_pic_search_url_base,
                    keywords)).get();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (doc == null){
                return null;
            }

            Log.d(TAG, doc.title());
            Elements newsHeadlines = doc.select("#mp-itn b a");
            for (Element headline : newsHeadlines) {
                Log.d(TAG, String.format("%s\n\t%s",
                    headline.attr("title"), headline.absUrl("href")));
            }

            Elements imgs = doc.select("img[src~=.(png|jpe?g)$]");
            for (Element img : imgs){
                Log.d(TAG, img.absUrl("src"));
            }

            List<String> urlList = extractPicUrl(doc.toString());
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
