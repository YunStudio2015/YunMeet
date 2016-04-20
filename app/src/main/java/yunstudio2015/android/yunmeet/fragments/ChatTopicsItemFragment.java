package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter;
import yunstudio2015.android.yunmeet.customviewz.SuperSwipeRefreshLayout;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.interfacez.OnLoadFinishCallBack;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

/***
 * By UAb
 */
public class ChatTopicsItemFragment extends Fragment {

    private static String param = "param";

    private OnFragmentInteractionListener mListener;
    private android.content.Context context;
    private ChatTopicRecyclerviewAdapter adapter;
    private boolean islocalLoaded = false;
    private List<Object> globalData;
    private int pageCount = 2;

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
                loadNewData (new OnLoadFinishCallBack() {
                    @Override
                    public void loadDone() {
                        swp.setRefreshing(false);
                    }

                    @Override
                    public void loadfailed(String msg) {
                        swp.setRefreshing(false);
                    }
                });
            }

            @Override
            public void onPullDistance(int distance) {
            }

            @Override
            public void onPullEnable(boolean enable) {
            }
        });
        swp.setOnPushLoadMoreListener(new SuperSwipeRefreshLayout.OnPushLoadMoreListener() {
            @Override
            public void onLoadMore() {
                // 加載更多的數據
                swp.setLoadMore(true);
                loadOlderData(new OnLoadFinishCallBack() {
                    @Override
                    public void loadDone() {
                        swp.setLoadMore(false);
                    }

                    @Override
                    public void loadfailed(String msg) {
                        swp.setLoadMore(false);
                    }
                });
            }

            @Override
            public void onPushDistance(int distance) {
            }

            @Override
            public void onPushEnable(boolean enable) {
            }
        });
        swp.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.list_loader_layout, null));
        return rootview;
    }

    private void loadOlderData(final OnLoadFinishCallBack callBack) {

        if (globalData != null && globalData.size()>0) {
            mT("older id == "+((ChatTopicEntity) globalData.get(globalData.size()-1)).id);
        }
        String paramz = "";
        if (globalData!=null && globalData.size() > 0) {
            ChatTopicEntity lastestItem = (ChatTopicEntity) globalData.get(globalData.size() - 1);
            // load the next 20 from the localdatabase, if possible ok, else, load what we have,
            // if we dont have enough, load from the
            paramz+="min="+lastestItem.topic_id;
        }
        paramz += "num="+pageCount
                +"token=" + UtilsFunctions.getToken(context);
        // get the id of the older one.
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST,
                paramz
                , new VolleyOnResultListener() {

                    @Override
                    public void onSuccess(String response) {
//                        L.d(response);
                        Gson gson = new Gson();
                        try {
                            JsonElement resp = gson.fromJson(response, JsonElement.class);
                            ChatTopicEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data").getAsJsonArray(),
                                    ChatTopicEntity[].class);
                            buildUiOld(data);
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

    private void mT(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


    public void refresh () {
        swp.setRefreshing(true);
        loadNewData(new OnLoadFinishCallBack() {

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

    private void loadNewData(@Nullable final OnLoadFinishCallBack callBack) {
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST, "token=" + UtilsFunctions.getToken(context)
                , new VolleyOnResultListener() {

                    @Override
                    public void onSuccess(String response) {
//                        L.d(response);
                        Gson gson = new Gson();
                        try {
                            JsonElement resp = gson.fromJson(response, JsonElement.class);
                            ChatTopicEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data").getAsJsonArray(),
                                    ChatTopicEntity[].class);
                            buildUiNew(data);
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

    private void buildUiNew(ChatTopicEntity[] data) {


        if (data != null && data.length > 0) {
            ChatTopicEntity e = data[0];
            if (Integer.valueOf(e.id) <= ((globalData != null && globalData.size()>0 ? globalData.size() : 0))) {
                mT("no more new for now");
                return;
            }
        } else {
            mT("no more new for now");
            return;
        }
        List<ChatTopicEntity> d = new ArrayList<>();
        for (ChatTopicEntity entity: data
                ) {
            d.add(entity);
        }
        i_showLoadingIc();
        if (globalData == null) {
            globalData = new ArrayList<>();
            adapter = new ChatTopicRecyclerviewAdapter(d);
            recyclerview_chattopic.setAdapter(adapter);
            recyclerview_chattopic.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        else {
            for (ChatTopicEntity entity: data
                    ) {
                adapter.addEntry(entity);
            }
            adapter.notifyItemRangeInserted(adapter.getItemCount()-data.length, data.length);
        }
    }

    private void buildUiOld(ChatTopicEntity[] data) {


        if (data != null && data.length > 0) {
            ChatTopicEntity e = data[0];
            if (Integer.valueOf(e.id) >= ((globalData != null && globalData.size()>0 ? globalData.size() : 0)))
                mT("still hv old");
            else {
                mT("no more new for now");
                setEndRecyclerview();
            }
        } else {
            mT("no more new for now");
            setEndRecyclerview();
        }
        List<ChatTopicEntity> d = new ArrayList<>();
        for (ChatTopicEntity entity: data
                ) {
            d.add(entity);
        }
        i_showLoadingIc();
        if (globalData == null) {
            globalData = new ArrayList<>();
            adapter = new ChatTopicRecyclerviewAdapter(d);
            recyclerview_chattopic.setAdapter(adapter);
            recyclerview_chattopic.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        else {
            for (ChatTopicEntity entity: data
                    ) {
                adapter.addEntry(entity);
            }
            adapter.notifyItemRangeInserted(adapter.getItemCount()-data.length, data.length);
        }
//        recyclerview_chattopic.addItemDecoration(new ChatTopicRecyclerviewAdapter.DividerItemDecoration(context, LinearLayoutManager.VERTICAL),1);
    }

    private void setEndRecyclerview() {
        mT("end recyclerview");
        swp.setOnPushLoadMoreListener(null);
        if (recyclerview_chattopic != null)
            swp.setFooterView(LayoutInflater.from(getActivity()).inflate(R.layout.rv_footer, swp, false));
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
