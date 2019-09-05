package com.zhenhua.microread.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.zhenhua.microread.R;
import com.zhenhua.microread.adapter.ShareAdapter;
import com.zhenhua.microread.entity.Result;
import com.zhenhua.microread.entity.Share;
import com.zhenhua.microread.presenter.HomeFollowFgPresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.SPUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.view.HomeFollowFgView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFollowFragment extends BaseFragment implements HomeFollowFgView {

    @BindView(R.id.recyclerview_follow)
    public XRecyclerView xrvfollow;

    private View rootView;

    private Unbinder unbinder;

    private Integer pageNum = 1; // 页
    private Integer pageSize = 5; // 行
    private ArrayList<Share> shareListTotal = new ArrayList<Share>();
    private ShareAdapter adapter;

    private HomeFollowFgPresenter presenter;
    private Map<String, Object> map;

    public HomeFollowFragment() {
        // Required empty public constructor
    }


//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home_follow, container, false);
//    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        presenter =  new HomeFollowFgPresenter();
        presenter.attachView(this);
        rootView = inflater.inflate(R.layout.fragment_home_follow, container, false);
//        xrvfollow = rootView.findViewById(R.id.recyclerview_follow);
        unbinder = ButterKnife.bind(this, rootView);
        setRecyclerview();
        return rootView;
    }

    @Override
    protected void initData() {
        if (!isPrepared()) {
            Log.w("initData", "目标已被回收");
            return;
        }
        getShareList();
    }

    private void getShareList(){
        String userId = (String) SPUtils.get(getActivity(), "userId", "");
        userId = "18311004536";
        map = new HashMap<>();
        map.put("userId", userId);
        map.put("type", "001");
        map.put("pageNum", pageNum + "");
        map.put("pageSize", pageSize + "");
        presenter.getShareList(map);
    }

    private void setRecyclerview(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        xrvfollow.setLayoutManager(layoutManager);

        xrvfollow.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        xrvfollow.setLoadingMoreProgressStyle(ProgressStyle.BallRotate);
        xrvfollow.setArrowImageView(R.mipmap.iconfont_downgrey);

        xrvfollow
                .getDefaultRefreshHeaderView()
                .setRefreshTimeVisible(true);
//        View header = LayoutInflater.from(this).inflate(R.bottom_group_buy_person_num.activity_score_header, (ViewGroup) findViewById(android.R.id.content), false);
//        header.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "这是头部", Toast.LENGTH_LONG).show();
//            }
//        });
//        recyclerview_notice.addHeaderView(header);
        xrvfollow.getDefaultFootView().setLoadingHint("加载中...");
        xrvfollow.getDefaultFootView().setNoMoreHint("没有更多数据了");
        xrvfollow.getDefaultFootView().setMinimumHeight(150);

        xrvfollow.setPullRefreshEnabled(true);
        xrvfollow.setLoadingMoreEnabled(true);
        xrvfollow.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageNum = 1;
//                        xrvfollow.refreshComplete();
                        getShareList();
                    }

                }, 0);            //refresh data here
            }

            @Override
            public void onLoadMore() {
                Log.e("onLoadMore", "call onLoadMore");
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        pageNum++;
                        getShareList();
                    }
                }, 1000);
            }
        });

        adapter = new ShareAdapter(getActivity(), shareListTotal);
        adapter.setClickCallBack(new ShareAdapter.ItemClickCallBack() {
            @Override
            public void onItemClick(int pos) {
                // 跳转到详情
//                Intent intent = new Intent(MessageListActivity.this, MessageDetailActivity.class);
//                id = list_message.get(pos).getId();
//                intent.putExtra("id", id);
//                startActivity(intent);
            }
        });
        xrvfollow.setAdapter(adapter);
    }
    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    @Override
    public void showShareList(String result) {
//        xrvfollow.refreshComplete();
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray jsonArray = jsonObject.getJSONArray("data");
        String jsonArrayStr = JSONObject.toJSONString(jsonArray); // 将array数组转换成字符串
        List<Share> dataList =  JSONObject.parseArray(jsonArrayStr, Share.class); // 把字符串转换成集合
//        LogUtils.i("--dataList.size--", "-dataList.size--" + dataList.get(0).getImgList().size());
        if (dataList != null && dataList.size() > 0){
            int sizeBefore = shareListTotal.size();
            shareListTotal.addAll(dataList);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObjectShare = (JSONObject) jsonArray.get(i);
                JSONArray jsonArrayImg = (JSONArray) jsonObjectShare.getJSONArray("imgList");
//                ArrayList<String> imgList = new ArrayList<>();
                if (jsonArrayImg != null && jsonArrayImg.size() > 0) {
                    String jsonArrayImgStr = JSONObject.toJSONString(jsonArrayImg); // 将array数组转换成字符串
                    List<String> imgList = JSONObject.parseArray(jsonArrayImgStr, String.class); // 把字符串转换成集合
                    shareListTotal.get(sizeBefore + i).setImgList(imgList);
//                    LogUtils.i("--shareList.size--", "-shareList.size--" + shareList.get(i).getImgList().size());
                }
            }
            LogUtils.i("--shareListTotal.size--", "-shareListTotal.size--" + shareListTotal.size());
            xrvfollow.refreshComplete();
            adapter.notifyDataSetChanged();
        } else {
            xrvfollow.refreshComplete();
            xrvfollow.setNoMore(true);
        }
        xrvfollow.refreshComplete();
    }

    @Nullable
    @Override
    public Context getContext() {
        return getActivity();
    }

    @Override
    public void showMessage(String message) {
        xrvfollow.refreshComplete();
        ToastUtils.showShort(getActivity(), message);
    }

    @Override
    public void onDestroyView() {
        presenter.detachView();
        super.onDestroyView();
        unbinder.unbind();
    }
}
