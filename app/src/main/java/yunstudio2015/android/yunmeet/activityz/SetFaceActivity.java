package yunstudio2015.android.yunmeet.activityz;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

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
    private static final int CODE_CROP_REQUEST = 2;

    private static final int UPLOAD_FINISH = 1;

    //图片裁剪后的宽和高
    private static int output_x = 480;
    private static int output_y = 480;

    private static final String IMG_FILE_NAME = "temp_head_image.png";

    private Bitmap headImg = null;

    private RequestQueue queue;

    private ProgressDialog progressDialog;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_FINISH){
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_face);

        initViews();

        sharedPreferences = getSharedPreferences("UserData",MODE_PRIVATE);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("提示");
        progressDialog.setMessage("正在上传，请稍候...");

        queue = Volley.newRequestQueue(getApplicationContext());

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //这里首先要验证用户是否选择了图片
                if (headImg == null){
                    Toast.makeText(SetFaceActivity.this,"你还没有选择图片",Toast.LENGTH_SHORT).show();
                } else {

                    progressDialog.show();

                    Map<String,String> map = new HashMap<String, String>();
                    map.put("token",sharedPreferences.getString("token", null));
                    map.put("face", convertIconToString(headImg));

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, YunApi.URL_SET_FACE, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            Message message = Message.obtain();
                            message.what = UPLOAD_FINISH;
                            handler.sendMessage(message);

                            try {
                                if (response.getString("error").equals("0")){
                                    //这里写activity的跳转，并且结束当前activity

                                } else {
                                    Toast.makeText(SetFaceActivity.this,response.getString("message"),Toast.LENGTH_SHORT).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(SetFaceActivity.this,R.string.wrong_process,Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Message message = Message.obtain();
                            message.what = UPLOAD_FINISH;
                            handler.sendMessage(message);

                            Toast.makeText(SetFaceActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
                        }
                    })
                    {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            HashMap<String,String> headers = new HashMap<String, String>();
                            headers.put("Accept","application/json");
                            headers.put("Content-Type","application/json,charset=UTF-8");
                            return headers;
                        }
                    };

                    queue.add(request);

                }
            }
        });

        tvSelectFromAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromGallery = new Intent();
                //设置文件类型
                intentFromGallery.setType("image/*");
                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_CANCELED){
            Toast.makeText(getApplicationContext(),"取消",Toast.LENGTH_SHORT).show();
            return;
        }

        switch (requestCode){

            case CODE_GALLERY_REQUEST:
                if(data == null)
                {
                    return;
                }
                Uri uri1 = data.getData();
                Uri fileUri = convertUri(uri1);
                startImageZoom(fileUri);
                break;
            case CODE_CAMERA_REQUEST:
                if(data == null)
                {
                    return;
                }
                else
                {
                    Bundle extras = data.getExtras();
                    if(extras != null)
                    {
                        Bitmap bm = extras.getParcelable("data");
                        Uri uri2 = saveBitmap(bm);
                        startImageZoom(uri2);
                    }
                }
                break;
            case CODE_CROP_REQUEST:
                if(data == null)
                {
                    return;
                }
                Bundle extras = data.getExtras();
                if(extras == null){
                    return;
                }
                headImg = extras.getParcelable("data");
                ivFace.setImageBitmap(headImg);
                break;

        }
    }

    //裁剪原始图片
    private void startImageZoom(Uri data) {

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

        startActivityForResult(intentCropPhoto, CODE_CROP_REQUEST);

    }

    private Uri saveBitmap(Bitmap bitmap){
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/yunstudio2015.android.yunmeet");
        if (!tmpDir.exists()){
            tmpDir.mkdir();
        }
        File img = new File(tmpDir.getAbsolutePath() + IMG_FILE_NAME);
        try {
            Long start =  System.currentTimeMillis();
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(img),1024*100);
            Long end = System.currentTimeMillis();
            Log.d("end",String.valueOf(end-start));
            //第一个参数为压缩的格式，第二个参数为压缩的质量，第三个参数为文件流
            bitmap.compress(Bitmap.CompressFormat.PNG, 85, bos);
            bos.flush();
            bos.close();
            return Uri.fromFile(img);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

    }

    private Uri convertUri(Uri uri){
        InputStream is = null;
        try {
            System.out.println(String.valueOf(System.currentTimeMillis()));
            is = getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            is.close();
            System.out.println(String.valueOf(System.currentTimeMillis()));
            return saveBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initViews() {

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        ivFace = (ImageView) findViewById(R.id.iv_face);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        tvSelectFromAlbum = (TextView) findViewById(R.id.tv_select_from_album);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);

    }

    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    @Override
    protected void onDestroy() {

        if (progressDialog.isShowing())
            progressDialog.dismiss();
        super.onDestroy();
    }
}