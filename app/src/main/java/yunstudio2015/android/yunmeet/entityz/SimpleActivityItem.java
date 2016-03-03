package yunstudio2015.android.yunmeet.entityz;

import android.graphics.Bitmap;

/**
 * Created by lizhaotailang on 2016/3/3.
 */
public class SimpleActivityItem {

    private String ID;
    private String theme;
    private String detail;
    private String image_url;
    private String pubtime;

    public SimpleActivityItem(String image_url,String id,String theme,String detail,String pubtime){

        this.image_url = image_url;
        this.ID = id;
        this.theme = theme;
        this.detail = detail;
        this.pubtime = pubtime;

    }

    public String getPubtime() {
        return pubtime;
    }

    public String getImage() {
        return image_url;
    }

    public String getDetail() {
        return detail;
    }

    public String getID() {
        return ID;
    }

    public String getTheme() {
        return theme;
    }
}
