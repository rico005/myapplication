package com.example.fangsheng.myapplication.dingtalkrobot;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by fangsheng on 2018/6/26.
 */

public class RobotConstants {

    public static String content_type = "application/json";

    public static String access_token_test = "6f0dafd6f2c75507a5ab6a9082f3d25a681782a722a4b236a8be0fba3c324acd";

    public static String access_token_mvp = "2a87f41267132e3bc5bed59ea001b69de94db8d3812123330579306049581f82";

    public static String dingtalk_robot_webhook = "https://oapi.dingtalk.com/robot/send?access_token=";

    public static Map<String, String> access_token_map = new LinkedHashMap(){
        {
            put("测试群", access_token_test);
            put("mvp群", access_token_mvp);
        }
    };
}
