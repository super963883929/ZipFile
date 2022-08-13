package com.yfoo.zip;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;


import net.zip4j.model.ZipParameters;
import net.zip4j.model.enums.AesKeyStrength;
import net.zip4j.model.enums.CompressionMethod;
import net.zip4j.model.enums.EncryptionMethod;
import net.zip4j.progress.ProgressMonitor;
import net.zip4j.util.Zip4jUtil;
import net.zip4j.zip.ZipFile;

import java.io.File;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


import static net.zip4j.model.enums.CompressionLevel.NORMAL;

/**
 *  压缩工具
 * Created by lcf on 2022/8/13.
 */
public final class ZipUtils {
    private ZipUtils() {}

    /**
     * 是否打印日志  默认为false
     */
    public static void debug(boolean debug) {
        ZipLog.config(debug);
    }

    private static final int     WHAT_START    = 100;
    private static final int     WHAT_FINISH   = 101;
    private static final int     WHAT_PROGRESS = 102;
    private static final Handler mUIHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg == null) {
                return;
            }
            switch (msg.what) {
                case WHAT_START:
                    ((ZipCallback) msg.obj).onStart();
                    ZipLog.debug("onStart.");
                    break;
                case WHAT_PROGRESS:
                    ((ZipCallback) msg.obj).onProgress(msg.arg1);
                    ZipLog.debug("onProgress: percentDone=" + msg.arg1);
                    break;
                case WHAT_FINISH:
                    ((ZipCallback) msg.obj).onFinish(true);
                    ZipLog.debug("onFinish: success=true");
                    break;
            }
        }
    };

    /**
     * 压缩文件或者文件夹
     *
     * @param targetPath          被压缩的文件路径
     * @param destinationFilePath 压缩后生成的文件路径
     * @param callback            压缩进度回调
     */
    public static void zipFile(String targetPath, String destinationFilePath, ZipCallback callback) {
        zipFile(targetPath, destinationFilePath, "", callback);
    }

    /**
     * 压缩文件或者文件夹
     *
     * @param targetPath          被压缩的文件路径
     * @param destinationFilePath 压缩后生成的文件路径
     * @param password            压缩加密 密码
     * @param callback            压缩进度回调
     */
    public static void zipFile(String targetPath, String destinationFilePath, String password, ZipCallback callback) {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(targetPath) || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFilePath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("zip: targetPath=" + targetPath + " , destinationFilePath=" + destinationFilePath + " , password=" + password);
        try {
            ZipParameters parameters = new ZipParameters();

            parameters.setCompressionMethod(CompressionMethod.DEFLATE);

            parameters.setCompressionLevel(NORMAL);

            ZipFile zipFile = new ZipFile(destinationFilePath);
            zipFile.setRunInThread(true);


            if (password.length() > 0) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod( EncryptionMethod.AES);

                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

                //parameters.setPassword(password);
                zipFile.setPassword(password.toCharArray());
            }

            File targetFile = new File(targetPath);
            if (targetFile.isDirectory()) {
                zipFile.addFolder(targetFile, parameters);
            } else {
                zipFile.addFile(targetFile, parameters);
            }
            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("zip: Exception=" + e.getMessage());
        }
    }

    /**
     * 压缩多个文件
     *
     * @param list                被压缩的文件集合
     * @param destinationFilePath 压缩后生成的文件路径
     * @param password            压缩 密码
     * @param callback            回调
     */
    public static void zipFiles(ArrayList<File> list, String destinationFilePath, String password, final ZipCallback callback) {
        if (list == null || list.size() == 0 || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFilePath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("zip: list=" + list.toString() + " , destinationFilePath=" + destinationFilePath + " , password=" + password);
        try {
            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(NORMAL);

            ZipFile zipFile = new ZipFile(destinationFilePath);
            zipFile.setRunInThread(true);
            zipFile.addFiles(list, parameters);

            if (password.length() > 0) {
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.AES);
                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);

                //parameters.setPassword(password);
                zipFile.setPassword(password.toCharArray());
            }

            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("zip: Exception=" + e.getMessage());
        }
    }

    /**
     * 压缩多个文件
     * @param list                被压缩的文件集合
     * @param destinationFilePath 压缩后生成的文件路径
     * @param callback            压缩进度回调
     */
    public static void zipFiles(ArrayList<File> list, String destinationFilePath, ZipCallback callback) {
        zipFiles(list, destinationFilePath, "", callback);
    }

    /**
     * 解压
     *
     * @param targetZipFilePath     待解压目标文件地址
     * @param destinationFolderPath 解压后文件夹地址
     * @param callback              回调
     */
    public static void unzip(String targetZipFilePath, String destinationFolderPath, ZipCallback callback) {
        unzip(targetZipFilePath, destinationFolderPath, "", callback);
    }

    /**
     * 解压
     *
     * @param targetZipFilePath     待解压目标文件地址
     * @param destinationFolderPath 解压后文件夹地址
     * @param password              解压密码
     * @param callback              回调
     */
    public static void unzip(String targetZipFilePath, String destinationFolderPath, String password, final ZipCallback callback) {
        if (!Zip4jUtil.isStringNotNullAndNotEmpty(targetZipFilePath) || !Zip4jUtil.isStringNotNullAndNotEmpty(destinationFolderPath)) {
            if (callback != null) callback.onFinish(false);
            return;
        }
        ZipLog.debug("unzip: targetZipFilePath=" + targetZipFilePath + " , destinationFolderPath=" + destinationFolderPath + " , password=" + password);
        try {
            ZipFile zipFile = new ZipFile(targetZipFilePath);
            if (zipFile.isEncrypted() && Zip4jUtil.isStringNotNullAndNotEmpty(password)) {
                char[] chars = password.toCharArray();
                zipFile.setPassword(chars);
            }
            zipFile.setRunInThread(true);
            zipFile.extractAll(destinationFolderPath);
            timerMsg(callback, zipFile);
        } catch (Exception e) {
            if (callback != null) callback.onFinish(false);
            ZipLog.debug("unzip: Exception=" + e.getMessage());
        }
    }

    //Handler send msg
    private static void timerMsg(final ZipCallback callback, ZipFile zipFile) {
        if (callback == null) return;
        mUIHandler.obtainMessage(WHAT_START, callback).sendToTarget();
        final ProgressMonitor progressMonitor = zipFile.getProgressMonitor();
        final Timer timer           = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mUIHandler.obtainMessage(WHAT_PROGRESS, progressMonitor.getPercentDone(), 0, callback).sendToTarget();
                if (progressMonitor.getResult() == ProgressMonitor.Result.SUCCESS) {
                    mUIHandler.obtainMessage(WHAT_FINISH, callback).sendToTarget();
                    this.cancel();
                    timer.purge();
                }
            }
        }, 0, 300);
    }

}
