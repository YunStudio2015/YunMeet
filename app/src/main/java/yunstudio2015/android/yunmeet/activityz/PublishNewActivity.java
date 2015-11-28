package yunstudio2015.android.yunmeet.activityz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import yunstudio2015.android.yunmeet.R;


//用于测试布局效果

public class PublishNewActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText et_title,et_discription;
    private Spinner sp_type,sp_person_number;
    private RadioGroup rg_whopays;
    private RadioButton rb_I,rb_U,rb_AA;
    private TextView tv_time,tv_location;
    private ImageButton ibtn_addpics;
    private Button btn_publish_activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_show);
        setSupportActionBar(toolbar);

        initView();//初始化控件


    }

    void initView(){
        et_title = (EditText) findViewById(R.id.et_title);
        et_discription = (EditText) findViewById(R.id.et_discription);
        sp_type = (Spinner) findViewById(R.id.sp_type);
        sp_person_number = (Spinner) findViewById(R.id.sp_person_number);
        rg_whopays = (RadioGroup) findViewById(R.id.rg_whopays);
        rb_I = (RadioButton) findViewById(R.id.rb_I);
        rb_U = (RadioButton) findViewById(R.id.rb_U);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_location = (TextView) findViewById(R.id.tv_location);
        ibtn_addpics = (ImageButton) findViewById(R.id.ibtn_addpics);
        btn_publish_activity = (Button) findViewById(R.id.btn_publish_activity);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.et_title){

        }
        if (v.getId() == R.id.et_discription){

        }
        if (v.getId() == R.id.sp_type){

        }
        if (v.getId() == R.id.sp_person_number){

        }
        if (v.getId() == R.id.tv_time){

        }
        if (v.getId() == R.id.tv_location){

        }
        if (v.getId() == R.id.ibtn_addpics){

        }
        if (v.getId() == R.id.btn_publish_activity){

        }
    }
}
