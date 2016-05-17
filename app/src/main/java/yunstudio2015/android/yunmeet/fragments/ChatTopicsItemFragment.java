package yunstudio2015.android.yunmeet.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aspsine.irecyclerview.IRecyclerView;
import com.aspsine.irecyclerview.OnLoadMoreListener;
import com.aspsine.irecyclerview.OnRefreshListener;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.LoadMoreFooterView;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.interfacez.OnLoadFinishCallBack;
import yunstudio2015.android.yunmeet.interfacez.TriggerLoadMore;
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
    private List<ChatTopicEntity> globalData; // 存着整个界面的数据的变量
    private int pageCount = 10;

    public ChatTopicsItemFragment() {
        // Required empty public constructor
    }



    @Bind(R.id.superswp)
    IRecyclerView swp;


    // TODO: Rename and change types and number of parameters
    public static ChatTopicsItemFragment newInstance(int type) {
        ChatTopicsItemFragment fragment = new ChatTopicsItemFragment();
        Bundle args = new Bundle();
        args.putInt(param, type);
        fragment.setArguments(args);
        return fragment;
    }

    LoadMoreFooterView footerview;

    @Bind(R.id.lny_refresh)
    ViewGroup  lny_refreshing;

    @Bind(R.id.lny_error_message)
    ViewGroup lny_error_message;

    @Bind(R.id.bt_action)
    Button bt_action;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_chat_topics_item, container, false);
        context = rootview.getContext();
        int type = getArguments().getInt(param, 0);
        ButterKnife.bind(this, rootview);


        // get the data according to the ...
        swp.setRefreshEnabled(true);
        footerview = (LoadMoreFooterView) swp.getLoadMoreFooterView();
        footerview.setStatus(LoadMoreFooterView.Status.LOADING);
        swp.setLoadMoreEnabled(true);
        swp.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {

                refreshContent(new OnLoadFinishCallBack() {
                    @Override
                    public void loadDone() {
//                        mT("更新成功");
                        swp.setRefreshing(false);
                        lny_error_message.setVisibility(View.GONE);
                        lny_refreshing.setVisibility(View.GONE);
                        swp.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                swp.setVisibility(View.VISIBLE);
                            }
                        }, 2000);
                    }

                    @Override
                    public void loadfailed(String msg) {
                        mT(context.getResources().getString(R.string.network_failure));
                        swp.setRefreshing(false);
                        lny_error_message.setVisibility(View.VISIBLE);
                        lny_refreshing.setVisibility(View.GONE);
                        swp.setVisibility(View.GONE);
                    }
                });
            }
        });


        swp.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(View view) {

                loadMoreContent(new OnLoadFinishCallBack() {
                    @Override
                    public void loadDone() {

                    }

                    @Override
                    public void loadfailed(String msg) {

                    }
                });
            }
        });

        bt_action.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swp.setVisibility(View.GONE);
                lny_error_message.setVisibility(View.GONE);
                lny_refreshing.setVisibility(View.VISIBLE);
                swp.setRefreshing(true);
            }
        });
        swp.setRefreshing(true);
        return rootview;
    }


    private void loadMoreContent (final OnLoadFinishCallBack callback) {

        // need the id of the older one.
        int min = 100;
        for (int i = 0; i < globalData.size(); i++) {
            if (min > Integer.valueOf(globalData.get(i).id))
                min = Integer.valueOf(globalData.get(i).id);
        }

        String paramz = "";
        paramz+="min="+min+"&"+"num="+pageCount
                +"&token=" + UtilsFunctions.getToken(context);
        // get the id of the older one.
        L.d(YunApi.URL_GET_ALL_TOPIC_LIST+paramz);
        final String finalParamz = paramz;
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST,
                paramz
                , new VolleyOnResultListener() {
                    @Override
                    public void onSuccess(String response) {
                        L.d("xxx", YunApi.URL_GET_ALL_TOPIC_LIST+"?"+ finalParamz);
                        L.d("xxx", response);
                        Gson gson = new Gson();
                        try {
                            response = response.replace("t_800", "t_256");
                            JsonElement resp = gson.fromJson(response, JsonElement.class);

                            if (resp.getAsJsonObject().get("error").getAsInt() == 0) {

                                ChatTopicEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data").getAsJsonArray(),
                                        ChatTopicEntity[].class);
                                if (data != null && data.length > 0) {
                                    L.d("xxx", "first " + data[0]);
                                    L.d("xxx", "last " + data[data.length - 1]);
                                } else {
                                    L.d("xxx", "NO DATA");
                                }
                                addMoreContentRefresh(data);
                                if (callback != null)
                                    callback.loadDone();
                            } else {
                                nomoreData();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            throwError("");
                            if (callback!= null)
                                callback.loadfailed("error");
                            nomoreData();
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (error.equals(context.getString(R.string.noconnection))) {
                        /* 网络出问题可以显示。。。*/
                            throwError(error);
                        }
                        throwError("");
                        if (callback!= null)
                            callback.loadfailed("error");
                      /* 不是那就是数据出问题了 */
//                mT(error);
                    }
                });

    }

    private void nomoreData() {
        swp.setLoadMoreEnabled(false);
        footerview.setStatus(LoadMoreFooterView.Status.THE_END);
    }

    private void addMoreContentRefresh(ChatTopicEntity[] data) {

        List<ChatTopicEntity> tmpd = new ArrayList<>();
        if (data.length < pageCount) {
            // items finish
            nomoreData();
        }
        if (globalData == null)
            globalData = new ArrayList<>();
        // apply refresh
        for (ChatTopicEntity item: data) {
            globalData.add(item);
            tmpd.add(item);
        }


        adapter.append(tmpd);
        swp.setVisibility(View.VISIBLE);
//        adapter.notifyDataSetChanged();
        L.d("xxx", "view set with "+globalData.size()+" items");


    }


    /**
     *  更新内容
     * @param callback
     */
    private void refreshContent(final OnLoadFinishCallBack callback) {

        // we need the id of the newest.

        // 请求服务器获取最新的说说id
        if (globalData != null && globalData.size()>0) {
//            mT("older id == "+((ChatTopicEntity) globalData.get(globalData.size()-1)).id);
        } else {
            // 基本还没数据
        }
        String paramz = "";
        paramz += "num="+pageCount
                +"&token=" + UtilsFunctions.getToken(context);
        // get the id of the older one.
        L.d(YunApi.URL_GET_ALL_TOPIC_LIST+paramz);
        final String finalParamz = paramz;
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST,
                paramz
                , new VolleyOnResultListener() {

                    @Override
                    public void onSuccess(String response) {
                        L.d("xxx", YunApi.URL_GET_ALL_TOPIC_LIST+"?"+ finalParamz);
                        L.d("xxx", response);
                        Gson gson = new Gson();
                        try {
                            // remplacing t_800 by t_256
                            response = response.replace("t_800", "t_256");
                            JsonElement resp = gson.fromJson(response, JsonElement.class);
                            ChatTopicEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data").getAsJsonArray(),
                                    ChatTopicEntity[].class);
                            if (data != null && data.length > 0) {
                                L.d("xxx", "first "+data[0]);
                                L.d("xxx", "last "+data[data.length-1]);
                            } else {
                                L.d("xxx", "NO DATA");
                            }
                            applyRefresh(data);
                            if (callback!= null)
                                callback.loadDone();
                        } catch (Exception e) {
                            e.printStackTrace();
                            throwError("");
                            if (callback!= null)
                                callback.loadfailed("error");
                        }
                    }

                    @Override
                    public void onFailure(String error) {
                        if (error.equals(context.getString(R.string.noconnection))) {
                        /* 网络出问题可以显示。。。*/
                            throwError(error);
                        }
                        throwError("");
                        if (callback!= null)
                            callback.loadfailed("error");
                      /* 不是那就是数据出问题了 */
//                mT(error);
                    }
                });

    }


    private void applyRefresh(ChatTopicEntity[] data) {

        globalData = new ArrayList<>();
        // apply refresh
        for (ChatTopicEntity item: data) {

            globalData.add(item);
        }

        adapter = new ChatTopicRecyclerviewAdapter(globalData, new TriggerLoadMore() {
            @Override
            public void loadMore() {

            }
        });
        swp.setLayoutManager(new LinearLayoutManager(getContext()));
        swp.setIAdapter(adapter);
        swp.setVisibility(View.VISIBLE);
        adapter.notifyDataSetChanged();
    }




  /*  private void loadOlderData(final OnLoadFinishCallBack callBack) {

        if (globalData != null && globalData.size()>0) {
            mT("older id == "+((ChatTopicEntity) globalData.get(globalData.size()-1)).id);
        }
        String paramz = "";
        if (globalData!=null && globalData.size() > 0) {
            ChatTopicEntity lastestItem = (ChatTopicEntity) globalData.get(globalData.size() - 1);
            // load the next 20 from the localdatabase, if possible ok, else, load what we have,
            // if we dont have enough, load from the
            paramz+="max="+lastestItem.id+"&";
        }
        paramz += "num="+pageCount
                +"&token=" + UtilsFunctions.getToken(context);
        // get the id of the older one.
        L.d(YunApi.URL_GET_ALL_TOPIC_LIST+paramz);
        final String finalParamz = paramz;
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ALL_TOPIC_LIST,
                paramz
                , new VolleyOnResultListener() {

                    @Override
                    public void onSuccess(String response) {
                        L.d("xxx", YunApi.URL_GET_ALL_TOPIC_LIST+"?"+ finalParamz);
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
                    *//* 网络出问题可以显示。。。*//*
                            throwError(error);
                        }
                        throwError("");
                        if (callBack!= null)
                            callBack.loadfailed("error");
                *//* 不是那就是数据出问题了 *//*
//                mT(error);
                    }
                });
    }*/

    private void mT(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }


/*    public void refresh () {
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
    }*/

    /*private void loadFromLocal(OnLoadFinishCallBack onLoadFinishCallBack) {

        onLoadFinishCallBack.loadDone();
    }*/

/*    private void loadNewData(@Nullable final OnLoadFinishCallBack callBack) {
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
                    *//* 网络出问题可以显示。。。*//*
                            throwError(error);
                        }
                        throwError("");
                        if (callBack!= null)
                            callBack.loadfailed("error");
                *//* 不是那就是数据出问题了 *//*
//                mT(error);
                    }
                });
    }*/

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

  /*  private void buildUiNew(ChatTopicEntity[] data) {


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
            adapter = new ChatTopicRecyclerviewAdapter(d, new TriggerLoadMore() {
                @Override
                public void loadMore() {

                }
            });
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
    }*/

   /* private void buildUiOld(ChatTopicEntity[] data) {


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
            adapter = new ChatTopicRecyclerviewAdapter(d, new TriggerLoadMore() {
                @Override
                public void loadMore() {
                    mT("更新中");
                    swp.setLoadMore(true);
                    L.d("loading more");
                    loadOlderData(new OnLoadFinishCallBack() {
                        @Override
                        public void loadDone() {
                            swp.setLoadMore(false);
                        }

                        @Override
                        public void loadfailed(String msg) {
                            swp.setLoadMore(false);
                      adapter.noMoreItems();
                        }
                    });
                }
            });
            recyclerview_chattopic.setAdapter(adapter);
            recyclerview_chattopic.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        }
        else {
            for (ChatTopicEntity entity: data
                    ) {
                adapter.addEntry(entity);
            }
            globalData = adapter.getData();
            adapter.notifyItemRangeInserted(adapter.getItemCount()-data.length, data.length);
        }
//        recyclerview_chattopic.addItemDecoration(new ChatTopicRecyclerviewAdapter.DividerItemDecoration(context, LinearLayoutManager.VERTICAL),1);
    }

*/


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri, null);
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
        void onFragmentInteraction(Uri uri, String[] imageLinks);
    }
}
