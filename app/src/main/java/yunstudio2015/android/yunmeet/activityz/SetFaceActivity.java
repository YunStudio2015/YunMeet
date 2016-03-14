package yunstudio2015.android.yunmeet.activityz;

import android.animation.Animator;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.handmark.pulltorefresh.library.internal.Utils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.File;
import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;
import yunstudio2015.android.yunmeet.utilz.UploadProfileImageTask;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * 作者：黎赵太郎
 * 时间：20160117
 * 用途：用于设置头像的activity
 *
 */

public class SetFaceActivity extends AppCompatActivity {

    private static final int IMAGE_URI_TAG_ID = 17;
    private ImageView ivBack;
    private CircleImageView ivFace;
    private Button btnFinish;
    private TextView tvSelectFromAlbum;
    private TextView tvTakePhoto;

    private static final int CODE_GALLERY_REQUEST = 0;//用于拍摄照片的requestCode
    private static final int CODE_CAMERA_REQUEST = 1;//用于裁剪照片的requestCode
    private static final int CODE_CROP_REQUEST = 2;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    private Toolbar toolbar;
    private String tmpPath;

    LoadingDialog dialog;
    public void i_showProgressDialog() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    public void i_showProgressDialog(String mess) {
        dialog = new LoadingDialog(this, mess);
        dialog.show();
    }

    public void i_dismissProgressDialog () {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_face);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();

        this.setSupportActionBar(toolbar); // 吧xml里面的toolbar设置成当前界面的actionbar
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tvSelectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new PickConfig.Builder(SetFaceActivity.this)
                        .pickMode(PickConfig.MODE_SINGLE_PICK)
                        .spanCount(3)
                        .toolbarColor(R.color.colorPrimary)
                        .build();
                overridePendingTransition(R.anim.noanim, R.anim.noanim);
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // add the location of the image
                File tmpFile = new File(Environment.getExternalStorageDirectory() + "/yunstudio2015.android.yunmeet/YUN-"+SystemClock.currentThreadTimeMillis()+".jpg");
                intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tmpFile));
                startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
                tmpPath = tmpFile.getAbsolutePath();
            }
        });

        ivFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tvSelectFromAlbum.getTag() == null) {
                    CropResultActivity.startUsing(null, SetFaceActivity.this);
                }  else
                    CropResultActivity.startUsing((String) tvSelectFromAlbum.getTag(), SetFaceActivity.this);
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImageToServer();
            }
        });
    }


    public void uploadImageToServer () {

        String imageLocalPath = (String) tvSelectFromAlbum.getTag();
        if (imageLocalPath == null || "".equals(imageLocalPath))
            return;
        // upload the file
        UploadProfileImageTask up = new UploadProfileImageTask(SetFaceActivity.this,new UploadFinishCallBack(){

            @Override
            public void uploadDone() {
                Toast.makeText(SetFaceActivity.this, getResources().getString(R.string.upload_success), Toast.LENGTH_SHORT).show();
                i_dismissProgressDialog();

                Intent intent = new Intent(SetFaceActivity.this,HallActivity.class);
                startActivity(intent);
                finish();

            }

            @Override
            public void uploadfailed(String rep) {
//                Toast.makeText(SetFaceActivity.this,  getResources().getString(R.string.upload_failure), Toast.LENGTH_SHORT).show();
                i_dismissProgressDialog();
                SetFaceActivity.this.mSnack(rep);
            }
        });

        up.execute(imageLocalPath, UtilsFunctions.getToken(SetFaceActivity.this));
        i_showProgressDialog(getResources().getString(R.string.uploading));
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


    private void mSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode == PickConfig.PICK_REQUEST_CODE) {

            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
//            startImageZoom(uri);
            startImageCroping (pick.get(0));
        }
        if (requestCode == CODE_CROP_REQUEST) {
            final String imagePath =   data.getExtras().getString("path");
            File imageFile = new File(imagePath);
            Glide.with(this)
                    .load(imageFile)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(ivFace);
            tvSelectFromAlbum.setTag(imagePath);
        }
        if (requestCode == CODE_CAMERA_REQUEST){


            // show dialog box.
            i_showProgressDialog();
            // wait for a moment, then launch the cropping activity
            ivFace.postDelayed(new Runnable() {
                @Override
                public void run() {

                    if (tmpPath != null && !"".equals(tmpPath)) {
                        startImageCroping (tmpPath);
                    }
                    i_dismissProgressDialog();
                }
            }, 2000);
        }
    }



    @Override
    protected void onPause() {
        super.onPause();
        i_dismissProgressDialog();
    }

    private void startImageCroping(String imagePath) {

        /* launch the activity to start cropping the image. */
        Intent intent = new Intent(SetFaceActivity.this, CroppingActivity.class);
        intent.putExtra("data", imagePath);
        startActivityForResult(intent, CODE_CROP_REQUEST);
    }



    private void initViews() {

        toolbar= (Toolbar) findViewById(R.id.toolbar);
        ivBack = (ImageView) findViewById(R.id.iv_back);
        ivFace = (CircleImageView) findViewById(R.id.iv_face);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        tvSelectFromAlbum = (TextView) findViewById(R.id.tv_select_from_album);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
    }

}