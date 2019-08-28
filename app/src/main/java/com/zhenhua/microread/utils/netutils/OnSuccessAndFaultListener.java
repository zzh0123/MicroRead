package com.zhenhua.microread.utils.netutils;

/**
 *
 */
public interface OnSuccessAndFaultListener {
    void onSuccess(String result);

    void onFault(String errorMsg);
}
