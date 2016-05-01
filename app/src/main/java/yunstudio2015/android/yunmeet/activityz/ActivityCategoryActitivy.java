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

import com.google.gson.Gson;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.fragments.ActivitiesFragment;
import yunstudio2015.android.yunmeet.fragments.ShowPicturesFragment;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

public class ActivityCategoryActitivy extends FragmentActivity implements
        ActivitiesFragment.OnFragmentInteractionListener,
ShowPicturesFragment.OnFragmentInteractionListener{


    @Bind(R.id.frame)
    FrameLayout frame;

    public final static String BASE = "base_api";
    private ShowPicturesFragment showpictureFragment;
    private static final String FRG_SHOWPICTURE = "FRG_SHOWPICTURE";
    private Gson gson;


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
    public void onBackPressed() {
//        super.onBackPressed();
        if (showpictureFragment != null && showpictureFragment.isVisible()) {
            // hide it
            hideShowPictureFragment();
        } else {
          finish();
        }
    }

    private void hideShowPictureFragment() {

        if (showpictureFragment != null) {
//            mT ("hide n null");
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//            trans.hide(showpictureFragment);
            trans.remove(showpictureFragment);
            showpictureFragment = null;
            trans.commit();
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri, @Nullable String[] themelinks) {


        showpictureFragment  = (ShowPicturesFragment) getSupportFragmentManager().findFragmentByTag(FRG_SHOWPICTURE);

        /* so it is an image, then,   @Bind(R.id.toolbar)
    Toolbar toolbar at we should do is show it... then try to show the bigger picture */

        if (uri.getScheme().equals(AppConstants.scheme_photo)) {
            /* show the small picture and load for the bigger */
        }
        if (uri.getScheme().equals(AppConstants.scheme_ui)) {

            if (gson == null) {
                gson = new Gson();
            }
            String imgz = null;
            try {
                imgz = gson.fromJson(UtilsFunctions.decodedPath(uri.getPath()), String.class);
            } catch (Exception e) {
                L.e("isnt showing object");
            }
            //
            if (imgz != null) {
//                mT ("clicked on != null");
//                showpictureFragment.updateData(imgz, themelinks);
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                if (showpictureFragment == null) {
//                    mT ("create n  show");
                    showpictureFragment = ShowPicturesFragment.newInstance(imgz, themelinks);
                    trans.add(R.id.frame_picture_shower, showpictureFragment, FRG_SHOWPICTURE);
                }
                if (showpictureFragment.isVisible()) {
                    hideShowPictureFragment();
                }
                trans.commit();
            }
        }
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
    public void onFragmentInteraction(Uri uri) {onFragmentInteraction(uri,null);

    }
}
