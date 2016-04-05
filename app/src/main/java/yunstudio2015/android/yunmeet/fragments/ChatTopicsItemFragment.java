package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adhamenaya.androidmosaiclayout.views.MosaicLayout;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.customviewz.SuperSwipeRefreshLayout;
import yunstudio2015.android.yunmeet.entityz.ActivityDownloadEntity;
import yunstudio2015.android.yunmeet.entityz.ChatTopicDownloadEntity;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.interfacez.OnLoadFinishCallBack;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

import static yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatTopicsItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatTopicsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatTopicsItemFragment extends Fragment {

    private static String param = "param";

    private OnFragmentInteractionListener mListener;
    private android.content.Context context;
    private ChatTopicRecyclerviewAdapter adapter;
    private boolean islocalLoaded = false;

    public ChatTopicsItemFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.recyclerview_chattopic)
    RecyclerView recyclerview_chattopic;

    @Bind(R.id.lny_error_message)
    LinearLayout lny_error_message;

    @Bind(R.id.superswp)
    SuperSwipeRefreshLayout swp;


    // TODO: Rename and change types and number of parameters
    public static ChatTopicsItemFragment newInstance(int type) {
        ChatTopicsItemFragment fragment = new ChatTopicsItemFragment();
        Bundle args = new Bundle();
        args.putInt(param, type);
        fragment.setArguments(args);
        return fragment;
    }

    /* recycler adapter */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_chat_topics_item, container, false);
        context = rootview.getContext();
        int type = getArguments().getInt(param, 0);
        ButterKnife.bind(this, rootview);
        // get the data according to the ...
        recyclerview_chattopic.setLayoutManager(new LinearLayoutManager(context));
        recyclerview_chattopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        // load data from local store
        loadFromLocal (new OnLoadFinishCallBack() {
            @Override
            public void loadDone() {
                refresh();
                islocalLoaded = true;
            }

            @Override
            public void loadfailed(String msg) {
            }
        });

        swp.setOnPullRefreshListener(new SuperSwipeRefreshLayout.OnPullRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
            }

            @Override
            public void onPullDistance(int distance) {
            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });
        swp.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.list_loader_layout, null));
        return rootview;
    }

    public void refresh () {
        swp.setRefreshing(true);
        loadData(new OnLoadFinishCallBack() {

            @Override
            public void loadDone() {
                //
                swp.setRefreshing(false);
            }

            @Override
            public void loadfailed(String msg) {
                swp.setRefreshing(false);
            }
        });
    }



    private void loadFromLocal(OnLoadFinishCallBack onLoadFinishCallBack) {

        onLoadFinishCallBack.loadDone();
    }



    private void loadData(@Nullable final OnLoadFinishCallBack callBack) {
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST, "token=" + UtilsFunctions.getToken(context)
                , new VolleyOnResultListener() {

                    @Override
                    public void onSuccess(String response) {
                        L.d(response);
                        Gson gson = new Gson();
                        try {
                            JsonElement resp = gson.fromJson(response, JsonElement.class);
                            ChatTopicEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data").getAsJsonArray(),
                                    ChatTopicEntity[].class);
                            buildUi(data);
                            if (callBack!= null)
                                callBack.loadDone();
                        } catch (Exception e) {
                            e.printStackTrace();
                            throwError("");
                            if (callBack!= null)
                                callBack.loadfailed("error");
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (error.equals(context.getString(R.string.noconnection))) {
                    /* 网络出问题可以显示。。。*/
                            throwError(error);
                        }
                        throwError("");
                        if (callBack!= null)
                            callBack.loadfailed("error");
                /* 不是那就是数据出问题了 */
//                mT(error);
                    }
                });
    }

    private void throwError(String s) {

           /* recyclerViewPager.setVisibility(View.GONE);
            progressbar.setVisibility(View.GONE);
            tv_error_message.setVisibility(View.VISIBLE);

            if (!"".equals(s)) {
                // empty content.... data error
                tv_error_message.setText(s);
                return;
            }
            tv_error_message.setText("大哥，你这问题很严重了。。。");*/
    }

    private void buildUi(ChatTopicEntity[] data) {


        List<Object> d = new ArrayList<>();

        for (ChatTopicEntity entity: data
                ) {
            d.add(entity);
        }
        i_showLoadingIc();
        // load data from the database.
        adapter = new ChatTopicRecyclerviewAdapter(d);
//        recyclerview_chattopic.addItemDecoration(new ChatTopicRecyclerviewAdapter.DividerItemDecoration(context, LinearLayoutManager.VERTICAL),1);
        recyclerview_chattopic.setAdapter(adapter);
        recyclerview_chattopic.setVisibility(View.VISIBLE);
        i_dismissLoadingIc();
    }


    public void i_showLoadingIc() {

    }

    public void i_showLoadingIc(String mess) {

    }

    public void i_dismissLoadingIc () {

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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
