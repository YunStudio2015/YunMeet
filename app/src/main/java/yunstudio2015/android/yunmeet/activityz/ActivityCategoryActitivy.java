package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.fragments.ActivitiesFragment;

public class ActivityCategoryActitivy extends FragmentActivity implements ActivitiesFragment.OnFragmentInteractionListener{


    @Bind(R.id.frame)
    FrameLayout frame;

    public final static String BASE = "base_api";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_category_actitivy);
        ButterKnife.bind(this);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);
        String tag = getIntent().getStringExtra(BASE);
        addFragment (tag);
    }

    private void addFragment(String tag) {

        ActivitiesFragment frg =  ActivitiesFragment.getInstance(tag);
        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        trans.add(R.id.frame, frg, tag);
        trans.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri, @Nullable String[] themelinks) {

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

}
