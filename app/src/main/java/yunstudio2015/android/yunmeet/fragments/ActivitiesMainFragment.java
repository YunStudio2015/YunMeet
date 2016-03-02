package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ActivityCategoryEntity;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;


public class ActivitiesMainFragment extends Fragment {


    private OnFragmentInteractionListener mListener;

    private OnTopCategoryMenuClickListener tab_category_menu_listener;

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


    public Map<String, Fragment> frgz;

    @Bind(R.id.fl_activitites_main)
    FrameLayout frameLayout;

    @Bind(R.id.lny_sc)
    LinearLayout hsc_lny;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_activities_main, container, false);
        ButterKnife.bind(this, rootview);

        //
        frgz = new HashMap<>();

        //
        tab_category_menu_listener = new OnTopCategoryMenuClickListener();
        setUpCategoriesHv(rootview.getContext());

        // get the list of activities from the db, and get the first with the id 0

        // set up a standard fragment.
        setFragment(rootview.getContext(), AppConstants.DEFAULT_BASE_ACTIVITIES_API.RECOMMEND);
        return rootview;
    }

    private void setFragment(Context context, String id) {

        ActivityCategoryEntity entity = new ActivityCategoryEntity(context, id);

        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.abc_fade_in, R.anim.abc_fade_out);
        // 先隐藏掉所有的Fragment，以防止有多个Fragment显示在界面上的情况
        hideFragments(transaction);

        ActivitiesFragment activitiesFragment = null;
        if (frgz.containsKey(entity.name)) {
            activitiesFragment = (ActivitiesFragment) frgz.get(entity.name);
            transaction.show(activitiesFragment);
        }
        else {

            activitiesFragment = (ActivitiesFragment) ActivitiesFragment.getInstance(entity.base_api);
            frgz.put(entity.name, activitiesFragment);
            transaction.add(R.id.fl_activitites_main, activitiesFragment, entity.name);
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        // go arround the map and hide all the fragment
        for (String key : frgz.keySet()) {
            transaction.hide((Fragment) frgz.get(key));
        }
    }


    int i = 0;
    private void setUpCategoriesHv(final Context mctx) {

        // standard categories, and other categories could be got the db
        // we doing this so that in case of no connection, there is
        // still some categories to show if we want to subdivise
        // or get a way to actualize the activity local database everytime it got
        // some changes.

        // get the default categories list inside the database, if it doesnt exist,
        // then change it inside this file

//        listener

        // 从本低那获取默认的类型
        String[] default_categories = getResources().getStringArray(R.array.default_categories);
        String[] default_categories_id = getResources().getStringArray(R.array.default_categories_idz);
        // we just create items and add them with a predefined width

        final int usewidth = getScreenWidth(mctx);

        hsc_lny.removeAllViews();


         /* 添加固定分类 、推荐、关注、热度 */
        for (i = 0; i < default_categories.length; i++) {
            TextView tv_tmp = new TextView(mctx);
            tv_tmp.setText(default_categories[i]);
            tv_tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.submenu_textsize));
            tv_tmp.setTextColor(getResources().getColor(R.color.actionbar_color));
            tv_tmp.setTag(R.id.category_position, i);
            tv_tmp.setTag(R.id.category_id, default_categories_id[i]);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.width = usewidth/6;
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tv_tmp.setLayoutParams(params);
            tv_tmp.setOnClickListener(tab_category_menu_listener);
            if (i == 0)
                params.leftMargin =  getResources().getDimensionPixelSize(R.dimen.hsc_left_margin);
            else if (i == default_categories.length -1)
                params.rightMargin = getResources().getDimensionPixelSize(R.dimen.hsc_right_margin);
            hsc_lny.addView(tv_tmp);
            // give to all of them the same listener and make
            // do his job.
            if (i == 0)
                tv_tmp.setTextColor(getResources().getColor(R.color.btn_background));
            // each child will keep an id for himself
        }

        /*调用接口去获取*/
        VolleyRequest.GetStringRequest(getContext(), YunApi.CATEGORYZ, "", new VolleyOnResultListener() {

            @Override
            public void onSuccess(String response) {
                L.i(response);
                try {
                    Gson gson = new Gson();
                    JsonElement resp = gson.fromJson(response, JsonElement.class);
                    int error = resp.getAsJsonObject().get("error").getAsInt();
                    if (error == 0) {
                        final ActivityCategoryEntity[] activityCategoryEntities = gson.fromJson(resp.getAsJsonObject().get("data"), ActivityCategoryEntity[].class);
                        // 获取成功
                        ((HallActivity) getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                for (int ii = 0; ii < activityCategoryEntities.length; ii++) {
                                    ActivityCategoryEntity categoryEntity = activityCategoryEntities[ii];
                                    L.i("adding " + categoryEntity.name);
                                    final TextView tv_tmp = new TextView(getContext());
                                    tv_tmp.setText(categoryEntity.name);
                                    tv_tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.submenu_textsize));
                                    tv_tmp.setTextColor(getResources().getColor(R.color.actionbar_color));
                                    tv_tmp.setTag(R.id.category_id, categoryEntity.id);
                                    tv_tmp.setTag(R.id.category_position, (i+ii));
                                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                    params.width = usewidth/6;
                                    params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                    tv_tmp.setLayoutParams(params);
                                    tv_tmp.setOnClickListener(tab_category_menu_listener);
                                    tv_tmp.setVisibility(View.GONE);
                                    if (ii == activityCategoryEntities.length -1)
                                        params.rightMargin = getResources().getDimensionPixelSize(R.dimen.hsc_right_margin);
                                    hsc_lny.addView(tv_tmp);
                                    tv_tmp.postDelayed(new Runnable() {
                                                           @Override
                                                           public void run() {
                                                               ((HallActivity) getContext()).runOnUiThread(new Runnable() {
                                                                   @Override
                                                                   public void run() {
                                                                       tv_tmp.setVisibility(View.VISIBLE);
                                                                       // run an apparition activity.
                                                                   }
                                                               });
                                                           }},1000
                                    );

                                }
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String error) {
                L.e(error);
            }

        });
    }

    // utils functions
    public static int getScreenWidth (Context context) {
        DisplayMetrics metrics = new DisplayMetrics();
        ((HallActivity) context).getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }


    // inner classes
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
                setFragment(view.getContext(), category_tag);
                mToaz("selected id -- " + view.getTag(R.id.category_id));
            }
        }
    }

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

}
