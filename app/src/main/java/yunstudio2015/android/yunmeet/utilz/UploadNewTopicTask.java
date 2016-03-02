package yunstudio2015.android.yunmeet.utilz;

import android.os.AsyncTask;

import java.io.File;
import java.io.IOException;
import java.util.List;

import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;

/**
 * Created by Ulrich on 3/2/2016.
 */
public class UploadNewTopicTask extends AsyncTask<UploadFinishCallBack, Long, Boolean> {


    private static final java.lang.String TAG = MyApplication.appname;
    private final String[] imgPath;
    private final String token;
    private  String content;

    UploadFinishCallBack callBack;

    public UploadNewTopicTask(String token, String content, String[] imgPath) {

        this.token = token;
        this.content = content;
        this.imgPath = imgPath;
    }

    @Override
    protected Boolean doInBackground(UploadFinishCallBack... params) {


        callBack = params[0];
        String iFileName = "image[]";
        String responseString = "";

        String charset = "utf-8";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(YunApi.URL_SET_FACE, charset);
            // In your case you are not adding form data so ignore this
                /*This is to add parameter values */
            multipart.addFormField("token", token); // add token
            multipart.addFormField("content", content); // add content
//add your filez here.
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
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if (aBoolean)
            callBack.uploadDone();
        else
            callBack.uploadfailed();
    }
}
