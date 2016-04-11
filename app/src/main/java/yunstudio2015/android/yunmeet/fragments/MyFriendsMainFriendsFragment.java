package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import yunstudio2015.android.yunmeet.adapterz.MyFriendsAdapter;
import yunstudio2015.android.yunmeet.entityz.SimpleFriendItemEntity;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/**
 * Created by lizhaotailang on 2016/4/11.
 */
public class MyFriendsMainFriendsFragment extends Fragment {

    private RecyclerView rvFriends;

    private List<SimpleFriendItemEntity> friends = new ArrayList<SimpleFriendItemEntity>();

    private RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_friends_friends,container,false);

        rvFriends = (RecyclerView) view.findViewById(R.id.friends_list);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(getActivity()));
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_FOCUS, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        if (friends.size() != 0){
                            friends.clear();
                        }
                        JSONArray list = response.getJSONArray("data");
                        for (int i = 0; i < list.length(); i++) {
                            SimpleFriendItemEntity friend = new SimpleFriendItemEntity(
                                    list.getJSONObject(i).getString("id"),
                                    list.getJSONObject(i).getString("face"),
                                    list.getJSONObject(i).getString("nickname"),
                                    list.getJSONObject(i).getString("signature")
                            );
                            friends.add(friend);
                        }
                        rvFriends.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvFriends.setAdapter(new MyFriendsAdapter(getActivity(), friends));
                    } else {
                        Toast.makeText(getActivity(), response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(req);

        return view;
    }
}
