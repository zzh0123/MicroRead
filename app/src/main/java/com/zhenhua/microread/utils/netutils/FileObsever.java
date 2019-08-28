package com.zhenhua.microread.utils.netutils;

import java.io.File;

import io.reactivex.observers.DisposableObserver;
import okhttp3.ResponseBody;

/**
 * Author: zzhh
 * Date: 2019/8/7 14:43
 * Description: ${DESCRIPTION}
 */
public abstract class FileObsever extends DisposableObserver<ResponseBody> {
    private String path;

    public FileObsever(String path) {
        super();
        this.path = path;
    }

    @Override
    protected void onStart() {
    }

    @Override
    public void onNext(ResponseBody o) {
        File file = FileUtil.saveFile(path, o);
        if (file != null && file.exists()) {
            onSuccess(file);
        } else {
            onErrorMsg("file is null or file not exists");
        }
    }

    @Override
    public void onError(Throwable e) {
        onErrorMsg(e.toString());
    }

    @Override
    public void onComplete() {

    }

    public abstract void onSuccess(File file);

    public abstract void onErrorMsg(String msg);
}
