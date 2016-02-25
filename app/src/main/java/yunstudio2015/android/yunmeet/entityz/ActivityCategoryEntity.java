package yunstudio2015.android.yunmeet.entityz;

import android.content.Context;

/**
 * Created by Ultima on 2015/11/25.
 */
public class ActivityCategoryEntity {

    public int id;
    public String base_api;
    public String name;

    public ActivityCategoryEntity(Context context, int id) {
        this.id = id;
        // get the other informations into the sharedprefs
        this.base_api = "";
        this.name = "";
    }
}
