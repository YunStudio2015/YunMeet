package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.MainVpAdapter;
import yunstudio2015.android.yunmeet.customviewz.NoPaggingViewPager;
import yunstudio2015.android.yunmeet.customviewz.SlidingTabLayout;


public class HallActivity extends AppCompatActivity {

    int pageCount = 10;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.vp)
    NoPaggingViewPager viewPager;

    @Bind(R.id.stl_menu)
    SlidingTabLayout tabs;

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        viewPager.setOffscreenPageLimit(pageCount);
        viewPager.setAdapter(new MainVpAdapter(getSupportFragmentManager ()));

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
        populateUpperTabStrip();


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
    }

    private void populateUpperTabStrip() {

        // set up title to null
        toolbar.setTitle("");
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

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT  != Build.VERSION_CODES.KITKAT)
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


}
