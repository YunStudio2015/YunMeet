package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoFragmentTopic extends Fragment {

    private TextView tvTip;
    private ListView lvTopics;

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getContext().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);

        Map<String,String> map = new HashMap<>();
        //token仅供测试！！！
        //ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx
        //sp.getString("token",null)
        map.put("token","ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx");
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_TOPIC_LIST, new JSONObject(map),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("error").equals("0")){

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        })
        {
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String,String> headers = new HashMap<>();
                headers.put("Accept","application/json");
                headers.put("Content-Type","application/json;charset=UTF-8");
                return headers;
            }
        };

        queue.add(request);

        return viewTopic;
    }
}
