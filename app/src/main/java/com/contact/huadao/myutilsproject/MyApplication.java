package com.contact.huadao.myutilsproject;

import android.app.Application;

import com.lyy.utils.logutils.MLogUtils;

/**
 * Created by huadao on 2018/1/29.
 */

public class MyApplication extends Application{

    private static MyApplication myApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        MLogUtils.init(true);

    }

    public static MyApplication getAppInstance(){
        if (myApplication == null) {
            myApplication = new MyApplication();
        }
        return myApplication;
    }

}
