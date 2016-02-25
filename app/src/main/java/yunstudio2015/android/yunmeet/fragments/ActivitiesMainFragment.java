package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.os.Bundle;
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

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.HallActivity;
import yunstudio2015.android.yunmeet.entityz.ActivityCategoryEntity;


public class ActivitiesMainFragment extends Fragment {


    private OnTopCategoryMenuClickListener tab_category_menu_listener;

    public ActivitiesMainFragment() {
        // Required empty public constructor
    }


    public static ActivitiesMainFragment getInstance() {
        ActivitiesMainFragment fragment = new ActivitiesMainFragment();
        return fragment;
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
        setFragment(rootview.getContext(), 0);
        return rootview;
    }

    private void setFragment(Context context, int id) {

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
            transaction.add(R.id.fl_activitites_main, activitiesFragment);
        }
        transaction.commit();
    }

    private void hideFragments(FragmentTransaction transaction) {
        // go arround the map and hide all the fragment
        for (String key : frgz.keySet()) {
            transaction.hide((Fragment) frgz.get(key));
        }
    }

    private void setUpCategoriesHv(Context mctx) {

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


        // we just create items and add them with a predefined width

//        TtDebug("sw " + getScreenWidth(this) + " hscw " + hsc_categories.getWidth());
        int usewidth = getScreenWidth(mctx);

        hsc_lny.removeAllViews();

        for (int i = 0; i <default_categories.length; i++) {

            TextView tv_tmp = new TextView(mctx);
            tv_tmp.setText(default_categories[i]);
            tv_tmp.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.submenu_textsize));
            tv_tmp.setTextColor(getResources().getColor(R.color.actionbar_color));
            tv_tmp.setTag(R.id.category_id, i);
            tv_tmp.setTag(R.id.category_position_id, i);
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
            if (((int)view.getTag(R.id.category_position_id)) != previous_selected) {
                ((TextView) hsc_lny.getChildAt(previous_selected)).setTextColor(getResources().getColor(R.color.actionbar_color));
                ((TextView) view).setTextColor(getResources().getColor(R.color.btn_background));
                previous_selected = (int) view.getTag(R.id.category_position_id);
                setFragment(view.getContext(), previous_selected);
            }
        }
    }


}
