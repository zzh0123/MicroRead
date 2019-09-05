package com.zhenhua.microread.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhenhua.microread.MyApplication;
import com.zhenhua.microread.R;
import com.zhenhua.microread.presenter.LoginPresenter;
import com.zhenhua.microread.presenter.VerifyCodePresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.view.VerifyCodeView;
import com.zhenhua.microread.widget.CountDownTextView;
import com.zhenhua.microread.widget.VerificationCodeEditText;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VerifyCodeActivity extends AppCompatActivity implements VerifyCodeView {

    @BindView(R.id.tv_head_title)
    public TextView tv_head_title;

    @BindView(R.id.et_verify_code)
    public VerificationCodeEditText et_verify_code;

    @BindView(R.id.tv_count_down)
    public CountDownTextView tv_count_down;

    private VerifyCodePresenter presenter;
    private Map<String, Object> map = new HashMap<>();
    private String phoneNum, code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new VerifyCodePresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_verify_code);

        MyApplication.addActivity(this);
        ButterKnife.bind(this);
        initView();
        phoneNum = getIntent().getStringExtra("phoneNum");
    }

    private void initView(){
        tv_head_title.setText("验证码登录");

        tv_count_down
                .setNormalText("获取验证码")
                .setCountDownText("", "秒后可重新获取验证码")
                .setCloseKeepCountDown(false)//关闭页面保持倒计时开关
                .setCountDownClickable(false)//倒计时期间点击事件是否生效开关
                .setShowFormatTime(false)//是否格式化时间
                .setIntervalUnit(TimeUnit.SECONDS)
                .setOnCountDownStartListener(new CountDownTextView.OnCountDownStartListener() {
                    @Override
                    public void onStart() {
//                        Toast.makeText(VerificationCodeActivity.this, "开始计时", Toast.LENGTH_SHORT).show();
                        tv_count_down.setTextColor(getResources().getColor(R.color.grey2));
                    }
                })
                .setOnCountDownTickListener(new CountDownTextView.OnCountDownTickListener() {
                    @Override
                    public void onTick(long untilFinished) {
//                        LogUtils.e("------", "onTick: " + untilFinished);
                    }
                })
                .setOnCountDownFinishListener(new CountDownTextView.OnCountDownFinishListener() {
                    @Override
                    public void onFinish() {
//                        Toast.makeText(VerificationCodeActivity.this, "倒计时完毕", Toast.LENGTH_SHORT).show();
                        tv_count_down.setTextColor(getResources().getColor(R.color.orange));
                    }
                })
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        Toast.makeText(VerificationCodeActivity.this, "短信已发送", Toast.LENGTH_SHORT).show();
                        tv_count_down.startCountDown(30);
                    }
                });
        tv_count_down.startCountDown(30);

        et_verify_code.setOnVerificationCodeChangedListener(new VerificationCodeEditText
                .OnVerificationCodeChangedListener() {

            @Override
            public void onVerCodeChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void onInputCompleted(CharSequence s) {
//                ToastUtils.showShort(VerificationCodeActivity.this, "输入完了");
                login();
            }
        });
    }

    private void login(){
        code = et_verify_code.getText().toString().trim();
        map.put("phoneNum", phoneNum);
        map.put("code", code);
        presenter.login(map);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showShort(this, message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        MyApplication.delActivity(this);
    }
}
