package com.example.fangsheng.myapplication.remote;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fangsheng on 2017/3/7.
 */

public class RemoteBusiness {

    public static RemoteResult getResultFromUrl(String urlString){
        RemoteResult resultObj = new RemoteResult();
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if (url == null){
            return resultObj;
        }

        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();

            //处理重定向
            urlConnection = recursiveTracePath(urlConnection);

            resultObj.resultCode = urlConnection.getResponseCode();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK){
                InputStream in = urlConnection.getInputStream();
                byte[] responseBody = getBytesByInputStream(in);
                resultObj.resultBody = getStringByBytes(responseBody);
            }else {
                resultObj.resultBody = urlConnection.getResponseMessage();
            }
            Log.d("RemoteBusiness", "getResultFromUrl: result is " + resultObj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return resultObj;
    }

    public static void postParamToUrl(String urlString, String postParam, String contentType){
        //OutputStream out = null;
        try {

            URL url = new URL(urlString);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            // 设置请求方式,请求超时信息
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            // 设置运行输入,输出:
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            // Post方式不能缓存,需手动设置为false
            urlConnection.setUseCaches(false);
            //设置请求的内容格式为json，但不同的情况会不一样，一般post都是application/x-www-form-urlencoded
            if (!TextUtils.isEmpty(contentType)){
                urlConnection.setRequestProperty("content-type", contentType);
            }

            //out = new BufferedOutputStream(urlConnection.getOutputStream());
            //BufferedWriter writer = new BufferedWriter (new OutputStreamWriter(out, "UTF-8"));
            //writer.write(postParam);
            //writer.flush();
            //writer.close();
            //out.close();
            //urlConnection.connect();

            // 这里可以写一些请求头的东东...
            // 获取输出流
            OutputStream out = urlConnection.getOutputStream();
            out.write(postParam.getBytes());
            out.flush();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                // 获取响应的输入流对象
                InputStream is = urlConnection.getInputStream();
                byte[] responseBody = getBytesByInputStreamV2(is);
                // 返回字符串
                String result = getStringByBytes(responseBody);

                Log.d("RemoteBusiness", "postParamToUrl: result is " + result);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * 递归处理重定向
     * @param urlConnection
     * @return
     */
    private static HttpURLConnection recursiveTracePath(HttpURLConnection urlConnection) throws IOException{
        URL url = urlConnection.getURL();
        //urlConnection.setInstanceFollowRedirects(true);
        if (needRedirect(urlConnection.getResponseCode())){ //重定向
            // get redirect url from "location" header field
            String newUrl = urlConnection.getHeaderField("Location");

            // get the cookie if need, for login
            String cookies = urlConnection.getHeaderField("Set-Cookie");

            if (!(newUrl.startsWith("http://") || newUrl
                .startsWith("https://"))) {
                //某些时候会省略host，只返回后面的path，所以需要补全url
                URL origionUrl = url;
                newUrl = origionUrl.getProtocol() + "://"
                    + origionUrl.getHost() + newUrl;
            }

            // open the new connnection again
            try {
                url = new URL(newUrl);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestProperty("Cookie", cookies);

            return recursiveTracePath(urlConnection);
        }

        return urlConnection;
    }

    private static boolean needRedirect(int code) {
        return (code == HttpURLConnection.HTTP_MOVED_PERM
            || code == HttpURLConnection.HTTP_MOVED_TEMP
            || code == HttpURLConnection.HTTP_SEE_OTHER);
    }

    /**
     * 从InputStream中读取数据，转换成byte数组，最后关闭InputStream
     * 用了BufferedInputStream和BufferedOutputStream，因此每次读取的byte长度可以增长到1024 * 8
     * @param is
     * @return
     */
    private static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    /**
     * 和上面的getBytesByInputStream()方法类似，
     * 中间少了BufferedInputStream和BufferedOutputStream的使用，因此每次读取byte的长度只能是1024
     * @param is
     * @return
     */
    private static byte[] getBytesByInputStreamV2(InputStream is){
        byte[] bytes = null;
        // 创建字节输出流对象
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // 定义读取的长度
        int len = 0;
        // 定义缓冲区
        byte[] buffer = new byte[1024];
        try {
            // 按照缓冲区的大小，循环读取
            while ((len = is.read(buffer)) != -1) {
                // 根据读取的长度写入到os对象中
                baos.write(buffer, 0, len);
            }
            bytes = baos.toByteArray();
        } catch (IOException e){
            e.printStackTrace();
        } finally {
            // 释放资源
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                baos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    //根据字节数组构建UTF-8字符串
    private static String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Bitmap getBitmapFromUrl(String urlString){
        URL url = null;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Bitmap bmp = null;
        try {
            bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        }catch (IOException e){
            e.printStackTrace();
        }
        return bmp;
    }
}
