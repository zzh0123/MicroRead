package com.zhenhua.microread.utils.netutils;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Author: zzhh
 * Date: 2019/8/28 9:35
 * Description: ${DESCRIPTION}
 */
public class RequestBodyUtil {
    public static RequestBody toRequestBodyText(String value) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), value);
        return requestBody;
    }

    public static RequestBody toRequestBodyJson(JSONObject jsonObject) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObject.toJSONString());
        return requestBody;
    }
}
