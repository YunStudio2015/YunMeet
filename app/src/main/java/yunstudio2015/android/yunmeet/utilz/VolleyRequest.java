package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.MyApplication;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;

/**
 * Created by Ultima on 2015/10/28.
 */
public class VolleyRequest {

    /** inside a request queue
     */

    public static void GetStringRequest (Context context, String url, String params, final VolleyOnResultListener callback) {
        if(NetWorkUtil.isNetworkConnected(context)){
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest( Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
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
            MyApplication.requestQueue.add(jsonObjectRequest);
        }else{
            // no internet connection
            if(callback!=null){
                callback.onFailure(context.getString(R.string.noconnection));
            }
        }
    }

    public static void PostStringRequest (String url, String params, VolleyOnResultListener listener) {

    }

}
