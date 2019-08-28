package com.zhenhua.microread.presenter;

import android.app.Activity;
import android.content.Intent;

import com.zhenhua.microread.activity.VerifyCodeActivity;
import com.zhenhua.microread.utils.netsubscribe.LoginSubscribe;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultListener;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultSub;
import com.zhenhua.microread.view.LoginView;

import java.util.Observable;

/**
 * @Description: java类作用描述
 * @Author: liys
 * @CreateDate: 2019-08-22 22:03
 */
public class LoginPresenter implements Presenter<LoginView> {

    private LoginView view;

    @Override
    public void attachView(LoginView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        if (view != null) view = null;
    }

    public void getVerifyCode(final String phoneNum){
        LoginSubscribe.getVerifyCode(phoneNum, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                view.showMessage("短信发送成功");
                Intent intent = new Intent(view.getContext(), VerifyCodeActivity.class);
                intent.putExtra("phoneNum", phoneNum);
                Activity activity = (Activity) view.getContext();
                activity.startActivity(intent);
            }

            @Override
            public void onFault(String errorMsg) {
                view.showMessage(errorMsg);
            }
        }, view.getContext()));
    }
}
