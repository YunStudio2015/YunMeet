package yunstudio2015.android.yunmeet.entityz;

import android.content.Context;
import android.os.SystemClock;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import yunstudio2015.android.yunmeet.R;

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
    public String[] comments, apply;
    public Imagee[] image;

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
                ", apply=" + Arrays.toString(apply) +
                ", image=" + Arrays.toString(image) +
                '}';
    }

     /*
        *
function ago( $datetime )
{
    $interval = date_create('now')->diff( $datetime );
    $suffix = ( $interval->invert ? ' 前' : '' );
    if ( $v = $interval->y >= 1 ) return $this->pluralize( $interval->y, 'year' ) . $suffix;
    if ( $v = $interval->m >= 1 ) return $this->pluralize( $interval->m, 'month' ) . $suffix;
    if ( $v = $interval->d >= 1 ) return $this->pluralize( $interval->d, 'day' ) . $suffix;
    if ( $v = $interval->h >= 1 ) return $this->pluralize( $interval->h, 'hour' ) . $suffix;
    if ( $v = $interval->i >= 1 ) return $this->pluralize( $interval->i, 'minute' ) . $suffix;
    return $this->pluralize( $interval->s, 'second' ) . $suffix;
}
*/

    public String getLaunchTime(Context context) {

        String format =  "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.CHINA);
        Date date = sdf.parse(pubtime, new ParsePosition(0));
//                return sdf.get2DigitYearStart();
        Date now = new Date(SystemClock.currentThreadTimeMillis());
        String res = "";

        if (now.getYear() - date.getYear() >= 1) res = (now.getYear() - date.getYear())+context.getResources().getString(R.string.year);
        if (now.getMonth() - date.getMonth() >= 1) res =  (now.getMonth() - date.getMonth())+context.getResources().getString(R.string.month);
        if (now.getDay() - date.getDay() >= 1) res =  (now.getDay() - date.getDay())+context.getResources().getString(R.string.day);
        if (now.getHours() - date.getHours() >= 1) res =  (now.getHours() - date.getHours())+context.getResources().getString(R.string.hour);
        if (now.getMinutes() - date.getMinutes() >= 1) res =  (now.getMinutes() - date.getMinutes())+context.getResources().getString(R.string.minute);

        return res+context.getResources().getString(R.string.before);
    }

    public String getSexText(Context context) {

        String res = "19 "+context.getResources().getText(R.string.male_symbol);
   /* switch (sex) {
        case "0":
            ag+context.getResources().getText(R.string.male_symbol);
            break;
        case "1";
    }*/
        return res;
    }
}
