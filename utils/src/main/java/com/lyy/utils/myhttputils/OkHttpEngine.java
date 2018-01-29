package com.lyy.utils.myhttputils;

import android.content.Context;

import com.lyy.utils.logutils.MLogUtils;

import java.io.File;
import java.io.IOException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/9/7 0007.
 * <p>
 * 默认引擎 okhttp
 */

public class OkHttpEngine implements IHttpEngine {

    private static OkHttpClient mOkHttpClient = new OkHttpClient();

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);

        MLogUtils.i("OkHttpEngine-get->请求路径"+jointUrl);
        // 省略部分代码......
        Request.Builder requestBuilder = new Request.Builder().url(url).tag(context);
        Request request = requestBuilder.build();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                HttpUtils.executeError(callBack,e);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                final String resultJson = response.body().string();
                // 当然有的时候还需要不同的些许处理
                HttpUtils.handler.post(new Runnable() {
                    @Override
                    public void run() {
                        HttpUtils.executeSuccessMethod(callBack,resultJson);
                    }
                });
            }
        });
    }

    @Override
    public void post(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {

        final String jointUrl = HttpUtils.jointParams(url, params);

        MLogUtils.i("OkHttpEngine-post->请求路径"+jointUrl);
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String,Object> entry:params.entrySet()){
            builder.add(entry.getKey(),entry.getValue().toString());
        }
        RequestBody body = builder.build();
//        // 省略部分代码......
//        RequestBody requestBody = appendBody(params);
        Request request = new Request.Builder()
                .url(url)
                .tag(context)
                .post(body)
                .build();

        mOkHttpClient.newCall(request).enqueue(
                new Callback() {
                    @Override
                    public void onFailure(Call call, final IOException e) {
                        HttpUtils.executeError(callBack, e);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String resultJson = response.body().string();
                        HttpUtils.handler.post(new Runnable() {
                            @Override
                            public void run() {
                                //两个回调方法都不是在主线程中的
                                HttpUtils.executeSuccessMethod(callBack, resultJson);
                                // 缓存处理，下一期我们没事干，自己手写数据库框架
                            }
                        });
                    }
                }
        );
    }


    /**
     * 组装post请求body
     *
     * @param params
     * @return
     */
    protected RequestBody appendBody(Map<String, Object> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        addParams(builder, params);
        return builder.build();
    }

    //添加参数
    private void addParams(MultipartBody.Builder builder, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            for (String key : params.keySet()) {
                builder.addFormDataPart(key, params.get(key) + "");
                Object value = params.get(key);
                if (value instanceof File) {
                    //处理文件  --> object File
                    File file = (File) value;
                    builder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                } else if (value instanceof List) {
                    //代表提交的是List集合
                    try {
                        List<File> listFiles = (List<File>) value;
                        for (int i = 0; i < listFiles.size(); i++) {
                            //获取文件
                            File file = listFiles.get(i);
                            builder.addFormDataPart(key + i, file.getName(), RequestBody.create(MediaType.parse(guessMimeType(file.getAbsolutePath())), file));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    builder.addFormDataPart(key, value + "");
                }
            }
        }
    }


    /**
     * 猜测文件类型
     */
    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}