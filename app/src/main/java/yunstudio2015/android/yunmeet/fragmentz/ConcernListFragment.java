package yunstudio2015.android.yunmeet.fragmentz;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.ImageLoadOptions;


public class ConcernListFragment extends Fragment {

    @Bind(R.id.swipecontainer)
    SwipeRefreshLayout swipeRefreshLayout;

    @Bind(R.id.lny_container)
    LinearLayout lny_container;


    private OnFragmentInteractionListener mListener;
    private DisplayMetrics metrics;
    private Context context;


    public static Fragment getInstance () {

        Fragment frg = new ConcernListFragment();
        return frg;
    }

    public ConcernListFragment() {
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
        addFakeElement(inflater, lny_container);
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



    private void addFakeElement(LayoutInflater inflater, LinearLayout lny_container) {

        View view = inflater.inflate(R.layout.activity_item_xml, lny_container, false);
        RelativeLayout relative = ButterKnife.findById(view, R.id.relative);
        lny_container.addView(view);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_activity_bg);
//        Bitmap bm = ((BitmapDrawable) iv.getDrawable()).getBitmap();
//        iv.setImageDrawable(new RoundedDrawable(bm, bm.getScaledWidth(metrics)));
        // set up the width of the iv
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//        layoutParams.height = layoutParams.width;
        iv.setLayoutParams(layoutParams);
        String link = "http://www.csw333.com/upload_files/qibosoft_news_/135/83555_20141120091105_8uctj.jpg";//"http://p5.img.cctvpic.com/nettv/newgame/2011/1118/20111118105936548.jpg";
        ImageLoader.getInstance().displayImage(link, iv, ImageLoadOptions.getDisplaySlightlyRoundedImageOptions(getContext()));
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
