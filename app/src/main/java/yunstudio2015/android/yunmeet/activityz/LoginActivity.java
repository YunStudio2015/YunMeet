package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.tencent.connect.UserInfo;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

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
 * author：黎赵太郎
 * time:20160114
 * funtion:用于用户登录
 * 请求返回的token将保存在SharedPreferences中
 */

public class LoginActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private EditText etPassword;
    private Button btnLogin;
    private TextView tvSignup;
    private TextView tvPhoneTip;
    private TextView tvPasswordTip;
    private TextView tvForgotPassword;
    private ImageButton ibtnQQ,ibtnWeibo;
    private String phoneNumber = null;
    private String password = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    private Tencent tencent;// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    private UserInfo userInfo;//QQ用户信息
    private IUiListener loginListener;//登录监听
    private IUiListener userInfoListener;//获取用户信息监听
    private String scope = "all";//获取用户信息的范围

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();

        //初始化sharedPreferences
        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        //初始化QQ登录所需要的数据
        initTencentData();

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
                finish();
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

                if (phoneNumber == null || !m1.matches()) { //电话号码输入错误
                    tvPhoneTip.setText(R.string.wrong_phone_number);
                } else if (password == null || !m2.matches()) { //密码输入为空
                    tvPasswordTip.setText(R.string.wrong_password);
                } else {

                    //向服务器发起用户登录的请求
                    //每个参数都写成一个字段
                    Map<String, String> map = new HashMap<>();
                    map.put("type", "phone");
                    map.put("phone", phoneNumber);
                    map.put("password", password);

                    VolleyRequest.PostStringRequest(LoginActivity.this, YunApi.URL_LOGIN, map, new VolleyOnResultListener() {
                        @Override
                        public void onSuccess(String response) {

                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Toast.makeText(LoginActivity.this, jsonObject.getString("error"), Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                                //解析原jsonobject中嵌套的jsonobject
                                JSONObject token = new JSONObject(jsonObject.getString("data"));
                                Toast.makeText(LoginActivity.this,token.getString("token"),Toast.LENGTH_SHORT).show();

                                //保存token到sharedpreferences中
                                editor.putString("token",token.toString());
                                editor.apply();

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(LoginActivity.this, "过程中出错了。", Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(String error) {
                            Toast.makeText(LoginActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    tvPhoneTip.setText(null);
                    tvPasswordTip.setText(null);
                }

            }
        });

        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });

        ibtnQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tencent.isSessionValid())
                    tencent.login(LoginActivity.this,scope,loginListener);

            }
        });

        ibtnWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                userInfo = new UserInfo(LoginActivity.this,tencent.getQQToken());
                userInfo.getUserInfo(userInfoListener);
            }
        });

    }

    @Override
    protected void onDestroy() {
        //注销登录
        if (tencent != null){
            tencent.logout(LoginActivity.this);
        }
        super.onDestroy();
    }

    public void initViews() {

        etPhoneNumber = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvPasswordTip = (TextView) findViewById(R.id.tv_password_tip);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        ibtnQQ = (ImageButton) findViewById(R.id.ibtn_QQ);
        ibtnWeibo = (ImageButton) findViewById(R.id.ibtn_weibo);

    }

    public void initTencentData(){

        //初始化tencent对象
        tencent = Tencent.createInstance(YunApi.TENCENT_APP_ID,getApplicationContext());

        loginListener = new IUiListener() {

            /**
             * 返回json数据样例
             *
             * {"ret":0,
             * "pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * pf":"desktop_m_qq-10000144-android-2002-",
             * "query_authority_cost":448,
             * "authority_cost":-136792089,
             * "openid":"015A22DED93BD15E0E6B0DDB3E59DE2D",
             * "expires_in":7776000,
             * "pfkey":"6068ea1c4a716d4141bca0ddb3df1bb9",
             * "msg":"",
             * "access_token":"A2455F491478233529D0106D2CE6EB45",
             * "login_cost":499}
             */
            @Override
            public void onComplete(Object o) {

                if (o == null){
                    Log.d("loginobj",String.valueOf(o));
                    return;
                }

                try {
                    int ret = ((JSONObject) o).getInt("ret");

                    Log.d("AAAAloginret",String.valueOf(ret));

                    if (ret == 0){
                        String openID = ((JSONObject) o).getString("openid");
                        String accessToken = ((JSONObject) o).getString("access_token");
                        System.out.println(accessToken);
                        String expires = ((JSONObject) o).getString("expires_in");

                        tencent.setOpenId(openID);
                        tencent.setAccessToken(accessToken, expires);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };

        userInfoListener = new IUiListener() {

            /**
             * 返回用户信息样例
             *
             * {
             * "is_yellow_year_vip": "0",
             * "ret": 0,
             * "figureurl_qq_1":"http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/40",
             * "figureurl_qq_2":"http://q.qlogo.cn/qqapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
             * "nickname": "小罗",
             * "yellow_vip_level": "0",
             * "msg": "",
             * "figureurl_1":"http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/50",
             * "vip": "0",
             * "level": "0",
             * "figureurl_2":"http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/100",
             * "is_yellow_vip": "0",
             * "gender": "男",
             * "figureurl":"http://qzapp.qlogo.cn/qzapp/222222/8C75BBE3DC6B0E9A64BD31449A3C8CB0/30"
             * }
             */

            @Override
            public void onComplete(Object o) {

                if (o == null){
                    return;
                }

                try {
                    int ret = ((JSONObject) o).getInt("ret");
                    Log.d("QQLoginRet",String.valueOf(ret));
                    int vip = ((JSONObject) o).getInt("vip");
                    Log.d("QQLoginVIP",String.valueOf(vip));
                    int level = ((JSONObject) o).getInt("level");
                    Log.d("QQLoginLevel",String.valueOf(level));
                    String nickname = ((JSONObject) o).getString("nickname");
                    Log.d("QQLoginNickName",nickname);
                    String gender = ((JSONObject) o).getString("gender");
                    Log.d("QQLoginGender",gender);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(UiError uiError) {

            }

            @Override
            public void onCancel() {

            }
        };

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //非常重要！！！官方文档中并没有指出这里的回调！！！
        Tencent.onActivityResultData(requestCode,resultCode,data,loginListener);

        if (requestCode == Constants.REQUEST_API){
            if (resultCode == Constants.REQUEST_LOGIN){
                tencent.handleLoginData(data,loginListener);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
