package yunstudio2015.android.yunmeet.activityz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import java.util.Set;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

public class SetNickNameActivity extends AppCompatActivity {


    private EditText etNickName;
    private ImageButton ibtnBack;
    private ImageView ivMale;
    private ImageView ivFemale;
    private TextView tvNameTip;
    private Button btnNextStep;
    private int selected = 0;//默认为选中男
    private String name = null;

    private SharedPreferences sharedPreferences;

    private ProgressDialog progressDialog;
    private static int FINISH = 1;

    private RequestQueue queue;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == FINISH){
                progressDialog.dismiss();
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick_name);

        initView();

        initData();

        queue = Volley.newRequestQueue(getApplicationContext());

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在上传，请稍候...");
        progressDialog.setCancelable(false);

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 0;
                ivMale.setImageResource(R.drawable.selected);
                ivFemale.setImageResource(R.drawable.unselected);
            }
        });

        ivFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = 1;
                ivMale.setImageResource(R.drawable.unselected);
                ivFemale.setImageResource(R.drawable.selected);
            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                //判断昵称是否填写
                name = String.valueOf(etNickName.getText());
                if (name == null || name.length() >= 20 || name.length() <= 0){
                    tvNameTip.setText(R.string.nick_name_tip);
                } else {

                    //设置提示语
                    tvNameTip.setText(" ");

                    progressDialog.show();

                    //请求服务器设置昵称，如果请求失败，则不再请求设置性别
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("token", sharedPreferences.getString("token", null));
                    map.put("nickname", name);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SET_NICK_NAME, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            try {
                                if (response.getString("error").equals("0")){

                                    //向服务器发起设置性别的请求
                                    Map<String, String> map_sex = new HashMap<String, String>();
                                    map_sex.put("token", sharedPreferences.getString("token", null));
                                    Log.d("nick_token", sharedPreferences.getString("token", "空"));
                                    map_sex.put("sex", String.valueOf(selected));
                                    Log.d("nick_sex", String.valueOf(selected));

                                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SET_SEX, new JSONObject(map_sex), new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            Message message = Message.obtain();
                                            message.what = FINISH;
                                            handler.sendMessage(message);

                                            try {
                                                if (response.getString("error").equals("0")){
                                                    Intent intent = new Intent(SetNickNameActivity.this, SetFaceActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {
                                            Message message = Message.obtain();
                                            message.what = FINISH;
                                            handler.sendMessage(message);
                                            Toast.makeText(SetNickNameActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    queue.add(jsonObjectRequest);

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Message message = Message.obtain();
                            message.what = FINISH;
                            handler.sendMessage(message);
                        }
                    });

                    queue.add(request);

                }
            }
        });

    }

    private void initView() {

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        etNickName = (EditText) findViewById(R.id.et_nickname);
        ivMale = (ImageView) findViewById(R.id.iv_male);
        ivFemale = (ImageView) findViewById(R.id.iv_female);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        tvNameTip = (TextView) findViewById(R.id.tv_nickname_tip);

    }

    private void initData() {

       if (selected == 0){
           ivMale.setImageResource(R.drawable.selected);
           ivFemale.setImageResource(R.drawable.unselected);
       } else {
           ivMale.setImageResource(R.drawable.unselected);
           ivFemale.setImageResource(R.drawable.selected);
       }

    }

}
