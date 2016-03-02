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
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.YunActivitiesListAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class FreeListFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private DisplayMetrics metrics;
    public static Context context;
    private static FragmentManager fragmentManager;
    private static ContentFragment.ErrorFragment error_fragment;
    private static ContentFragment content_fragment;


    public static Fragment getInstance() {

        Fragment frg = new FreeListFragment();
        return frg;
    }

    public FreeListFragment() {
        // Required empty public constructor zzz
    }


    View rootview;

    @Bind(R.id.framecontroller_f)
    FrameLayout frameLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.frg_controller_f, container, false);
        context = rootview.getContext();
        ButterKnife.bind(this, rootview);
        // get transaction manager and make the change.
        retry ();
        return rootview;
    }

    public void retry() {
        fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);
        if (content_fragment == null) {
            content_fragment = (ContentFragment) ContentFragment.getInstance();
            transaction.add(R.id.framecontroller_f, content_fragment);
        }
        else {
            transaction.show(content_fragment);
        }
        transaction.commit();

    }

    private static void hideFragments(FragmentTransaction transaction) {
        if (error_fragment != null)
            transaction.hide(error_fragment);
        if (content_fragment != null) {
            transaction.hide(content_fragment);
        }
    }

    private static void addFakeElement(RecyclerView mRecyclerView) {

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        List<ActivityEntity> myDataset = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            myDataset.add(new ActivityEntity());
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


        private LinearLayoutManager mLayoutManager;
        private List<ActivityEntity> lActivity;
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
//            for (int i = 0; i < 3; i++)
//                addFakeElement(mRecyclerView);
            mLayoutManager = new LinearLayoutManager(context);
            mRecyclerView.setLayoutManager(mLayoutManager);
//           reload();
            return rootview2;
        }

   /*     private void reload() {

            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                }
            });
            // reload with the link.
            L.d(YunApi.ACTIVITYZ_LIST);
            VolleyRequest.GetStringRequest(context, YunApi.ACTIVITYZ_LIST, "", new VolleyOnResultListener() {
                @Override
                public void onSuccess(String resp) {

                    // it easier to get a boolean who tells whether there is a change inside
                    // the db than to retrieve all the elements and then compare with the actual one.

                    // if there is an update {get if it is a modification, or a delete, or an adding}
                    // if it is a modification, get the modified stuffs.
                    // if it is a delete get an array of the delete stuffs.
                    // if it is an adding, get the last added data according to the last item key-
                    // -that was retrieve before.
                    // convert the json object
                    try {
                        JSONObject response = new JSONObject(resp);
                        L.v(response.toString());
                        mT(response.toString());
                        // new data
//                    if (response.get("error") == 0) {
                    *//*List<ActivityEntity>*//*
                        lActivity = (List<ActivityEntity>) response.get("data");
                        modUpView(lActivity);
                    } catch (Exception e) {
                        throwError(-2);
                    }
                    mT("success");
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
                    mT("error");
                    swipeRefreshLayout.setRefreshing(false);
                }
            });
        }*/


        private void mT(String string) {

            Toast.makeText(getActivity().getApplicationContext(), string, Toast.LENGTH_SHORT).show();
        }


        private void modUpView(List<ActivityEntity> lActivity) {

            mRecyclerView.setHasFixedSize(true);
            if (lActivity != null && lActivity.size() > 0) {
                mAdapter = new YunActivitiesListAdapter(lActivity);
                mRecyclerView.setAdapter(mAdapter);
            } else {
                mT("no result to show");
            }
        }

        private void throwError(int errodid) {

            // change the fragment adding the message in the middle.
            switch (errodid) {
                case -1:
                    if (mAdapter == null || mAdapter.getItemCount() > 0) {
//                         mRecyclerView.setVisibility(View.GONE);

                        fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        hideFragments(transaction);
                        if (error_fragment == null) {
                            error_fragment = (ErrorFragment) ErrorFragment.getInstance();
                            transaction.add(R.id.framecontroller_f, error_fragment);
                        }
                        else {
                            transaction.show(error_fragment);
                        }
                        transaction.commit();
                        error_fragment.setErrorMess("出现异常");
                    } else {
                        mT("出现异常");
                    }
                    break;
                case -2:
                    if (mAdapter != null && mAdapter.getItemCount() > 0) {
//                         mRecyclerView.setVisibility(View.GONE);

                        fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        hideFragments(transaction);
                        if (error_fragment == null) {
                            error_fragment = (ErrorFragment) ErrorFragment.getInstance();
                            transaction.add(R.id.framecontroller_f, error_fragment);
                        }
                        else {
                            transaction.show(error_fragment);
                        }
                        transaction.commit();
                        error_fragment.setErrorMess("数据结构识别出现异常");
                    } else {
                        mT("数据结构识别出现异常");
                    }
                    break;
            }
        }

        public static class ErrorFragment extends Fragment {


            @Bind(R.id.error_message)
            TextView tv_error_message;

            @Bind(R.id.bt_error_try)
            Button bt_error_try;

            public static Fragment getInstance() {

                Fragment frg = new ErrorFragment();
                return frg;
            }

            @Nullable
            @Override
            public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
                View rootview3 = inflater.inflate(R.layout.frg_error_fragment, container, false);
                ButterKnife.bind(ErrorFragment.this, rootview3);
                return rootview3;
            }

            public void setErrorMess (String mess) {
                if (tv_error_message != null)
                    tv_error_message.setText(mess);
            }

            @OnClick(R.id.bt_error_try)
            public void trytry () {
                fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
//		transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
                // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
                hideFragments(transaction);
                if (content_fragment == null) {
                    content_fragment = (ContentFragment) ContentFragment.getInstance();
                    transaction.add(R.id.framecontroller_f, content_fragment);
                }
                else {
                    transaction.show(content_fragment);
                }
                transaction.commit();
//                content_fragment.reload();
            }
        }



    }

}