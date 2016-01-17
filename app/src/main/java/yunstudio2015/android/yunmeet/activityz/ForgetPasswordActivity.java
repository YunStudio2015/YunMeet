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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


/**
 * 作者:黎赵太郎
 * 时间：20160117
 * 功能：重置密码
 *
 */

public class ForgetPasswordActivity extends AppCompatActivity {

    private ImageButton ibtnBack;
    private Button btnNextStep;
    private Button btnSendCode;
    private EditText etPhoneNumber;
    private EditText etCode;
    private TextView tvPhoneTip;
    private TextView tvCodeTip;
    private String phoneNumber = null;//电话号码
    private String code = null;//验证码

    private TimeCount time = new TimeCount(1000*60,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        initViews();

        ibtnBack.setOnClickListener(new View.OnClickListener() {
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

                if (code == null || code.length() != 6){
                    tvCodeTip.setText(R.string.wrong_verification_code);
                } else {

                    //向服务器请求更改密码

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
                    VolleyRequest.PostStringRequest(ForgetPasswordActivity.this, YunApi.URL_GET_CHECK_CODE, map, new VolleyOnResultListener() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                response = jsonObject.getString("message");
                                Toast.makeText(ForgetPasswordActivity.this,response,Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(ForgetPasswordActivity.this,"出错了",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(ForgetPasswordActivity.this,error,Toast.LENGTH_SHORT).show();
                        }
                    });

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

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        btnNextStep = (Button) findViewById(R.id.btn_next_step);
        btnSendCode = (Button) findViewById(R.id.send_code);
        etPhoneNumber = (EditText) findViewById(R.id.et_phone_number);
        etCode = (EditText) findViewById(R.id.et_code);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvCodeTip = (TextView) findViewById(R.id.tv_code_tip);

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
            btnSendCode.setText(millisUntilFinished/1000+"秒");
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
