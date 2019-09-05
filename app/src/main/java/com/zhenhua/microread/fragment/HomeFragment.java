package com.zhenhua.microread.fragment;


import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.zhenhua.microread.R;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.ToastUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements OnTabSelectListener {

    private Context context = getActivity();
    private static ArrayList<Fragment> fragmentList = new ArrayList<>();
    private final String[] titleArray = new String[]{
            "关注", "推荐", "养生"
            , "科技", "旅游", "钓鱼", "体育" , "股票"
    };
    private MyPagerAdapter adapter;

    @BindView(R.id.stl_home_fg)
    public SlidingTabLayout tabLayout;

    @BindView(R.id.vp_home_fg)
    public ViewPager viewPager;

    private View rootView;

    private Unbinder unbinder;

    public HomeFragment() {
        // Required empty public constructor
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_home, container, false);
//    }


    @Override
    protected View initViews(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        initFragmentList();

        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager());
//        LogUtils.i("--initViews--");
        viewPager.setAdapter(adapter);
        tabLayout.setViewPager(viewPager);
//        viewPager.setOffscreenPageLimit();
        viewPager.setCurrentItem(0);
        tabLayout.setOnTabSelectListener(this);
        return rootView;
    }

    private void initFragmentList(){
//        titleArray = new String[]{
//                "关注", "推荐", "热点"
//                , "前端", "后端", "设计", "工具资源" , "前端", "后端", "设计", "工具资源"
//        };
        fragmentList.clear();
        for (String title : titleArray) {
            HomeFollowFragment homeFollowFragment = new HomeFollowFragment();
            fragmentList.add(homeFollowFragment);
        }
        LogUtils.i("--fragmentList--" + fragmentList.size());
//        LogUtils.i("--titleArray--" + titleArray.length);
    }
    // 若不需要Lazy加载则initData方法内留空,初始化内容放到initViews即可
    @Override
    protected void initData() {
    }

    @Override
    protected void setDefaultFragmentTitle(String title) {

    }

    public static void refresh(){
        HomeFollowFragment homeFollowFragment = (HomeFollowFragment) fragmentList.get(0);
        homeFollowFragment.xrvfollow.refresh();
    }

    @Override
    public void onTabSelect(int position) {
//        ToastUtils.showShort(getActivity(), "onTabSelect&position--->" + position);
    }

    @Override
    public void onTabReselect(int position) {
//        ToastUtils.showShort(getActivity(), "onTabReselect&position--->" + position);
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
            return titleArray[position];
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
