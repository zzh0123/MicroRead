package com.zhenhua.microread.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.lcw.library.imagepicker.ImagePicker;
import com.zhenhua.microread.R;
import com.zhenhua.microread.presenter.MainPresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.SPUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.utils.imgutils.GlideLoader;
import com.zhenhua.microread.utils.netsubscribe.MianSubscribe;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultListener;
import com.zhenhua.microread.utils.netutils.OnSuccessAndFaultSub;
import com.zhenhua.microread.view.MainView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MainPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);
    }

    @OnClick({R.id.ll_search, R.id.iv_release})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.ll_search:
                test();
                break;
            case R.id.iv_release:
                pickImg();
                break;
        }
    }

    private void test(){
        List<Integer> typeList = new ArrayList<>();
        typeList.add(0);
        typeList.add(0);
        typeList.add(0);
        typeList.add(0);
        typeList.add(1);
        typeList.add(1);
        typeList.add(1);
        typeList.add(1);

        String json_str = JSON.toJSONString(typeList);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json"), json_str);
        RequestBody requestBody4 = toRequestBody(json_str);
        MianSubscribe.test(typeList, new OnSuccessAndFaultSub(new OnSuccessAndFaultListener() {
            @Override
            public void onSuccess(String result) {

            }

            @Override
            public void onFault(String errorMsg) {

            }
        }));
    }

    private void pickImg(){
        ImagePicker.getInstance()
                .setTitle("标题")//设置标题
                .showCamera(true)//设置是否显示拍照按钮
                .showImage(true)//设置是否展示图片
                .showVideo(true)//设置是否展示视频
                .setMaxCount(9)//设置最大选择图片数目(默认为1，单选)
                .setSingleType(true)//设置图片视频不能同时选择
                .setImagePaths(imagePathList)//设置历史选择记录
                .setImageLoader(new GlideLoader())//设置自定义图片加载器
                .start(MainActivity.this, REQUEST_SELECT_IMAGES_CODE);//REQEST_SELECT_IMAGES_CODE为Intent调用的requestCode
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
            imagePathList = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);
            LogUtils.i("--size--", "--size--" + imagePathList.size());
            typeList = new ArrayList<Integer>();
            for (String s: imagePathList) {
                typeList.add(0);
            }
            LogUtils.i("--size1--", "--size1--" + typeList.size());
            releastShare();
        }
    }

    private void releastShare(){
//        map.put("AppId", RequestBody.create(MediaType.parse("text/plain"), Url.APP_ID));
//        map.put("file" + i + "\";filename=\"" + files.getName(), requestBody);

        userId = (String) SPUtils.get(this, "userId", "");
        userId = "18311004536";
        LogUtils.i("--userId--", "--userId--" + userId);
        String content = "的路嘻嘻嘻！";

//        Gson  gson = new Gson();
//        String json_str = gson.toJson(typeList);
//        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json; charset=utf-8"),json_str);

//        Map<String, RequestBody> map = new HashMap<>();

        String json_str = JSON.toJSONString(typeList);
        RequestBody requestBody3 = RequestBody.create(MediaType.parse("application/json;charset=utf-8"), json_str);
//        RequestBody requestBody3 = toRequestBody(json_str);

        RequestBody requestBody_userId = toRequestBody(userId);
        RequestBody requestBody_content = toRequestBody(content);
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        map.put("typeList", requestBody3);
        map.put("userId", requestBody_userId);
        map.put("content", requestBody_content);


        List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
        for (int i = 0; i < imagePathList.size(); i++) {
            File file = new File(imagePathList.get(i));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
//            parts[i] = filePart;
            parts.add(filePart);

            typeList.add(1);
        }
        presenter.releastShare(map, parts);
    }

    public RequestBody toRequestBody(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain"), value);
        return requestBody;
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
    }
}
