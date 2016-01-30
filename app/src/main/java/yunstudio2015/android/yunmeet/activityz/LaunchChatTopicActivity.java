package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.crosswall.photo.pick.PickConfig;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.LaunchTopicImageAdapter;

/**
 * Created by Ulrich on 1/29/2016.
 */
public class LaunchChatTopicActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ed_new_topic)
    EditText ed_text_zone;

    @Bind(R.id.rel_pic_container)
    RelativeLayout empty_pic_container;

    @Bind(R.id.gv_img)
    RecyclerView pickRecyclerview;

    /* 展示选出来的照片的adapter */
    LaunchTopicImageAdapter adapter;

    public final static int spanCount = 4;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_topic);
        ButterKnife.bind(this);
        this.setSupportActionBar(toolbar); // 吧xml里面的toolbar设置成当前界面的actionbar
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        adapter = new LaunchTopicImageAdapter(this, new ArrayList<String>());
        pickRecyclerview.setLayoutManager(new GridLayoutManager(this, spanCount));
        pickRecyclerview.setAdapter(adapter);

        empty_pic_container.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                empty_pic_container.setMinimumHeight(8*empty_pic_container.getWidth()/9); //height is ready
            }
        });
        pickRecyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                pickRecyclerview.setMinimumHeight(4*getResources().getDisplayMetrics().widthPixels/5); //height is ready
            }
        });


    }

    @OnClick(R.id.iv_add_pic)
    public void picPhotoFromEmpty () {
        new PickConfig.Builder(this)
                .pickMode(PickConfig.MODE_MULTIP_PICK)
                .maxPickSize(9)
                .spanCount(3)
                .toolbarColor(R.color.colorPrimary)
                .build();
        overridePendingTransition(R.anim.noanim,R.anim.noanim);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode==PickConfig.PICK_REQUEST_CODE){
            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            Toast.makeText(this,"pick size:"+pick.size(),Toast.LENGTH_SHORT).show();
            if (adapter != null) {
                adapter.clearAdapter();
                adapter.updateData(pick);
                empty_pic_container.setVisibility(View.GONE);
                pickRecyclerview.setVisibility(View.VISIBLE);
            }
        }
    }

}
