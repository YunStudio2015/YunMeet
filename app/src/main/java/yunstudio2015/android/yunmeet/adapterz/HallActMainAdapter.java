package yunstudio2015.android.yunmeet.adapterz;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.fragments.ChatMainFragment;
import yunstudio2015.android.yunmeet.fragments.DiscoverMainFragment;
import yunstudio2015.android.yunmeet.fragments.MyFriendsMainFragment;
import yunstudio2015.android.yunmeet.fragments.MySpaceMainFragment;

/**
 * Created by Ulrich on 2/26/2016.
 */
public class HallActMainAdapter extends FragmentPagerAdapter {

    Map<String, Fragment> frg;

    public HallActMainAdapter(FragmentManager fm) {
        super(fm);
        frg = new HashMap<>();
    }

    @Override
    public Fragment getItem(int position) {


        if (frg == null)
            frg = new HashMap<>();
        // create the fragment over.
        switch (position){
            case 0:
                if (!frg.containsKey(""+position))
                    frg.put(""+position, DiscoverMainFragment.newInstance());
                return frg.get(""+position);
            case 1:
                if (!frg.containsKey(""+position))
                    frg.put("" + position, ChatMainFragment.newInstance());
                return frg.get(""+position);
            case 2:
                if (!frg.containsKey(""+position))
                    frg.put("" + position, MyFriendsMainFragment.newInstance());
                return frg.get(""+position);
            case 3:
                if (!frg.containsKey(""+position))
                    frg.put(""+position, MySpaceMainFragment.newInstance());
                return frg.get(""+position);
            default:
                return frg.get(""+0);
        }
    }

    @Override
    public int getCount() {
        return 4;
    }


}
