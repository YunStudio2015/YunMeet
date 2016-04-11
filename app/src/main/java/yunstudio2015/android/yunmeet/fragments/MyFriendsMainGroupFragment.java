package yunstudio2015.android.yunmeet.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by lizhaotailang on 2016/4/11.
 */
public class MyFriendsMainGroupFragment extends Fragment {

    private RecyclerView rvGroups;

    private RequestQueue queue;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        queue = Volley.newRequestQueue(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_friends_group,container,false);

        return view;
    }
}
