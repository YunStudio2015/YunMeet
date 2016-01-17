package yunstudio2015.android.yunmeet.activityz;

import android.content.Intent;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import yunstudio2015.android.yunmeet.R;

public class SetFaceActivity extends AppCompatActivity {

    private ImageButton ibtnBack;
    private ImageButton ibtnSetFace;
    private Button btnFinish;
    private TextView tvSelectFromAlbum;
    private TextView tvTakePhoto;
    private TextView tvUseQQFace;
    private int requestCodeCemera = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_face);
        
        initViews();

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ibtnSetFace.setOnClickListener(new View.OnClickListener() {
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

            }
        });

        tvTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent takePhoto = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(takePhoto,requestCodeCemera);

            }
        });

        tvUseQQFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void initViews() {

        ibtnBack = (ImageButton) findViewById(R.id.ibtn_back);
        ibtnSetFace = (ImageButton) findViewById(R.id.ibtn_set_face);
        btnFinish = (Button) findViewById(R.id.btn_finish);
        tvSelectFromAlbum = (TextView) findViewById(R.id.tv_select_from_album);
        tvTakePhoto = (TextView) findViewById(R.id.tv_take_photo);
        tvUseQQFace = (TextView) findViewById(R.id.tv_using_qq_face);

    }

}
