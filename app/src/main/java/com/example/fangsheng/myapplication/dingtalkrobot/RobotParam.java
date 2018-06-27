package com.example.fangsheng.myapplication.dingtalkrobot;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fangsheng on 2018/6/26.
 */

public class RobotParam {

    public String msgtype = "text";

    public Text text = new Text();

    public class Text {
        public String content = "send from client";
    }

    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("msgtype", msgtype);

            JSONObject textJsonObj = new JSONObject();
            textJsonObj.put("content", text.content);

            jsonObject.put("text", textJsonObj.toString());

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }
}
