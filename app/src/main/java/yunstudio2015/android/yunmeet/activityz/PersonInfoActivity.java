package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import yunstudio2015.android.yunmeet.R;

/**
 * Created by lizhaotailang on 2016/2/26.
 */
public class PersonInfoActivity extends AppCompatActivity{

    private Toolbar toolbar;
    private Button btnTopic;
    private Button btnActivity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.personal_data);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initViews();

        this.setSupportActionBar(toolbar);
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        btnTopic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控制获得点击的按钮的背景色为绿色，字体颜色为白色
                //没有获得点击的按钮的背景色为白色，字体颜色为绿色
                btnTopic.setBackgroundColor(getResources().getColor(R.color.btn_background));
                btnTopic.setTextColor(Color.WHITE);
                btnActivity.setBackgroundColor(Color.WHITE);
                btnActivity.setTextColor(getResources().getColor(R.color.btn_background));
            }
        });

        btnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //控制获得点击的按钮的背景色为绿色，字体颜色为白色
                //没有获得点击的按钮的背景色为白色，字体颜色为绿色
                btnActivity.setBackgroundColor(getResources().getColor(R.color.btn_background));
                btnActivity.setTextColor(Color.WHITE);
                btnTopic.setBackgroundColor(Color.WHITE);
                btnTopic.setTextColor(getResources().getColor(R.color.btn_background));
            }
        });
    }

    private void initViews() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnTopic = (Button) findViewById(R.id.btn_person_info_topic);
        btnActivity = (Button) findViewById(R.id.btn_person_info_activity);

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
}
