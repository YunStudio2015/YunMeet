package yunstudio2015.android.yunmeet.fragmentz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.MyActivitizAdapter;
import yunstudio2015.android.yunmeet.entityz.ActivityEntity;


public class FreeListFragment extends Fragment {


    @Bind(R.id.swipecontainer)
    SwipeRefreshLayout swipeRefreshLayout;

  /*  @Bind(R.id.lny_container)
    LinearLayout lny_container;*/

    @Bind(R.id.my_recycler_view)
    RecyclerView mRecyclerView;


    private OnFragmentInteractionListener mListener;
    private DisplayMetrics metrics;
    private Context context;


    public static Fragment getInstance () {

        Fragment frg = new FreeListFragment();
        return frg;
    }

    public FreeListFragment() {
        // Required empty public constructor
    }

    View rootview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_activity_list, container, false);
        context = rootview.getContext();
        ButterKnife.bind(this, rootview);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                reload();
            }
        });
        // Configure the refreshing colors
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        // add one element.
        for (int i = 0; i < 3; i++)
            addFakeElement(mRecyclerView);
        return rootview;
    }

    private void reload() {
        swipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 3000);
    }



    private void addFakeElement(RecyclerView mRecyclerView) {

        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        // specify an adapter (see also next example)
        List<ActivityEntity> myDataset = new ArrayList<>();
        for (int i = 0; i < 7; i++)
            myDataset.add(new ActivityEntity());
        MyActivitizAdapter mAdapter = new MyActivitizAdapter(myDataset);
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

}
