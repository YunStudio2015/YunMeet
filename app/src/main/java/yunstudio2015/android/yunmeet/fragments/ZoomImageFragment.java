package yunstudio2015.android.yunmeet.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;

/**
 * Created by Ulrich on 3/25/2016.
 */
public class ZoomImageFragment extends Fragment {

    @Bind(R.id.img)
    ImageView imageView;


    PhotoViewAttacher mAttacher;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        String imgurl = getArguments().getString("path");
        ButterKnife.bind(this, view);
        Glide.with(getActivity())
                .load(imgurl)
                .error(me.crosswall.photo.pick.R.drawable.default_error)
                .into(imageView);
        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(mImageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(AppConstants.scheme_ui+"://"+AppConstants.authority);
                ((ShowPicturesFragment.OnFragmentInteractionListener)getActivity()).onFragmentInteraction(uri);
            }
        });
    }

    public static Fragment newInstance(String s) {

        ZoomImageFragment frg = new ZoomImageFragment();
        Bundle args = new Bundle();
        args.putString("path", s);
        frg.setArguments(args);
        return frg;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_image_fragment, container, false);
    }
}


