package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.HallActMainAdapter;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.NoPaggingViewPager;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;
import yunstudio2015.android.yunmeet.fragments.ActivitiesFragment;
import yunstudio2015.android.yunmeet.fragments.ActivitiesMainFragment;
import yunstudio2015.android.yunmeet.fragments.ChatMainFragment;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsItemFragment;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsMainFragment;
import yunstudio2015.android.yunmeet.fragments.DiscoverMainFragment;
import yunstudio2015.android.yunmeet.fragments.MyFriendsMainFragment;
import yunstudio2015.android.yunmeet.fragments.MySpaceMainFragment;
import yunstudio2015.android.yunmeet.fragments.ShowPicturesFragment;


public class HallActivity extends AppCompatActivity implements
        DiscoverMainFragment.OnFragmentInteractionListener,
        ActivitiesMainFragment.OnFragmentInteractionListener,
        ChatMainFragment.OnFragmentInteractionListener,
        ActivitiesFragment.OnFragmentInteractionListener,
        MyFriendsMainFragment.OnFragmentInteractionListener,
        MySpaceMainFragment.OnFragmentInteractionListener,
        ChatTopicsMainFragment.OnFragmentInteractionListener,
        ChatTopicsItemFragment.OnFragmentInteractionListener,
        ShowPicturesFragment.OnFragmentInteractionListener{

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

    @Bind(R.id.fab1)
    FloatingActionButton floatingActionButton;

    @Bind(R.id.rel_popup_menu)
    RelativeLayout rel_popup_menu;

    @Bind(R.id.tv_title)
    TextView tv_title;

    @Bind(R.id.picture_shower)
    View picture_shower;

    private ShowPicturesFragment showpictureFragment;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        moveTaskToBack(true);
    }

    private PopupWindow mPopupWindow;

    View bottom_launcher;

    /* previously checked button id */
    int previouslyCheckButtonId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        ButterKnife.bind(this);

        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        setSupportActionBar(toolbar);

        // setup main viewpager
//        hallmain_viewpager.setOffscreenPageLimit(3);
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
                    tv_title.setVisibility(View.GONE);
                    tabs.setVisibility(View.VISIBLE);
                } else {
                    //  hide it.
                    tabs.setVisibility(View.GONE);
                    if (checkedId == R.id.radiobutton_bottom_menu_4) {
                        // set wode visible.
                        tv_title.setText(getResources().getString(R.string.myspace));
                        tv_title.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        radiogroup_bottom_menu.check(R.id.radiobutton_bottom_menu_1);

        // manage popup launch menu
        View popupView = getLayoutInflater().inflate(R.layout.launch_button_poplayout, null);


        mPopupWindow = new PopupWindow(popupView, 7*getScreenWidth()/10, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopupWindow.setTouchable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));

        View leftbutton = mPopupWindow.getContentView().findViewById(R.id.leftbutton);
        View rightbutton = mPopupWindow.getContentView().findViewById(R.id.rightbutton);

        leftbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Snackbar.make(v, "left button" , Snackbar.LENGTH_SHORT).show();
                if (mPopupWindow != null)
                    mPopupWindow.dismiss();
                Intent intent = new Intent(HallActivity.this, LaunchChatTopicActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.noanim);
            }
        });

        rightbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(v, "right button" , Snackbar.LENGTH_SHORT).show();
                if (mPopupWindow != null)
                    mPopupWindow.dismiss();
                Intent intent = new Intent(HallActivity.this, LaunchActivityActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.noanim);
            }
        });


        floatingActionButton.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                floatingActionButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                mPopupWindow.showAsDropDown(v, getScreenWidth()/2 - mPopupWindow.getWidth()/2, -40);

                /* get the y equivalent to the position of the center of the floating button*/
                        int flt_btHeight;
                        int flt_bt_margin_bottom = 0;

                        if (getScreenWidth() /2 >= 600) {
                            flt_btHeight = getResources().getDimensionPixelSize(R.dimen.flt_launch_button_7_pouces);
                            flt_bt_margin_bottom = getResources().getDimensionPixelSize(R.dimen.launch_button_margin_bottom_7_pouces);

                        }else {
                            flt_btHeight = getResources().getDimensionPixelSize(R.dimen.flt_launch_button);
                            flt_bt_margin_bottom = getResources().getDimensionPixelSize(R.dimen.launch_button_margin_bottom);
                        }



                        TypedValue tv = new TypedValue();
                        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                        {
                            int actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
                            L.d("computed one "+ (flt_btHeight/2 + flt_bt_margin_bottom));
                            L.d("gotttt one "+ (getScreenHeight() - actionBarHeight - floatingActionButton.getY()));
/*
                            L.d("screen width in px "+getScreenWidth());
                            L.d("screen width in dp "+ UtilsFunctions.convertPxtoDip(getScreenWidth(), HallActivity.this));
                      */
                        }

                        mPopupWindow.showAtLocation(v, Gravity.BOTTOM,
                                0, /*(int) floatingActionButton.getY()*/ 4*flt_btHeight/3 + flt_bt_margin_bottom);
                        rel_popup_menu.setVisibility(View.VISIBLE);
                    }
                });

                mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        rel_popup_menu.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

    }

    private int getScreenWidth() {
        // utils functions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    private int getScreenHeight() {
        // utils functions
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return metrics.heightPixels;
    }


   /* private void populateUpperTabStrip() {

        // set up title to null
        toolbar.setTitle("");
     *//*   tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true,
        // This makes the tabs Space Evenly in Available width
        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.btn_background);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(viewPager);*//*
    }*/


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

        // if uri means that we should hide the fragment, then we hide it.
        showpictureFragment = (ShowPicturesFragment) getSupportFragmentManager().findFragmentById(R.id.picture_shower);

        /* so it is an image, then, what we should do is show it... then try to show the bigger picture */

        if (uri.getScheme().equals(AppConstants.scheme_photo)) {

            /* show the small picture and load for the bigger */
        }

        if (uri.getScheme().equals(AppConstants.scheme_ui)) {
            View frame = showpictureFragment.getView().findViewById(R.id.frame);
            if (frame == null) {
                Toast.makeText(this, "frame is null", Toast.LENGTH_SHORT).show();
                return;
            }
            if (frame.getVisibility() == View.VISIBLE) {
                frame.setVisibility(View.GONE);
                // hide the fragment
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.hide(showpictureFragment);
                trans.commit();
            } else {
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.show(showpictureFragment);
                trans.commit();
                frame.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(android.R.anim.slide_in_left,R.anim.noanim);
    }
}
