package com.yfoo.zipfile;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class Utils {


    /**
     * 创建目录
     * @param path
     * @return
     */
    public static boolean createDir(String path) {
        String[] dir = path.split("/");
        String dist = dir[0];
        boolean result = true;
        if (dir.length <= 0) {
            return false;
        }
        for (int i = 1; i < dir.length; i++) {
            dist = dist + "/" + dir[i];
            File mkdir = new File(dist);
            if (!mkdir.exists()) {
                result = mkdir.mkdir();
            }
        }
        return result;
    }

    /**
     * 写出资源文件
     * @param filename 资源文件路径
     * @param outFileName 写出文件保存的全路径
     * @return 是否成功
     */
    public static boolean outResourceFile(Context context, String filename, String outFileName) {
        try {
            InputStream stream = context.getAssets().open(filename);
            if (stream != null && writeStreamToFile(stream, new File(outFileName))) {
                return true;
            }
            return false;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }


    private static boolean writeStreamToFile(InputStream stream, File file) throws Throwable {
        Exception e1;
        Throwable th;
        OutputStream output = null;
        try {
            OutputStream output2 = new FileOutputStream(file);
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    int read = stream.read(buffer);
                    if (read != -1) {
                        output2.write(buffer, 0, read);
                    } else {
                        output2.flush();
                        try {
                            output2.close();
                            stream.close();
                            output = output2;
                            return true;
                        } catch (IOException e) {
                            e.printStackTrace();
                            output = output2;
                            return false;
                        }
                    }
                }
            } catch (Exception e2) {
                e1 = e2;
                output = output2;
                try {
                    e1.printStackTrace();
                    try {
                        output.close();
                        stream.close();
                        return false;
                    } catch (IOException e3) {
                        e3.printStackTrace();
                        return false;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    try {
                        output.close();
                        stream.close();
                        throw th;
                    } catch (Throwable e32) {
                        e32.printStackTrace();
                        return false;
                    }
                }
            } catch (Throwable th3) {
                th = th3;
                output = output2;
                output.close();
                stream.close();
                throw th;
            }
        } catch (Exception e4) {
            e1 = e4;
            e1.printStackTrace();
            output.close();
            stream.close();
            return false;
        }
    }

}
