package com.contact.huadao.myutilsproject;

import android.Manifest;
import android.content.Context;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.downuploadutils.DUtil;
import com.downuploadutils.Utils.Utils;
import com.downuploadutils.callback.DownloadCallback;
import com.downuploadutils.callback.SimpleUploadCallback;
import com.downuploadutils.download.DownloadManger;
import com.lyy.utils.Permisson.PermissionListener;
import com.lyy.utils.logutils.MLogUtils;
import com.lyy.utils.myhttputils.EngineCallBack;
import com.lyy.utils.myhttputils.HttpUtils;
import com.lyy.utils.myviewioc.ViewById;
import com.lyy.utils.myviewioc.ViewClick;
import com.lyy.utils.myviewioc.ViewUtiles;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseActivity {

    @ViewById(R.id.btn_test)
    Button btn_test;
    @ViewById(R.id.tv_test)
    TextView tv_test;
    @ViewById(R.id.btn_downupload)
    Button btn_downupload;
    @ViewById(R.id.btn_upload)
    Button btn_upload;
    private DownloadManger downloadManger;
    private String url;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtiles.inject(this);
        mContext = this;

    }

    @Override
    protected void findViewById() {

    }

    @Override
    protected void initView() {

    }

    @ViewClick({R.id.btn_test,R.id.tv_test,R.id.btn_downupload,R.id.btn_upload})
    public void Click(View view){
        switch (view.getId()){
            case R.id.btn_test:
                MLogUtils.i("-------Click------");
                break;
            case R.id.tv_test:
                HttpUtils.with(this).get().url("http://www.wanandroid.com/tools/mockapi/2281/login").execute(new EngineCallBack() {
                    @Override
                    public void onPreExecute(Context context, Map<String, Object> params) {
                        MLogUtils.i("-----onPreExecute-----");
                    }

                    @Override
                    public void onError(Exception e) {
                        MLogUtils.i("-----onError-----"+e);

                    }

                    @Override
                    public void onSuccess(String result) {
                        MLogUtils.i("-----onSuccess-----"+result);

                    }
                });
                break;
            case R.id.btn_downupload:
                MLogUtils.i("-------------btn_downupload---------");
                requestRuntimePermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                    @Override
                    public void onGranted() {
                        File foder = new File(Environment.getExternalStorageDirectory() + "/DUtil/");
                        if (!foder.exists()) {
                            foder.mkdirs();
                        }

                        url = "http://192.168.1.26:8808/System/20180308/wu_1c82mdhkm1285l7olsb1fmc13og7.png";

                        downloadManger = DUtil.init(mContext)
                                .url(url)
                                .path(Environment.getExternalStorageDirectory() + "/DUtil/")
                                .name(url.substring(url.lastIndexOf('/') + 1))
                                .childTaskCount(3)
                                .build()
                                .start(new DownloadCallback() {

                                    @Override
                                    public void onStart(long currentSize, long totalSize, float progress) {

                                    }

                                    @Override
                                    public void onProgress(long currentSize, long totalSize, float progress) {

                                    }

                                    @Override
                                    public void onPause() {
                                    }

                                    @Override
                                    public void onCancel() {
                                    }

                                    @Override
                                    public void onFinish(File file) {

                                        MLogUtils.i("下载完成----------------");

                                    }

                                    @Override
                                    public void onWait() {

                                    }

                                    @Override
                                    public void onError(String error) {
                                    }

                                });

                    }

                    @Override
                    public void onDenied(List<String> deniedPermission) {
                        for (String persmission : deniedPermission) {
                            Toast.makeText(MainActivity.this, "被拒绝的权限：" + persmission, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;

            case R.id.btn_upload:

                submit_sign();

                break;

        }

    }


    private void submit_sign() {
        MLogUtils.i("---");
        DUtil.initFormUpload()
                .url("http://192.168.1.202:31137/tz_qm")
                .addFile("file","ceshifujian",new File(Environment.getExternalStorageDirectory() + "/DUtil/wu_1c82mdhkm1285l7olsb1fmc13og7.png"))
                .fileUploadBuild()
                .upload(new SimpleUploadCallback() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        MLogUtils.i("-----onStart()------");
                    }
                    @Override
                    public void onFinish(String response) {
                        super.onFinish(response);

                        MLogUtils.i(response);

                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);
                            int code = jsonObject.getInt("code");
                            String info = jsonObject.getString("info");
                            if (code == 200) {
                                JSONObject data = jsonObject.getJSONObject("data");
                                int status = data.getInt("status");
                                String desc = data.getString("desc");
                                if (status == 1) {
                                    String data2 =data.getString("data");
                                    MLogUtils.i("-----------"+data2);
                                } else {
                                }
                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
//        }
    }

}
