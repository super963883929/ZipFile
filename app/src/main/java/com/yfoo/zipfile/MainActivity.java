package com.yfoo.zipfile;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.miyouquan.library.DVPermissionUtils;
import com.yfoo.zip.ZipCallback;
import com.yfoo.zip.ZipUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    String[] permissions = DVPermissionUtils.arrayConcatAll(DVPermissionUtils.PERMISSION_FILE_STORAGE);
    private String sdcardPath = "";
    private String tagPath = "";
    private String dirPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getPermission();

        findViewById(R.id.button1).setOnClickListener(v -> {
            zip();
        });

        findViewById(R.id.button2).setOnClickListener(v -> {
            unZip();
        });
    }


    private void zip(){
        getPermission();
         tagPath = sdcardPath + "/zipfile.zip";
         ZipUtils.zipFile(dirPath , tagPath,"123456", new ZipCallback() {
             @Override
             public void onStart() {
                 loadingShow(0);
             }

             @Override
             public void onProgress(int percentDone) {
                 loadingShow(percentDone);
             }

             @Override
             public void onFinish(boolean success) {
                 loadingDismiss();
                 if (success){
                     new  AlertDialog.Builder(MainActivity.this)
                             .setTitle("提示")
                             .setMessage("压缩成功: " + tagPath)
                             .setPositiveButton("确定",null)
                             .show();
                 }else{
                     Toast.makeText(MainActivity.this, "压缩失败", Toast.LENGTH_SHORT).show();
                 }
             }
         });

    }


    private void unZip(){
        Log.d(TAG,tagPath + "---" + sdcardPath + "/");
        ZipUtils.unzip(tagPath, sdcardPath + "/解压后文件夹", "123456",new ZipCallback() {
            @Override
            public void onStart() {
                loadingShow(0);
            }

            @Override
            public void onProgress(int percentDone) {
                loadingShow(percentDone);
            }

            @Override
            public void onFinish(boolean success) {
                loadingDismiss();
                if (success){
                    new  AlertDialog.Builder(MainActivity.this)
                            .setTitle("提示")
                            .setMessage("解压成功: " + sdcardPath + "/解压后文件夹")
                            .setPositiveButton("确定",null)
                            .show();
                }else{
                    Toast.makeText(MainActivity.this, "解压失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    /**进度对话框*/
    private ProgressDialog mLoading;
    private void loadingShow(int percent) {
        if (mLoading == null) {
            mLoading=  new ProgressDialog(this);
            mLoading.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mLoading.setMax(100);
        }
        if (percent > 0) {
            mLoading.setProgress(percent);
            mLoading.setMessage(percent + "%");
        }
        if (!mLoading.isShowing()) mLoading.show();
    }

    private void loadingDismiss() {
        if (mLoading != null && mLoading.isShowing()) {
            mLoading.dismiss();
            mLoading = null;
        }
    }




    /**
     * 申请权限(获取权限)
     */
    private void getPermission() {

        if (!DVPermissionUtils.verifyHasPermission(this, permissions)) {
            DVPermissionUtils.requestPermissions(this, permissions, new DVPermissionUtils.OnPermissionListener() {
                @Override
                public void onPermissionGranted() {
                    sdcardPath = getSdCardPath();
                    writeResourceFile();
                }
                @Override
                public void onPermissionDenied() {
                    Toast.makeText(MainActivity.this, "权限申请失败,请手动允许“存储”权限", Toast.LENGTH_LONG).show();
                }
            });
        }else{
            sdcardPath = getSdCardPath();
            writeResourceFile();
        }
    }



    private void writeResourceFile(){
        //文件夹路径
        dirPath = sdcardPath + "/" + "zipfile";
        Utils.createDir(dirPath);
        Utils.outResourceFile(this,"img_123.jpg", dirPath +"/img_123.jpg");
        Utils.outResourceFile(this,"img_456.jpg", dirPath +"/img_456.jpg");

        Utils.createDir(dirPath + "/images");
        Utils.outResourceFile(this,"images/123.jpg", dirPath +"/images/123.jpg");
        Utils.outResourceFile(this,"images/456.jpg", dirPath +"/images/456.jpg");
    }



    /**
     * 取存储卡路径
     * @return ...
     */
    public static String getSdCardPath() {
        if ("mounted".equals(Environment.getExternalStorageState()) && Environment.getExternalStorageDirectory().canWrite()) {
            return Environment.getExternalStorageDirectory().getPath();
        }
        return "";
    }




}