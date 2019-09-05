package com.zhenhua.microread.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnSelectListener;
import com.zhenhua.microread.MyApplication;
import com.zhenhua.microread.R;
import com.zhenhua.microread.entity.TabEntity;
import com.zhenhua.microread.fragment.HomeFragment;
import com.zhenhua.microread.fragment.MyFragment;
import com.zhenhua.microread.fragment.ShortVideoFragment;
import com.zhenhua.microread.presenter.MainPresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.SPUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.view.MainView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class MainActivity extends AppCompatActivity implements MainView {

    @BindView(R.id.ll_search)
    public LinearLayout ll_search;

    @BindView(R.id.iv_release)
    public ImageView iv_release;

    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private ArrayList<String> imagePathList;

    private MainPresenter presenter;

    private String userId;
    private List<Integer> typeList;

    private Context context = this;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    private String[] tabTitleArray = {"首页", "小视频", "我的"};
    private int[] tabIconUnselectArray = {
            R.mipmap.ic_tab_home_unselect, R.mipmap.ic_tab_video_unselect,
            R.mipmap.ic_tab_my_unselect};
    private int[] tabIconSelectArray = {
            R.mipmap.ic_tab_home_select, R.mipmap.ic_tab_video_select,
            R.mipmap.ic_tab_my_select};
    private ArrayList<CustomTabEntity> tabEntitiyList = new ArrayList<>();
    private View decorView;

    @BindView(R.id.vp_main)
    public ViewPager viewPager;

    @BindView(R.id.ctl_main)
    public CommonTabLayout tabLayout;

    Random mRandom = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_main);

        MyApplication.addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        HomeFragment homeFragment = new HomeFragment();
        ShortVideoFragment shortVideoFragment = new ShortVideoFragment();
        MyFragment myFragment = new MyFragment();
        fragmentList.add(homeFragment);
        fragmentList.add(shortVideoFragment);
        fragmentList.add(myFragment);

        for (int i = 0; i < tabTitleArray.length; i++) {
            tabEntitiyList.add(new TabEntity(tabTitleArray[i], tabIconSelectArray[i], tabIconUnselectArray[i]));
        }

//        decorView = getWindow().getDecorView();
//        viewPager = ViewFindUtils.find(mDecorView, R.id.vp_2);
        viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));

        tabLayout.setTabData(tabEntitiyList);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                if (position == 0) {
                    tabLayout.showMsg(0, mRandom.nextInt(100) + 1);
//                    UnreadMsgUtils.show(mTabLayout_2.getMsgView(0), mRandom.nextInt(100) + 1);
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(3);

        //显示未读红点
        tabLayout.showDot(2);

        //两位数
        tabLayout.showMsg(0, 55);
        tabLayout.setMsgMargin(0, -5, 5);

        //三位数
        tabLayout.showMsg(1, 100);
        tabLayout.setMsgMargin(1, -10, 5);

        //设置未读消息红点
//        tabLayout.showDot(2);
//        MsgView rtv_2_2 = tabLayout.getMsgView(2);
//        if (rtv_2_2 != null) {
//            UnreadMsgUtils.setSize(rtv_2_2, dp2px(7.5f));
//        }

        //设置未读消息背景
//        tabLayout.showMsg(3, 5);
//        tabLayout.setMsgMargin(3, 0, 5);
//        MsgView rtv_2_3 = tabLayout.getMsgView(3);
//        if (rtv_2_3 != null) {
//            rtv_2_3.setBackgroundColor(Color.parseColor("#6D8FB0"));
//        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitleArray[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    protected int dp2px(float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    @OnClick({R.id.ll_search, R.id.iv_release})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_search:

                break;
            case R.id.iv_release:
                showPop(iv_release);
                break;
        }
    }

    private void showPop(View view){
        new XPopup.Builder(getContext())
                .hasShadowBg(false)
//                        .popupAnimation(PopupAnimation.NoAnimation) //NoAnimation表示禁用动画
//                        .isCenterHorizontal(true) //是否与目标水平居中对齐
//                        .offsetY(-10)
//                        .popupPosition(PopupPosition.Top) //手动指定弹窗的位置
                .atView(view)  // 依附于所点击的View，内部会自动判断在上方或者下方显示
                .asAttachList(new String[]{"发图文", "写文章", "拍小视频"},
                        new int[]{R.mipmap.ic_image_and_text, R.mipmap.ic_write_article,
                                R.mipmap.ic_shoot_short_video},
                        new OnSelectListener() {
                            @Override
                            public void onSelect(int position, String text) {
//                                ToastUtils.showShort(MainActivity.this,position + "click " + text);
                                if (position == 0){
                                    Intent intent = new Intent(MainActivity.this, PublishImageAndTextActivity.class);
                                    startActivityForResult(intent, 1000);
                                } else if (position == 1){
                                    Intent intent = new Intent(MainActivity.this, PublishArticleActivity.class);
                                    startActivityForResult(intent, 1001);
                                } else if (position == 2){
                                    Intent intent = new Intent(MainActivity.this, PublishShortVideoActivity.class);
                                    startActivityForResult(intent, 1002);
                                }
                            }
                        })
                .show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showMessage(String message) {
        ToastUtils.showShort(this, message);
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        MyApplication.delActivity(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            switch (requestCode){
                case 1000:
                    LogUtils.i("--onActivityResult--", "--onActivityResult--");
                    HomeFragment homeFragment = (HomeFragment)fragmentList.get(0);
                    homeFragment.refresh();
                    break;
            }
        }
    }
}
