package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import yunstudio2015.android.yunmeet.R;


public class MyFriendsMainFragment extends Fragment {

    private ImageView ivFriend;
    private ImageView ivGroup;

    // position用于保存当前所处fragment，取值只能为0或者1
    private int position = 1;

    private MyFriendsMainFriendsFragment friendsFragment = new MyFriendsMainFriendsFragment();
    private MyFriendsMainGroupFragment groupFragment = new MyFriendsMainGroupFragment();

    private OnFragmentInteractionListener mListener;

    public MyFriendsMainFragment() {
        // Required empty public constructor
    }


    public static MyFriendsMainFragment newInstance( ) {
        MyFriendsMainFragment fragment = new MyFriendsMainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_friends_main, container, false);

        initViews(rootView);

        // 初始化界面为好友界面
        changeToFragment(0);

        ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 0){
                    // 更改对应的图片资源
                    ivFriend.setImageResource(R.drawable.friends_0);
                    ivGroup.setImageResource(R.drawable.group_0);

                    changeToFragment(position);

                    position = 1;
                }

            }
        });

        ivGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (position == 1){
                    ivFriend.setImageResource(R.drawable.friends_1);
                    ivGroup.setImageResource(R.drawable.group_1);

                    changeToFragment(position);

                    position = 0;
                }

            }
        });

        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void initViews(View view) {

        // toolbar中两个imageview需要用context来获取，不能简单的通过传入的view获取
        ivFriend = (ImageView) getActivity().findViewById(R.id.iv_friends);
        ivGroup = (ImageView) getActivity().findViewById(R.id.iv_group);

    }

    /**
     * 用于加载不同的fragment
     * @param position 当前所处的位置，0或者1
     */
    private void changeToFragment(int position){
        if (position == 0){
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.my_friends_main_container,friendsFragment)
                    .commit();
        } else {
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.my_friends_main_container,groupFragment)
                    .commit();
        }
    }
}