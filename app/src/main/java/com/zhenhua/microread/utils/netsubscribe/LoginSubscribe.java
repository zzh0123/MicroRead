package com.zhenhua.microread.utils.netsubscribe;

import com.zhenhua.microread.utils.netutils.RetrofitFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Author: zzhh
 * Date: 2019/7/24 10:47
 * Description: ${DESCRIPTION}
 */
public class LoginSubscribe {
     /**
      * @method
      * @description 获取短信验证码
      * @date:
      * @author: zzhh
      * @param
      * @return
      */
    public static void getVerifyCode(String phoneNum, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getVerifyCode(phoneNum);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    /**
     * 验证码登录
     * @param map
     * @param subscriber
     */
    public static void login(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().login(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
