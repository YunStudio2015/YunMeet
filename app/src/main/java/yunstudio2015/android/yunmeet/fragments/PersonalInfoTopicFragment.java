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
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoTopicFragment extends Fragment {

    private TextView tvTip;
    private RecyclerView recyclerViewTopics;

    private List<SimpleTopicItem> list = new ArrayList<SimpleTopicItem>();

    private SimpleTopicAdapter adapter;

    private RequestQueue queue;

    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);

        tvTip = (TextView) viewTopic.findViewById(R.id.tv_person_info_topic);
        recyclerViewTopics = (RecyclerView) viewTopic.findViewById(R.id.recyclerview_simple_topics);
        recyclerViewTopics.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!list.isEmpty()){
            list.clear();
        }
        adapter = new SimpleTopicAdapter(getActivity(),list);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(getActivity()));
        map.put("id",id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_USER_TOPIC_LIST, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            SimpleTopicItem item = new SimpleTopicItem(
                                    array.getJSONObject(i).getString("image"),
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("content"),
                                    array.getJSONObject(i).getString("pubtime"));
                            list.add(item);
                        }

                        tvTip.setVisibility(View.GONE);
                        recyclerViewTopics.setAdapter(adapter);
                        adapter.setOnClickListener(new SimpleActivityAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), TopicDetailsActivity.class);
                                intent.putExtra("topicID",list.get(position).getId());
                                startActivity(intent);
                            }
                        });

                    } else {
                        recyclerViewTopics.setVisibility(View.GONE);
                        tvTip.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(request);

        return viewTopic;
    }

}
