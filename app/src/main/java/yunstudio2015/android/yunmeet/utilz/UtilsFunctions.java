package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Environment;
import android.util.DisplayMetrics;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.atomic.AtomicInteger;

import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.Imagee;

/**
 * Created by Ulrich on 3/1/2016.
 */
public class UtilsFunctions {


    public static final long fromStringToMillisecond (String text) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date = null;
        try {
            date = format.parse(text);
            return date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    /**
     * Generate a value suitable for use in {@link #setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (;;) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }

    public static String[] getImagesLink(Imagee[] image) {

        String[] tmp = new String[image.length];
        for (int i = 0; i < image.length; i++) {
            tmp[i] = image[i].url;
        }
        return tmp;
    }


    public static String encodedPath (String path)
    {
        String encodedPath = "";
        try {
            encodedPath = java.net.URLEncoder.encode(path, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedPath;
    }

    public static String decodedPath (String encodedPath)
    {
        String decodedPath = "";
        try {
            decodedPath = java.net.URLDecoder.decode(encodedPath, "ISO-8859-1");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (decodedPath.length() > 1)
            decodedPath = decodedPath.substring(1);
        return decodedPath;
    }

    public static float convertPixelsToDp(float px){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float dp = px / (metrics.densityDpi / 160f);
        return Math.round(dp);
    }

    public static float convertDpToPixel(float dp){
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        float px = dp * (metrics.densityDpi / 160f);
        return Math.round(px);
    }


    public static String getToken(Context ctx) {

        //return "ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx";

        SharedPreferences sharedPreferences;
        sharedPreferences = ctx.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);

        return token;
    }

    public static String getID(Context context){
        //return "1";

        SharedPreferences sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sp.getString("id",null);

    }

    public static void setToken(Context context,String token){
        SharedPreferences sp = context.getSharedPreferences("UserData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("token",token);
        editor.apply();
    }

    public static void setID(Context context,String id){
        SharedPreferences sp = context.getSharedPreferences("UserData",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("id",id);
        editor.apply();
    }

    public static String getAppImageCachePath(Context context) {


        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            L.i("external storage mounted "+Environment.getExternalStorageDirectory().getAbsolutePath());
            return Environment.getExternalStorageDirectory().getAbsolutePath()+"/yunmeet_image_cache";
        } else {
            L.e("external NOT storage mounted");
            return Environment.getDownloadCacheDirectory().getAbsolutePath()+"/yunmeet_image_cache";
        }
    }

}
