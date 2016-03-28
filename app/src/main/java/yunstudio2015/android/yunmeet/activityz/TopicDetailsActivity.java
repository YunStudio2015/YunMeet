package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/3/22.
 */
public class TopicDetailsActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private TextView tvUserName;
    private TextView tvTopic;
    private TextView tvTime;
    private TextView tvLike;
    private TextView tvComment;

    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_details_layout);

        initViews();

        Intent i = getIntent();
        String topicID = i.getStringExtra("topicID");

        queue = Volley.newRequestQueue(getApplicationContext());

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(TopicDetailsActivity.this));
        map.put("topic_id ",topicID);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_TOPIC, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        JSONArray array = response.getJSONArray("data");
                        tvTopic.setText(array.getJSONObject(0).getString("content"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);

    }

    private void initViews() {

        circleImageView = (CircleImageView) findViewById(R.id.iv_launcher_pic);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        tvTopic = (TextView) findViewById(R.id.tv_topic);
        tvTime = (TextView) findViewById(R.id.tv_publish_time);
        tvLike = (TextView) findViewById(R.id.tv_like);
        tvComment = (TextView) findViewById(R.id.tv_comment);

    }
}
