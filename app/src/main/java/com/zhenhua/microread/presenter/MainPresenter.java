package com.zhenhua.microread.presenter;

import com.zhenhua.microread.utils.netsubscribe.MianSubscribe;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultListener;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultSub;
import com.zhenhua.microread.view.MainView;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * @Description:
 * @Author: zzh
 * @CreateDate: 2019-08-24 16:45
 */
public class MainPresenter implements Presenter<MainView> {

    private MainView view;

    @Override
    public void attachView(MainView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        if (view != null) view = null;
    }

    public void releastShare(Map<String, RequestBody> map, List<MultipartBody.Part> parts){
        MianSubscribe.multiUpload(map, parts, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                view.showMessage(result);
            }

            @Override
            public void onFault(String errorMsg) {
                view.showMessage(errorMsg);
            }
        }, view.getContext()));
    }
}
