package yunstudio2015.android.yunmeet.entityz;

import java.io.Serializable;

/**
 * Created by abiguime on 2016/4/26.
 */
public class Imagee implements Serializable {

    public int width, height;
    public String url;

    public Imagee(int width, int height, String url) {
        this.width = width;
        this.height = height;
        this.url = url;
    }
}

