package com.yfoo.zip;

import android.util.Log;

/**
 * function:
 *
 * <p>
 * Created by lcf on 2022/8/13.
 */
final class ZipLog {
    private static final String TAG = "ZipLog";

    private static boolean DEBUG = false;

    static void config(boolean debug) {
        DEBUG = debug;
    }

    static void debug(String msg) {
        if (DEBUG) Log.d(TAG, msg);
    }
}
