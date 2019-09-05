package com.zhenhua.microread.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
import com.zhenhua.microread.MyApplication;
import com.zhenhua.microread.R;
import com.zhenhua.microread.adapter.GridImageAdapter;
import com.zhenhua.microread.presenter.PubImgTextPresenter;
import com.zhenhua.microread.utils.LogUtils;
import com.zhenhua.microread.utils.SPUtils;
import com.zhenhua.microread.utils.ToastUtils;
import com.zhenhua.microread.utils.netutils.RequestBodyUtil;
import com.zhenhua.microread.view.PublishImageAndTextView;
import com.zhenhua.microread.widget.FullyGridLayoutManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static butterknife.OnTextChanged.Callback.AFTER_TEXT_CHANGED;

public class PublishImageAndTextActivity extends AppCompatActivity implements PublishImageAndTextView {
    private final static String TAG = PublishImageAndTextActivity.class.getSimpleName();
    @BindView(R.id.tv_cancel)
    public TextView tv_cancel;

    @BindView(R.id.tv_publish)
    public TextView tv_publish;

    @BindView(R.id.et_content)
    public EditText et_content;


    private int maxSelectNum = 9;
    private List<LocalMedia> selectList = new ArrayList<>();
    private List<String> selectPathList = new ArrayList<>();
    private List<Integer> typeList = new ArrayList<Integer>();;

    @BindView(R.id.recyclerView_pic)
    public RecyclerView recyclerView_pic;

    private GridImageAdapter adapter;
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener;

    private PubImgTextPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new PubImgTextPresenter();
        presenter.attachView(this);
        setContentView(R.layout.activity_publish_image_and_text);

        MyApplication.addActivity(this);
        ButterKnife.bind(this);
        initView();
    }

    private void initView(){
        onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
            @Override
            public void onAddPicClick() {
                selectPic();
            }
        };
        FullyGridLayoutManager manager = new FullyGridLayoutManager(PublishImageAndTextActivity.this,
                4, GridLayoutManager.VERTICAL, false);
        recyclerView_pic.setLayoutManager(manager);
        adapter = new GridImageAdapter(PublishImageAndTextActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(maxSelectNum);
        recyclerView_pic.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    switch (mediaType) {
                        case 1:
                            // 预览图片 可自定长按保存路径
                            //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                            PictureSelector.create(PublishImageAndTextActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                            break;
                        case 2:
                            // 预览视频
                            PictureSelector.create(PublishImageAndTextActivity.this).externalPictureVideo(media.getPath());
                            break;
                        case 3:
                            // 预览音频
                            PictureSelector.create(PublishImageAndTextActivity.this).externalPictureAudio(media.getPath());
                            break;
                    }
                }
            }
        });

    }

    @OnClick({R.id.tv_cancel, R.id.tv_publish})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_publish:
                String content = et_content.getText().toString().trim();
                if (TextUtils.isEmpty(content) && (selectPathList.size() == 0)){
                    ToastUtils.showShort(this, "写点啥");
                    return;
                }
                publishImageAndText();
                break;
        }
    }

    @OnTextChanged(value = R.id.et_content, callback = AFTER_TEXT_CHANGED )
    public void onAfterTextChange(Editable editable) {
        if (editable.toString().trim().length() > 0) {
            tv_publish.setClickable(true);
            tv_publish.setTextColor(getResources().getColor(R.color.blue));
        } else if (editable.toString().trim().length() <= 0 && selectPathList.size() <= 0){
            tv_publish.setClickable(false);
            tv_publish.setTextColor(getResources().getColor(R.color.grey2));
        }
    }

    private void publishImageAndText(){
        String userId = (String) SPUtils.get(this, "userId", "");
//        userId = "18311004536";
        String content = et_content.getText().toString().trim();
        RequestBody rbUserId = RequestBodyUtil.toRequestBodyText(userId);
        RequestBody rbContent = RequestBodyUtil.toRequestBodyText(content);
        RequestBody rbTypeList  = RequestBodyUtil.toRequestBodyJson(typeList);
        Map<String, RequestBody> map = new HashMap<String, RequestBody>();
        map.put("userId", rbUserId);
        map.put("content", rbContent);
        map.put("typeList", rbTypeList);

        List<MultipartBody.Part> parts = new ArrayList<MultipartBody.Part>();
        for (int i = 0; i < selectPathList.size(); i++) {
            File file = new File(selectPathList.get(i));
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("file", file.getName(), requestFile);
            parts.add(filePart);
        }
        presenter.multiUpload(map, parts);
    }

    private void selectPic(){
//        String path = Environment. getExternalStorageDirectory() + "/MicroReadPhoto1";
//        File file = new File(Environment.getExternalStoragePublicDirectory(
//                Environment.DIRECTORY_PICTURES), "MicroReadPhoto1");
//        if(!file.mkdirs()) {
//            LogUtils.e("Directory not created");
//        }
//        String path = file.getAbsolutePath().toString();

        // 进入相册 以下是例子：不需要的api可以不写
        PictureSelector.create(PublishImageAndTextActivity.this)
                .openGallery(PictureMimeType.ofImage())// 全部.PictureMimeType.ofAll()、图片.ofImage()、视频.ofVideo()、音频.ofAudio()
                .theme(R.style.picture_default_style)// 主题样式设置 具体参考 values/styles   用法：R.style.picture.white.style
                .maxSelectNum(maxSelectNum)// 最大图片选择数量
                .minSelectNum(1)// 最小选择数量
                .imageSpanCount(3)// 每行显示个数
                .selectionMode(PictureConfig.MULTIPLE)// 多选 or 单选 PictureConfig.MULTIPLE : PictureConfig.SINGLE
                .previewImage(true)// 是否可预览图片
//                .previewVideo(true)// 是否可预览视频
//                .enablePreviewAudio(true) // 是否可播放音频
                .isCamera(true)// 是否显示拍照按钮
                .isZoomAnim(true)// 图片列表点击 缩放效果 默认true
                //.imageFormat(PictureMimeType.PNG)// 拍照保存图片格式后缀,默认jpeg
//                .setOutputCameraPath("/MicroReadPhoto")// 自定义拍照保存路径
//                .enableCrop(true)// 是否裁剪
                .compress(true)// 是否压缩
                .synOrAsy(true)//同步true或异步false 压缩 默认同步
//                .compressSavePath(path)//压缩图片保存地址
                //.sizeMultiplier(0.5f)// glide 加载图片大小 0~1之间 如设置 .glideOverride()无效
                .glideOverride(160, 160)// glide 加载宽高，越小图片列表越流畅，但会影响列表图片浏览的清晰度
//                .withAspectRatio(aspect_ratio_x, aspect_ratio_y)// 裁剪比例 如16:9 3:2 3:4 1:1 可自定义
//                .hideBottomControls(cb_hide.isChecked() ? false : true)// 是否显示uCrop工具栏，默认不显示
                .isGif(true)// 是否显示gif图片
//                .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
//                .circleDimmedLayer(cb_crop_circular.isChecked())// 是否圆形裁剪
//                .showCropFrame(cb_showCropFrame.isChecked())// 是否显示裁剪矩形边框 圆形裁剪时建议设为false
//                .showCropGrid(cb_showCropGrid.isChecked())// 是否显示裁剪矩形网格 圆形裁剪时建议设为false
//                .openClickSound(cb_voice.isChecked())// 是否开启点击声音
                .selectionMedia(selectList)// 是否传入已选图片
                //.isDragFrame(false)// 是否可拖动裁剪框(固定)
//                        .videoMaxSecond(15)
//                        .videoMinSecond(10)
                .previewEggs(false)// 预览图片时 是否增强左右滑动图片体验(图片滑动一半即可看到上一张是否选中)
                .cropCompressQuality(90)// 裁剪压缩质量 默认100
                .minimumCompressSize(150)// 小于100kb的图片不压缩
                //.cropWH()// 裁剪宽高比，设置如果大于图片本身宽高则无效
                //.rotateEnabled(true) // 裁剪是否可旋转图片
                //.scaleEnabled(true)// 裁剪是否可放大缩小图片
                //.videoQuality()// 视频录制质量 0 or 1
                //.videoSecond()//显示多少秒以内的视频or音频也可适用
                //.recordVideoSecond()//录制视频秒数 默认60s
                .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    selectList = PictureSelector.obtainMultipleResult(data);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，已取压缩路径为准，因为是先裁剪后压缩的
                    LogUtils.i("--selectList.size--", "--selectList.size--" + selectList.size());
                    if (selectList != null && selectList.size() > 0){
                        for (LocalMedia media : selectList) {
                            typeList.add(0); // 0：图片， 1：视频
                            selectPathList.add(media.getCompressPath());
                            LogUtils.i(TAG, "压缩---->" + media.getCompressPath());
                            LogUtils.i(TAG, "原图---->" + media.getPath());
                            LogUtils.i(TAG, "裁剪---->" + media.getCutPath());
                        }

                        adapter.setList(selectList);
                        adapter.notifyDataSetChanged();
                    }

                    LogUtils.i("--typeList.size--", "--typeList.size--" + typeList.size());
                    break;
            }
        }
    }


    @Override
    public void showMessage(String message) {
        ToastUtils.showShort(this, message);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        presenter.detachView();
        super.onDestroy();
        MyApplication.delActivity(this);
    }
}