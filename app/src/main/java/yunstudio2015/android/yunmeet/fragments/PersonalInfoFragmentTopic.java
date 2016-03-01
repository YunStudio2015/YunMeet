package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.SimpleTopicAdapter;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;
import yunstudio2015.android.yunmeet.utilz.GetSimpleTopicsTask;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoFragmentTopic extends Fragment {

    private TextView tvTip;
    private ListView lvTopics;

    private List<SimpleTopicItem> list = new ArrayList<SimpleTopicItem>();
    private String message = null;

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(getContext().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);

        tvTip = (TextView) viewTopic.findViewById(R.id.tv_person_info_topic);
        lvTopics = (ListView) viewTopic.findViewById(R.id.lv_simple_topics);

        GetSimpleTopicsTask task = new GetSimpleTopicsTask(getActivity(), queue, new GetSimpleTopicsTask.GetSimpleTopicsFinishCallback() {
            @Override
            public void GetTopicsDone(List<SimpleTopicItem> data) {
                if (list.isEmpty()){
                    lvTopics.setVisibility(View.GONE);
                    tvTip.setVisibility(View.VISIBLE);
                    Log.d("调用", "data为空");
                } else {
                    tvTip.setVisibility(View.GONE);
                    SimpleTopicAdapter adapter = new SimpleTopicAdapter(getActivity(),R.layout.simple_topic_item,data);
                    lvTopics.setAdapter(adapter);
                    Log.d("调用", "data不为空");
                }
            }

            @Override
            public void GetTopicsFailed(String failedMsg) {
                message = failedMsg;
                tvTip.setText(message);
                Log.d("调用", "data为空");
            }
        });

        task.execute(sharedPreferences.getString("token",null));

        return viewTopic;
    }

}
