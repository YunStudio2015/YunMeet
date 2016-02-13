package yunstudio2015.android.yunmeet.utilz;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import yunstudio2015.android.yunmeet.commonLogs.L;

/**
 * Created by Ulrich on 2/13/2016.
 */
public class UploadImageTask extends AsyncTask<String, Long, Boolean> {

    private UploadFinishCallBack callback;
    public UploadImageTask(UploadFinishCallBack uploadFinishCallBack) {
        this.callback = uploadFinishCallBack;
    }

    public interface UploadFinishCallBack {
        public void uploadDone();
        public void uploadfailed();
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
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="fSnd";

        try
        {
            L.e(Tag,"Starting Http File Sending to URL");

            // Open a HTTP connection to the URL
            URL connectURL = new URL(YunApi.URL_SET_FACE);
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);

            HttpURLConnection conn = (HttpURLConnection)connectURL.openConnection();

            // Allow Inputs
            conn.setDoInput(true);

            // Allow Outputs
            conn.setDoOutput(true);

            // Don't use a cached copy.
            conn.setUseCaches(false);

            // Use a post method.
            conn.setRequestMethod("POST");

            conn.setRequestProperty("Connection", "Keep-Alive");

            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);
            dos.writeBytes("Content-Disposition: form-data; name=\"token\""+ lineEnd);
            dos.writeBytes(lineEnd);
            dos.writeBytes(token);
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + lineEnd);

            dos.writeBytes("Content-Disposition: form-data; name=\"face\";filename=\"" + iFileName +"\"" + lineEnd);
            dos.writeBytes(lineEnd);

            L.e(Tag,"Headers are written");

            // create a buffer of maximum size
            int bytesAvailable = fileInputStream.available();

            int maxBufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);
            byte[ ] buffer = new byte[bufferSize];

            // read file and write it into form...
            int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            long totalProgress = 0;
            long totalSize = file.length();

            while (bytesRead > 0)
            {
                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable,maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0,bufferSize);
                publishProgress(totalProgress, totalSize);
                totalProgress += bytesRead;
            }
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // close streams
            fileInputStream.close();

            dos.flush();

            L.e(Tag,"File Sent, Response: "+String.valueOf(conn.getResponseCode()));

            InputStream is = conn.getInputStream();

            // retrieve the response from server
            int ch;

            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) != -1 ){ b.append( (char)ch ); }
            String s=b.toString();
            L.i("Response",s);
            dos.close();
        }
        catch (MalformedURLException ex)
        {
            L.e(Tag, "URL error: " + ex.getMessage(), ex);
            return false;
        }

        catch (IOException ioe)
        {
            L.e(Tag, "IO error: " + ioe.getMessage(), ioe);
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
