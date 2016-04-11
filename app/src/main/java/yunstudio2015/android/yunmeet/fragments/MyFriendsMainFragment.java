package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class MyFriendsMainFragment extends Fragment {

    private RecyclerView rvFriends;
    private List<SimpleFriendItemEntity> friends = new ArrayList<SimpleFriendItemEntity>();
    private Context context;
    private RequestQueue queue;
    private Toolbar toolbar;

    private OnFragmentInteractionListener mListener;

    public MyFriendsMainFragment() {
        // Required empty public constructor
    }


    public static MyFriendsMainFragment newInstance( ) {
        MyFriendsMainFragment fragment = new MyFriendsMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getContext().getApplicationContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_friends_main, container, false);

        context = rootView.getContext();

        initViews(rootView);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(context));
        Log.d("friends", UtilsFunctions.getToken(context));
        /*VolleyRequest.PostStringRequest(getActivity(), YunApi.URL_GET_FOCUS, map , new VolleyOnResultListener() {
            @Override
            public void onSuccess(String response) {

                Log.d("friends", "resq suc");
                try {
                    JSONObject resp = new JSONObject(response);
                    Log.d("friends", "data");
                    if (resp.getString("error").equals("0")) {

                        JSONArray list = resp.getJSONArray("data");
                        for (int i = 0; i < list.length(); i++) {
                            SimpleFriendItemEntity friend = new SimpleFriendItemEntity(
                                    list.getJSONObject(i).getString("id"),
                                    list.getJSONObject(i).getString("face"),
                                    list.getJSONObject(i).getString("nickname"),
                                    list.getJSONObject(i).getString("signature")
                            );
                            Log.d("friends", "dddddddddddddddd");
                            friends.add(friend);
                        }
                        rvFriends.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvFriends.setAdapter(new MyFriendsAdapter(getActivity(), friends));
                    } else {
                        Toast.makeText(getActivity(), resp.getString("message"), Toast.LENGTH_SHORT).show();
                        Log.d("friend", resp.getString("message"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(getActivity(), error, Toast.LENGTH_SHORT).show();
            }
        });*/

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
                        Log.d("friend", response.getString("message"));
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

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViews(View view) {

        rvFriends = (RecyclerView) view.findViewById(R.id.friends_list);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);

    }
}
