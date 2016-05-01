package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.Imagee;

/**
 * Created by Ulrich on 3/1/2016.
 */
public class UtilsFunctions {

    public static int convertPxtoDip(int pixel, Context context){

        float scale = context.getResources().getDisplayMetrics().density;
        int dips=(int) ((pixel * scale) + 0.5f);
        return dips;
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

    public static int convertDiptoPx(int dips, Context context){

        float scale = context.getResources().getDisplayMetrics().density;
        int pixel= (int)((dips - 0.5f) / scale);
        return pixel;
    }

    public static String getToken(Context ctx) {

        return "ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx";
/*
            SharedPreferences sharedPreferences;
            sharedPreferences = ctx.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
            return token;*/
    }

    public static String getID(Context context){
        return "1";

        /*SharedPreferences sp = context.getSharedPreferences("UserData", Context.MODE_PRIVATE);
        return sp.getString("id",null);*/

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
