package yunstudio2015.android.yunmeet.activityz;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.utilz.ImageLoadOptions;

public class MainActivity extends AppCompatActivity {


    private static final String[] PIC_LINKS = {
            "http://www.sfs-cn.com/images/thumbnailimg/month_1412/201412251027386278.jpg",
            "http://attach.bbs.miui.com/forum/201308/28/195800loquaugdcud5dhwa.png",
            "http://img5q.duitang.com/uploads/item/201501/04/20150104112918_NwRrz.png",
            "http://img5.duitang.com/uploads/item/201412/24/20141224101049_vXcr4.png",
            "http://h.hiphotos.baidu.com/zhidao/pic/item/d0c8a786c9177f3e883c02dc72cf3bc79e3d56d3.jpg",
            "http://cdn.duitang.com/uploads/item/201409/17/20140917231320_UBWuJ.thumb.700_0.jpeg"
    };

    @Bind(R.id.viewpager)
    ViewPager viewpager;
    @Bind(R.id.tv)
    TextView tv_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
       //
        ButterKnife.bind(this);
        getSupportActionBar().hide();
        final ViewPagerAdapter adap = new ViewPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adap);
        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                updateTotal(position, PIC_LINKS.length);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        updateTotal(0, PIC_LINKS.length);
    }

    public void updateTotal(int position, int length) {
        position++;
        if (tv_id != null)
            tv_id.setText(position+"/"+length);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getRandomNumber() {
        return (int) (System.currentTimeMillis() % PIC_LINKS.length);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageViewFragment.getInstance(PIC_LINKS[position]);
        }

        @Override
        public int getCount() {
            return PIC_LINKS.length;
        }
    }


    public static class ImageViewFragment extends Fragment {

        private static final String IMG = "imgxxx";


        public static Fragment getInstance (String imgLink) {
            ImageViewFragment frg = new ImageViewFragment();
            Bundle args = new Bundle();
            args.putString(IMG, imgLink);
            frg.setArguments(args);
            return frg;
        }

        private ImageView iv_tmp;
        private View rootview;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootview = inflater.inflate(R.layout.frg_iv, null);
            iv_tmp = ButterKnife.findById(rootview, R.id.iv_tmp);
            String link = getArguments().getString(IMG);
            ImageLoader.getInstance().displayImage(link, iv_tmp, ImageLoadOptions.getDisplayImageOptions(getContext()));
            return rootview;
        }
    }
}
