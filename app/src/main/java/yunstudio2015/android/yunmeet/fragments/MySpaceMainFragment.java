package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.PersonInfoActivity;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class MySpaceMainFragment extends Fragment {

    private CircleImageView ivFace;
    private TextView tvName;
    private TextView tvFansAmount;
    private TextView tvFocusAmount;
    private TextView tvSex;
    private TextView tvNewFansAmount;
    private TextView tvNewMsgAmount;
    private ImageView ivBg;
    private RelativeLayout RVMySpace;
    private LinearLayout LLNewMsg;
    private LinearLayout LLNewFans;
    private LinearLayout LLSetting;

    private RequestQueue queue;

    private OnFragmentInteractionListener mListener;

    public MySpaceMainFragment() {
        // Required empty public constructor
    }


    public static MySpaceMainFragment newInstance() {
        MySpaceMainFragment fragment = new MySpaceMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewMySpace = inflater.inflate(R.layout.fragment_my_space_main, container, false);

        initViews(viewMySpace);

        Map<String,String> map = new HashMap<String,String>();
        map.put("token", UtilsFunctions.getToken(getActivity()));
        /*JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_FOCUS_COUNT, new JSONObject(map), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        tvFansAmount.setText(response.getJSONObject("data").getString("focused"));
                        tvFocusAmount.setText(response.getJSONObject("data").getString("focus"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Accept", "application/json");
                headers.put("Content-Type", "application/json; charset=UTF-8");
                return headers;
            }
        };

        queue.add(req);*/

        Map<String,String> map1 = new HashMap<String,String>();
        map1.put("id",UtilsFunctions.getID(getActivity()));
        map1.put("token", UtilsFunctions.getToken(getActivity()));
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_GET_INFO, new JSONObject(map1), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("error").equals("0")){
                        tvName.setText(response.getJSONObject("data").getString("nickname"));
                        Glide.with(getActivity()).load(response.getJSONObject("data").getString("face")).into(ivFace);
                        if (response.getJSONObject("data").getString("sex").equals("0")){
                            tvSex.setText(getString(R.string.male_symbol));
                        } else {
                            tvSex.setText(getString(R.string.female_symbol));
                        }
                        Glide.with(getActivity()).load(response.getJSONObject("data").getString("background")).into(ivBg);

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
                Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
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

        RVMySpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), PersonInfoActivity.class);
                startActivity(i);
            }
        });

        LLNewFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"新粉丝",Toast.LENGTH_SHORT).show();
            }
        });

        LLNewMsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"最新消息通知",Toast.LENGTH_SHORT).show();
            }
        });

        LLSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"设置",Toast.LENGTH_SHORT).show();
            }
        });

        return viewMySpace;
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViews(View view) {

        ivFace = (CircleImageView) view.findViewById(R.id.iv_person_info_face);
        tvName = (TextView) view.findViewById(R.id.tv_person_info_name);
        tvFansAmount = (TextView) view.findViewById(R.id.person_info_fans_amount);
        tvFocusAmount = (TextView) view.findViewById(R.id.person_info_focus_amount);
        tvSex = (TextView) view.findViewById(R.id.tv_person_info_sex);
        tvNewMsgAmount = (TextView) view.findViewById(R.id.tv_new_msg_amount);
        tvNewFansAmount = (TextView) view.findViewById(R.id.tv_new_fans_amount);
        ivBg = (ImageView) view.findViewById(R.id.iv_person_info_bg);
        RVMySpace = (RelativeLayout) view.findViewById(R.id.RL_my_space);
        LLNewMsg = (LinearLayout) view.findViewById(R.id.LL_new_msg);
        LLNewFans = (LinearLayout) view.findViewById(R.id.LL_new_fans);
        LLSetting = (LinearLayout) view.findViewById(R.id.LL_setting);

    }

}
