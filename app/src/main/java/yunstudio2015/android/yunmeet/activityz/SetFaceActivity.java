package yunstudio2015.android.yunmeet.activityz;

import android.animation.Animator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

import me.crosswall.photo.pick.PickConfig;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.utilz.UploadImageTask;

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

    private SharedPreferences sharedPreferences;

    private static final int CODE_GALLERY_REQUEST = 0;//用于拍摄照片的requestCode
    private static final int CODE_CAMERA_REQUEST = 1;//用于裁剪照片的requestCode
    private static final int CODE_CROP_REQUEST = 2;

    private static final int UPLOAD_FINISH = 1;
    private static final int LOAD_FINISH = 2;

    //图片裁剪后的宽和高
    private static int output_x = 480;
    private static int output_y = 480;

    private static final String IMG_FILE_NAME = "temp_head_image.png";

    private Bitmap headImg = null;

    private RequestQueue queue;

    //用于线程之间的传递
    private Intent data;

    //上传图片时显示的dialog
    private ProgressDialog upLoadingDialog;
    //在客户端加载图片时显示的dialog
    private ProgressDialog loadingDialog;

    // Hold a reference to the current animator,
    // so that it can be canceled mid-way.
    private Animator mCurrentAnimator;

    // The system "short" animation time duration, in milliseconds. This
    // duration is ideal for subtle animations or animations that occur
    // very frequently.
    private int mShortAnimationDuration;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_FINISH){
                if (upLoadingDialog.isShowing())
                    upLoadingDialog.dismiss();
            }

            if (msg.what == LOAD_FINISH){
                if (loadingDialog.isShowing())
                    upLoadingDialog.dismiss();
            }
        }
    };

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

        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        upLoadingDialog = new ProgressDialog(this);
        upLoadingDialog.setTitle(getString(R.string.tip));
        upLoadingDialog.setMessage(getString(R.string.uploading));

        loadingDialog = new ProgressDialog(this);
        loadingDialog.setTitle(getString(R.string.tip));
        loadingDialog.setMessage(getString(R.string.loading));

        queue = Volley.newRequestQueue(getApplicationContext());

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
        UploadImageTask up = new UploadImageTask(new UploadImageTask.UploadFinishCallBack(){

            @Override
            public void uploadDone() {
                Toast.makeText(SetFaceActivity.this, "upload done... exc", Toast.LENGTH_SHORT).show();
                i_dismissProgressDialog();
            }

            @Override
            public void uploadfailed() {
                Toast.makeText(SetFaceActivity.this, "upload failed... exc", Toast.LENGTH_SHORT).show();
                i_dismissProgressDialog();
            }
        });
        String token = "Vhi0zGIl7UOmFqWZt8Jz07MQHFRoUcvh";
        up.execute(imageLocalPath, token);
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

    public static String convertIconToString(Bitmap bitmap)
    {
        L.d("convert",String.valueOf(System.currentTimeMillis()));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        L.d("convert",String.valueOf(System.currentTimeMillis()));
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    @Override
    protected void onDestroy() {

        if (upLoadingDialog.isShowing())
            upLoadingDialog.dismiss();
        super.onDestroy();
    }

}