package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by Ultima on 2015/10/28.
 */
public class ImageLoadOptions {

    public static DisplayImageOptions getDisplayImageOptions (Context mContext) {


        AnimationDrawable frameAnimation = (AnimationDrawable) mContext.getResources().getDrawable(R.drawable.onloading_drawable);
        // Start the animation (looped playback by default).
        frameAnimation.start();
        DisplayImageOptions options = new DisplayImageOptions.Builder()
//                .showImageOnLoading(R.drawable.onloading_drawable)
                .showImageOnLoading(frameAnimation)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(Color.WHITE, 5))
                .build();
        return options;
    }
}
