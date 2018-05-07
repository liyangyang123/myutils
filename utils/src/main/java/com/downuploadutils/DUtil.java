package com.downuploadutils;

import android.content.Context;

import com.downuploadutils.download.DBuilder;
import com.downuploadutils.upload.DirectUploadBuilder;
import com.downuploadutils.upload.FormUploadBuilder;

public class DUtil {

    /**
     * 下载
     *
     * @param context
     * @return
     */
    public static DBuilder init(Context context) {
        return new DBuilder(context);
    }

    /**
     * 表单式文件上传
     *
     * @return
     */
    public static FormUploadBuilder initFormUpload() {
        return new FormUploadBuilder();
    }

    /**
     * 将文件作为请求体上传
     *
     * @return
     */
    public static DirectUploadBuilder initUpload() {
        return new DirectUploadBuilder();
    }
}
