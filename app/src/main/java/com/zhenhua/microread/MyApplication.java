package com.zhenhua.microread;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.zhenhua.microread.utils.LogUtils;

import java.util.ArrayList;

/**
 * Author: zzhh
 * Date: 2019/7/23 10:53
 * Description: ${DESCRIPTION}
 */
public class MyApplication extends Application {
    public static Context appContext;
    public static ArrayList<Activity> allActivities = new ArrayList<Activity>();
    public static MyApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        app = this;
        LogUtils.isDebug = true;
    }

    public static Context getContext(){
        return appContext;
    }

    public static MyApplication getApp(){
        return app;
    }

    public static void addActivity(Activity activity) {
        allActivities.add(activity);
    }

    public static void delActivity(Activity activity) {
        allActivities.remove(activity);
    }

    //遍历所有Activity并finish
    public void exit() {
        for(Activity activity:allActivities) {
            activity.finish();
        }
        allActivities.clear();
    }
}
