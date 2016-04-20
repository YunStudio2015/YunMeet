package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.lsjwzh.widget.recyclerviewpager.RecyclerViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.ActivityCategoryActitivy;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ActivityCategoryEntity;
import yunstudio2015.android.yunmeet.entityz.ActivityDownloadEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class ActivitiesMainFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Context context;
    private Drawable placeholder;

//    private OnTopCategoryMenuClickListener tab_category_menu_listener;

    public ActivitiesMainFragment() {
        // Required empty public constructor
    }


    public static ActivitiesMainFragment getInstance() {
        ActivitiesMainFragment fragment = new ActivitiesMainFragment();
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setRetainInstance(true);
    }


//    public Map<String, Fragment> frgz;


    @Bind(R.id.lny_recommended)
    LinearLayout lny_recommended;

    @Bind(R.id.lny_hot)
    LinearLayout lny_hot;

    @Bind(R.id.lny_new)
    LinearLayout lny_new;

    @Bind({R.id.lny_1,R.id.lny_2,R.id.lny_3,R.id.lny_4,R.id.lny_5,R.id.lny_6,R.id.lny_7,R.id.lny_8,R.id.lny_9,R.id.lny_10})
    List<LinearLayout> lny_z;

    String[] cat = new String[]{"re", "1", "2", "3", "4", "ho", "5", "6","1", "2"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_activities_main, container, false);
        ButterKnife.bind(this, rootview);
        context = rootview.getContext();
        placeholder = context.getResources().getDrawable(R.drawable.wait_for_load);
        inflateRecommendedActivities();
        inflateHotActivities();
        inflateNewActivities();
        for (int i = 0; i < lny_z.size(); i++) {
            lny_z.get(i).setTag(cat[i]);
            lny_z.get(i).setOnClickListener(new MoveToActivityzOnClickListener());
        }
        return rootview;
    }

    private void inflateHotActivities() {

        loadData("ho", lny_hot);
    }

    private void inflateNewActivities() {
        loadData("re", lny_new);
    }

    private void inflateRecommendedActivities() {
        loadData("re", lny_recommended);
    }

    private void throwError(String s) {
    }

    private void loadData (final String base_api, final LinearLayout recycler)  {
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
                    buildRecyclerview(recycler, data);
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


    public void buildRecyclerview(LinearLayout recyclerViewPager, ActivityDownloadEntity[] data) {

        LayoutInflater inf = LayoutInflater.from(context);
        HorizontalScrollView.LayoutParams pz = (HorizontalScrollView.LayoutParams) recyclerViewPager.getLayoutParams();
        int i = 0;
        for (ActivityDownloadEntity item: data
                ) {
            if (i > 7)
                break;
            i++;
            // inflate a new element and add it to the lny
            View view = inf.inflate(R.layout.mini_activity_item_xml, recyclerViewPager, false);
            MiniItemsViewHolder vh = new MiniItemsViewHolder(view);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, pz.height);
            params.leftMargin = 20;
            view.setLayoutParams(params);
            recyclerViewPager.addView(view);
            Glide.with(context)
                    .load(item.face)
                    .centerCrop()
                    .placeholder(placeholder)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(vh.iv_user);
            if (item.image != null && item.image.length >0)
                Glide.with(context)
                        .load(item.image[0])
                        .centerCrop()
                        .placeholder(placeholder)
                        .error(me.crosswall.photo.pick.R.drawable.default_error)
                        .into(vh.iv_bg);
            vh.tv_name.setText(item.theme);
            L.d("face img is "+item.face);
            if (item.image != null && item.image.length >0)
                L.d("bg img is "+item.image[0]);
        }
    }

    class MiniItemsViewHolder {

        @Bind(R.id.iv_activity_bg)
        public ImageView iv_bg;
        @Bind(R.id.user_ic)
        public ImageView iv_user;
        @Bind(R.id.tv_username)
        public TextView tv_name;

        public MiniItemsViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }


    // utils functions
    public static int getScreenWidth (Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((HallActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }




    /*// inner classes
    class OnTopCategoryMenuClickListener implements View.OnClickListener {

        private int previous_selected = 0;
        @Override
        public void onClick(View view) {

            // change the fragment in the current viewpager page.
            // deselect the previous clicked
            // and select the clicked one.
            if (((int)view.getTag(R.id.category_position)) != previous_selected) {
                ((TextView) hsc_lny.getChildAt(previous_selected)).setTextColor(getResources().getColor(R.color.actionbar_color));
                ((TextView) view).setTextColor(getResources().getColor(R.color.btn_background));
                previous_selected = (int) view.getTag(R.id.category_position);
                String category_tag = (String) view.getTag(R.id.category_id);
                mToaz("selected id -- " + view.getTag(R.id.category_id));
                setFragment(view.getContext(), category_tag);
            }
        }
    }*/

    private void mToaz(String s) {
        Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
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

    private class MoveToActivityzOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String tag = (String) v.getTag();
            if (tag == null || "".equals(tag)) {
                tag = "re";
            }
            Intent intent = new Intent(getActivity(), ActivityCategoryActitivy.class);
            intent.putExtra(ActivityCategoryActitivy.BASE, "tag");
            startActivity(intent);
        }
    }
}
