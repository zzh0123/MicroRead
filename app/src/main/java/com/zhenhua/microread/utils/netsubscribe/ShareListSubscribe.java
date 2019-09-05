package com.zhenhua.microread.utils.netsubscribe;

import com.zhenhua.microread.utils.netutils.RetrofitFactory;

import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Author: zzhh
 * Date: 2019/9/4 11:00
 * Description: ${DESCRIPTION}
 */
public class ShareListSubscribe {

    /**
     * 获取图文列表
     * @param map
     * @param subscriber
     */
    public static void getShareList(Map<String, Object> map, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().getShareList(map);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
