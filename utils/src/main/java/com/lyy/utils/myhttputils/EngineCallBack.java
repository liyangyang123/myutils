package com.lyy.utils.myhttputils;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public interface EngineCallBack {

    public void onPreExecute(Context context, Map<String, Object> params);

    public void onError(Exception e);

    public void onSuccess(String result);



    //默认的
    public final EngineCallBack DEFUALT_CALL_BACK =new EngineCallBack() {
        @Override
        public void onPreExecute(Context context, Map<String, Object> params) {

        }

        @Override
        public void onError(Exception e) {

        }

        @Override
        public void onSuccess(String result) {

        }
    };
}
