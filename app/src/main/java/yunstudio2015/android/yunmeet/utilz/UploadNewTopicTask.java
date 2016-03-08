package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;

/**
 * Created by Ulrich on 3/2/2016.
 */
public class UploadNewTopicTask extends AsyncTask<UploadFinishCallBack, Long, String> {


    private static final java.lang.String TAG = MyApplication.appname;
    private final ArrayList<String> imgPath;
    private final String token;
    private final Context context;
    private  String content;

    UploadFinishCallBack callBack;

    public UploadNewTopicTask(Context context, String content, ArrayList<String> imgPath) {

        this.context = context;
        this.token = UtilsFunctions.getToken(context);
        this.content = content;
        this.imgPath = imgPath;
    }

    @Override
    protected String doInBackground(UploadFinishCallBack... params) {


        callBack = params[0];
        String iFileName = "image[]";
        String responseString = "";

        String charset = "utf-8";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(YunApi.URL_NEW_TOPIC, charset);
            // In your case you are not adding form data so ignore this
                /*This is to add parameter values */
            multipart.addFormField("token", token); // add token
            multipart.addFormField("content", content); // add content
            //add your filez here.
            if (imgPath != null)
                for (String ipath: imgPath) {
                    multipart.addFilePart(iFileName, new File(ipath));
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
                break;
            case "0":
                callBack.uploadDone();
                break;
            default:
                callBack.uploadfailed(res);
        }
    }
}
