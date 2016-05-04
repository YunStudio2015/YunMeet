package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.YunActivitiesListAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.UploadActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class ConcernListFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private DisplayMetrics metrics;
    public static Context context;
    private FragmentManager fragmentManager;
    private ErrorFragment error_fragment;


    public static Fragment getInstance() {

        Fragment frg = new ConcernListFragment();
        return frg;
    }

    public ConcernListFragment() {
        // Required empty public constructor
    }


    View rootview;

    @Bind(R.id.framecontroller_c)
    FrameLayout frameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.frg_controller_c, container, false);
        context = rootview.getContext();
        ButterKnife.bind(this, rootview);
        // get transaction manager and make the change.
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
          /*  if (homeFragment == null) {
                homeFragment = new HomeFragment();
                transaction.add(R.id.content_frame, homeFragment);
            } else
                transaction.show(homeFragment);*/
        if (error_fragment == null) {
            error_fragment = (ErrorFragment) ErrorFragment.getInstance();
            transaction.add(R.id.framecontroller_c, error_fragment);
            L.v("adding 1");
        }
        else {
            transaction.show(error_fragment);
            L.v("showing 1");
        }
        transaction.commit();
        return rootview;
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

  /*  @Bind(R.id.lny_container)
    LinearLayout lny_container;*/

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
                    *//*List<UploadActivityEntity>*//*
                        JSONObject resp = new JSONObject(response);
                        lActivity = (List<UploadActivityEntity>) resp.get("data");
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


        private void mT(String string) {

            Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
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