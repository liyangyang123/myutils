package com.lyy.utils.myhttputils;

import android.content.Context;

import com.google.gson.Gson;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/16 0016.
 */

public abstract class HttpCallBack<T> implements EngineCallBack {

    @Override
    public void onPreExecute(Context context, Map<String, Object> params) {
        //必带参数可以放这里
    }

    @Override
    public void onSuccess(String result) {
        Gson gson =new Gson();
        T objResult = (T) gson.fromJson(result, HttpUtils.amalysisClazzInfo(this));
        onSuccess(objResult);
    }
    //返回直接可以操作的对象
    public abstract void onSuccess(T result);
}
