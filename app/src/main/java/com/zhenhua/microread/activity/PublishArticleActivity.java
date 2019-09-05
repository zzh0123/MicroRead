package com.zhenhua.microread.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.zhenhua.microread.MyApplication;
import com.zhenhua.microread.R;

/**
 *  发布文章
 */
public class PublishArticleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_article);
        MyApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MyApplication.delActivity(this);
    }
}
