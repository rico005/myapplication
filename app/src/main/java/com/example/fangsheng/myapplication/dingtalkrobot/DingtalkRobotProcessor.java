package com.example.fangsheng.myapplication.dingtalkrobot;

import android.os.AsyncTask;
import com.example.fangsheng.myapplication.remote.RemoteBusiness;

public class DingtalkRobotProcessor {

    public static void sendTextByRobot(String targetToken, String text){
        new RemotePostTask().execute(new String[]{RobotConstants.dingtalk_robot_webhook + targetToken, new RobotParam().text(text).toJSON(), RobotConstants.content_type});
    }

    public static class RemotePostTask extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            RemoteBusiness.postParamToUrl(params[0], params[1], params[2]);
            return null;
        }
    }
}
