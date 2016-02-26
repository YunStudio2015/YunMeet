package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.HallActMainAdapter;
import yunstudio2015.android.yunmeet.adapterz.MainVpAdapter;
import yunstudio2015.android.yunmeet.customviewz.NoPaggingViewPager;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;
import yunstudio2015.android.yunmeet.fragments.ActivitiesMainFragment;
import yunstudio2015.android.yunmeet.fragments.ChatMainFragment;
import yunstudio2015.android.yunmeet.fragments.DiscoverMainFragment;
import yunstudio2015.android.yunmeet.fragments.MyFriendsMainFragment;
import yunstudio2015.android.yunmeet.fragments.MySpaceMainFragment;


public class HallActivity extends AppCompatActivity implements DiscoverMainFragment.OnFragmentInteractionListener,
        ActivitiesMainFragment.OnFragmentInteractionListener,
        ChatMainFragment.OnFragmentInteractionListener,
        MyFriendsMainFragment.OnFragmentInteractionListener,
        MySpaceMainFragment.OnFragmentInteractionListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.stl_menu)
    SlidingTabLayout tabs;

    @Bind({R.id.radiobutton_bottom_menu_1, R.id.radiobutton_bottom_menu_2, R.id.radiobutton_bottom_menu_3, R.id.radiobutton_bottom_menu_4})
    List<RadioButton> radiobuttonz;

    @Bind(R.id.radiogroup_bottom_menu)
    RadioGroup radiogroup_bottom_menu;

    @Bind(R.id.hallmain_vp)
    NoPaggingViewPager hallmain_viewpager;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
    }


    /* previously checked button id */
    int previouslyCheckButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // setup main viewpager
        hallmain_viewpager.setOffscreenPageLimit(3);
        hallmain_viewpager.setAdapter(new HallActMainAdapter(getSupportFragmentManager()));

        // 不允许拖动animation
        hallmain_viewpager.setSwipeLocked(true);
        hallmain_viewpager.setScrollDurationFactor(0);

        // setup bottom navigation panel

        // previously checked button id
        previouslyCheckButtonId = R.id.radiobutton_bottom_menu_1;

        radiogroup_bottom_menu.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton radioButton1 = (RadioButton) group.findViewById(previouslyCheckButtonId);
                RadioButton radioButton2 = (RadioButton) group.findViewById(checkedId);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    radioButton1.setTextColor(getResources().getColor(R.color.bottom_text_black, getTheme()));
                    radioButton2.setTextColor(getResources().getColor(R.color.btn_background, getTheme()));
                } else {
                    radioButton1.setTextColor(getResources().getColor(R.color.bottom_text_black));
                    radioButton2.setTextColor(getResources().getColor(R.color.btn_background));
                }

                previouslyCheckButtonId = checkedId;
//                Snackbar.make(group, ""+ radiobuttonz.indexOf(radioButton2) , Snackbar.LENGTH_SHORT).show();

                 /* changing the fragment. */
                hallmain_viewpager.setCurrentItem(radiobuttonz.indexOf(radioButton2));
                if (checkedId == R.id.radiobutton_bottom_menu_1) {
                    // show the strip inside the action bar
                    tabs.setVisibility(View.VISIBLE);
                } else {
                    // else hide it.
                    tabs.setVisibility(View.GONE);
                }
            }
        });

        radiogroup_bottom_menu.check(R.id.radiobutton_bottom_menu_1);
    }

    private void populateUpperTabStrip() {

        // set up title to null
        toolbar.setTitle("");
     /*   tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.btn_background);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);*/
    }

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
            return;
        setTranslucentStatus(activity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
