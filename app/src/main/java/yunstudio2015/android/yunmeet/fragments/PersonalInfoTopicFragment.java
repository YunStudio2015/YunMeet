package yunstudio2015.android.yunmeet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.TopicDetailsActivity;
import yunstudio2015.android.yunmeet.adapterz.SimpleActivityAdapter;
import yunstudio2015.android.yunmeet.adapterz.SimpleTopicAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.entityz.LiteChatTopicEntity;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoTopicFragment extends Fragment {

    private TextView tvTip;
    private RecyclerView recyclerViewTopics;

    private ChatTopicEntity[] list;

    private SimpleTopicAdapter adapter;

    private String id;
    private ChatTopicEntity[] data;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);

        tvTip = (TextView) viewTopic.findViewById(R.id.tv_person_info_topic);
        recyclerViewTopics = (RecyclerView) viewTopic.findViewById(R.id.recyclerview_simple_topics);
        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(getActivity()));

        list = new ChatTopicEntity[]{};

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(getActivity()));
//        map.put("id",id);
        map.put("num", ""+1000);
        loadData(map); // 获取数据
        return viewTopic;
    }

    private void loadData(Map<String, String> map) {
        VolleyRequest.PostStringRequest(getActivity(), YunApi.URL_GET_ALL_TOPIC_LIST, map, new VolleyOnResultListener() {
            @Override
            public void onSuccess(String resp) {
                Gson gson = new Gson();
                JsonElement response = null;
                L.d(resp);
                try {
                    resp = resp.replace("t_800", "t_256");
                    response = gson.fromJson(resp, JsonElement.class);
                    if (response.getAsJsonObject().get("error").getAsInt() == 0) {

                        data = gson.fromJson(response.getAsJsonObject().get("data").getAsJsonArray(),
                                ChatTopicEntity[].class);
                        tvTip.setVisibility(View.GONE);
                        updateView (data);
                    } else {
                        recyclerViewTopics.setVisibility(View.GONE);
                        tvTip.setVisibility(View.VISIBLE);
                        mT(response.getAsJsonObject().get("message").getAsString());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    recyclerViewTopics.setVisibility(View.GONE);
                    tvTip.setVisibility(View.VISIBLE);
                    mT("异常");
                }
            }

            @Override
            public void onFailure(String error) {
                mT("异常");
            }
        });
    }

    private void updateView(final ChatTopicEntity[] data) {
        if (adapter == null) {
            adapter = new SimpleTopicAdapter(getActivity(), data);
            recyclerViewTopics.setAdapter(adapter);
        }   else
            adapter.append(data);
        adapter.setOnClickListener(new SimpleActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), TopicDetailsActivity.class);
                intent.putExtra("topic", data[position]);
                startActivity(intent);
            }
        });
        mT("updating ui for personal info topic fragment");
    }

    public void mT (String mess) {

        Toast.makeText(getActivity(), mess, Toast.LENGTH_SHORT).show();
    }


}
