package com.yfoo.zip;

/**
 * 压缩回调接口
 * Created by lcf on 2022/8/13.
 */
public interface ZipCallback {
    /**
     * 开始
     */
    void onStart();

    /**
     * 进度回调
     *
     * @param percentDone 完成百分比
     */
    void onProgress(int percentDone);

    /**
     * 完成
     *
     * @param success 是否成功
     */
    void onFinish(boolean success);
}
