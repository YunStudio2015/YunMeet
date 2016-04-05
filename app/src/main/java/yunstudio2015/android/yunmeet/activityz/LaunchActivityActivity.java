package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.datetimepicker.date.DatePickerDialog;
import com.android.datetimepicker.time.RadialPickerLayout;
import com.android.datetimepicker.time.TimePickerDialog;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.vi.swipenumberpicker.OnValueChangeListener;
import com.vi.swipenumberpicker.SwipeNumberPicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.crosswall.photo.pick.PickConfig;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.LaunchActivityImageAdapter;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.ErrorDialog;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.entityz.ActivityCategoryEntity;
import yunstudio2015.android.yunmeet.entityz.UploadActivityEntity;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallback;
import yunstudio2015.android.yunmeet.interfacez.VolleyOnResultListener;
import yunstudio2015.android.yunmeet.utilz.UploadNewActivityTask;
import yunstudio2015.android.yunmeet.utilz.VolleyRequest;
import yunstudio2015.android.yunmeet.utilz.YunApi;

public class LaunchActivityActivity extends AppCompatActivity implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.recycler_launch_activity_img)
    RecyclerView recyclerView;

    @Bind(R.id.rellll)
    RelativeLayout rellll;

    private LaunchActivityImageAdapter adapter;
    private int spanCount = 5;

    private StageCustomRadioGroup /*radioGroup1,*/ radioGroup2, radioGroup3;

    @Bind(R.id.ed_act_title)
    public EditText ed_title;

    @Bind(R.id.ed_place_title)
    public EditText ed_place;

    @Bind(R.id.ed_description)
    public EditText ed_description;

    @Bind(R.id.rel_img_container)
    RelativeLayout rel_img_container;

    @Bind(R.id.sp_radiobutton_1_1)
    Spinner categoriez_spinner;

    @Bind(R.id.btn_finish_launch)
    Button bt_launch_activity;

    @Bind(R.id.tv_time)
    TextView tv_time;

    @Bind(R.id.tv_date)
    TextView tv_date;

    @Bind(R.id.switch_radiobutton_1_3)
    SwitchCompat isRecommandedSwicth;

    private ArrayAdapter<String> spinner_adapter;

    /* date time */
    private static final String TIME_PATTERN = "HH:mm";


    private Calendar calendar;
    private DateFormat dateFormat;
    private SimpleDateFormat timeFormat;
    private static final  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Bind(R.id.snp_radiobutton_1_2)
    public SwipeNumberPicker swipeNumberPicker; // number picker

//    ed_place, ed_description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this); // 获取所有view
        setSupportActionBar(toolbar);

        this.setTranslucentStatusColor(this, R.color.actionbar_color); // 设计notificationbar的颜色

//        i_showProgressDialog();
        /* init time picker params */
        calendar = Calendar.getInstance();
        dateFormat = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
        timeFormat = new SimpleDateFormat(TIME_PATTERN, Locale.getDefault());
        tv_time.setOnClickListener(this);
        tv_date.setOnClickListener(this);
        update(); // update date
/*


*/
/* inflate radio groups *//*

        radioGroup1 = new StageCustomRadioGroup(
                new  ViewHolder(findViewById(R.id.sp_radiobutton_1_1), findViewById(R.id.iv_radiobutton_1_1)),
                new  ViewHolder(findViewById(R.id.snp_radiobutton_1_2), findViewById(R.id.iv_radiobutton_1_2)),
                new  ViewHolder(findViewById(R.id.tv_radiobutton_1_3), findViewById(R.id.iv_radiobutton_1_3)));

// set listener
        findViewById(R.id.rel_radiobutton_1_1).setOnClickListener(radioGroup1);

        findViewById(R.id.snp_radiobutton_1_2).setOnClickListener(radioGroup1);
        findViewById(R.id.tv_radiobutton_1_3).setOnClickListener(radioGroup1);
*/

        /* inflate radio groups */
        radioGroup2 = new StageCustomRadioGroup(
                new  ViewHolder(findViewById(R.id.tv_radiobutton_2_1), findViewById(R.id.iv_radiobutton_2_1)),
                new  ViewHolder(findViewById(R.id.tv_radiobutton_2_2), findViewById(R.id.iv_radiobutton_2_2)),
                new  ViewHolder(findViewById(R.id.tv_radiobutton_2_3), findViewById(R.id.iv_radiobutton_2_3)));

// set listener
        findViewById(R.id.tv_radiobutton_2_1).setOnClickListener(radioGroup2);
        findViewById(R.id.tv_radiobutton_2_2).setOnClickListener(radioGroup2);
        findViewById(R.id.tv_radiobutton_2_3).setOnClickListener(radioGroup2);

        /* inflate radio groups */
        radioGroup3 = new StageCustomRadioGroup(
                new  ViewHolder(findViewById(R.id.tv_radiobutton_3_1), findViewById(R.id.iv_radiobutton_3_1)),
                new  ViewHolder(findViewById(R.id.tv_radiobutton_3_2), findViewById(R.id.iv_radiobutton_3_2)),
                new  ViewHolder(findViewById(R.id.tv_radiobutton_3_3), findViewById(R.id.iv_radiobutton_3_3)));

        // set listener
        findViewById(R.id.tv_radiobutton_3_1).setOnClickListener(radioGroup3);
        findViewById(R.id.tv_radiobutton_3_2).setOnClickListener(radioGroup3);
        findViewById(R.id.tv_radiobutton_3_3).setOnClickListener(radioGroup3);


     /*   recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                //
                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rel_img_container.getLayoutParams();
                params.height = recyclerView.getWidth() / spanCount;
                rel_img_container.setLayoutParams(params);
            }
        });*/
     /* */  recyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
//                   这个时候当前空间已经自己设计好他的width 和height ， 可以获取了
//                 提前获取的话可能会出现一个为0的情况
                recyclerView.setMinimumHeight(((9/spanCount == 0? 9/spanCount+1 : 9/spanCount +2)) * getResources().getDisplayMetrics().widthPixels / spanCount); //height is ready
            }
        });

        adapter = new LaunchActivityImageAdapter(this, new ArrayList<String>());
        recyclerView.setLayoutManager(new GridLayoutManager(this, spanCount));
//        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        findViewById(R.id.iv_rellll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                picPhotoFromEmpty();
            }
        });

      /*  categoriez_spinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                findViewById(R.id.iv_radiobutton_1_1).setVisibility(View.INVISIBLE);
            }
        });
*/
        i_showProgressDialog();
        /* set spinner adapter */
        VolleyRequest.GetStringRequest(this, YunApi.CATEGORYZ, "", new VolleyOnResultListener() {

            @Override
            public void onSuccess(String response) {
                L.i(response);
                try {
                    Gson gson = new Gson();
                    JsonElement resp = gson.fromJson(response, JsonElement.class);
                    int error = resp.getAsJsonObject().get("error").getAsInt();
                    if (error == 0) {
                        final ActivityCategoryEntity[] activityCategoryEntities = gson.fromJson(resp.getAsJsonObject().get("data"), ActivityCategoryEntity[].class);
                        // 获取成功


                        spinner_adapter = new ArrayAdapter<String>(LaunchActivityActivity.this,
                                android.R.layout.simple_spinner_item, getStringListFrom(activityCategoryEntities));

                        categoriez_spinner.setAdapter(spinner_adapter);
                        spinner_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        spin.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                        ArrayAdapter<String> adapter = new ArrayAdapter<String>()
                        categoriez_spinner.setSelection(0);
                        i_dismissProgressDialog();

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    i_dismissProgressDialog();
                    i_showErrorDialog(getResources().getString(R.string.data_failure));
//                    LaunchActivityActivity.this.mSnack(getResources().getString(R.string.network_failure));
                    recyclerView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 网络异常
                            finish();
                        }
                    }, 2000);
                }
            }

            @Override
            public void onFailure(String error) {
                L.e(error);
                i_dismissProgressDialog();
                i_showErrorDialog(getResources().getString(R.string.network_failure));
//                LaunchActivityActivity.this.mSnack(getResources().getString(R.string.network_failure));
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // 网络异常
                        finish();
                    }
                }, 2000);
            }
        });

        bt_launch_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                L.d("title", ed_title.getText().toString());
                L.d("type", spinner_adapter.getItem((int) categoriez_spinner.getSelectedItemId()).toString()+" "+
                        swipeNumberPicker.getValue()+" "+isRecommandedSwicth.isChecked());
                L.d("sex", "" + radioGroup2.selected_tag);
                L.d("payingtype", "" + radioGroup3.selected_tag);
                L.d("place", "" + ed_place.getText().toString());
                L.d("description", "" + ed_description.getText().toString());
                L.d("picpaths", "" + adapter.getData().toString());
                if ("".equals(ed_title.getText().toString().trim())) {
                    mSnack(getString(R.string.title_empty_failure));
                    return;
                }
                if ("".equals(ed_place.getText().toString().trim())) {
                    mSnack(getString(R.string.place_empty_failure));
                    return;
                }
                if ("".equals(ed_description.getText().toString().trim())) {
                    mSnack(getString(R.string.description_empty_failure));
                    return;
                }
                i_showProgressDialog();
                (new UploadNewActivityTask(LaunchActivityActivity.this, new UploadActivityEntity(
                        ed_title.getText().toString(),
                        ed_description.getText().toString(),
                        ed_place.getText().toString(),
                        sdf.format(new Date(calendar.getTimeInMillis())), // date
                        isRecommandedSwicth.isChecked(),
                        ""+swipeNumberPicker.getValue(),
                        ""+categoriez_spinner.getSelectedItemId()+1,
                        ""+radioGroup3.selected_tag,
                        adapter.getData())))
                        .execute(new UploadFinishCallback() {
                            @Override
                            public void uploadDone() {
                                i_dismissProgressDialog();
                                mSnack(getString(R.string.upload_success), Snackbar.LENGTH_INDEFINITE);
                                bt_launch_activity.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                LaunchActivityActivity.this.finish();
                                            }
                                        });
                                    }
                                }, 2000);
                            }

                            @Override
                            public void uploadfailed(String rep) {
                                i_dismissProgressDialog();
                                i_showErrorDialog(rep);
                                bt_launch_activity.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                i_dismissErrorDialog();
                                            }
                                        });
                                    }
                                }, 2000);
                            }
                        });

            }
        });

        swipeNumberPicker.setOnValueChangeListener(new OnValueChangeListener() {
            @Override
            public boolean onValueChange(SwipeNumberPicker view, int oldValue, int newValue) {
                boolean isValueOk = (newValue > 0 && newValue <= 100);
                if (isValueOk) {

                    swipeNumberPicker.setText(Integer.toString(newValue) + "人");
//                    findViewById(R.id.iv_radiobutton_1_2).setVisibility(View.VISIBLE);
//                    radioGroup1.selected_tag="2";
//                    radioGroup1.select(2);
                } else {
                    mSnack("参加人数不行");
                }
                return isValueOk;
            }
        });
        swipeNumberPicker.setMaxValue(100);
        swipeNumberPicker.setMinValue(1);
        swipeNumberPicker.setValue(2, true);
    }



    private void update() {
        tv_date.setText(dateFormat.format(calendar.getTime()));
        tv_time.setText(timeFormat.format(calendar.getTime()));
    }

    private String[] getStringListFrom(ActivityCategoryEntity[] activityCategoryEntities) {

        String[] data_array = new String[activityCategoryEntities.length];
        for (int i = 0; i < activityCategoryEntities.length; i++) {
            data_array[i] = activityCategoryEntities[i].name;
        }
        return data_array;
    }


    LoadingDialog loading_dialog;
    ErrorDialog err_dialog;


    public void i_showProgressDialog() {
        i_dismissErrorDialog();
        i_dismissProgressDialog();
        loading_dialog = new LoadingDialog(this);
        loading_dialog.show();
    }

    public void i_showProgressDialog(String msg) {
        i_dismissErrorDialog();
        i_dismissProgressDialog();
        loading_dialog = new LoadingDialog(this, msg);
        loading_dialog.show();
    }


    public void i_dismissProgressDialog () {
        if (loading_dialog != null) {
            loading_dialog.cancel();
            loading_dialog.dismiss();
            loading_dialog = null;
        }
    }

    public void i_showErrorDialog() {
        i_dismissErrorDialog();
        i_dismissProgressDialog();
        err_dialog = new ErrorDialog(this);
        err_dialog.show();
    }

    public void i_showErrorDialog(String msg) {
        i_dismissErrorDialog();
        i_dismissProgressDialog();
        err_dialog = new ErrorDialog(this, msg);
        err_dialog.show();
    }

    public void i_dismissErrorDialog () {
        if (err_dialog != null) {
            err_dialog.cancel();
            err_dialog.dismiss();
            err_dialog = null;
        }
    }


    public void redrawRecyclerview(int position) {
        if (recyclerView != null) {

            recyclerView.refreshDrawableState();
            recyclerView.removeViewAt(position);
        }
    }

    public static void setTranslucentStatusColor(Activity activity, @ColorRes int color) {
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.KITKAT)
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

    @Override @OnClick (R.id.iv_back)
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.noanim, R.anim.noanim);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode== PickConfig.PICK_REQUEST_CODE){
            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            if (adapter != null) {
                adapter.clearAdapter();
                adapter.updateData(pick);
                recyclerView.setVisibility(View.VISIBLE);
                rel_img_container.refreshDrawableState();
            }
        }
        /* 如果是从浏览已选的照片返回来，就来看看状态 */
    }

    public void mSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

    public void mSnack (String s, int LENGTH) {
        Snackbar.make(getWindow().getDecorView(), s, LENGTH).show();
    }

    public void picPhotoFromEmpty () {
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(9)
                .spanCount(3)
                .toolbarColor(R.color.colorPrimary)
                .build();
        overridePendingTransition(R.anim.noanim,R.anim.noanim);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                DatePickerDialog.newInstance(LaunchActivityActivity.this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show(getFragmentManager(), "datePicker");
                break;
            case R.id.tv_time:
                TimePickerDialog.newInstance(LaunchActivityActivity.this, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show(getFragmentManager(), "timePicker");
                break;
        }
    }

    @Override
    public void onDateSet(DatePickerDialog dialog, int year, int monthOfYear, int dayOfMonth) {
        calendar.set(year, monthOfYear, dayOfMonth);
        update();
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);
        update();
    }
    private class StageCustomRadioGroup implements View.OnClickListener {

        //        ViewHolder vh1,   vh2,   vh3;
        ArrayList<ViewHolder> holders;
        String selected_tag = "1";


        public StageCustomRadioGroup(ViewHolder v1, ViewHolder v2, ViewHolder v3) {

            holders = new ArrayList<>();
            holders.add(v1);
            holders.add(v2);
            holders.add(v3);
        }

        @Override
        public void onClick(View v) {

            // put the others in a inactive state
            // keep the tag of the current
            int i = 0;
            for (ViewHolder holder: holders
                    ) {
                i++;
                if (v.equals(holder.tv_title)) {

                /* if i wasnt clicked, click it
                 * - save a tag inside.
                 * */
                    if (holder.tv_title instanceof TextView)
                        ((TextView)holder.tv_title).setTextColor(LaunchActivityActivity.this.getResources().getColor(R.color.black));

                    holder.iv_ic.setVisibility(View.VISIBLE);
//                    holder.tv_title.setTag(""+i);
                    selected_tag = ""+i;
                } else {

                /* unclick it if it was clicked. */
                    if (holder.tv_title instanceof TextView)
                        ((TextView)holder.tv_title).setTextColor(LaunchActivityActivity.this.getResources().getColor(R.color.gray));
                    holder.iv_ic.setVisibility(View.INVISIBLE);
//                    holder.tv_title.setTag("");
                }
            }
            if (v.equals(findViewById(R.id.rel_radiobutton_1_1))) {
                holders.get(0).iv_ic.setVisibility(View.VISIBLE);
//                holders.get(0).tv_title.setTag("");
                selected_tag = "1";
            }
        }

        public void select(int c) {
            for (int i = 0; i < 3; i++) {
                if (i+1 == c) {
                    holders.get(i).iv_ic.setVisibility(View.VISIBLE);
                    this.selected_tag = ""+c;
                } else {
                    holders.get(i).iv_ic.setVisibility(View.INVISIBLE);
                }
            }

            int i = 0;


        }
    }
    public class ViewHolder {

        public View tv_title;
        public ImageView iv_ic;

        public ViewHolder (View tv, View iv) {
            this.tv_title =  tv;
            this.iv_ic = (ImageView) iv;
        }


    }


}
