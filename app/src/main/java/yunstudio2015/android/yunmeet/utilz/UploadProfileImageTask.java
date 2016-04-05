package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.File;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallback;

/**
 * Created by Ulrich on 2/13/2016.
 */
public class UploadProfileImageTask extends AsyncTask<String, Long, String> {

    private static final java.lang.String TAG = "yunmeet";
    private final Context context;
    private UploadFinishCallback callBack;

    public UploadProfileImageTask(Context context, UploadFinishCallback uploadFinishCallBack) {

        this.context = context;
        this.callBack = uploadFinishCallBack;
    }


    @Override
    protected void onProgressUpdate(Long... values) {
//        super.onProgressUpdate(values);
        L.i("progress", values[0]+" --- "+values[1]);
    }

    @Override
    protected String doInBackground(String... params) {
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
