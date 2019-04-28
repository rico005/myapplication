package com.example.fangsheng.myapplication.dingtalkrobot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.fangsheng.myapplication.R;

public class DingtalkMessageActivity extends AppCompatActivity {

    private Spinner mTargetSP;
    private EditText mContentED;
    private Button mSendBtn;

    private String currentTargetToken;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dingtalk_message);

        mTargetSP = findViewById(R.id.target_sp);
        mContentED = findViewById(R.id.msg_content_et);
        mSendBtn = findViewById(R.id.send_dingtalk_btn);

        setSpinner();

        mSendBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(currentTargetToken)){
                    Toast.makeText(DingtalkMessageActivity.this, "请选择有效的目标会话", Toast.LENGTH_SHORT).show();
                    return;
                }
                String content = mContentED.getText().toString();

                content = "1、上新改为双排Feed流\n"
                    + "2、详情页面去掉挂件\n"
                    + "3、拼脚波谱图快速验证页面上预发（需要扫特定二维码入口）\n 二维码入口详见群公告地址，务必切换到预发环境！！！";

                if (TextUtils.isEmpty(content)){
                    Toast.makeText(DingtalkMessageActivity.this, "发送内容不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                DingtalkRobotProcessor.sendTextByRobot(currentTargetToken, content);
                Toast.makeText(DingtalkMessageActivity.this, "消息已发送", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (String key : RobotConstants.access_token_map.keySet()){
            adapter.add(key);
        }

        mTargetSP.setAdapter(adapter);
        mTargetSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.d("DingtalkMessageActivity", position + "被选择");
                int i = 0;
                for (String key : RobotConstants.access_token_map.keySet()){
                    if (i == position){
                        currentTargetToken = RobotConstants.access_token_map.get(key);
                        return;
                    }
                    i++;
                }
                currentTargetToken = null;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.d("DingtalkMessageActivity", "没有内容被选择");
                currentTargetToken = null;
            }
        });
    }
}
