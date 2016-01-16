package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.ActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;

/**
 * author：黎赵太郎
 * time:20160114
 * funtion:用于用户登录
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private TextView tvPhoneTip;
    private TextView tvPasswordTip;
    private ImageButton ibtnQQ,ibtnWeibo;
    private String phoneNumber = null;
    private String password = null;

    private String url = "http://www.yunstudio-ym.cn/Yunmeet/index.php/Api/User/login";

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
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else if ( password == null || !m2.matches()){ //密码输入为空
                    tvPasswordTip.setText(R.string.wrong_password);
                } else {

                    //这里写用户登录的请求
                    Map<String, String> map = new HashMap<>();
                    map.put("type", "phone");
                    map.put("phone", phoneNumber);
                    map.put("password", password);

                    VolleyRequest.PostStringRequest(LoginActivity.this, url, map, new VolleyOnResultListener() {
                        @Override
                        public void onSuccess(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                response =  jsonObject.getString("message");
                                Toast.makeText(LoginActivity.this, response,Toast.LENGTH_SHORT).show();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "过程中出错了。",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    });


                    tvPhoneTip.setText(null);
                    tvPasswordTip.setText(null);
                }

            }
        });

    }

    public void initViews() {

        etPhoneNumber = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvPasswordTip = (TextView) findViewById(R.id.tv_password_tip);
        ibtnQQ = (ImageButton) findViewById(R.id.ibtn_QQ);
        ibtnWeibo = (ImageButton) findViewById(R.id.ibtn_weibo);

    }
}
