package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adhamenaya.androidmosaiclayout.views.MosaicLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;

import static yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter.*;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChatTopicsItemFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChatTopicsItemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChatTopicsItemFragment extends Fragment {

    private static String param = "param";

    private OnFragmentInteractionListener mListener;
    private android.content.Context context;
    private RecyclerView.Adapter adapter;

    public ChatTopicsItemFragment() {
        // Required empty public constructor
    }

    @Bind(R.id.recyclerview_chattopic)
    RecyclerView recyclerview_chattopic;

    @Bind(R.id.lny_error_message)
    LinearLayout lny_error_message;


    // TODO: Rename and change types and number of parameters
    public static ChatTopicsItemFragment newInstance(int type) {
        ChatTopicsItemFragment fragment = new ChatTopicsItemFragment();
        Bundle args = new Bundle();
        args.putInt(param, type);
        fragment.setArguments(args);
        return fragment;
    }

    /* recycler adapter */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootview = inflater.inflate(R.layout.fragment_chat_topics_item, container, false);
        context = rootview.getContext();
        int type = getArguments().getInt(param, 0);
        ButterKnife.bind(this, rootview);
        // get the data according to the ...
        recyclerview_chattopic.setLayoutManager(new LinearLayoutManager(context));
        recyclerview_chattopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        loadData();
        return rootview;
    }

    private void loadData() {

        i_showProgressDialog();
        List<Object> d = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            d.add(new Object());
        }
        adapter = new ChatTopicRecyclerviewAdapter(d);
        recyclerview_chattopic.addItemDecoration(new ChatTopicRecyclerviewAdapter.DividerItemDecoration(context, LinearLayoutManager.VERTICAL));
        recyclerview_chattopic.setAdapter(adapter);
        recyclerview_chattopic.setVisibility(View.VISIBLE);
        i_dismissProgressDialog();
    }

    LoadingDialog dialog;
    public void i_showProgressDialog() {
        dialog = new LoadingDialog(context);
        dialog.show();
    }

    public void i_showProgressDialog(String mess) {
        dialog = new LoadingDialog(context, mess);
        dialog.show();
    }

    public void i_dismissProgressDialog () {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
