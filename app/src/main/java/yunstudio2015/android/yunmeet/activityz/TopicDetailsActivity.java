package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
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

    private Gson gson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.topic_details_layout);

        initViews();

        Intent i = getIntent();
        ChatTopicEntity chattopic = (ChatTopicEntity) i.getSerializableExtra("topic");

        updateView(chattopic);
//        Map<String,String> map = new HashMap<String,String>();
//        map.put("token", UtilsFunctions.getToken(TopicDetailsActivity.this));
//        map.put("topic_id ",topicID);
/*
        String params = "";
        params+="token="+UtilsFunctions.getToken(this);
        params+="&topic_id"+topicID;

        VolleyRequest.GetStringRequest(this, YunApi.URL_GET_TOPIC, params, new VolleyOnResultListener() {
            @Override
            public void onSuccess(String resp) {
                if(gson == null)
                    gson = new Gson();
                try {
                    L.d(resp);
                    resp = resp.replace("t_800", "t_256");
                    JsonElement response = gson.fromJson(resp, JsonElement.class);
                    ChatTopicEntity[] data = gson.fromJson(response.getAsJsonObject().get("data").getAsJsonArray(),
                            ChatTopicEntity[].class);
                    updateView(data[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {

            }
        });*/
    }

    // inflate the view
    private void updateView(ChatTopicEntity chatTopicEntity) {

        // get in this activity with all the informations necessary

        //

        tvTopic.setText(chatTopicEntity.content);
        tvUserName.setText(chatTopicEntity.nickname);
        tvTime.setText(chatTopicEntity.pubtime);
        String s = chatTopicEntity.for_num + "赞";
        tvLike.setText(s);
        s = chatTopicEntity.comment_num + "评论";
        tvComment.setText(s);
        Glide.with(TopicDetailsActivity.this)
                .load(chatTopicEntity.face)
                .centerCrop()
                .into(circleImageView);
    }


    public void mT(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
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
