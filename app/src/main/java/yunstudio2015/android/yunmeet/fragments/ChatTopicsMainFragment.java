package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;


public class ChatTopicsMainFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Context context;
    private ChatTopicsFragmentAdapter viewpagerAdapter;


    public ChatTopicsMainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    public static ChatTopicsMainFragment getInstance() {
        ChatTopicsMainFragment fragment = new ChatTopicsMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true); // retain instance cant  be called on nested fragments
    }

    @Bind(R.id.vp)
    ViewPager viewpager;

    @Bind(R.id.stl_menu)
    SlidingTabLayout tabs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_chat_topics, container, false);
        ButterKnife.bind(this, rootview);
        context = rootview.getContext();
        viewpagerAdapter = new ChatTopicsFragmentAdapter(getChildFragmentManager(),
                context.getResources().getStringArray(R.array.default_chat_topics_names));
        viewpager.setAdapter(viewpagerAdapter);
        populateUpperTabStrip();
        return rootview;
    }


    private void populateUpperTabStrip() {

        // set up title to null
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.transparent);
            }
        });
        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewpager);
        tabs.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

                                         @Override
                                         public void onPageSelected(int position) {
                                             super.onPageSelected(position);
                                             for (int i = 0; i < tabs.getStripTextviewsList().size(); i++) {

                                                 if (position == i) {
                                                     mToaz("page selected === color to btn_background ... " + position);
                                                     tabs.getStripTextviewsList().get(position).setTextColor(getResources().getColor(R.color.btn_background));
                                                 } else {
                                                     mToaz("page selected  === color to gray ...  " + i);
                                                     tabs.getStripTextviewsList().get(i).setTextColor(getResources().getColor(R.color.gray));
                                                 }
                                             }
                                         }
                                     }
        );
        for (int i = 0; i < tabs.getStripTextviewsList().size(); i++) {

            tabs.getStripTextviewsList().get(i).setTextColor(getResources().getColor(i == 0 ? R.color.btn_background : R.color.gray));
        }
        viewpager.setCurrentItem(0);
        setisLoading(0);
    }

    // utils functions
    public static int getScreenWidth (Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((HallActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    public void setisLoading (int i) {
        TextView tv = tabs.getStripTextviewsList().get(i);
//        tv.setCompoundDrawables(getResources().getDrawable(R.drawable.progress_medium_white),null,null,null);
    }


    private void mToaz(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
    }

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
        void onFragmentInteraction(Uri uri, @Nullable String[] themelinks);
    }

    private class ChatTopicsFragmentAdapter extends android.support.v4.app.FragmentPagerAdapter {

        private final String[] titles;

        public ChatTopicsFragmentAdapter(FragmentManager manager, String[] stringArray) {
            super(manager);
            this.titles = stringArray;
        }

        private Map<String, Fragment> frg;

        @Override
        public Fragment getItem(int position) {
            if (frg == null)
                frg = new HashMap<>();
            if (frg.get("n"+position) == null)
                frg.put("n"+position, ChatTopicsItemFragment.newInstance(position));
            return frg.get("n"+position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return titles.length;
        }
    }
}
