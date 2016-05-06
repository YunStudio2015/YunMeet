package yunstudio2015.android.yunmeet.activityz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.Oauth2AccessToken;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.sina.weibo.sdk.net.RequestListener;
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
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.utilz.User;
import yunstudio2015.android.yunmeet.utilz.UsersAPI;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.WeiboAccessKeeper;
import yunstudio2015.android.yunmeet.utilz.YunApi;

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
    private ImageView ivQQ, ivWeibo;
    private String password = null;
    private String phoneNumber = null;
    private String verificationCode = null;

    private Tencent tencent;// Tencent类是SDK的主要实现类，开发者可通过Tencent类访问腾讯开放的OpenAPI。
    private UserInfo qqUserInfo;//QQ用户信息
    private IUiListener loginListener;//登录监听
    private IUiListener userInfoListener;//获取用户信息监听
    private String scope = "all";//获取用户信息的范围
    private String qqOpenID;//qq用户的唯一识别码，用作网络请求的参数
    private String qqAccessToken;

    //用户信息接口
    private UsersAPI weiboUserAPI;
    private String wbUID;//微博用户的唯一识别码

    private AuthInfo authInfo;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken weiboAccessToken;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler ssoHandler;

    private RequestQueue queue;

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

        queue = Volley.newRequestQueue(getApplicationContext());

        initTencentData();
        initWeiboData();

        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
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

                phoneNumber = String.valueOf(etPhoneNumber.getText());

                if (phoneNumber != null && isInputRight(0, phoneNumber) && password != null && isInputRight(1, password)) {
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
                } else if (password == null || !isInputRight(1, password)) {
                    tvPasswordTip.setText(R.string.wrong_password);
                } else {
                    //向服务器请求发送验证码到该手机号码
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("type", "regist");
                    map.put("phone", phoneNumber);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_CHECK_CODE, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignupActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() {

                            HashMap<String, String> headers = new HashMap<String, String>();
                            headers.put("Accept", "application/json");
                            headers.put("Content-Type", "application/json;charset=UTF-8");
                            return headers;
                        }
                    };

                    queue.add(request);

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

                    i_showProgressDialog(getString(R.string.loading));

                    //请求服务器注册新账号
                    Map<String,String> map = new HashMap<String, String>();

                    map.put("type","phone");
                    map.put("phone",phoneNumber);
                    map.put("password", password);
                    map.put("code", verificationCode);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SIGNUP, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            i_dismissProgressDialog();

                            try {
                                if (response.getString("error").equals("0")){

                                    Map<String, String> map1 = new HashMap<>();
                                    map1.put("type", "phone");
                                    map1.put("phone", phoneNumber);
                                    map1.put("password", password);

                                    JsonObjectRequest request1 = new JsonObjectRequest(Request.Method.POST, YunApi.URL_LOGIN, new JSONObject(map1), new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                            try {
                                                if (response.getString("error").equals("0")){

                                                    Toast.makeText(SignupActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();

                                                    //解析respose中嵌套的json object
                                                    JSONObject token = new JSONObject(response.getString("data"));

                                                    //保存到sharedpreferences中
                                                    // @TODO 希望能够在调用注册接口是返回ID
                                                    UtilsFunctions.setToken(SignupActivity.this,token.getString("token"));

                                                    Toast.makeText(SignupActivity.this, token.getString("token"), Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(SignupActivity.this, SetNickNameActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                                    startActivity(intent);

                                                    finish();

                                                } else {
                                                    Toast.makeText(SignupActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                                Toast.makeText(SignupActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
                                            }

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                            i_dismissProgressDialog();

                                            Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                                        }
                                    })
                                    {
                                        @Override
                                        public Map<String, String> getHeaders() {

                                            HashMap<String,String> params = new HashMap<String, String>();
                                            params.put("Accept","application/json");
                                            params.put("Content-Type","application/json,charset=UTF-8");
                                            return params;
                                        }
                                    };

                                    queue.add(request1);

                                } else {
                                    Toast.makeText(SignupActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SignupActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            i_dismissProgressDialog();

                            Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() {
                            HashMap<String,String> headers = new HashMap<String, String>();
                            headers.put("Accept","application/json");
                            headers.put("Content-Type","application/josn,charset=UTF-8");
                            return headers;
                        }
                    };

                    queue.add(request);

                    tvPhoneTip.setText(null);
                    tvPasswordTip.setText(null);
                    tvCodeTip.setText(null);
                }

            }
        });

        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tencent.isSessionValid())
                    tencent.login(SignupActivity.this,scope,loginListener);
            }
        });

        ivWeibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SSO 授权, ALL IN ONE   如果手机安装了微博客户端则使用客户端授权,没有则进行网页授权
                ssoHandler.authorize(new AuthListener());

            }
        });

    }

    private void initTencentData() {

        //初始化tencent对象
        tencent = Tencent.createInstance(YunApi.TENCENT_APP_ID,getApplicationContext());

        loginListener = new IUiListener() {

            /**
             * 返回json数据样例
             *
             * {"ret":0,
             * "pay_token":"D3D678728DC580FBCDE15722B72E7365",
             * "pf":"desktop_m_qq-10000144-android-2002-",
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
                    return;
                }

                try {
                    int ret = ((JSONObject) o).getInt("ret");
                    if (ret == 0){
                        String openID = ((JSONObject) o).getString("openid");
                        qqAccessToken = ((JSONObject) o).getString("access_token");
                        String expires = ((JSONObject) o).getString("expires_in");

                        tencent.setOpenId(openID);
                        tencent.setAccessToken(qqAccessToken, expires);

                        qqOpenID = openID;

                        qqUserInfo = new UserInfo(SignupActivity.this,tencent.getQQToken());
                        qqUserInfo.getUserInfo(userInfoListener);

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
            public void onComplete(final Object o) {

                if (o == null){
                    return;
                }

                Map<String,String> map = new HashMap<>();
                map.put("type", "qq");
                map.put("access_token", qqAccessToken);
                map.put("app_id", YunApi.TENCENT_APP_ID);
                map.put("openid", qqOpenID);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_LOGIN, new JSONObject(map), new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        authorizeSuccess(response);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
                {
                    @Override
                    public Map<String, String> getHeaders(){

                        HashMap<String,String> mapHeader = new HashMap<String,String>();
                        mapHeader.put("Accept","application/json");
                        mapHeader.put("Content-Type","application/json;charset=UTF-8");
                        return mapHeader;
                    }
                };

                queue.add(request);

            }

            @Override
            public void onError(UiError uiError) {

                Toast.makeText(SignupActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(SignupActivity.this,R.string.cancle,Toast.LENGTH_SHORT).show();

            }
        };

    }

    private void initWeiboData() {

        authInfo = new AuthInfo(SignupActivity.this,YunApi.WEIBO_APP_KEY,YunApi.WEIBO_REDIRECT_URL,YunApi.WEIBO_SCOPE);
        ssoHandler = new SsoHandler(SignupActivity.this,authInfo);

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
        ivQQ = (ImageView) findViewById(R.id.iv_QQ);
        ivWeibo = (ImageView) findViewById(R.id.iv_weibo);

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
            btnSendCode.setText(millisUntilFinished/1000+getString(R.string.second));
        }

        //计时完成时触发
        @Override
        public void onFinish() {
            btnSendCode.setText(R.string.send_code_again);
            btnSendCode.setClickable(true);
            btnSendCode.setBackgroundColor(getResources().getColor(R.color.btn_background));
        }
    }

    /**
     * 微博认证授权回调类。
     * 1. SSO 授权时，需要在 {@link #onActivityResult} 中调用 {@link SsoHandler#authorizeCallBack} 后，
     *    该回调才会被执行。
     * 2. 非 SSO 授权时，当授权结束后，该回调就会被执行。
     * 当授权成功后，请保存该 access_token、expires_in、uid 等信息到 SharedPreferences 中。
     */
    class AuthListener implements WeiboAuthListener {

        @Override
        public void onComplete(Bundle values) {

            // 从 Bundle 中解析 Token
            weiboAccessToken = Oauth2AccessToken.parseAccessToken(values);
            Log.d("weibotoken", weiboAccessToken.toString());
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  weiboAccessToken.getPhoneNum();
            if (weiboAccessToken.isSessionValid()) {

                // 保存 Token 到 SharedPreferences
                WeiboAccessKeeper.writeAccessToken(SignupActivity.this, weiboAccessToken);
                Toast.makeText(SignupActivity.this,
                        R.string.authorize_success, Toast.LENGTH_SHORT).show();

                //开始获取用户信息
                weiboUserAPI  = new UsersAPI(SignupActivity.this,YunApi.WEIBO_APP_KEY,weiboAccessToken);
                if (weiboAccessToken != null && weiboAccessToken.isSessionValid()){
                    //微博用户的唯一识别码
                    wbUID = weiboAccessToken.getUid();
                    Log.d("weibouid",wbUID);
                    long[] uids = { Long.parseLong(weiboAccessToken.getUid()) };
                    weiboUserAPI.counts(uids,requestListener);

                    Map<String,String> map = new HashMap<>();
                    map.put("type", "weibo");
                    map.put("access_token", String.valueOf(weiboAccessToken));
                    map.put("appkey", YunApi.WEIBO_APP_KEY);
                    map.put("uid", String.valueOf(wbUID));

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_LOGIN, new JSONObject(map), new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            authorizeSuccess(response);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(SignupActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders(){

                            HashMap<String,String> mapHeader = new HashMap<String,String>();
                            mapHeader.put("Accept","application/json");
                            mapHeader.put("Content-Type","application/json;charset=UTF-8");
                            return mapHeader;
                        }
                    };

                    queue.add(request);
                }

            } else {
                // 以下几种情况，您会收到 Code：
                // 1. 当您未在平台上注册的应用程序的包名与签名时；
                // 2. 当您注册的应用程序包名与签名不正确时；
                // 3. 当您在平台上注册的包名和签名与您当前测试的应用的包名和签名不匹配时。
                String code = values.getString("code");
                String message = "授权失败";
                if (!TextUtils.isEmpty(code)) {
                    message = message + "\nObtained the code: " + code;
                }
                Toast.makeText(SignupActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(SignupActivity.this,
                    R.string.authorize_cancle, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(SignupActivity.this,
                    "Auth exception : " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 微博 OpenAPI 回调接口。
     */
    private RequestListener requestListener = new RequestListener() {
        @Override
        public void onComplete(String response) {
            if (!TextUtils.isEmpty(response)) {
                // 调用 User#parse 将JSON串解析成User对象
                User user = User.parse(response);
                if (user != null) {
                    Toast.makeText(SignupActivity.this,
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(SignupActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            String info = e.getMessage();
            Toast.makeText(SignupActivity.this, info, Toast.LENGTH_LONG).show();
        }
    };

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

        /**
         * 微博
         * 当 SSO 授权 Activity 退出时，该函数被调用。
         *
         * @see {@link Activity#onActivityResult}
         */
        if (ssoHandler != null) {
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    public void authorizeSuccess(JSONObject response){

        //授权成功后先储存获得的token,然后进行activity的跳转，并且同时结束当前的activity
        try {
            if (response.getString("error").equals("0")){
                //这里写activity的跳转
                Toast.makeText(SignupActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();

                //解析原jsonobject中嵌套的jsonobject
                JSONObject res = new JSONObject(response.getString("data"));

                Toast.makeText(SignupActivity.this,res.getString("token"),Toast.LENGTH_SHORT).show();

                //保存token到sharedpreferences中
                UtilsFunctions.setToken(SignupActivity.this,res.getString("token"));
                UtilsFunctions.setID(SignupActivity.this,res.getString("id"));

                Intent i = new Intent(SignupActivity.this,HallActivity.class);
                startActivity(i);

                finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(SignupActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
        }
    }

    LoadingDialog dialog;
    public void i_showProgressDialog() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    public void i_showProgressDialog(String mess) {
        dialog = new LoadingDialog(this, mess);
        dialog.show();
    }

    public void i_dismissProgressDialog () {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }

}