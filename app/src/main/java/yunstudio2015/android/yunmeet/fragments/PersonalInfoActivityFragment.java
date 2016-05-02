package yunstudio2015.android.yunmeet.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import yunstudio2015.android.yunmeet.activityz.ActivityDetailsActivity;
import yunstudio2015.android.yunmeet.adapterz.SimpleActivityAdapter;
import yunstudio2015.android.yunmeet.entityz.SimpleActivityItem;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/2/26.
 */
public class PersonalInfoActivityFragment extends Fragment {

    private TextView tvTip;
    private RecyclerView recyclerViewActivities;

    private List<SimpleActivityItem> list = new ArrayList<SimpleActivityItem>();

    private SimpleActivityAdapter adapter;

    private RequestQueue queue;

    // 用于初始化的用户的id
    private String id;

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View viewActivity = inflater.inflate(R.layout.person_info_activity,container,false);

        tvTip = (TextView) viewActivity.findViewById(R.id.tv_person_info_activity);
        recyclerViewActivities = (RecyclerView) viewActivity.findViewById(R.id.recyclerview_simple_activities);
        recyclerViewActivities.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new SimpleActivityAdapter(getContext(),list);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(getActivity()));
        map.put("id",id);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_BRIEF_ACTIVITY_LIST, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        JSONArray array = response.getJSONArray("data");
                        for (int i = 0; i < array.length(); i++) {
                            SimpleActivityItem item = new SimpleActivityItem(
                                    array.getJSONObject(i).getString("image"),
                                    array.getJSONObject(i).getString("id"),
                                    array.getJSONObject(i).getString("theme"),
                                    array.getJSONObject(i).getString("detail"),
                                    array.getJSONObject(i).getString("pubtime"));
                            list.add(item);
                        }

                        tvTip.setVisibility(View.GONE);
                        recyclerViewActivities.setAdapter(adapter);
                        adapter.setOnItemClickListener(new SimpleActivityAdapter.OnRecyclerViewItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                Intent intent = new Intent(getActivity(), ActivityDetailsActivity.class);
                                intent.putExtra("activityID",list.get(position).getID());
                                startActivity(intent);
                            }
                        });
                    } else {
                        tvTip.setVisibility(View.VISIBLE);
                        recyclerViewActivities.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        queue.add(request);

        return viewActivity;
    }

}