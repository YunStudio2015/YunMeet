package yunstudio2015.android.yunmeet.utilz;

/**
 * Created by Ultima on 2015/11/25.
 */
public class YunApi {

    public static final String IP = "http://www.yunstudio-ym.cn/";


    // 获取活动所有的类型
    public static final String CATEGORYZ = IP+"Yunmeet/index.php/Api/Atys/getCates";

    // 获取活动列表，包括最新活动~
//    public static final String ACTIVITYZ_LIST =IP+"Yunmeet/index.php/Api/Atys/atys";

    //腾讯APP_ID,测试账号为222222，实际申请的账号为1105049205
    public static final String TENCENT_APP_ID = "222222";
    //腾讯APP_KEY
    public static final String TENCENT_APP_KEY = "uyZZDP5JwGHuLvK4";

    //新浪微博APP_KEY
    public static final String WEIBO_APP_KEY = "3636173077";
    //新浪微博APP_SECRET
    public static final String WEIBO_APP_SECRET = "a513ae986254d6e29e754182fbc08153";
    //微博默认回调页
    public static final String WEIBO_REDIRECT_URL = "https://api.weibo.com/oauth2/default.html";
    /**
     * 微博
     * Scope 是 OAuth2.0 授权机制中 authorize 接口的一个参数。通过 Scope，平台将开放更多的微博
     * 核心功能给开发者，同时也加强用户隐私保护，提升了用户体验，用户在新 OAuth2.0 授权页中有权利
     * 选择赋予应用的功能。
     *
     * 我们通过新浪微博开放平台-->管理中心-->我的应用-->接口管理处，能看到我们目前已有哪些接口的
     * 使用权限，高级权限需要进行申请。
     *
     * 目前 Scope 支持传入多个 Scope 权限，用逗号分隔。
     *
     * 有关哪些 OpenAPI 需要权限申请，请查看：http://open.weibo.com/wiki/%E5%BE%AE%E5%8D%9AAPI
     * 关于 Scope 概念及注意事项，请查看：http://open.weibo.com/wiki/Scope
     */
    public static final String WEIBO_SCOPE =
            "email,direct_messages_read,direct_messages_write,"
                    + "friendships_groups_read,friendships_groups_write,statuses_to_me_read,"
                    + "follow_app_official_microblog," + "invitation_write";

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
    // 发布话题
    public static String URL_NEW_TOPIC = URL + "Topic/add";
    //设置性别
    public static final String URL_SET_SEX = URL + "User/setSex";
    //设置头像,先使用setFace2接口测试，正式接口为setFace
    public static final String URL_SET_FACE = URL + "User/setFace";
    //设置个人主页背景
    public static final String URL_SET_BACKGROUND = URL + "Uer/setBackground";
    //获取单个用户发布的所有话题
    public static final String URL_GET_TOPIC_LIST = URL + "Topic/getBriefList";

    // 获取QQ用户信息接口
    public static final String GET_QQ_USER_INFO = "https://graph.qq.com/user/get_simple_userinfo";

}
