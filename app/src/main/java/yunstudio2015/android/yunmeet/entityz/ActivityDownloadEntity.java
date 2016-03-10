package yunstudio2015.android.yunmeet.entityz;

import java.util.Arrays;

/**
 * Created by Ulrich on 3/10/2016.
 */
public class ActivityDownloadEntity {

    /*
    *
    * "id": "10",
            "user_id": "1",
            "isrec": "0",
            "pepnum": "2",
            "theme": "标题还是算了吧",
            "detail": "描述没什么含义，就那样干干就好了。。。",
            "time": "2016-03-09 06:34:00",
            "pubtime": "2016-03-09 01:34:56",
            "cost": "1",
            "place": "地方值要说是我寝室就好了",
            "view": "0",
            "status": "0",
            "image": [
                "http://www.yunstudio-ym.cn/Yunmeet/Uploads/activity/20160309/t_800/56df0d422bb22.jpg"
            ],
            "commentnum": "0",
            "passnum": "0",
            "category_id": "1",
            "category": "吃饭",
            "nickname": "哈哈哈",
            "face": "http://www.yunstudio-ym.cn/Yunmeet/Uploads/face/20160302/56d6dc2cf2e39.jpg",
            "sex": "1",
            "isvip": "0",
            "school_id": null,
            "credit": "0",
            "comments": [],
            "background": "",
            "apply": []
    * */

    public String user_id, isrec, pepnum, theme, detail, time, pubtime, cost, place,
            view, status, commentnum, passnum, category_id, category, nickname, face, sex, isvip, school_id,
            credit, background;
    public String[] comments, image, apply;

        @Override
        public String toString() {
                return "ActivityDownloadEntity{" +
                        "user_id='" + user_id + '\'' +
                        ", isrec='" + isrec + '\'' +
                        ", pepnum='" + pepnum + '\'' +
                        ", theme='" + theme + '\'' +
                        ", detail='" + detail + '\'' +
                        ", time='" + time + '\'' +
                        ", pubtime='" + pubtime + '\'' +
                        ", cost='" + cost + '\'' +
                        ", place='" + place + '\'' +
                        ", view='" + view + '\'' +
                        ", status='" + status + '\'' +
                        ", commentnum='" + commentnum + '\'' +
                        ", passnum='" + passnum + '\'' +
                        ", category_id='" + category_id + '\'' +
                        ", category='" + category + '\'' +
                        ", nickname='" + nickname + '\'' +
                        ", face='" + face + '\'' +
                        ", sex='" + sex + '\'' +
                        ", isvip='" + isvip + '\'' +
                        ", school_id='" + school_id + '\'' +
                        ", credit='" + credit + '\'' +
                        ", background='" + background + '\'' +
                        ", comments=" + Arrays.toString(comments) +
                        ", image=" + Arrays.toString(image) +
                        ", apply=" + Arrays.toString(apply) +
                        '}';
        }
}
