package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.adapterz.MainVpAdapter;
import yunstudio2015.android.yunmeet.customviewz.NoPaggingViewPager;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DiscoverMainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DiscoverMainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DiscoverMainFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    public DiscoverMainFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static DiscoverMainFragment newInstance( ) {
        DiscoverMainFragment fragment = new DiscoverMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }


    @Bind(R.id.vp)
    NoPaggingViewPager viewPager;

    int pageCount = 10;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_discover_main, container, false);
        ButterKnife.bind(this, rootview);
        viewPager.setOffscreenPageLimit(pageCount);
        viewPager.setAdapter(new MainVpAdapter(getChildFragmentManager()));

        // 不允许viewpager拖动
        viewPager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        // 不允许拖动animation
        viewPager.setSwipeLocked(true);
        viewPager.setScrollDurationFactor(0);

        // <:OPLL{:{P}
        viewPager.setCurrentItem(1);

        // populate upper tab strip
        populateUpperTabStrip(rootview.getContext());


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

        return rootview;
    }



    private void populateUpperTabStrip(Context context) {



        // set up title to null
        SlidingTabLayout tabs = (SlidingTabLayout) ((HallActivity) context).findViewById(R.id.stl_menu);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.btn_background);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);
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
