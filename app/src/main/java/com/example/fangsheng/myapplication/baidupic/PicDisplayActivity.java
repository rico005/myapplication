package com.example.fangsheng.myapplication.baidupic;

import java.util.List;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.fangsheng.myapplication.R;

public class PicDisplayActivity extends AppCompatActivity {

    private static final String init_keywords = "飞机";

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private EditText mSearchKeywordsEdt;
    private Button mSearchBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_display);

        mSearchKeywordsEdt = (EditText)findViewById(R.id.search_keywords_edt);
        mSearchBtn = (Button)findViewById(R.id.search_btn);
        mSearchBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchKeywordsEdt != null){
                    String keywords = mSearchKeywordsEdt.getText().toString();
                    if (!TextUtils.isEmpty(keywords)){
                        doFetchPicByKeywords(keywords);
                    }else {
                        Toast.makeText(getApplicationContext(), "关键词为空", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        mRecyclerView = (RecyclerView)findViewById(R.id.pic_list);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new BaiduPicListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        mSearchKeywordsEdt.setText(init_keywords);
        doFetchPicByKeywords(init_keywords);
    }

    private void doFetchPicByKeywords(String keywords){
        new BaiduPicSearchJsoupParser().fetchRemotePicList(keywords, new GetResultListener<List<String>, Object>() {

            @Override
            public void onSuccess(List<String> data, Object context) {
                ((BaiduPicListAdapter)mAdapter).refresh(data);
            }

            @Override
            public void onError(String errorCode, String errorMsg, Object context) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
