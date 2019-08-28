package com.zhenhua.microread.presenter;

import android.app.Activity;
import android.content.Intent;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zhenhua.microread.activity.MainActivity;
import com.zhenhua.microread.entity.Result;
import com.zhenhua.microread.utils.SPUtils;
import com.zhenhua.microread.utils.netsubscribe.LoginSubscribe;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultListener;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultSub;
import com.zhenhua.microread.view.VerifyCodeView;

import java.util.Map;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2019-08-22 23:36
 */
public class VerifyCodePresenter implements Presenter<VerifyCodeView>{

    private VerifyCodeView view;

    @Override
    public void attachView(VerifyCodeView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        if (view != null) view = null;
    }

    public void login(Map<String, Object> map){
        LoginSubscribe.login(map, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                view.showMessage("登录成功！");
                Result r = JSON.parseObject(result, Result.class);
                String data = r.getData().toString();

                JSONObject dataJsonObject = JSONObject.parseObject(data);
                String userId = dataJsonObject.getString("userId");
                SPUtils.put(view.getContext(), "userId", userId);
                Activity activity = (Activity) view.getContext();
                Intent intent = new Intent(activity, MainActivity.class);
                activity.startActivity(intent);
                activity.finish();
            }

            @Override
            public void onFault(String errorMsg) {
                view.showMessage(errorMsg);
            }
        }, view.getContext()));
    }
}
