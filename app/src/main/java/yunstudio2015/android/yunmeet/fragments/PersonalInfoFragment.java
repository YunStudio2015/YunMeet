package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by lizhaotailang on 2016/2/26.
 */
public class PersonalInfoFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";
    private int page;

    public static PersonalInfoFragment newInstance(int page){
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE,page);
        PersonalInfoFragment fragment = new PersonalInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt(ARG_PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (page == 1){
            View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);
            return viewTopic;
        }
        View viewActivity = inflater.inflate(R.layout.person_info_activity,container,false);
        return viewActivity;
    }


}
