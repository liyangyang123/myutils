package com.lyy.utils.myhttputils;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/14 0014.
 */

public class VolleyEngine implements IHttpEngine {
    private RequestQueue mQueue;

    @Override
    public void get(Context context, String url, Map<String, Object> params, final EngineCallBack callBack) {
        final String jointUrl = HttpUtils.jointParams(url, params);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, jointUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HttpUtils.executeSuccessMethod(callBack, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HttpUtils.executeError(callBack, error);
            }
        });
        getInstance(context).add(stringRequest);
    }

    @Override
    public void post(Context context, String url, final Map<String, Object> params, final EngineCallBack callBack) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        HttpUtils.executeSuccessMethod(callBack, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HttpUtils.executeError(callBack, error);
            }
        }) {
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    headers.put(entry.getKey(), entry.getValue().toString());
                }
                return headers;
            }
        };
        getInstance(context).add(stringRequest);
    }

    private RequestQueue getInstance(Context mContext) {
        if (mQueue == null) {
            mQueue = Volley.newRequestQueue(mContext);
        }
        return mQueue;
    }
}
