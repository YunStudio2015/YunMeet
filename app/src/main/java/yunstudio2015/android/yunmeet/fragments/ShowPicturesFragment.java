package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ShowPicturesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ShowPicturesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowPicturesFragment extends Fragment {



    private OnFragmentInteractionListener mListener;
    private String[] path = {"http://e.hiphotos.baidu.com/zhidao/pic/item/08f790529822720e68560ffe78cb0a46f21fab1e.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a870b354c87c45c10385243b5d7.jpg",
            "http://img4.duitang.com/uploads/item/201203/02/20120302210557_KSWNP.thumb.600_0.jpeg",
            "http://a.hiphotos.baidu.com/zhidao/pic/item/bd3eb13533fa828b26108612ff1f4134970a5a0b.jpg",
            "http://pica.nipic.com/2007-12-25/2007122515616115_2.jpg",
            "http://img4.imgtn.bdimg.com/it/u=20230736,313391552&fm=21&gp=0.jpg",
            "http://imgstore.cdn.sogou.com/app/a/100540002/710338.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/d788d43f8794a4c22f0aafe10cf41bd5ac6e39ca.jpg",
            "http://g.hiphotos.baidu.com/zhidao/pic/item/77c6a7efce1b9d16eb0ad811f2deb48f8d5464f4.jpg",
            "http://img3.3lian.com/2014/c2/88/d/79.jpg",
            "http://f.hiphotos.baidu.com/zhidao/pic/item/9213b07eca806538514d9b1f96dda144ad348212.jpg"};


    private ArrayList<ImageView> bottom_iv;

    public ShowPicturesFragment() {
        // Required empty public constructor
    }

    public static final String currenting = "currenting", imgarray = "imgarray";

    // TODO: Rename and change types and number of parameters
    public static ShowPicturesFragment newInstance(String imgz, String[] themelinks) {
        ShowPicturesFragment fragment = new ShowPicturesFragment();
        Bundle args = new Bundle();
        args.putString(currenting, imgz);
        args.putStringArray(imgarray, themelinks);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_pictures, container, false);
    }

    @Bind(R.id.vp_img_container)
    ViewPager vp_img_container;

    @Bind(R.id.frame)
    RelativeLayout relativeLayout;

    @Bind(R.id.lny_ic_container)
    LinearLayout lny_ic_container;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
         /* retrieve the viewpager and inflate it setting it's adapter. */
//        relativeLayout.setOnClickListener(new OnClickListener_());
        Bundle args = getArguments();
        String currentimage = args.getString(currenting);
        String[] imz = args.getStringArray(imgarray);
        vp_img_container.setOnClickListener(new OnClickListener_());
        adapter = new Adapter(getChildFragmentManager(), new String[]{});
        vp_img_container.setOffscreenPageLimit(1);
        vp_img_container.setAdapter(adapter);
        updateData(currentimage, imz);
        inflateBottomIcContainer(LayoutInflater.from(getActivity()));
        if (imz != null && imz.length > 1){
            lny_ic_container.setVisibility(View.VISIBLE);
        }else
            lny_ic_container.setVisibility(View.INVISIBLE);
    }

    private void inflateBottomIcContainer(LayoutInflater inf) {

        if (bottom_iv == null)
            bottom_iv = new ArrayList<>();
        else
            bottom_iv.clear();
        for (int i = 0; i < adapter.getCount(); i++) {
            ImageView v = (ImageView) inf.inflate(R.layout.img_indic, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            params.rightMargin = 10;
            v.setLayoutParams(params);
            bottom_iv.add(v);
            lny_ic_container.addView(v);
            if (i == vp_img_container.getCurrentItem())
                v.setImageResource(R.drawable.ic_indic_bottom_selected);
            else
                v.setImageResource(R.drawable.ic_indic_bottom_notselected);
        }
        vp_img_container.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (i == position)
                        bottom_iv.get(position).setImageResource(R.drawable.ic_indic_bottom_selected);
                    else
                        bottom_iv.get(i).setImageResource(R.drawable.ic_indic_bottom_notselected);
                }
            }
        });
    }

    private void mT(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }

    Adapter adapter = null;

    public void updateData (String link, String[] otherlinks) {
//        vp_img_container.removeAllViews();
        if (otherlinks == null)
            otherlinks = new String[]{link};
        final String[] finalOtherlinks = otherlinks;
        if (adapter == null) {
            adapter = new Adapter(getChildFragmentManager(), finalOtherlinks);
        }
        else {
            adapter.setData(finalOtherlinks);
        }
        adapter.notifyDataSetChanged();
        vp_img_container.invalidate();
        vp_img_container.setOnClickListener(new OnClickListener_());
        for (int i = 0; i < otherlinks.length; i++) {
            if (link.contentEquals(otherlinks[i])){
                vp_img_container.setCurrentItem(i);
                return;
            }
        }
    }


    private class Adapter extends android.support.v4.app.FragmentPagerAdapter {


        private String[] data;

        public Adapter(FragmentManager fm, String[] data) {
            super(fm);
            this.data = data;
        }

        Map<String, ZoomImageFragment> frg;

        @Override
        public Fragment getItem(int position) {
            if (frg == null)
                frg = new HashMap<>();
            ZoomImageFragment fragment = frg.get(""+position);
            if (fragment == null)
                fragment = ZoomImageFragment.newInstance(data[position]);
            else
                fragment.update(data[position]);
            frg.put(""+position, fragment);
            return fragment;
        }


        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        public void setData(String[] finalOtherlinks) {

            this.data = finalOtherlinks;
        }
    }


    private class OnClickListener_ implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Uri uri = Uri.parse(AppConstants.scheme_ui+"://"+AppConstants.authority);
            mListener.onFragmentInteraction(uri);
        }
    }

/*

    private void zoomImageFromThumb(final View thumbView, int imageResId) {
        // If there's an animation in progress, cancel it
        // immediately and proceed with this one.
        AnimatorSet mCurrentAnimator;
        if (mCurrentAnimator != null) {
            mCurrentAnimator.cancel();
        }

        // Load the high-resolution "zoomed-in" image.
        final ImageView expandedImageView = (ImageView) findViewById(
                R.id.expanded_image);
        expandedImageView.setImageResource(imageResId);

        // Calculate the starting and ending bounds for the zoomed-in image.
        // This step involves lots of math. Yay, math.
        final Rect startBounds = new Rect();
        final Rect finalBounds = new Rect();
        final Point globalOffset = new Point();

        // The start bounds are the global visible rectangle of the thumbnail,
        // and the final bounds are the global visible rectangle of the container
        // view. Also set the container view's offset as the origin for the
        // bounds, since that's the origin for the positioning animation
        // properties (X, Y).
        thumbView.getGlobalVisibleRect(startBounds);
        findViewById(R.id.container)
                .getGlobalVisibleRect(finalBounds, globalOffset);
        startBounds.offset(-globalOffset.x, -globalOffset.y);
        finalBounds.offset(-globalOffset.x, -globalOffset.y);

        // Adjust the start bounds to be the same aspect ratio as the final
        // bounds using the "center crop" technique. This prevents undesirable
        // stretching during the animation. Also calculate the start scaling
        // factor (the end scaling factor is always 1.0).
        float startScale;
        if ((float) finalBounds.width() / finalBounds.height()
                > (float) startBounds.width() / startBounds.height()) {
            // Extend start bounds horizontally
            startScale = (float) startBounds.height() / finalBounds.height();
            float startWidth = startScale * finalBounds.width();
            float deltaWidth = (startWidth - startBounds.width()) / 2;
            startBounds.left -= deltaWidth;
            startBounds.right += deltaWidth;
        } else {
            // Extend start bounds vertically
            startScale = (float) startBounds.width() / finalBounds.width();
            float startHeight = startScale * finalBounds.height();
            float deltaHeight = (startHeight - startBounds.height()) / 2;
            startBounds.top -= deltaHeight;
            startBounds.bottom += deltaHeight;
        }

        // Hide the thumbnail and show the zoomed-in view. When the animation
        // begins, it will position the zoomed-in view in the place of the
        // thumbnail.
        thumbView.setAlpha(0f);
        expandedImageView.setVisibility(View.VISIBLE);

        // Set the pivot point for SCALE_X and SCALE_Y transformations
        // to the top-left corner of the zoomed-in view (the default
        // is the center of the view).
        expandedImageView.setPivotX(0f);
        expandedImageView.setPivotY(0f);

        // Construct and run the parallel animation of the four translation and
        // scale properties (X, Y, SCALE_X, and SCALE_Y).
        AnimatorSet set = new AnimatorSet();
        set
                .play(ObjectAnimator.ofFloat(expandedImageView, View.X,
                        startBounds.left, finalBounds.left))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.Y,
                        startBounds.top, finalBounds.top))
                .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X,
                        startScale, 1f)).with(ObjectAnimator.ofFloat(expandedImageView,
                View.SCALE_Y, startScale, 1f));
        set.setDuration(mShortAnimationDuration);
        set.setInterpolator(new DecelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mCurrentAnimator = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mCurrentAnimator = null;
            }
        });
        set.start();
        mCurrentAnimator = set;

        // Upon clicking the zoomed-in image, it should zoom back down
        // to the original bounds and show the thumbnail instead of
        // the expanded image.
        final float startScaleFinal = startScale;
        expandedImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mCurrentAnimator != null) {
                    mCurrentAnimator.cancel();
                }

                // Animate the four positioning/sizing properties in parallel,
                // back to their original values.
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator
                        .ofFloat(expandedImageView, View.X, startBounds.left))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.Y,startBounds.top))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_X, startScaleFinal))
                        .with(ObjectAnimator
                                .ofFloat(expandedImageView,
                                        View.SCALE_Y, startScaleFinal));
                set.setDuration(mShortAnimationDuration);
                set.setInterpolator(new DecelerateInterpolator());
                set.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        thumbView.setAlpha(1f);
                        expandedImageView.setVisibility(View.GONE);
                        mCurrentAnimator = null;
                    }
                });
                set.start();
                mCurrentAnimator = set;
            }
        });
    }
*/


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
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}