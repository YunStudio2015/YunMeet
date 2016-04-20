package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.fragments.PersonalInfoActivityFragment;
import yunstudio2015.android.yunmeet.fragments.PersonalInfoTopicFragment;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/26.
 */
public class PersonInfoActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private Button btnTopic;
    private Button btnActivity;
    private LinearLayout layoutChat;
    private Button btnAddFocus;
    private ImageView ivFace;
    private ImageView ivBg;
    private TextView tvName;
    private TextView tvFansAmount;
    private TextView tvFocusAmount;
    private TextView tvSex;
    //都是toolbar上的控件
    private ImageView ivBack;
    private TextView tvTitle;
    private Button btnNextStep;

    private RequestQueue queue;

    private String id = null;

    PersonalInfoActivityFragment fragmentActivity = new PersonalInfoActivityFragment();
    PersonalInfoTopicFragment fragmentTopic = new PersonalInfoTopicFragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_data);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();

        queue = Volley.newRequestQueue(getApplicationContext());

        Intent intent = getIntent();
        id = intent.getStringExtra("id");

        this.setSupportActionBar(toolbar);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        initMyTopic();

        Map<String,String> map = new HashMap<String,String>();
        map.put("token",id );
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_FOCUS_COUNT, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        tvFansAmount.setText(response.getJSONObject("data").getString("focused"));
                        tvFocusAmount.setText(response.getJSONObject("data").getString("focus"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        queue.add(req);

        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("id",id);
        map1.put("token", UtilsFunctions.getToken(PersonInfoActivity.this));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_INFO, new JSONObject(map1), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        tvName.setText(response.getJSONObject("data").getString("nickname"));
                        tvTitle.setText(response.getJSONObject("data").getString("nickname"));
                        Glide.with(PersonInfoActivity.this).load(response.getJSONObject("data").getString("face")).into(ivFace);
                        if (response.getJSONObject("data").getString("sex").equals("0")){
                            tvSex.setText(getString(R.string.male_symbol));
                        } else {
                            tvSex.setText(getString(R.string.female_symbol));
                        }
                        Glide.with(PersonInfoActivity.this).load(response.getJSONObject("data").getString("background")).into(ivBg);

                    } else {
                        Toast.makeText(PersonInfoActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonInfoActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        queue.add(request);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonInfoActivity.this,"next",Toast.LENGTH_SHORT).show();
            }
        });

        btnTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //控制获得点击的按钮的背景色为绿色，字体颜色为白色
                //没有获得点击的按钮的背景色为白色，字体颜色为绿色
                btnTopic.setBackgroundColor(getResources().getColor(R.color.btn_background));
                btnTopic.setTextColor(Color.WHITE);
                btnActivity.setBackgroundColor(Color.WHITE);
                btnActivity.setTextColor(getResources().getColor(R.color.btn_background));

                initMyTopic();

            }
        });

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控制获得点击的按钮的背景色为绿色，字体颜色为白色
                //没有获得点击的按钮的背景色为白色，字体颜色为绿色
                btnActivity.setBackgroundColor(getResources().getColor(R.color.btn_background));
                btnActivity.setTextColor(Color.WHITE);
                btnTopic.setBackgroundColor(Color.WHITE);
                btnTopic.setTextColor(getResources().getColor(R.color.btn_background));

                initMyActivity();

            }
        });

        btnAddFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        layoutChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PersonInfoActivity.this,"chat",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnTopic = (Button) findViewById(R.id.btn_person_info_topic);
        btnActivity = (Button) findViewById(R.id.btn_person_info_activity);
        layoutChat = (LinearLayout) findViewById(R.id.person_info_chat);
        btnAddFocus = (Button) findViewById(R.id.btn_person_info_plus_focus);
        ivFace = (ImageView) findViewById(R.id.iv_person_info_face);
        ivBg = (ImageView) findViewById(R.id.iv_person_info_bg);
        tvName = (TextView) findViewById(R.id.tv_person_info_name);
        tvSex = (TextView) findViewById(R.id.tv_person_info_sex);
        tvFansAmount = (TextView) findViewById(R.id.person_info_fans_amount);
        tvFocusAmount = (TextView) findViewById(R.id.person_info_focus_amount);

        ivBack = (ImageView) findViewById(R.id.iv_back);
        tvTitle = (TextView) findViewById(R.id.toolbar_txt);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);

    }

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT  != Build.VERSION_CODES.KITKAT)
            return;
        setTranslucentStatus(activity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    private void initMyTopic(){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentTopic.isAdded()){
            transaction.hide(fragmentActivity).show(fragmentTopic);
        } else {
            transaction.add(R.id.person_framelayout,fragmentTopic);
        }

        transaction.commit();

    }

    private void initMyActivity(){

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (fragmentActivity.isAdded()){
            transaction.hide(fragmentTopic).show(fragmentActivity);
        } else {
            if (fragmentTopic.isAdded()){
                transaction.hide(fragmentTopic);
            }
            transaction.add(R.id.person_framelayout, fragmentActivity);
        }

        transaction.commit();

    }

}
