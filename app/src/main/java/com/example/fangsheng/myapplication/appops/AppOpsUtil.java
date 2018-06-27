package com.example.fangsheng.myapplication.appops;
//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Build.VERSION_CODES;
import android.text.TextUtils;
import android.util.Log;

public class AppOpsUtil {
    private static final String TAG = "AppOpsUtil";
    public static final String OP_POST_NOTIFICATION_VAR = "OP_POST_NOTIFICATION";
    public static final String OP_SYSTEM_ALERT_WINDOW_VAR = "OP_SYSTEM_ALERT_WINDOW";

    public AppOpsUtil() {
    }

    @TargetApi(VERSION_CODES.KITKAT)
    public static AppOpsUtil.OptionCheckResult checkOption(Context context, String opVariableName) {
        AppOpsUtil.OptionCheckResult result = AppOpsUtil.OptionCheckResult.UNKNOW;

        try {
            AppOpsManager appOps = (AppOpsManager)context.getSystemService("appops");
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;
            Class appOpsClass = null;
            appOpsClass = Class.forName(AppOpsManager.class.getName());
            Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", new Class[]{Integer.TYPE, Integer.TYPE, String.class});
            Field opPostWindowValue = appOpsClass.getDeclaredField(opVariableName);
            int value = ((Integer)opPostWindowValue.get(appOps)).intValue();
            int ret = ((Integer)checkOpNoThrowMethod.invoke(appOps, new Object[]{Integer.valueOf(value), Integer.valueOf(uid), pkg})).intValue();
            if(ret == 0) {
                result = AppOpsUtil.OptionCheckResult.ALLOWED;
            } else {
                result = AppOpsUtil.OptionCheckResult.NOT_ALLOWED;
            }
        } catch (Throwable var12) {
            Log.e("AppOpsUtil", "checkOption", var12);
        }

        return result;
    }

    public static enum OptionCheckResult {
        ALLOWED("allowed"),
        NOT_ALLOWED("notAllowed"),
        UNKNOW("unknow");

        private String type;

        private OptionCheckResult(String type) {
            this.type = type;
        }

        public String type() {
            return this.type;
        }

        public String toString() {
            return this.type.toString();
        }
    }

    public static String getSystemProperty() {
        String line = null;
        BufferedReader reader = null;

        try {
            Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");
            reader = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = reader.readLine();
            Log.d("ClipUtils", "getSystemProperty : " + line);
        } catch (IOException var11) {
            Log.e("getSystemProperty", var11.getMessage());
        } finally {
            try {
                reader.close();
            } catch (IOException var10) {
                var10.printStackTrace();
            }

        }

        return TextUtils.isEmpty(line)?"UNKNOWN":line;
    }
}

