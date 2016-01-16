package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunstudio2015.android.yunmeet.R;

/**
 * author:黎赵太郎
 * time:2016.1.14
 * function:主要用于用户的注册
 * 验证码在发送之前要同时验证用户输入的手机号和密码
 * 验证输入正确后，发送验证码的按钮才是clickable
 * 用户点击了发送验证码后，手机号和密码变为不可编辑的状态
**/
public class SignupActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private EditText etPassword;
    private EditText etVerificationCode;
    private Button btnSendCode;//view,send virification code
    private Button btnSignUp;
    private TextView tvLogin;
    private TextView tvPhoneTip;
    private TextView tvPasswordTip;
    private TextView tvCodeTip;
    private ImageButton ibtnQQ, ibtnWeibo;
    private String password = null;
    private String phoneNumber = null;
    private String verificationCode = null;

    /**
     * 内部类，用于btnSendCode上的倒计时
     * 构造函数参数对应为1000*6为倒计时开始时间，1000表示计时的间隔
     */
    private TimeCount time = new TimeCount(1000*60,1000);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //init the views
        initViews();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
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

                phoneNumber = String.valueOf(etPhoneNumber.getText());

                if (phoneNumber != null && isInputRight(0, phoneNumber) && password != null && isInputRight(1,password)) {
                    btnSendCode.setBackgroundColor(getResources().getColor(R.color.btn_background));
                } else {
                    btnSendCode.setBackgroundColor(Color.TRANSPARENT);
                }
            }

        });

        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                password = String.valueOf(etPassword.getText());

                if (password != null && isInputRight(1,password) && phoneNumber != null && isInputRight(0, phoneNumber)){
                    btnSendCode.setBackgroundColor(getResources().getColor(R.color.btn_background));
                } else {
                    btnSendCode.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        btnSendCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (phoneNumber == null || !isInputRight(0, phoneNumber)) {
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else if (password == null || !isInputRight(1,password)) {
                    tvPasswordTip.setText(R.string.wrong_password);
                } else {
                    //这里写向服务器请求发送验证码到该手机号码的逻辑

                    //提示部分不设置文字
                    tvPhoneTip.setText(null);
                    tvPasswordTip.setText(null);
                    //将电话输入框变为不可输入状态
                    etPhoneNumber.setEnabled(false);
                    //将电话输入框变为不可输入状态
                    etPassword.setEnabled(false);
                    //将button状态设置为不可点击
                    btnSendCode.setClickable(false);
                    btnSendCode.setBackgroundColor(Color.TRANSPARENT);
                    //设置button上的文字有内部类TimeCount完成

                    //启动倒计时
                    time.start();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verificationCode = String.valueOf(etVerificationCode.getText());
                password = String.valueOf(etPassword.getText());

                if (phoneNumber == null || !isInputRight(0, phoneNumber)) {
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else if (password == null || !isInputRight(1, password)) {
                    tvPasswordTip.setText(R.string.wrong_password);
                } else if (verificationCode == null || verificationCode.length() != 6) {
                    tvCodeTip.setText(R.string.wrong_verification_code);
                } else {
                    //这里写请求服务器注册新账号的逻辑

                    tvPhoneTip.setText(null);
                    tvPasswordTip.setText(null);
                    tvCodeTip.setText(null);
                    String s = "电话：" + phoneNumber + "\n" + "密码：" + password + "\n" + "验证码：" + verificationCode;
                    Toast.makeText(SignupActivity.this, s, Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void initViews() {

        etPhoneNumber = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        etVerificationCode = (EditText) findViewById(R.id.et_verification_code);
        btnSendCode = (Button) findViewById(R.id.btn_send_virification_code);
        btnSignUp = (Button) findViewById(R.id.btn_signup);
        tvLogin = (TextView) findViewById(R.id.tv_login);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvPasswordTip = (TextView) findViewById(R.id.tv_password_tip);
        tvCodeTip = (TextView) findViewById(R.id.tv_code_tip);
        ibtnQQ = (ImageButton) findViewById(R.id.ibtn_QQ);
        ibtnWeibo = (ImageButton) findViewById(R.id.ibtn_weibo);

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
            Matcher m1 = p1.matcher(phoneNumber);

            return m1.matches();
        } else if (type == 1) {
            //密码验证规则
            String regExPassword = "^[0-_a-zA-Z]{6,20}$";

            Pattern p2 = Pattern.compile(regExPassword);

            Matcher m2 = p2.matcher(password);

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