package yunstudio2015.android.yunmeet.interfacez;

import org.json.JSONObject;

/**
 * Created by Ultima on 2015/10/28.
 */
public interface VolleyOnResultListener {

    public void onSuccess (JSONObject response);
    public void onFailure (String error);
}
