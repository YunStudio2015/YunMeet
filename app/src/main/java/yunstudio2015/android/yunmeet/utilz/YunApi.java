package yunstudio2015.android.yunmeet.utilz;

/**
 * Created by Ultima on 2015/11/25.
 */
public class YunApi {

    public static final String IP = "http://121.42.160.23/";


    // 获取活动所有的类型
    public static final String CATEGORYZ = IP+"Yunmeet/index.php/Api/Atys/cates";
    // 获取活动列表，包括最新活动~
    public static final String ACTIVITYZ_LIST =IP+"Yunmeet/index.php/Api/Atys/atys";

    //腾讯APP_ID
    public static final String TENCENT_APP_ID = "1105049205";
    //腾讯APP_KEY
    public static final String TENCENT_APP_KEY = "uyZZDP5JwGHuLvK4";

    //测试阶段url
    public static final String URL = " http://www.yunstudio-ym.cn/Yunmeet/index.php/Api/";
    //登录
    public static final String URL_LOGIN = URL+"User/login";
    //注册
    public static final String URL_SIGNUP = URL + "User/regist";
    //获取验证码
    public static final String URL_GET_CHECK_CODE = URL + "User/getChkCode";
    //设置昵称
    public static final String URL_SET_NICK_NAME = URL + "User/setNick";
    //设置性别
    public static final String URL_SET_SEX = URL + "User/setSex";
    //设置头像
    public static final String URL_SET_FACE = URL + "User/setFace";
    //设置个人主页背景
    public static final String URL_SET_BACKGROUND = URL + "Uer/setBackground";

}
