package yunstudio2015.android.yunmeet.commonLogs;

import android.util.Log;

import java.io.IOException;

import yunstudio2015.android.yunmeet.app.MyApplication;


/**
 * Created by ulrich on 15-9-14.
 */
public class L {

    public static void d (String log) {
        if (MyApplication.debug)
            Log.d(MyApplication.appname, log);
    }

    public static void d (String tag, String log) {
        if (MyApplication.debug)
            Log.d(tag, log);
    }

    public static void v (String log) {
        if (MyApplication.debug)
            Log.v(MyApplication.appname, log);
    }

    public static void v (String tag, String log) {
        if (MyApplication.debug)
            Log.v(tag, log);
    }

    public static void e (String log) {
        if (MyApplication.debug)
            Log.e(MyApplication.appname, log);
    }

    public static void e (String tag, String log) {
        if (MyApplication.debug)
            Log.e(tag, log);
    }

    public static void i (String log) {
        if (MyApplication.debug)
            Log.i(MyApplication.appname, log);
    }

    public static void i (String tag, String log) {
        if (MyApplication.debug)
            Log.i(tag, log);
    }


    public static void e(String tag, String s, IOException ioe) {
        if (MyApplication.debug)
            Log.i(tag, s, ioe);
    }
}
