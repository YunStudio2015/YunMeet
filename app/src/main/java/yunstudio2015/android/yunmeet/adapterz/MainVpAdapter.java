package yunstudio2015.android.yunmeet.adapterz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.fragments.ActivitiesMainFragment;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsMainFragment;
import yunstudio2015.android.yunmeet.fragments.MySpaceMainFragment;

/**
 * Created by Ultima on 2015/11/25.
 */
public class MainVpAdapter extends FragmentStatePagerAdapter {


    public MainVpAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

       if (position == 0)
            return ChatTopicsMainFragment.getInstance();
        else if (position == 1)
            return ActivitiesMainFragment.getInstance();
        else
            return MySpaceMainFragment.newInstance();
    }

    @Override
    public int getCount() {
        return AppConstants.topMenu.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (AppConstants.topMenu == null) {
            return "";
        }
        return AppConstants.topMenu[position];
    }

}
