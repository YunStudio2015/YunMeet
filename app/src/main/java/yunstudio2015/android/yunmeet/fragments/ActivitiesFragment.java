package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.ActivityCategoryActitivy;
import yunstudio2015.android.yunmeet.adapterz.YunActivitiesListAdapter;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ActivityDownloadEntity;
import yunstudio2015.android.yunmeet.entityz.UploadActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class ActivitiesFragment extends Fragment {


    private static final String BASE_ACTIVITIES_API = "re";
    private OnFragmentInteractionListener mListener;
    private DisplayMetrics metrics;
    public static Context context;
    private FragmentManager fragmentManager;
    private ErrorFragment error_fragment;
    private Adapter adapter;
    private Drawable placeholder;


    public static ActivitiesFragment getInstance(String base_api) {

        ActivitiesFragment frg = new  ActivitiesFragment();
        Bundle args = new Bundle();
        args.putString("base", base_api);
        frg.setArguments(args);
        return frg;
    }

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    View rootview;


    @Bind(R.id.list)
    RecyclerViewPager recyclerViewPager; //

/*
    @Bind(R.id.pulltorefresh_main)
    PullToRefreshHorizontalScrollView pullToRefreshHorizontalScrollView;
*/

    Map<String, Fragment> fragmentMap; // 保存fragment的map

    @Bind(R.id.progressbar)
    ProgressBar progressbar; // 第一次进去进度条

    @Bind(R.id.error_message)
    TextView tv_error_message; // 万一程序出错，显示本textview 和出错愿意


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.discoveractivitiesfragment, container, false);
        context = rootview.getContext();
        ButterKnife.bind(this, rootview);
        fragmentMap = new HashMap<>();
        placeholder = context.getResources().getDrawable(R.drawable.wait_for_load); // 获取当照片还没加载完成的图片 (暂时只是个灰色背景)
        return rootview;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String base_api = getArguments().getString("base", BASE_ACTIVITIES_API);
        // id of the categorie, and it will help retrieve activities.
        /* */
        loadData(base_api);
        if (mListener == null)
            mListener = (OnFragmentInteractionListener) getActivity();
    }

    private void loadData(String base_api) {
        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ACTIVITY_LIST, "token=" + UtilsFunctions.getToken(context) +
                "&id=" + base_api, new VolleyOnResultListener() {
            @Override
            public void onSuccess(String response) {
//                mT(response);
                Gson gson = new Gson();
                L.d(response);
                try {
                    JsonElement resp = gson.fromJson(response, JsonElement.class);
                    ActivityDownloadEntity[] data = gson.fromJson(resp.getAsJsonObject().get("data"),
                            ActivityDownloadEntity[].class);
                    buildUi(data);
                } catch (Exception e) {
                    e.printStackTrace();
                    throwError("");
                }
            }

            @Override
            public void onFailure(String error) {
                if (error.equals(context.getString(R.string.noconnection))) {
                    /* 网络出问题可以显示。。。*/
                    throwError(error);
                }
                throwError("");
                /* 不是那就是数据出问题了 */
//                mT(error);
            }
        });
    }

    private void throwError(String s) {

        recyclerViewPager.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        tv_error_message.setVisibility(View.VISIBLE);

        if (!"".equals(s)) {
            // empty content.... data error
            tv_error_message.setText(s);
            return;
        }
        tv_error_message.setText("大哥，你这问题很严重了。。。");
    }

    public void mT(String string) {

        Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
   /**/ }


    private void buildUi(final ActivityDownloadEntity[] data) {
        // set adapter to the viewpager
        tv_error_message.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        recyclerViewPager.setVisibility(View.VISIBLE);

        LinearLayoutManager layout = new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false);
        recyclerViewPager.setLayoutManager(layout);
        recyclerViewPager.addItemDecoration(new ItemDecorator(15));
        adapter = new Adapter(data, mListener);
        recyclerViewPager.setAdapter(adapter);
        recyclerViewPager.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int i, int i2) {
                int childCount = recyclerViewPager.getChildCount();
                int width = recyclerViewPager.getChildAt(0).getWidth();
                int padding  = (recyclerViewPager.getWidth() - width)/2;

                for (int j = 0; j < childCount; j++) {
                    View v = recyclerView.getChildAt(j);
                    float rate = 0;
                    if (v.getLeft() <= padding) {
                        if (v.getLeft() >= padding - v.getWidth()) {
                            rate = (padding - v.getLeft()) * 1f / v.getWidth();
                        } else {
                            rate = 1;
                        }
                        v.setScaleY(1 - rate * 0.1f);
                    } else {
                        if (v.getLeft() <= recyclerView.getWidth() - padding) {
                            rate = (recyclerView.getWidth() - padding - v.getLeft()) * 1f / v.getWidth();
                        }
                        v.setScaleY(0.9f + rate * 0.1f);
                    }
                }
            }
        });
        // registering addOnLayoutChangeListener  aim to setScale at first layout action
        recyclerViewPager.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if(recyclerViewPager.getChildCount()<3){
                    if (recyclerViewPager.getChildAt(1) != null) {
                        View v1 = recyclerViewPager.getChildAt(1);
                        v1.setScaleY(0.9f);
                    }
                }else {
                    if (recyclerViewPager.getChildAt(0) != null) {
                        View v0 = recyclerViewPager.getChildAt(0);
                        v0.setScaleY(0.9f);
                    }
                    if (recyclerViewPager.getChildAt(2) != null) {
                        View v2 = recyclerViewPager.getChildAt(2);
                        v2.setScaleY(0.9f);
                    }
                }

            }
        });
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
//            mListener.onFragmentInteraction(uri, null);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri, @Nullable String[] themelinks);
    }


    // utils functions
    public static int getScreenWidth (Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((ActivityCategoryActitivy) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }



    // error fragment

    /*a content fragment that get inflated on the basis of a link.
     - he manages himself the inneradapter.
    * */
    public static class ContentFragment extends Fragment {


        @Bind(R.id.swipecontainer)
        SwipeRefreshLayout swipeRefreshLayout;

        @Bind(R.id.my_recycler_view)
        RecyclerView recyclerViewPager;


        @Bind(R.id.error_message)
        TextView tv_error_message;


        private LinearLayoutManager mLayoutManager;
        private List<UploadActivityEntity> lActivity;
        private YunActivitiesListAdapter mAdapter;

        public static Fragment getInstance() {

            Fragment frg = new ContentFragment();
            return frg;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootview2 = inflater.inflate(R.layout.fragment_activity_list, container, false);
            ButterKnife.bind(ContentFragment.this, rootview2);
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
//                    reload();
                }
            });
            // Configure the refreshing colors
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_green_light,
                    android.R.color.holo_green_light);
            // add one element.
//        for (int i = 0; i < 3; i++)
//            addFakeElement(recyclerViewPager);
            mLayoutManager = new LinearLayoutManager(context);
            recyclerViewPager.setLayoutManager(mLayoutManager);
//          reload();
            return rootview2;
        }


       /* private void reload() {

//        swipeRefreshLayout.setRefreshing(true);
            // reload with the link.
            L.d(YunApi.ACTIVITYZ_LIST);
            VolleyRequest.GetStringRequest(context, YunApi.ACTIVITYZ_LIST, "", new VolleyOnResultListener() {

                @Override
                public void onSuccess(String response) {

                    L.v("XXX, " + response.toString());
                    mT(response.toString());
                    try {
                        // new data
//                    if (response.get("error") == 0) {
                    *//*List<ActivityEntity>*//*
                        JSONObject resp = new JSONObject(response);
                        lActivity = (List<ActivityEntity>) resp.get("data");
                        modUpView(lActivity);
                    } catch (Exception e) {
                        throwError(-2);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(String error) {
                    // toast the error
                    if (error.equals(context.getString(R.string.noconnection))) {
                        mT(context.getString(R.string.noconnection));
                    } else {
                        throwError(-1);
                    }
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }
*/


        private void throwError(int errodid) {

            // change the fragment adding the message in the middle.
            switch (errodid) {
                case -1:
                    if (mAdapter != null && mAdapter.getItemCount() > 0) {
//                         recyclerViewPager.setVisibility(View.GONE);
                        tv_error_message.setText("出现异常");
                    }
                    break;
                case -2:
                    if (mAdapter != null && mAdapter.getItemCount() > 0) {
//                         recyclerViewPager.setVisibility(View.GONE);
                        tv_error_message.setText("数据结构识别出现异常");
                    }
                    break;
            }
        }


        private void modUpView(List<UploadActivityEntity> lActivity) {

            recyclerViewPager.setHasFixedSize(true);
            // use a linear layout manager
            // specify an adapter (see also next example)
//             recyclerViewPager.setVisibility(View.VISIBLE);
//             rel_error.setVisibility(View.GONE);
            if (lActivity != null && lActivity.size() > 0) {
                mAdapter = new YunActivitiesListAdapter(lActivity);
                recyclerViewPager.setAdapter(mAdapter);
            } else {
                // hide the mrecycler and show the network error fragment.
                // change the current fragment into a network error fragment.
                mT("no result to show");
            }
        }

        public void mT(String string) {

            Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
        }


    }

    public static class ErrorFragment extends Fragment {


        public static Fragment getInstance() {

            Fragment frg3 = new ErrorFragment();
            return frg3;
        }


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootview3 = inflater.inflate(R.layout.frg_error_fragment, container, false);
            ButterKnife.bind(ErrorFragment.this, rootview3);
            return rootview3;
        }
    }


    public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

        private final ActivityDownloadEntity[] data;
        private final OnFragmentInteractionListener mListener;
        private final Gson gson;


        public Adapter(ActivityDownloadEntity[] data, OnFragmentInteractionListener mListener) {
            this.data = data;
            this.mListener = mListener;
            gson = new Gson();
        }

        @Override
        public Adapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item_xml, null);
            return (new ViewHolder(view));
        }

        @Override
        public void onBindViewHolder(Adapter.ViewHolder holder, int position) {

            final ActivityDownloadEntity item = data[position];

            // bind texts
            holder.tv_act_launcher_name.setText(item.nickname);
            holder.tv_act_paymode.setText(item.cost.equals("1") ? context.getResources().getString(R.string.i_invite) : (item.cost.equals("2") ?
                    context.getResources().getString(R.string.look_for_invite) : context.getResources().getString(R.string.invite_aa)));
            holder.tv_act_date_time.setText(item.time);
            holder.tv_act_launch_time.setText(item.pubtime);
            holder.tv_act_people_count.setText(item.pepnum);
            holder.tv_activity_place.setText(item.place);
            holder.tv_activity_description.setText(item.detail);
            holder.tv_activity_title.setText(item.theme);

/* ill need to build up the paterns for all view types, and then set the rules */
            // setting background
            if (item.image != null && item.image.length > 0)
                Glide.with(context)
                        .load(item.image[0])
                        .centerCrop()
                        .placeholder(placeholder)
//                    .thumbnail(0.3f)
                        .error(me.crosswall.photo.pick.R.drawable.default_error)
                        .into(holder.iv_activity_bg);

            // setting userface
            Glide.with(context)
                    .load(item.face)
                    .centerCrop()
                    .placeholder(placeholder)
//                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(holder.iv_activity_owner);

            holder.iv_activity_owner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // zooming
                    Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                            UtilsFunctions.encodedPath(gson.toJson(item.face)));
                    if (mListener != null)
                        mListener.onFragmentInteraction(uri,null);
                    else {
//                        Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.iv_activity_bg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    // zooming
                    Uri uri = Uri.parse(AppConstants.scheme_ui+"://"+AppConstants.authority+"/"+
                            UtilsFunctions.encodedPath(gson.toJson(item.image[0])));
                    if (mListener != null)
                        mListener.onFragmentInteraction(uri, item.image);
                    else {
//                        Toast.makeText(context, "null", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return data.length;
        }


        public class ViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.iv_activity_owner)
            public ImageView iv_activity_owner;

            @Bind(R.id.iv_activity_bg)
            public ImageView iv_activity_bg;

            @Bind(R.id.tv_act_launcher_name)
            public TextView tv_act_launcher_name;

            @Bind(R.id.tv_activity_title)
            public TextView tv_activity_title;


            @Bind(R.id.tv_act_sex_ic)
            public TextView tv_act_sex_ic;

            /* android:text="19点半"*/
            @Bind(R.id.tv_act_date_time)
            public TextView tv_act_date_time;

            /*   android:text="理工大学丁香园"*/
            @Bind(R.id.tv_activity_place)
            public TextView tv_activity_place;


            @Bind(R.id.tv_activity_description)
            public TextView tv_activity_description;

            /*  android:text="参加人数 41"*/
            @Bind(R.id.tv_takepart_count)
            public TextView tv_takepart_count;

            /*   android:text="评论 75"*/
            @Bind(R.id.tv_comment_count)
            public TextView tv_comment_count;

            /*  android:text="AA制"*/
            @Bind(R.id.tv_act_paymode)
            public TextView tv_act_paymode;

            /*   android:text="阅读 555"*/
            @Bind(R.id.tv_viewed_count)
            public TextView tv_viewed_count;

            /*  android:text="2人约"*/
            @Bind(R.id.tv_act_people_count)
            public TextView tv_act_people_count;

            /* android:text="30 分钟前" */
            @Bind(R.id.tv_act_launch_time)
            public TextView tv_act_launch_time;

            @Bind(R.id.lny_comments)
            LinearLayout lny_comments; // 评论


            public ViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
                // set default size of background view.
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) iv_activity_bg.getLayoutParams();
                params.width = ActivitiesFragment.getScreenWidth(context);
                params.height = ActivitiesFragment.getScreenWidth(context);
                iv_activity_bg.setLayoutParams(params);
            }
        }
    }

    private class ItemDecorator extends RecyclerViewPager.ItemDecoration {

        private final int horizontalItemSpace;

        public ItemDecorator(int horizontalItemSpace) {
            super();
            this.horizontalItemSpace = horizontalItemSpace;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != parent.getAdapter().getItemCount() - 1) {
                outRect.right = horizontalItemSpace;
            }
        }
    }

}
