package com.zhenhua.microread.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhenhua.microread.MyApplication;
import com.zhenhua.microread.R;
import com.zhenhua.microread.utils.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyFragment extends Fragment {

    @BindView(R.id.tv_sign_out)
    public TextView tvSignOut;

    private View rootView;
    private Unbinder unbinder;

    public MyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_my, container, false);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @OnClick({R.id.tv_sign_out})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_sign_out:
//                SPUtils.clear(getActivity());
                SPUtils.remove(getActivity(), "userId");
                MyApplication.exit();
                break;
        }
    }

}
