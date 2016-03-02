package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;

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



}
