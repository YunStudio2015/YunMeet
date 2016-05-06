package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.datetimepicker.Utils;
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
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.utilz.User;
import yunstudio2015.android.yunmeet.utilz.UsersAPI;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.WeiboAccessKeeper;
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
    private ImageView ivQQ,ivWeibo;
    private String phoneNumber = null;
    private String password = null;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

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
    private String wbNickName;//微博用户的昵称

    private AuthInfo authInfo;
    /** 封装了 "access_token"，"expires_in"，"refresh_token"，并提供了他们的管理功能  */
    private Oauth2AccessToken weiboAccessToken;
    /** 注意：SsoHandler 仅当 SDK 支持 SSO 时有效 */
    private SsoHandler ssoHandler;

    private static int FINISH = 1;

    private RequestQueue queue;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化sharedPreferences
        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (null != sharedPreferences.getString("token",null) ){
            Intent i = new Intent(LoginActivity.this,HallActivity.class);
            startActivity(i);
            finish();
        }

        setContentView(R.layout.activity_login);

        initViews();

        //初始化QQ登录所需要的数据
        initTencentData();

        //初始化微博登录所需要的数据
        initWeiboData();

        queue = Volley.newRequestQueue(getApplicationContext());

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

                    i_showProgressDialog(getString(R.string.loading));

                    //向服务器发起用户登录的请求
                    //每个参数都写成一个字段
                    Map<String, String> map = new HashMap<>();
                    map.put("type", "phone");
                    map.put("phone", phoneNumber);
                    map.put("password", password);

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_LOGIN,new JSONObject(map),
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    i_dismissProgressDialog();

                                    Log.d("login",response.toString());

                                    authorizeSuccess(response);

                                }

                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                            i_dismissProgressDialog();

                            Log.d("login", "error "+ volleyError.toString());
                            Toast.makeText(LoginActivity.this,volleyError.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }

                    ) {
                        /* 给后台描述我们请求转发的数据类型为json */
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

        ivQQ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!tencent.isSessionValid())
                    tencent.login(LoginActivity.this,scope,loginListener);

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

    private void initWeiboData() {

        authInfo = new AuthInfo(LoginActivity.this,YunApi.WEIBO_APP_KEY,YunApi.WEIBO_REDIRECT_URL,YunApi.WEIBO_SCOPE);
        ssoHandler = new SsoHandler(LoginActivity.this,authInfo);

    }

    public void initViews() {

        etPhoneNumber = (EditText) findViewById(R.id.et_phone);
        etPassword = (EditText) findViewById(R.id.et_password);
        btnLogin = (Button) findViewById(R.id.btn_login);
        tvSignup = (TextView) findViewById(R.id.tv_signup);
        tvPhoneTip = (TextView) findViewById(R.id.tv_phone_tip);
        tvPasswordTip = (TextView) findViewById(R.id.tv_password_tip);
        tvForgotPassword = (TextView) findViewById(R.id.tv_forgot_password);
        ivQQ = (ImageView) findViewById(R.id.iv_QQ);
        ivWeibo = (ImageView) findViewById(R.id.iv_weibo);

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

                        qqUserInfo = new UserInfo(LoginActivity.this,tencent.getQQToken());
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
                        Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
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

                Toast.makeText(LoginActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancel() {

                Toast.makeText(LoginActivity.this,R.string.cancle,Toast.LENGTH_SHORT).show();

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
            Log.d("weibotoken",weiboAccessToken.toString());
            //从这里获取用户输入的 电话号码信息
            String  phoneNum =  weiboAccessToken.getPhoneNum();
            if (weiboAccessToken.isSessionValid()) {

                // 保存 Token 到 SharedPreferences
                WeiboAccessKeeper.writeAccessToken(LoginActivity.this, weiboAccessToken);
                Toast.makeText(LoginActivity.this,
                        R.string.authorize_success, Toast.LENGTH_SHORT).show();

                //开始获取用户信息
                weiboUserAPI  = new UsersAPI(LoginActivity.this,YunApi.WEIBO_APP_KEY,weiboAccessToken);
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
                            Toast.makeText(LoginActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                            Log.d(">>>","error"+error.networkResponse.statusCode
                            +">>>"+error.networkResponse.data
                            +">>>"+error.getCause()
                            +">>>"+error.getMessage());
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
                Toast.makeText(LoginActivity.this, message, Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public void onCancel() {
            Toast.makeText(LoginActivity.this,
                    R.string.authorize_cancle, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onWeiboException(WeiboException e) {
            Toast.makeText(LoginActivity.this,
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
                    Toast.makeText(LoginActivity.this,
                            "获取User信息成功，用户昵称：" + user.screen_name,
                            Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(LoginActivity.this, response, Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            String info = e.getMessage();
            Toast.makeText(LoginActivity.this, info, Toast.LENGTH_LONG).show();
        }
    };

    //授权成功后调用这个函数
    public void authorizeSuccess(JSONObject response){

        //授权成功后先储存获得的token,然后进行activity的跳转，并且同时结束当前的activity
        try {
            if (response.getString("error").equals("0")){
                //这里写activity的跳转
                Toast.makeText(LoginActivity.this,R.string.login_success,Toast.LENGTH_SHORT).show();

                //解析原jsonobject中嵌套的jsonobject
                JSONObject data = new JSONObject(response.getString("data"));

                Toast.makeText(LoginActivity.this,data.getString("token"),Toast.LENGTH_SHORT).show();

                //保存token到sharedpreferences中
                UtilsFunctions.setToken(LoginActivity.this,data.getString("token"));
                UtilsFunctions.setID(LoginActivity.this,data.getString("id"));

                Intent i = new Intent(LoginActivity.this,HallActivity.class);
                startActivity(i);

                finish();

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(LoginActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
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