package yunstudio2015.android.yunmeet.entityz;

/**
 * Created by abiguime on 2016/5/10.
 */
public class Comment {

    public   boolean isTopic; // 是否话题
    public   boolean isActivity; // 是否活动
    public   String user_to; // 是否@了某个用户
    public   String user_from; // 谁写了当前评论
    public   String entityId; // 话题或者活动id
    public   String content; // 评论内容
}
