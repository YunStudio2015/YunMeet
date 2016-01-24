package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import yunstudio2015.android.yunmeet.R;

/**
 * 作者：黎赵太郎
 * 时间：20160117
 * 用途：用于设置头像的activity
 *
 */

public class SetFaceActivity extends AppCompatActivity {

    private ImageButton ibtnBack;
    private ImageView ivFace;
    private Button btnFinish;
    private TextView tvSelectFromAlbum;
    private TextView tvTakePhoto;

    private SharedPreferences sharedPreferences;

    private static final int CODE_GALLERY_REQUEST = 0;//用于拍摄照片的requestCode
    private static final int CODE_CAMERA_REQUEST = 1;//用于裁剪照片的requestCode
    private static final int CODE_RESULT_REQUEST = 2;

    //图片裁剪后的宽和高
    private static int output_x = 480;
    private static int output_y = 480;

    private static final String IMG_FILE_NAME = "temp_head_image.jpg";

    private Bitmap headImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_face);

        initViews();

        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ivFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        tvSelectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseHeadImageFromGallery();
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseHeadImageFromCameraCapture();
            }
        });

    }

    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {

        Intent intentFromGallery = new Intent();
        //设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);

    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {

        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //判断sd卡是否可用
        if (hasSdcard()){
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), IMG_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture,CODE_CAMERA_REQUEST);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"取消",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode){

            case CODE_GALLERY_REQUEST:
                cropRawPhoto(data.getData());
                break;
            case CODE_CAMERA_REQUEST:
                if (hasSdcard()){
                    File tempFile = new File(Environment.getExternalStorageDirectory(),IMG_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplicationContext(),"没有SD卡",Toast.LENGTH_SHORT).show();
                }
                break;
            case CODE_RESULT_REQUEST:
                Log.d("data",data.toString());
                if (data != null){
                    setImageToHeadView(data);
                }
                break;

        }
    }

    //裁剪原始图片
    private void cropRawPhoto(Uri data) {

        Intent intentCropPhoto = new Intent("com.android.camera.action.CROP");
        intentCropPhoto.setDataAndType(data, "image/*");

        //设置裁剪
        intentCropPhoto.putExtra("crop","true");

        //aspectX,aspectY宽高的比例
        intentCropPhoto.putExtra("aspectX",1);
        intentCropPhoto.putExtra("aspectY",1);

        //outputX,outputY,裁剪图片的宽高
        intentCropPhoto.putExtra("outputX",output_x);
        intentCropPhoto.putExtra("outputY",output_y);
        intentCropPhoto.putExtra("return-data",true);

        startActivityForResult(intentCropPhoto,CODE_RESULT_REQUEST);

    }

    //提取保存后的图片数据，并设置头像部分的view
    private void setImageToHeadView(Intent intent) {

         Bundle extras = intent.getExtras();
        if (extras != null){
            headImg = extras.getParcelable("data");
            ivFace.setImageBitmap(headImg);
        }

    }

    //查看是否有内存卡
    private boolean hasSdcard() {

        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)){
            return true;
        }
        return false;
    }

    private void initViews() {

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        ivFace = (ImageView) findViewById(R.id.iv_face);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        tvSelectFromAlbum = (TextView) findViewById(R.id.tv_select_from_album);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);

    }

}