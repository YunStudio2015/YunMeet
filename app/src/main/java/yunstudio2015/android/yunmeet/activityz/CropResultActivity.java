/*
 * Copyright (C) 2015 Lyft, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;
import uk.co.senab.photoview.PhotoViewAttacher;
import yunstudio2015.android.yunmeet.R;

public class CropResultActivity extends Activity {

    private static final String EXTRA_FILE_PATH = "EXTRA_FILE_PATH";

    @Bind(R.id.result_image)
    ImageView resultView;

    /* Photoview attacher... handler of all the zoom and other actions */
    PhotoViewAttacher mAttacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_crop_result);
        ButterKnife.bind(this);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        String filePath = getIntent().getStringExtra(EXTRA_FILE_PATH);
        if ("".equals(filePath) || filePath == null) {
            Glide.with(this)
                    .load(R.drawable.setface)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(resultView);
        } else {
            File imageFile = new File(filePath);
            Glide.with(this)
                    .load(imageFile)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(resultView);
            mAttacher = new PhotoViewAttacher(resultView);
        }
    }

    static void startUsing(String croppedPath, Activity activity) {
        Intent intent = new Intent(activity, CropResultActivity.class);
        intent.putExtra(EXTRA_FILE_PATH, croppedPath);
        activity.startActivity(intent);
    }

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT  != Build.VERSION_CODES.KITKAT)
            return;
        setTranslucentStatus(activity, true);
        SystemBarTintManager tintManager = new SystemBarTintManager(activity);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setNavigationBarTintEnabled(true);
        tintManager.setStatusBarTintResource(color);
    }

    private static void setTranslucentStatus(Activity activity, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
