package yunstudio2015.android.yunmeet.activityz;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;


/**
 * 作者:黎赵太郎
 * 时间：20160117
 * 功能：重置密码
 *
 */

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageView ivBack;
    private Button btnNextStep;
    private Button btnSendCode;
    private EditText etPhoneNumber;
    private EditText etCode;
    private EditText etOldPassword;
    private EditText etNewPassword;
    private TextView tvPhoneTip;
    private TextView tvCodeTip;
    private String phoneNumber = null;//电话号码
    private String code = null;//验证码
    private RequestQueue queue;

    private TimeCount time = new TimeCount(1000*60,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initViews();

        queue = Volley.newRequestQueue(getApplicationContext());

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        etPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                phoneNumber = etPhoneNumber.getText().toString();

                if (phoneNumber != null && isInputRight(0,phoneNumber)){
                    btnSendCode.setBackgroundColor(getResources().getColor(R.color.btn_background));
                } else {
                    btnSendCode.setBackgroundColor(Color.TRANSPARENT);
                }

            }
        });

        btnNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                code = etCode.getText().toString();
                phoneNumber = String.valueOf(etPhoneNumber.getText());

                if (code == null || code.length() != 6){
                    tvCodeTip.setText(R.string.wrong_verification_code);
                } else if(phoneNumber == null || etPhoneNumber.getText().length() != 11) {
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else {
                    //向服务器请求更改密码

                    Map<String,String> map = new HashMap<String, String>();
                    map.put("token", UtilsFunctions.getToken(ForgetPasswordActivity.this));
                    map.put("oldPassword",String.valueOf(etOldPassword.getText()));
                    map.put("newPassword",String.valueOf(etNewPassword.getText()));

                    JsonObjectRequest re = new JsonObjectRequest(Request.Method.POST, YunApi.URL_FORGET_PASSWORD, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                if (response.getString("error").equals("0")){
                                    Toast.makeText(ForgetPasswordActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                   finish();
                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgetPasswordActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(re);

                    tvPhoneTip.setText(null);
                    tvCodeTip.setText(null);
                }

            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phoneNumber == null || !isInputRight(0, phoneNumber)) {
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else {

                    Map<String,String> map = new HashMap<String, String>();
                    map.put("type","password");
                    map.put("phone",phoneNumber);

                    //向服务器请求发送验证码到该手机号
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_CHECK_CODE, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                Toast.makeText(ForgetPasswordActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                if (response.getString("error").equals("0")){
                                    etOldPassword.setVisibility(View.VISIBLE);
                                    etNewPassword.setVisibility(View.VISIBLE);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ForgetPasswordActivity.this,"出错了",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ForgetPasswordActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
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

                    queue.add(request);

                    tvPhoneTip.setText(null);
                    etPhoneNumber.setEnabled(false);
                    btnSendCode.setClickable(false);
                    btnSendCode.setBackgroundColor(Color.TRANSPARENT);

                    //启动计时器
                    time.start();
                }
            }
        });

    }

    private void initViews(){

        ivBack = (ImageView) findViewById(R.id.iv_back);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        btnSendCode = (Button) findViewById(R.id.send_code);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etCode = (EditText) findViewById(R.id.et_code);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvCodeTip = (TextView) findViewById(R.id.tv_code_tip);
        etOldPassword = (EditText) findViewById(R.id.et_old_password);
        etOldPassword.setVisibility(View.GONE);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etNewPassword.setVisibility(View.GONE);

    }

    /**
     * @param type  需要验证的类型，可选择类型为2种，type=0,验证类型为电话号码，type=1，验证类型为密码
     * @param input 需要验证的内容
     */
    public Boolean isInputRight(int type, String input) {

        if (type == 0) {
            //电话号码验证规则
            String regExPhone = "^1[3|4|5|8][0-9]\\d{8}$";
            //编译正则表达式
            Pattern p1 = Pattern.compile(regExPhone);
            //匹配电话号码
            Matcher m1 = p1.matcher(input);

            return m1.matches();
        } else if (type == 1) {
            //密码验证规则
            String regExPassword = "^[0-_a-zA-Z]{6,20}$";

            Pattern p2 = Pattern.compile(regExPassword);

            Matcher m2 = p2.matcher(input);

            return m2.matches();
        }

        return false;

    }

    //内部类用于倒计时
    class TimeCount extends CountDownTimer {

        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btnSendCode.setClickable(false);
            btnSendCode.setText(millisUntilFinished/1000 + getString(R.string.second));
        }

        //计时完成时触发
        @Override
        public void onFinish() {
            btnSendCode.setText(R.string.send_code_again);
            btnSendCode.setClickable(true);
            btnSendCode.setBackgroundColor(getResources().getColor(R.color.btn_background));
        }
    }

}