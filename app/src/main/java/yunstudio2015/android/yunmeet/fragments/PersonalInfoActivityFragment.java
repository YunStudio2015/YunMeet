package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.SimpleActivityAdapter;
import yunstudio2015.android.yunmeet.entityz.SimpleActivityItem;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/26.
 */
public class PersonalInfoActivityFragment extends Fragment {

    private TextView tvTip;
    private RecyclerView recyclerViewActivities;

    private List<SimpleActivityItem> list = new ArrayList<SimpleActivityItem>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewActivity = inflater.inflate(R.layout.person_info_activity,container,false);

        tvTip = (TextView) viewActivity.findViewById(R.id.tv_person_info_activity);
        recyclerViewActivities = (RecyclerView) viewActivity.findViewById(R.id.recyclerview_simple_activities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getActivity()));

        Log.d("init","activity初始");

        VolleyRequest.GetStringRequest(getActivity(), YunApi.URL_GET_ACTIVITY_LIST, "token=ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx", new VolleyOnResultListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONArray array = object.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            SimpleActivityItem item = new SimpleActivityItem(
                                    array.getJSONObject(i).getString("image"),
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("theme"),
                                    array.getJSONObject(i).getString("detail"),
                                    array.getJSONObject(i).getString("pubtime"));
                            list.add(item);
                            Log.d("atys", item.toString());
                        }

                        tvTip.setVisibility(View.GONE);
                        recyclerViewActivities.setAdapter(new SimpleActivityAdapter(getActivity(), list));

                    } else {
                        tvTip.setVisibility(View.VISIBLE);
                        recyclerViewActivities.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });

        return viewActivity;
    }


}
