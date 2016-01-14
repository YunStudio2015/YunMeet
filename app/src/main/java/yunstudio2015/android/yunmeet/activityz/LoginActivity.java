package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunstudio2015.android.yunmeet.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private ImageButton ibtnQQ,ibtnWechat;
    private String phoneNumber = null;
    private String password = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phoneNumber = String.valueOf(etPhoneNumber.getText());
                password = String.valueOf(etPassword.getText());

                //电话号码验证规则
                String regExPhone = "^1[3|4|5|8][0-9]\\d{8}$";
                //密码验证规则
                String regExPassword = "^[0-9_a-zA-Z]{6,20}$";

                //编译正则表达式
                Pattern p1 = Pattern.compile(regExPhone);
                Pattern p2 = Pattern.compile(regExPassword);

                Matcher m1 = p1.matcher(phoneNumber);
                Matcher m2 = p2.matcher(password);

                if ( phoneNumber == null || !m1.matches() ) { //电话号码输入错误
                    Toast.makeText(LoginActivity.this,R.string.wrong_phone_number,Toast.LENGTH_SHORT).show();
                } else if ( password == null || !m2.matches()){ //密码输入为空
                    Toast.makeText(LoginActivity.this,R.string.wrong_password,Toast.LENGTH_SHORT).show();
                } else {
                    String s = "phone:"+phoneNumber+"\n"+"password:"+password;
                    Toast.makeText(LoginActivity.this,s,Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void initViews() {

        etPhoneNumber = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        ibtnQQ = (ImageButton) findViewById(R.id.ibtn_QQ);
        ibtnWechat = (ImageButton) findViewById(R.id.ibtn_wechat);

    }
}
