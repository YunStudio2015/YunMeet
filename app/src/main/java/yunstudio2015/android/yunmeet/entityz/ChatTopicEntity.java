package yunstudio2015.android.yunmeet.entityz;

import java.io.Serializable;

/**
 * Created by Ulrich on 3/28/2016.
 */
public class ChatTopicEntity implements Serializable {



    public Imagee[] image;
    public String  id, nickname, face, content  , user_id, comment_num, transfer, for_num, pubtime;
    public At_id[] at_id;
    public Topic_id topic_id;
    public Comment[] comment;
    /*  "data": [
        {
            "id": "4",                  // 话题id
            "user_id": "2",             // 发布者id
            "image": [                  // 话题图片
                "http://www.yunstudio-ym.cn/Yunmeet/Uploads/topic/20160215/t_800/56c17e2a0e3ea.png",
                "http://www.yunstudio-ym.cn/Yunmeet/Uploads/topic/20160215/t_800/56c17e2a11555.png"
            ],
            "content": "内容",          // 话题具体内容
            "comment_num": "0",         // 话题被评论数目
            "transfer": "0",            // 话题被转发次数
            "for_num": "0",             // 话题被赞数目
            "pubtime": "2016-02-15 16:03:25", // 话题发布时间
            "topic_id": {               // 被转发的话题信息
                "id": "2",              // 被转发的话题id
                "user_id": "1",         // 被转发的话题发布者id
                "image": "topic/20160215/56c1861468de0.png;topic/20160215/56c186146af93.png;",
                "content": "内容",      // 被转发的话题内容
                "comment_num": "0",
                "transfer": "0",
                "for_num": "0",
                "pubtime": "1455523348",
                "topic_id": "1",
                "at_id": null,
                "nickname": "匿名者",   // 被转发话题发布者的昵称
                "face": ""
            },
            "at_id": [                  // 被@的用户信息
                {
                    "id": "1",          // 用户id
                    "nickname": "匿名者"  // 用户昵称
                },
                ...
            ],
            "nickname": "haha",        // 话题发布者昵称
            "face": "http://www.yunstudio-ym.cn/Yunmeet/Uploads/face/20160215/56c1a080df7a6.png", // 话题发布者头像
            "comment": [                // 该话题的评论
                {
                    "id": "1",          // 评论id
                    "user_id": "1",     // 评论者id
                    "content": "good\r\n", // 评论内容
                    "pubtime": "1970-01-01 08:33:36",     // 评论发布时间
                    "topic_id": "2"
                },
                ...
            ]
        },
        ...
    ]*/

    public class Comment {
        public String id, user_id, content, pubtime, topic_id;
    }

    public class Topic_id {
        public String id, user_id, image, content, comment_num, transfer, for_num, pubtime, topic_id, at_id, nickname, face;
    }

    public class At_id {
        public String id;
        public String nickname;
    }

}
