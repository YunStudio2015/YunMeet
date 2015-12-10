package yunstudio2015.android.yunmeet.utilz;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by Ultima on 2015/10/28.
 */
public class ImageLoadOptions {

    public static DisplayImageOptions getDisplayImageOptions (Context mContext) {


        Drawable drawable = mContext.getResources().getDrawable(R.drawable.onloading);

        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(drawable)
                .showImageForEmptyUri(R.drawable.no_image)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
//                .displayer(new RoundedBitmapDisplayer(Color.WHITE, 5))
                .build();
        return options;
    }

    public static DisplayImageOptions getDisplaySlightlyRoundedImageOptions(Context context) {


        Drawable drawable = context
                .getResources().getDrawable(R.drawable.onloading);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .displayer(new RoundedBitmapDisplayer(context.getResources().getDimensionPixelSize(R.dimen.image_dimen_menu)))
                .showImageOnLoading(drawable)
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
