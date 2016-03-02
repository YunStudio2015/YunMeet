package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;

/**
 * Created by lizhaotailang on 2016/3/1.
 */
public class GetSimpleTopicsTask extends AsyncTask<String,Void,List<SimpleTopicItem>>{

    private GetSimpleTopicsFinishCallback callback;

    private List<SimpleTopicItem> list = new ArrayList<SimpleTopicItem>();
    private String FailedMsg = null;
    private Context context = null;
    private RequestQueue queue;

    public GetSimpleTopicsTask (Context context,RequestQueue queue,GetSimpleTopicsFinishCallback callback){
        this.callback = callback;
        this.context = context;
        this.queue = queue;
        Log.d("async","construct");
    }

    @Override
    protected List<SimpleTopicItem> doInBackground(String... params) {

        Log.d("async","doinbackground");

        String token = params[0];
        Map<String, String> map = new HashMap<>();
        //token仅供测试！！！
        //ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx
        //sp.getString("token",null)
        map.put("token", "ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_TOPIC_LIST, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("error").equals("0")) {
                                JSONArray array = response.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    SimpleTopicItem item = new SimpleTopicItem(array.getJSONObject(i).getString("id"),
                                            array.getJSONObject(i).getString("content"),
                                            array.getJSONObject(i).getString("pubtime"));
                                    Log.d("asyc"+i,array.getJSONObject(i).toString());
                                    list.add(item);
                                }
                            } else {
                                FailedMsg = response.getString("message");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        Log.d("list...",list.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                FailedMsg = error.toString();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json;charset=UTF-8");
                return headers;
            }
        };
        queue.add(request);

        return list;
    }

    @Override
    protected void onPostExecute(List<SimpleTopicItem> simpleTopicItems) {
        super.onPostExecute(simpleTopicItems);
        Log.d("async","onpostexecute");
        if (simpleTopicItems != null) {
            Log.d("list-->",list.toString());
            Log.d("params",simpleTopicItems.toString());
            callback.GetTopicsDone(list);
        } else {
            callback.GetTopicsFailed(FailedMsg);
        }
    }

    public interface GetSimpleTopicsFinishCallback{
        public void GetTopicsDone(List<SimpleTopicItem> data);
        public void GetTopicsFailed(String failedMsg);
    }
}
