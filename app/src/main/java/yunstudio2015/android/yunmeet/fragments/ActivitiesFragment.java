package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.handmark.pulltorefresh.library.PullToRefreshHorizontalScrollView;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.YunActivitiesListAdapter;
import yunstudio2015.android.yunmeet.customviewz.InnerScrollviewViewPager;
import yunstudio2015.android.yunmeet.entityz.ActivityCategoryEntity;
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


    public static Fragment getInstance(String base_api) {

        Fragment frg = new  ActivitiesFragment();
        Bundle args = new Bundle();
        args.putString("base", base_api);
        frg.setArguments(args);
        return frg;
    }

    public ActivitiesFragment() {
        // Required empty public constructor
    }

    View rootview;


    @Bind(R.id.vp_inner_activities)
    InnerScrollviewViewPager viewPager;

    @Bind(R.id.pulltorefresh_main)
    PullToRefreshHorizontalScrollView pullToRefreshHorizontalScrollView;


    @Bind(R.id.progressbar)
    ProgressBar progressbar;

    @Bind(R.id.error_message)
    TextView tv_error_message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.discoveractivitiesfragment, container, false);
        context = rootview.getContext();
        ButterKnife.bind(this, rootview);

        String base_api = getArguments().getString("base", BASE_ACTIVITIES_API);

        // id of the categorie, and it will help retrieve activities.


        /* */

        VolleyRequest.GetStringRequest(context, YunApi.URL_GET_ACTIVITY_LIST, "token=" + UtilsFunctions.getToken(context) +
                "&id=" + base_api, new VolleyOnResultListener() {
            @Override
            public void onSuccess(String response) {
//                mT(response);
                Gson gson = new Gson();
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

//        buildUi();
        return rootview;
    }

    private void throwError(String s) {

        viewPager.setVisibility(View.GONE);
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
    }


    private void buildUi(final ActivityDownloadEntity[] data) {
        // set adapter to the viewpager
        tv_error_message.setVisibility(View.GONE);
        progressbar.setVisibility(View.GONE);
        viewPager.setVisibility(View.VISIBLE);
        viewPager.setPageMargin(getResources().getDimensionPixelOffset(R.dimen.viewpager_margin));
        final int pageCount = data.length;
        viewPager.setOffscreenPageLimit(pageCount/2 + 1);

        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                //if(position%2==0)
                //    return new RecyclerViewFragment();
                //else
                return ScrollViewFragment.newInstance(data[position]);
            }

            @Override
            public int getCount() {
                return pageCount;
            }
        });

        // get the actual being dragged items, and progressively change their margin.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        pullToRefreshHorizontalScrollView.setInnerViewpager(viewPager);

    }


    private void hideFragments(FragmentTransaction transaction) {
        if (error_fragment != null)
            transaction.hide(error_fragment);
    }

    private void addFakeElement(RecyclerView mRecyclerView) {

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        List<UploadActivityEntity> myDataset = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            myDataset.add(new UploadActivityEntity());
        YunActivitiesListAdapter mAdapter = new YunActivitiesListAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    // error fragment

    /*a content fragment that get inflated on the basis of a link.
     - he manages himself the inneradapter.
    * */
    public static class ContentFragment extends Fragment {


        @Bind(R.id.swipecontainer)
        SwipeRefreshLayout swipeRefreshLayout;

        @Bind(R.id.my_recycler_view)
        RecyclerView mRecyclerView;


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
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light,
                    android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);
            // add one element.
//        for (int i = 0; i < 3; i++)
//            addFakeElement(mRecyclerView);
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
//        reload();
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
//                         mRecyclerView.setVisibility(View.GONE);
                        tv_error_message.setText("出现异常");
                    }
                    break;
                case -2:
                    if (mAdapter != null && mAdapter.getItemCount() > 0) {
//                         mRecyclerView.setVisibility(View.GONE);
                        tv_error_message.setText("数据结构识别出现异常");
                    }
                    break;
            }
        }


        private void modUpView(List<UploadActivityEntity> lActivity) {

            mRecyclerView.setHasFixedSize(true);
            // use a linear layout manager
            // specify an adapter (see also next example)
//             mRecyclerView.setVisibility(View.VISIBLE);
//             rel_error.setVisibility(View.GONE);
            if (lActivity != null && lActivity.size() > 0) {
                mAdapter = new YunActivitiesListAdapter(lActivity);
                mRecyclerView.setAdapter(mAdapter);
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


}
