package com.zhenhua.microread.presenter;

public interface Presenter<V> {

    void attachView(V view);

    void detachView();

}
