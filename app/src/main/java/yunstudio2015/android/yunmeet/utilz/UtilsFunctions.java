package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Ulrich on 3/1/2016.
 */
public class UtilsFunctions {

    public static int convertPxtoDip(int pixel, Context context){

        float scale = context.getResources().getDisplayMetrics().density;
        int dips=(int) ((pixel * scale) + 0.5f);
        return dips;
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
}
