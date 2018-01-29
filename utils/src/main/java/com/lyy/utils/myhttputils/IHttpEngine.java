package com.lyy.utils.myhttputils;

import android.content.Context;

import java.util.Map;

/**
 * Created by Administrator on 2017/9/7 0007.
 *
 * 网络引擎    李洋洋
 */

public interface IHttpEngine {

    //get请求
    void get(Context context, String url, Map<String, Object> params, EngineCallBack callBack);
    //post请求
    void post(Context context, String url, Map<String, Object> params, EngineCallBack callBack);

    //上传文件

    //https添加证书

}
