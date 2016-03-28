package yunstudio2015.android.yunmeet.activityz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

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
public class ActivityDetailsActivity extends AppCompatActivity {

    private RequestQueue queue;

    private ImageView ivActivity;
    private TextView tvActivityTitle;
    private CircleImageView circleIvOwner;
    private TextView tvActLauncherName;
    private TextView tvActSexIc;
    private TextView tvActLaunchTime;
    private TextView tvActPeopleCount;
    private TextView tvActPaymode;
    private TextView tvActDateTime;
    private TextView tvActivityPlace;
    private TextView tvActivityDescription;
    private TextView tvViewedCount;
    private TextView tvTakepartCount;
    private TextView tvCommentCount;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_layout);

        queue = Volley.newRequestQueue(getApplicationContext());

        initViews();

        Intent intent = getIntent();
        String id = intent.getStringExtra("activityID");
        String IMG = intent.getStringExtra("activityIMG");

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivActivity.getLayoutParams();
        params.width = getScreenWidth(ActivityDetailsActivity.this);
        params.height = getScreenWidth(ActivityDetailsActivity.this);
        ivActivity.setLayoutParams(params);

        Glide.with(ActivityDetailsActivity.this).load(parseUrl(IMG)).centerCrop().into(ivActivity);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(ActivityDetailsActivity.this));
        map.put("id",id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_ACTIVITY, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        JSONArray array = response.getJSONArray("data");
                        tvActivityTitle.setText(array.getJSONObject(0).getString("theme"));
                        circleIvOwner.setBackgroundColor(Color.TRANSPARENT);
                        Glide.with(ActivityDetailsActivity.this).load(parseUrl(array.getJSONObject(0).getString("face"))).into(circleIvOwner);
                        tvActLauncherName.setText(array.getJSONObject(0).getString("nickname"));
                        if (array.getJSONObject(0).getString("sex").equals("0")){
                            tvActSexIc.setText(getString(R.string.male_symbol));
                        } else {
                            tvActSexIc.setText(getString(R.string.female_symbol));
                        }
                        tvActLaunchTime.setText(array.getJSONObject(0).getString("pubtime"));
                        tvActPeopleCount.setText(array.getJSONObject(0).getString("passnum"));

                        if (array.getJSONObject(0).getString("cost").equals("0")){
                            tvActPaymode.setText(getString(R.string.i_invite));
                        } else if (array.getJSONObject(0).getString("cost").equals("1")){
                            tvActPaymode.setText(getString(R.string.look_for_invite));
                        } else {
                            tvActPaymode.setText(getString(R.string.invite_aa));
                        }

                        tvActDateTime.setText(array.getJSONObject(0).getString("time"));
                        tvActivityPlace.setText(array.getJSONObject(0).getString("place"));
                        tvActivityDescription.setText(array.getJSONObject(0).getString("detail"));
                        tvViewedCount.setText(array.getJSONObject(0).getString("view"));
                        tvTakepartCount.setText(array.getJSONObject(0).getString("passnum"));
                        tvCommentCount.setText(array.getJSONObject(0).getString("comment"));
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

        ivActivity = (ImageView) findViewById(R.id.iv_activity_bg);

        tvActivityTitle = (TextView) findViewById(R.id.tv_activity_title);
        circleIvOwner = (CircleImageView) findViewById(R.id.iv_activity_owner);
        tvActLauncherName = (TextView) findViewById(R.id.tv_act_launcher_name);
        tvActSexIc = (TextView) findViewById(R.id.tv_act_sex_ic);
        tvActLaunchTime = (TextView) findViewById(R.id.tv_act_launch_time);
        tvActPeopleCount = (TextView) findViewById(R.id.tv_act_people_count);
        tvActPaymode = (TextView) findViewById(R.id.tv_act_paymode);
        tvActDateTime = (TextView) findViewById(R.id.tv_act_date_time);
        tvActivityPlace = (TextView) findViewById(R.id.tv_activity_place);
        tvActivityDescription = (TextView) findViewById(R.id.tv_activity_description);
        tvViewedCount = (TextView) findViewById(R.id.tv_viewed_count);
        tvTakepartCount = (TextView) findViewById(R.id.tv_takepart_count);
        tvCommentCount = (TextView) findViewById(R.id.tv_comment_count);
    }

    private String parseUrl(String IMG){
        IMG = IMG.replace("[","");
        IMG = IMG.replace("]","");
        IMG = IMG.replace("\\","");
        IMG = IMG.replace("\"","");

        return IMG;
    }

    private int getScreenWidth(Context context){
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

}
