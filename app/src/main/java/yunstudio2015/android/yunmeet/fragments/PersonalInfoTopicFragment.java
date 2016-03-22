package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.SimpleActivityAdapter;
import yunstudio2015.android.yunmeet.adapterz.SimpleTopicAdapter;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoTopicFragment extends Fragment {

    private TextView tvTip;
    private RecyclerView recyclerViewTopics;

    private List<SimpleTopicItem> list = new ArrayList<SimpleTopicItem>();

    private SimpleTopicAdapter adapter;

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

        adapter = new SimpleTopicAdapter(getActivity(),list);

        VolleyRequest.GetStringRequest(getActivity(), YunApi.URL_GET_TOPIC_LIST, "token=ffW0R10FJB8V8Cok6S3plWGpZkx7uIgx", new VolleyOnResultListener() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    if (object.getString("error").equals("0")) {
                        JSONArray array = object.getJSONArray("data");
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
                                Toast.makeText(getContext(),String.valueOf(position),Toast.LENGTH_SHORT).show();
                            }
                        });

                    } else {
                        recyclerViewTopics.setVisibility(View.GONE);
                        tvTip.setVisibility(View.VISIBLE);
                        Toast.makeText(getActivity(), object.getString("message"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });

        return viewTopic;
    }

}
