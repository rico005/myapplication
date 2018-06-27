package com.example.fangsheng.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.fangsheng.myapplication.decorate.DecorateBuilder;
import com.example.fangsheng.myapplication.decorate.DecorateHelper;
import com.example.fangsheng.myapplication.decorate.decorator.TestBaseDecorator;

public class DecoratorTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        TestBaseDecorator ins = DecorateHelper.resolve(getIntent()).getDecoratorInstance(TestBaseDecorator.class, null);

        TextView tv_result = (TextView) findViewById(R.id.tv_result);
        tv_result.setText(ins.decorate());
    }
}
