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
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private ImageView ivSetFace;
    private Button btnFinish;
    private TextView tvSelectFromAlbum;
    private TextView tvTakePhoto;
    private TextView tvUseQQFace;

    private SharedPreferences sharedPreferences;

    public static final int TAKE_PHOTO = 1;//用于拍摄照片的requestCode
    public static final int CROP_PHOTO = 2;//用于裁剪照片的requestCode

    private Uri imageUri;

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

        ivSetFace.setOnClickListener(new View.OnClickListener() {
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

                File outputImage = new File(Environment.getExternalStorageDirectory(),setFileName());
                //如果文件存在，则删除该文件，创建新的文件
                if (outputImage.exists()){
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                imageUri = Uri.fromFile(outputImage);
                Intent intent = new Intent("com.intent.action.GET_CONTENT");
                intent.setType("image/*");
                intent.putExtra("crop", true);
                intent.putExtra("scale",true);
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,CROP_PHOTO);

            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //创建File对象，用于储存拍照后的对象
                //这里涉及到文件的读取，需要在manifest文件中添加内存访问权限
                File outputImage = new File(Environment.getExternalStorageDirectory(),setFileName());
                if (outputImage.exists()){
                    outputImage.delete();
                }
                try {
                    outputImage.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                //imageUri标示着所拍摄照片的唯一地址
                imageUri = Uri.fromFile(outputImage);
                //构建隐式的Intent对象，并将这个Intent的action指定为android.media.action.IMAGE_CAPTURE
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                //调用Intent的putExtra方法，将刚刚得到的URI对象
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                //启动
                startActivityForResult(intent,TAKE_PHOTO);

            }
        });

        tvUseQQFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case TAKE_PHOTO:
                if (resultCode == RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");
                    intent.setDataAndType(imageUri, "/image/*");

                    //设置剪裁
                    intent.putExtra("crop",true);
                    //剪裁比例
                    intent.putExtra("aspectX",1);
                    intent.putExtra("aspectY",1);

                    //剪裁高度和宽度定为500x500
                    intent.putExtra("outputX",500);
                    intent.putExtra("outputY",500);

                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    //启动裁剪程序
                    startActivityForResult(intent,CROP_PHOTO);

                }
                break;
            case CROP_PHOTO:
                if (resultCode == RESULT_OK){
                    try {
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        //将裁剪后的图片显示在ImageView上
                        ivSetFace.setImageBitmap(bitmap);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;

        }
    }

    private void initViews() {

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        ivSetFace = (ImageView) findViewById(R.id.iv_set_face);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        tvSelectFromAlbum = (TextView) findViewById(R.id.tv_select_from_album);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tvUseQQFace = (TextView) findViewById(R.id.tv_using_qq_face);

    }

    public String setFileName(){
        //将当前时间转换为20160117201011的格式
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = new Date();
        String name =  format.format(date) + ".jpg";
        return name;
    }

}
