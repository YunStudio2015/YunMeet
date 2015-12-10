package yunstudio2015.android.yunmeet.activityz;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import yunstudio2015.android.yunmeet.R;


public class PublishNewActivity extends AppCompatActivity{


    private EditText etTitle,etDiscription;
    private Spinner spType,spPersonNumber;
    private RadioGroup rgWhopays;
    private RadioButton rbI,rbU,rbAA;
    private TextView tvTime,tvLocation;
    private ImageButton ibtnAddpics;
    private Button btnPublishActivity;
    private String time;//活动发生的时间 12-01-15 20:58:32 月-日-年 时：分：秒


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
                switch (checkedId) {
                    case R.id.rb_AA:
                        Toast.makeText(PublishNewActivity.this, "AA", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_I:
                        Toast.makeText(PublishNewActivity.this, "I", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.rb_U:
                        Toast.makeText(PublishNewActivity.this, "U", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {

            Calendar calendar = Calendar.getInstance();
            int mYear, mMonth, mDate, mMinute, mSecond;
            int mHour = 0;

            @Override
            public void onClick(View v) {

                //初始化
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDate = calendar.get(Calendar.DAY_OF_MONTH);
                mMinute = calendar.get(Calendar.MINUTE);
                mSecond = calendar.get(Calendar.SECOND);

                if (time12or24() == 12){
                    mHour = calendar.get(Calendar.HOUR)+12;
                }else {
                    mHour = calendar.get(Calendar.HOUR_OF_DAY);
                }

                setTime();

            }

            void setDate(){

                final DatePickerDialog datePickerDialog = new DatePickerDialog(
                        PublishNewActivity.this,
                        new android.app.DatePickerDialog.OnDateSetListener() {
                            //onDateSet中的参数就是选择后的日期
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Log.d("date", String.valueOf(year));
                            }
                        },
                        mYear,
                        mMonth,
                        mDate
                );
                datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                datePickerDialog.show();
            }

            void setTime(){
                final TimePickerDialog timePickerDialog = new TimePickerDialog(
                        PublishNewActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            //onTimeSet中的参数就是选择之后的时间
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                            }
                        },
                        mHour,
                        mMinute,
                        true
                );
                timePickerDialog.setCancelable(false);
                timePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDate();
                    }
                });
                timePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "CANCLE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                timePickerDialog.show();

            }
        });

        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PublishNewActivity.this,"You clicked the location",Toast.LENGTH_SHORT).show();
            }
        });

        ibtnAddpics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PublishNewActivity.this,"You clicked the button addpics",Toast.LENGTH_SHORT).show();
            }
        });

        btnPublishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        ibtnAddpics = (ImageButton) findViewById(R.id.ibtn_addpics);
        btnPublishActivity = (Button) findViewById(R.id.btn_publish_activity);
    }

    int time12or24(){
        //获取当前系统为12小时制还是24小时制
        ContentResolver cr = getContentResolver();
        String strTimeFormat = android.provider.Settings.System.getString(cr, Settings.System.TIME_12_24);
        if (strTimeFormat != null && strTimeFormat.equals("24")) {
            return 24;
        }
        return 12;
    }

}
