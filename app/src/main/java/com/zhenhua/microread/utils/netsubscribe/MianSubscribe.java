package com.zhenhua.microread.utils.netsubscribe;

import com.zhenhua.microread.utils.netutils.RetrofitFactory;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2019-08-24 16:48
 */
public class MianSubscribe {

    /**
     * 发布内容
     * @param parts
     * @param subscriber
     */
    public static void multiUpload(RequestBody userId, RequestBody content, RequestBody typeList,
                                   MultipartBody.Part[] parts, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().multiUpload(userId, content, typeList, parts);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }

    public static void test(List<Integer> typeList, DisposableObserver<ResponseBody> subscriber) {
        Observable<ResponseBody> observable =  RetrofitFactory.getInstance().getHttpApi().test(typeList);
        RetrofitFactory.getInstance().toSubscribe(observable, subscriber);
    }
}
