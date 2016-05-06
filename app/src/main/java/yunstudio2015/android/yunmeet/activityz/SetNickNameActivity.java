package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

public class SetNickNameActivity extends AppCompatActivity {


    private EditText etNickName;
    private ImageView ivBack;
    private ImageView ivMale;
    private ImageView ivFemale;
    private TextView tvNameTip;
    private Button btnNextStep;
    private int selected = 0;//默认为选中男
    private String name = null;

    private LoadingDialog dialog;

    private RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_nick_name);

        initView();

        initData();

        queue = Volley.newRequestQueue(getApplicationContext());

        dialog = new LoadingDialog(SetNickNameActivity.this);

        ivBack.setOnClickListener(new View.OnClickListener() {
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

                    //显示dialog
                    dialog.show();

                    //请求服务器设置昵称，如果请求失败，则不再请求设置性别
                    Map<String,String> map = new HashMap<String, String>();
                    map.put("token", UtilsFunctions.getToken(SetNickNameActivity.this));
                    map.put("nickname", name);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SET_NICK_NAME, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(SetNickNameActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(request);

                    //向服务器发起设置性别的请求
                    Map<String, String> map_sex = new HashMap<String, String>();
                    map_sex.put("token",UtilsFunctions.getToken(SetNickNameActivity.this));
                    map_sex.put("sex", String.valueOf(selected));

                    JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SET_SEX, new JSONObject(map_sex), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            dialog.dismiss();

                            try {

                                if (response.getString("error").equals("0")){

                                    Intent i = getIntent();

                                    /**
                                     * 是否要启动设置头像，参数 crop_face 代表是否从个人主页进入这个activity
                                     * false即从个人主页进入，不需要启动设置头像
                                     * true 只在首次登录时需要启动
                                     */
                                    if ( !i.getBooleanExtra("crop_face",false)){

                                        finish();

                                    } else {

                                        Intent intent = new Intent(SetNickNameActivity.this, SetFaceActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                        startActivity(intent);

                                        finish();
                                    }

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SetNickNameActivity.this, R.string.wrong_process,Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            dialog.cancel();
                            dialog.dismiss();

                            Toast.makeText(SetNickNameActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String,String> headers = new HashMap<String, String>();
                            headers.put("Accept","application/json");
                            headers.put("Content-Type","application/json,charset=UTF-8");
                            return headers;
                        }
                    };

                    queue.add(jsonObjectRequest);

                }
            }
        });

    }

    private void initView() {

        ivBack = (ImageView) findViewById(R.id.iv_back);
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

    @Override
    protected void onDestroy() {

        if (dialog.isShowing())
            dialog.dismiss();
        super.onDestroy();
    }
}
