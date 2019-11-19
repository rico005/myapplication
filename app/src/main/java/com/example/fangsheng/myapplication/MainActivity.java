package com.example.fangsheng.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.fangsheng.myapplication.ShortcutBadger.ShortcutBadgerUtils;
import com.example.fangsheng.myapplication.appops.AppOpsUtil;
import com.example.fangsheng.myapplication.baidupic.PicDisplayActivity;
import com.example.fangsheng.myapplication.circleprogressbar.CircleProgressBar;
import com.example.fangsheng.myapplication.decorate.DecorateHelper;
import com.example.fangsheng.myapplication.decorate.decorator.TestChildDecorator;
import com.example.fangsheng.myapplication.dingtalkrobot.DingtalkRobotProcessor;
import com.example.fangsheng.myapplication.image.ImageCutActivity;
import com.example.fangsheng.myapplication.notification.AgooNotificationManger;
import com.example.fangsheng.myapplication.shadow.ShadowImageActivity;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    protected static Random notificationRandom = new Random(100000);

    private LinearLayout view;
    private Button contactBtn;
    private Button notificationBtn1;
    private Button notificationBtn2;
    private Button dingtalkrobotBtn;
    private Button baidupicfetchBtn;
    private Button imageCutBtn;
    private TextView titleTv;
    private TextView htmlTv;
    private ScrollView textSv;
    private CircleProgressBar circleProgressBar; // 自定义的进度条
    private Button shadowImgBtn;

    GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getCurMethodName();
        getCurMethodName(new Object(){});
//        testEnumValueOf();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view = (LinearLayout)findViewById(R.id.activity_main);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "whole layout clicked");
            }
        });
        htmlTv = (TextView) findViewById(R.id.html_tv);
        contactBtn = (Button)findViewById(R.id.contact_btn);
        String htmlRaw = "<a href=\"taobao://message/root\">example.com</a>";
        htmlTv.setText(Html.fromHtml(htmlRaw));
        htmlTv.setMovementMethod(LinkMovementMethod.getInstance());
        contactBtn.setText("联系" + "\n" + "人");
        notificationBtn1 = (Button)findViewById(R.id.notification_btn1);
        notificationBtn2 = (Button)findViewById(R.id.notification_btn2);
        dingtalkrobotBtn = (Button)findViewById(R.id.dingtalkrobot_btn);
        baidupicfetchBtn = (Button)findViewById(R.id.baidupicfetch_btn);
        imageCutBtn = findViewById(R.id.imagecut_btn);
        titleTv = (TextView)findViewById(R.id.title_tv);
        gestureDetector = new GestureDetector(this.getApplicationContext(), new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                Log.d("MainActivity", "onSingleTapConfirmed");
                return false;
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                Log.d("MainActivity", "onDoubleTap");
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                Log.d("MainActivity", "onLongPress");
            }
        });
        titleTv.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                return gestureDetector.onTouchEvent(event);
            }

        });
        textSv = (ScrollView) findViewById(R.id.textview_sv);
        shadowImgBtn = (Button) findViewById(R.id.shadow_btn);

        handleClickSpan();

//        String sql_create_contact = "";
//        String sql_drop_contact = "";
//        String sql_create_message = "";
//        String sql_drop_message = "";
//        try{
//            DatabaseType databaseType = new BaseSqliteDatabaseType();
//            sql_create_contact = TableUtils.getCreateTableStatements(databaseType, Contact.class).toString();
//            sql_drop_contact = TableUtils.getDropTableStatements(databaseType, Contact.class).toString();
//            sql_create_message = TableUtils.getCreateTableStatements(databaseType, ImMessage.class).toString();
//            sql_drop_message = TableUtils.getDropTableStatements(databaseType, ImMessage.class).toString();
//        }catch (SQLException e){
//
//        }
//
////        Log.d("MainActivity", sql_create_contact);
////        Log.d("MainActivity", sql_drop_contact);
//
//        Log.d("MainActivity", sql_create_message);
//        Log.d("MainActivity", sql_drop_message);
//
//        Log.d("MainActivity", ""+System.getProperty("ro.build.version.emui"));
//        try {
//            Process du = Runtime.getRuntime().exec("getprop ro.build.version.emui");
//            BufferedReader in = new BufferedReader(new InputStreamReader(du.getInputStream()));
//            String gpsvalue = in.readLine();
//            Log.d("MainActivity", "emui: "+gpsvalue);
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        notificationBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                performNotify(0);
//                gotoDecoratorTestActivity();
                sendBroadcast();
            }
        });

        notificationBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                performNotify(1);
                sendLocalBoradcast();
            }
        });

        dingtalkrobotBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DingtalkRobotProcessor.sendTextByRobot();
            }
        });

        baidupicfetchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoPicDisplayActivity();
            }
        });

        imageCutBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoImageCutActivity();
            }
        });

        contactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchContact();
            }
        });

        shadowImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoImageShadowActivity();
            }
        });

//        new FloatView(getApplicationContext()).showFloat();

        registerBroadcast();

        initCircleProgressBar();

        ShortcutBadgerUtils.ShortcutBadgerTest(getApplicationContext(), 100);

        Log.d("main", "isWindowManagerAvaliable=" + isWindowManagerAvaliable());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterBoradcast();
    }

    private void launchContact(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType(ContactsContract.Contacts.CONTENT_TYPE);

        intent.putExtra(ContactsContract.Intents.Insert.PHONE, "123");

        startActivity(intent);
    }

    private void performNotify(int type){
        AgooNotificationManger.instance(getApplicationContext()).sendNotify(null, null, type);
    }

    private void gotoPicDisplayActivity(){
        Intent i = new Intent(this, PicDisplayActivity.class);
        this.startActivity(i);
    }

    private void gotoImageCutActivity(){
        Intent i = new Intent(this, ImageCutActivity.class);
        this.startActivity(i);
    }

    private void gotoImageShadowActivity(){
        Intent i = new Intent(this, ShadowImageActivity.class);
        this.startActivity(i);
    }

    private void gotoDecoratorTestActivity(){
        Intent i = new Intent(this, DecoratorTestActivity.class);
        i = DecorateHelper.assembleIntent(i, null, DecorateHelper.build().addDecorator(TestChildDecorator.class));
        this.startActivity(i);
    }

    public String getCurMethodName(){
        String clazzName1 = new Throwable().getStackTrace()[1].getMethodName();
        String clazzName2 = Thread.currentThread().getStackTrace()[2].getMethodName();
        Log.d("MainActivity", "getCurMethodName: " + clazzName2 + " | " + clazzName1);
        return clazzName2;
    }

    public String getCurMethodName(Object anonymousObj){
        if (anonymousObj == null){
            anonymousObj = new Object(){};
        }
        String name1 = anonymousObj.getClass().getEnclosingMethod().getName();
        String class1 = anonymousObj.getClass().getEnclosingClass().getSimpleName();
//        String name2 = this.getClass().getEnclosingMethod().getName();
        Log.d("MainActivity", "getCurMethodName1: " + name1 + " | " + class1);
        return name1;
    }

    public String testEnumValueOf(){
        return GroupUserIdentityModel.valueOf("2").code();
    }

    /*
    private void setTabDrawable(int txtColor, int txt, RadioButton host) {
        Drawable draw = host.getCompoundDrawables()[1];
        if (draw != null) {
            if (txtColor != 0)
                ((TextDrawable) draw).setTextColor(getResources().getColor(txtColor));
            if (txt != 0)
                ((TextDrawable) draw).setText(getResources().getString(txt));
        } else {
            TextDrawable mDrawable = new TextDrawable(this);
            mDrawable.setTextColor(getResources().getColor(txtColor));
            mDrawable.setTextSize(24);
            mDrawable.setText(getResources().getString(txt));
            try {
                Typeface iconfont = Typeface.createFromAsset(Globals.getApplication().getAssets(), "uik_iconfont.ttf");
                mDrawable.setTypeface(iconfont);
            } catch (Exception e) {
                e.printStackTrace();
            }
            host.setCompoundDrawablesWithIntrinsicBounds(null, mDrawable, null, null);
        }
    }
    */

    private void sendBroadcast(){
        Intent intent = new Intent();
        intent.setAction("update");
        this.getApplicationContext().sendBroadcast(intent);
    }

    private void sendLocalBoradcast(){
        Intent intent = new Intent();
        intent.setAction("update");
        LocalBroadcastManager.getInstance(this.getApplicationContext()).sendBroadcast(intent);
    }

    private void registerBroadcast(){
        IntentFilter filter = new IntentFilter("update");
        LocalBroadcastManager.getInstance(this.getApplicationContext()).registerReceiver(receiverLocal, filter);
        this.getApplicationContext().registerReceiver(receiver, filter);
    }

    private void unregisterBoradcast(){
        LocalBroadcastManager.getInstance(this.getApplicationContext()).unregisterReceiver(receiverLocal);
        this.getApplicationContext().unregisterReceiver(receiver);
    }

    BroadcastReceiver receiverLocal = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                Log.d("MainActivity","receiverLocal onReceive " + intent.getAction());
            }
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null){
                Log.d("MainActivity","receiver onReceive " + intent.getAction());
            }
        }
    };

    private void handleClickSpan(){
        titleTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("MainActivity", "titleTv clicked");
            }
        });
        try {
            String textall = titleTv.getText().toString();
            for (int i=0; i<100; i++){
                textall+="这个是测试内容!!";
            }
            titleTv.setText(textall);
            //超级链接使用内置浏览器打开
            SpannableStringBuilder spanStr = new SpannableStringBuilder(titleTv.getText());
            URLSpan[] urls = spanStr.getSpans(0, titleTv.getText().length(), URLSpan.class);
            ClickableSpan[] clickables = spanStr.getSpans(0, titleTv.getText().length(), ClickableSpan.class);
            if (urls.length > 0 || clickables.length > 0) {
                for (URLSpan url : urls) {
                    final Uri uri = Uri.parse(url.getURL());
                    if ("http".equals(uri.getScheme()) || "https".equals(uri.getScheme())) {
                        int endPos = spanStr.getSpanEnd(url);
                        int startPos = spanStr.getSpanStart(url);
                        int flag = spanStr.getSpanFlags(url);
                        if (endPos != -1 && startPos != -1) {
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View paramView) {
                                    Log.d("MainActivity", "url clicked");
                                }
                            };
                            spanStr.setSpan(clickableSpan, startPos, endPos, flag);
                        }
                    }
                }
                for (ClickableSpan clickable : clickables){
                    int start = spanStr.getSpanStart(clickable);
                    int end = spanStr.getSpanEnd(clickable);
                    int flag = spanStr.getSpanFlags(clickable);
                    String content = spanStr.subSequence(start, end).toString();
                    Log.d("MainActivity", "onClick [" + content + "]");
                    if (Patterns.PHONE.matcher(content).matches()){
                        ClickableSpan clickableSpan = new ClickableSpan() {
                            @Override
                            public void onClick(View paramView) {
                                Log.d("MainActivity", "phone num clicked");
                            }
                        };
                        spanStr.setSpan(clickableSpan, start, end, flag);
                    }
                }
                titleTv.setText(spanStr);
                titleTv.setMovementMethod(ScrollingMovementMethod.getInstance());
            }
        } catch (Exception e) {
            Log.e("MainActivity", e.getMessage());
        }
    }

    private void initCircleProgressBar(){
        circleProgressBar = (CircleProgressBar) findViewById(R.id.circleProgressBar);
//		circleProgressBar.setFirstColor(Color.LTGRAY);
		circleProgressBar.setColorArray(new int[] { Color.parseColor("#FF9000"), Color.parseColor("#FF5000")}); //觉得进度条颜色丑的，这里可以自行传入一个颜色渐变数组。
//		circleProgressBar.setCircleWidth(6);
        textSv.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                int scrollY = textSv.getScrollY(); // For ScrollView
                int scrollX = textSv.getScrollX(); // For HorizontalScrollView
                // DO SOMETHING WITH THE SCROLL COORDINATES
                Log.d("MainActivity", "scrollY=" + scrollY + "|scrollX=" + scrollX);
                circleProgressBar.setProgress(scrollY/10);
            }
        });
//        circleProgressBar.setProgress(60, true);
    }

    private boolean isWindowManagerAvaliable(){
        if (AppOpsUtil.checkOption(getApplication().getApplicationContext(), AppOpsUtil.OP_SYSTEM_ALERT_WINDOW_VAR)
            .equals(AppOpsUtil.OptionCheckResult.ALLOWED)){
            return true;
        }

        if(!AppOpsUtil.getSystemProperty().toUpperCase().equals("V8")){
            // not MIUI V8
            return true;
        }

        return false;
    }
}
