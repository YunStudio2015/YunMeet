package yunstudio2015.android.yunmeet.utilz;

import android.os.AsyncTask;
import android.os.Debug;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;

/**
 * Created by Ulrich on 2/13/2016.
 */
public class UploadProfileImageTask extends AsyncTask<String, Long, Boolean> {

    private static final java.lang.String TAG = "yunmeet";
    private UploadFinishCallBack callback;

    public UploadProfileImageTask(UploadFinishCallBack uploadFinishCallBack) {
        this.callback = uploadFinishCallBack;
    }


    @Override
    protected void onProgressUpdate(Long... values) {
//        super.onProgressUpdate(values);
        L.i("progress", values[0]+" --- "+values[1]);
    }

    @Override
    protected Boolean doInBackground(String... params) {
        // string path..., token...
        String filePath = params[0];
        String token = params[1];
        String iFileName = "face";
        String responseString = "";

        String charset = "utf-8";
        MultipartUtility multipart = null;
        try {
            multipart = new MultipartUtility(YunApi.URL_SET_FACE, charset);
            // In your case you are not adding form data so ignore this
                /*This is to add parameter values */
            multipart.addFormField("token", token);

//add your file here.
                /*This is to add file content*/
            multipart.addFilePart("face",
                    new File(filePath));

            List<String> response = multipart.finish();
            L.e(TAG, "SERVER REPLIED:");
            for (String line : response) {
// get your server response here.
                responseString = line;
            }
            L.e(TAG, "Upload Files Response:::" + responseString);
            (new File(filePath)).deleteOnExit();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean verdict) {

        super.onPostExecute(verdict);
        if (verdict) {
            callback.uploadDone();
        } else
            callback.uploadfailed();
    }



}
