package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.fragments.ImageFragment;

public class BrowseSelectedPicturesActivity extends AppCompatActivity implements ImageFragment.OnFragmentInteractionListener {

    @Bind(R.id.vp_browsepics)
    ViewPager vp_browsepics;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.tv_img_index)
    TextView tv_img_index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_selected_pictures);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);
        final ArrayList<String> pics = getIntent().getStringArrayListExtra("data");
        int position = getIntent().getExtras().getInt("position");
        /* 初始化viewpager里面的fragment */
        ImageVpAdapter adapter = new ImageVpAdapter(getSupportFragmentManager(), pics);
        vp_browsepics.setAdapter(adapter);
        vp_browsepics.setCurrentItem(position);
        vp_browsepics.setOffscreenPageLimit(pics.size()-1);
        tv_img_index.setText(position+"/"+pics.size());
        vp_browsepics.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_img_index.setText(position+"/"+pics.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.iv_back)
    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


    private class ImageVpAdapter extends FragmentPagerAdapter {

        private final ArrayList<String> data;

        public ImageVpAdapter(FragmentManager fm, ArrayList<String> data) {
            super(fm);
            this.data = data;
        }

        @Override
        public Fragment getItem(int position) {
            return ImageFragment.newInstance(data.get(position));
        }

        @Override
        public int getCount() {
            return data.size();
        }
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
