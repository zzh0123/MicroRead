package com.zhenhua.microread.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.util.LogPrinter;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zhenhua.microread.R;
import com.zhenhua.microread.presenter.LoginPresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.view.LoginView;
import com.zhenhua.microread.widget.ClearEditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

public class LoginActivity extends AppCompatActivity implements LoginView {

    private LoginPresenter presenter;

    @BindView(R.id.et_phoneNum)
    public ClearEditText et_phoneNum;

    @BindView(R.id.tv_getVerifyCode)
    public TextView tv_getVerifyCode;

    @BindView(R.id.ll_wexin_login)
    public LinearLayout ll_wexin_login;

    private String phoneNum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new LoginPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_login);
        // 必须在setContentView();之后
        ButterKnife.bind(this);
    }

    @OnTextChanged(value = R.id.et_phoneNum, callback = AFTER_TEXT_CHANGED )
    void onAfterTextChange(Editable editable) {
        if (editable.toString().trim().length() == 11) {
            tv_getVerifyCode.setClickable(true);
            tv_getVerifyCode.setBackgroundResource(R.drawable.bt_rectangle_shape_orange);
        } else {
            tv_getVerifyCode.setClickable(false);
            tv_getVerifyCode.setBackgroundResource(R.drawable.bt_rectangle_shape_grey);
        }
    }

    @OnClick({R.id.tv_getVerifyCode, R.id.ll_wexin_login})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_getVerifyCode:
                getVerifyCode();
                break;
            case R.id.ll_wexin_login:

                break;
        }
    }

    private void getVerifyCode(){
        phoneNum = et_phoneNum.getText().toString().trim();
        presenter.getVerifyCode(phoneNum);
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showShort(this, message);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        LogUtils.i("--detachView--", "--detachView--");
    }

    @Override
    public Context getContext() {
        return this;
    }
}
