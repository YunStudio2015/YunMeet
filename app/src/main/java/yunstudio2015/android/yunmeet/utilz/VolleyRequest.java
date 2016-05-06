package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;

import static com.android.volley.Request.Method;

/**
 * Created by Ultima on 2015/10/28.
 */
public class VolleyRequest {

    private static final int MY_SOCKET_TIMEOUT_MS = 5000;

    /** inside a request queue
     */

    public static void GetStringRequest (Context context, String url, String params, final VolleyOnResultListener callback) {
        if(NetWorkUtil.isNetworkConnected(context)){
            StringRequest  jsonObjectRequest = new StringRequest (Method.GET, url+(params!=""?"?":"")+params,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String  response) {
                            // TODO Auto-generated method stub
                            if(callback!=null)
                                callback.onSuccess(response);
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    if(callback!=null)
                        callback.onFailure(error.toString());
                }

            });
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MyApplication.requestQueue.add(jsonObjectRequest);
        }else{
            // no internet connection
            if(callback!=null){
                callback.onFailure(context.getString(R.string.noconnection));
            }
        }
    }

    public static void PostStringRequest (Context context, String url, final Map<String, String> param, final VolleyOnResultListener callback) {

        //if(NetWorkUtil.isNetworkConnected(context)){
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, url, new JSONObject(param), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (callback != null)
                    callback.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (callback != null)
                    callback.onFailure(error.toString());
            }
        });

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MyApplication.requestQueue.add(jsonObjectRequest);
    }


}
