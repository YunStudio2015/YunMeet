package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by lizhaotailang on 2016/2/27.
 */
public class PersonalInfoFragmentTopic extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View viewTopic = inflater.inflate(R.layout.person_info_topic,container,false);
        return viewTopic;
    }
}
