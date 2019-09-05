package com.zhenhua.microread.presenter;

import com.zhenhua.microread.utils.netsubscribe.ShareListSubscribe;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultListener;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultSub;
import com.zhenhua.microread.view.HomeFollowFgView;

import java.util.Map;

/**
 * Author: zzhh
 * Date: 2019/9/4 10:57
 * Description: ${DESCRIPTION}
 */
public class HomeFollowFgPresenter implements Presenter<HomeFollowFgView> {

    private HomeFollowFgView view;

    @Override
    public void attachView(HomeFollowFgView view) {
        this.view = view;
    }

    @Override
    public void detachView() {
        if (view != null) view = null;
    }

    public void getShareList(Map<String, Object> map){
        ShareListSubscribe.getShareList(map, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {
                view.showShareList(result);
            }

            @Override
            public void onFault(String errorMsg) {
                view.showMessage(errorMsg);
            }
        }, view.getContext()));
    }
}
