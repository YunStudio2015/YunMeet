package yunstudio2015.android.yunmeet.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;
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

        final String imgurl = getArguments().getString("path");
        ButterKnife.bind(this, view);
        Glide.with(getActivity())
                .load(imgurl)
                .error(me.crosswall.photo.pick.R.drawable.default_error)
                .into(imageView);
        // Attach a PhotoViewAttacher, which takes care of all of the zooming functionality.
        mAttacher = new PhotoViewAttacher(imageView);
        mAttacher.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

            @Override
            public void onViewTap(View view, float x, float y) {
                Uri uri = Uri.parse(AppConstants.scheme_ui+"://"+AppConstants.authority+"/"+toGson(imgurl));
                ((ActivitiesFragment.OnFragmentInteractionListener)getActivity()).onFragmentInteraction(uri, null);
            }
        });
    }

    private Gson gson;
    private String toGson (Object obj) {
        if (gson == null)
            gson = new Gson();
        return gson.toJson(obj);
    }

    public static ZoomImageFragment newInstance(String s) {

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

    public void update(String s) {
        if (imageView != null) {
//            mT("updating the view");
            s = s.replace("t_256", "");
            s = s.replace("t_800", "");
            ImageLoader.getInstance().displayImage(s, imageView);
            mAttacher.update();
        }else {
//            mT("fragment imageview is null");
        }
    }

    private void mT(String s) {
        Toast.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
    }
}


