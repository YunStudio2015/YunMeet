package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.UploadActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;

/**
 * Created by Ulrich on 3/2/2016.
 */
public class UploadNewActivityTask extends AsyncTask<UploadFinishCallBack, Long, String> {


    private static final String TAG = MyApplication.appname;
    private final String token;
    private final UploadActivityEntity entity;
    private final Context context;

    UploadFinishCallBack callBack;

    public UploadNewActivityTask(Context context, UploadActivityEntity entity) {

        this.context = context;
        this.token = UtilsFunctions.getToken(context);
        this.entity = entity;
    }

    @Override
    protected String doInBackground(UploadFinishCallBack... params) {


        callBack = params[0];
        String iFileName = "img"; // then add the 1/2/3/4 to it to make the img
        String responseString = "";

        String charset = "utf-8";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(YunApi.URL_NEW_ACTIVITY, charset);
            // In your case you are not adding form data so ignore this
                /*This is to add parameter values */
            multipart.addFormField("token", token); // add token
            multipart.addFormField("pepnum", ""+entity.pepnum); // add people number
            multipart.addFormField("theme", ""+entity.theme); // add   theme
            multipart.addFormField("detail", ""+entity.detail); // add   detail
            multipart.addFormField("time", ""+entity.time); // add   time
            multipart.addFormField("cost", ""+entity.cost); // add   cost
            multipart.addFormField("place", ""+entity.place); // add   place
            multipart.addFormField("category_id", ""+entity.category_id); // add category_id
            multipart.addFormField("isrec", ""+entity.isrec); // add isrec

            //add your filez here.
            int i = 0;
            if (entity.pic_paths != null && entity.pic_paths.size() > 0)
                for (String ipath: entity.pic_paths) {
                    i++;
                    multipart.addFilePart(iFileName+i, new File(ipath));
                    (new File(ipath)).deleteOnExit();
                }
             /*   *//*This is to add file content*//*
            multipart.addFilePart("face",
                    new File(filePath));*/
            List<String> response = multipart.finish();
            L.e("SERVER REPLIED:");
            for (String line : response) {
                // get your server response here.
                responseString = line;
            }
            L.e("Upload Files Response:::" + responseString);
            JSONObject obj = new JSONObject(responseString);
            if ("1".equals(obj.get("error"))) {
                return obj.get("message").toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "1";
        }
        return "0";
    }


    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        /*if (aBoolean)
            callBack.uploadDone();
        else
            callBack.uploadfailed();*/
        switch (res){
            case "1":
                callBack.uploadfailed(context.getResources().getString(R.string.data_failure));
                return;
            case "0":
                callBack.uploadDone();
                return;
            default:
                callBack.uploadfailed(res);
        }
    }


}
