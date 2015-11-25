package yunstudio2015.android.yunmeet.tricksbag;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.utilz.ImageLoadOptions;

import static yunstudio2015.android.yunmeet.activityz.MainActivity.PIC_LINKS;

/**
 * Created by Ultima on 2015/11/25.
 */
public class VpAdap  extends FragmentStatePagerAdapter {


    public VpAdap(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ImageViewFragment.getInstance(PIC_LINKS[position]);
        }

        @Override
        public int getCount() {
            return 2;
        }


    @Override
    public CharSequence getPageTitle(int position) {
        if (AppConstants.topMenu == null) {
            return "";
        }
        return AppConstants.topMenu[position];
    }


    static class ImageViewFragment extends Fragment {

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
