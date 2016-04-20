package yunstudio2015.android.yunmeet.entityz;

import android.graphics.drawable.Drawable;

/**
 * Created by lizhaotailang on 2016/2/29.
 */
public class SimpleTopicItem {

    private String id;
    private String content;
    private String pubtime;
    private String logoUrl;

    public SimpleTopicItem(String logoUrl,String id,String content,String pubtime){

        this.id = id;
        this.content = content;
        this.pubtime = pubtime;
        this.logoUrl = logoUrl;

    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getPubtime() {
        return pubtime;
    }

    public String getLogoUrl() {
        return logoUrl;
    }
}
