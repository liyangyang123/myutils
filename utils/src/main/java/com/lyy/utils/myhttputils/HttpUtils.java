package com.lyy.utils.myhttputils;

import android.content.Context;
import android.os.Handler;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/7 0007.
 */

public class HttpUtils {
    public static Handler handler = new Handler();
    //直接带参数，链式调用
    private String mUrl;
    private int mType = GET_TYPE;
    private static final int POST_TYPE = 0x0011;
    private static final int GET_TYPE = 0x00111;
    private Map<String, Object> mParams;
    private Context mContext;

    // 网络访问引擎
    private static IHttpEngine iHttpEngine = new OkHttpEngine();

    private HttpUtils(Context context) {
        this.mContext = context;
        mParams = new HashMap<>();
    }

    public static HttpUtils with(Context context) {
        return new HttpUtils(context);
    }

    public HttpUtils url(String url) {
        this.mUrl = url;
        return this;
    }

    public HttpUtils post() {
        mType = POST_TYPE;
        return this;
    }

    public HttpUtils get() {
        mType = GET_TYPE;
        return this;
    }

    //添加参数

    public HttpUtils addParam(String key, Object value) {
        mParams.put(key, value);
        return this;
    }

    public HttpUtils addParams(Map<String, Object> params) {
        mParams.putAll(params);
        return this;
    }


    //添加回调 ,执行
    public void execute(EngineCallBack callBack) {

        callBack.onPreExecute(mContext,mParams);

        if (callBack == null) {
            callBack = EngineCallBack.DEFUALT_CALL_BACK;
        }

        //判断执行方法
        if (mType == POST_TYPE) {
            iHttpEngine.post(mContext,mUrl, mParams, callBack);
        }

        if (mType == GET_TYPE) {
            iHttpEngine.get(mContext,mUrl, mParams, callBack);
        }
    }

    public void execute() {
        execute(null);
    }

    //在Application中初始化引擎
    public static void init(IHttpEngine httpEngine) {
        iHttpEngine = httpEngine;
    }

    /**
     * 自带引擎
     *
     * @param httpEngine
     */
    public void exchangeEngin(IHttpEngine httpEngine) {
        iHttpEngine = httpEngine;
    }

    public static String jointParams(String url, Map<String, Object> params) {
        if (params == null || params.size() <= 0) {
            return url;
        }

        StringBuffer stringBuffer = new StringBuffer(url);
        if (!url.contains("?")) {
            stringBuffer.append("?");
        } else {
            if (!url.endsWith("?")) {
                stringBuffer.append("&");
            }
        }
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            stringBuffer.append(entry.getKey() + "=" + entry.getValue() + "&");
        }
        stringBuffer.deleteCharAt(stringBuffer.length() - 1);
        return stringBuffer.toString();
    }


    /**
     * 执行成功的方法
     **/
    public static void executeSuccessMethod(final EngineCallBack httpCallBack, final String resultJson) {
        try {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    httpCallBack.onSuccess(resultJson);
                }
            });
        } catch (Exception e) {
            executeError(httpCallBack, e);
            e.printStackTrace();
        }
    }

    /**
     * 执行失败的方法
     */
    public static void executeError(final EngineCallBack httpCallBack, final Exception e) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                httpCallBack.onError(e);
            }
        });
    }


    /**
     * 解析一个类上面的class信息
     * @param object
     * @return
     */
    public static  Class<?> amalysisClazzInfo(Object object){
        Type genType =object.getClass().getGenericSuperclass();
        Type[] params =((ParameterizedType)genType).getActualTypeArguments();
        return (Class<?>) params[0];
    }

}
