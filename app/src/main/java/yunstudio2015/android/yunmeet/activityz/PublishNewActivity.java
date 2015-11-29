package yunstudio2015.android.yunmeet.activityz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;


//用于测试布局效果

public class PublishNewActivity extends AppCompatActivity implements View.OnClickListener{


    private EditText etTitle,etDiscription;
    private Spinner spType,spPersonNumber;
    private RadioGroup rgWhopays;
    private RadioButton rbI,rbU,rbAA;
    private TextView rbTime,rbLocation;
    private ImageButton ibtnAddpics;
    private Button btnPublishActivity;

    private List<String> listType = new ArrayList<String>();
    private List<String> listPersonNumber = new ArrayList<String>();
    private ArrayAdapter<String> adapterType;
    private ArrayAdapter<String> adapterPersonNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publish_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_show);
        setSupportActionBar(toolbar);

        initView();//初始化控件
        rbI.setChecked(true);//初始化默认RadioButton 我请客选中

        listType.add(getString(R.string.publish_activity_type_listitem0));
        listType.add(getString(R.string.publish_activity_type_listitem1));
        adapterType = new ArrayAdapter<String>(PublishNewActivity.this,R.layout.support_simple_spinner_dropdown_item,listType);
        spType.setAdapter(adapterType);

        spType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(PublishNewActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    if (!listPersonNumber.isEmpty()) {
                        listPersonNumber.clear();
                    }
                    listPersonNumber.add(getString(R.string.publish_activity_person_number_listitem0));
                    adapterPersonNumber = new ArrayAdapter<String>(PublishNewActivity.this, R.layout.support_simple_spinner_dropdown_item, listPersonNumber);
                    spPersonNumber.setAdapter(adapterPersonNumber);
                } else if (position == 1) {
                    if (!listPersonNumber.isEmpty()) {
                        listPersonNumber.clear();
                    }
                    listPersonNumber.add(getString(R.string.publish_activity_person_number_listitem1));
                    listPersonNumber.add(getString(R.string.publish_activity_person_number_listitem2));
                    listPersonNumber.add(getString(R.string.publish_activity_person_number_listitem3));

                    adapterPersonNumber = new ArrayAdapter<String>(PublishNewActivity.this, R.layout.support_simple_spinner_dropdown_item, listPersonNumber);
                    spPersonNumber.setAdapter(adapterPersonNumber);

                    spPersonNumber.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(PublishNewActivity.this, parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            Toast.makeText(PublishNewActivity.this, "You selected nothing", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(PublishNewActivity.this, "You selected nothing", Toast.LENGTH_SHORT).show();
            }
        });

        rgWhopays.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

            }
        });

    }

    void initView(){
        etTitle = (EditText) findViewById(R.id.et_title);
        etDiscription = (EditText) findViewById(R.id.et_discription);
        spType = (Spinner) findViewById(R.id.sp_type);
        spPersonNumber = (Spinner) findViewById(R.id.sp_person_number);
        rgWhopays = (RadioGroup) findViewById(R.id.rg_whopays);
        rbI = (RadioButton) findViewById(R.id.rb_I);
        rbU = (RadioButton) findViewById(R.id.rb_U);
        rbTime = (TextView) findViewById(R.id.tv_time);
        rbLocation = (TextView) findViewById(R.id.tv_location);
        ibtnAddpics = (ImageButton) findViewById(R.id.ibtn_addpics);
        btnPublishActivity = (Button) findViewById(R.id.btn_publish_activity);
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
