package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.crosswall.photo.pick.PickConfig;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.LaunchTopicImageAdapter;
import yunstudio2015.android.yunmeet.adapterz.TextWatcherAdapter;
import yunstudio2015.android.yunmeet.customviewz.LoadingDialog;
import yunstudio2015.android.yunmeet.interfacez.UploadFinishCallBack;
import yunstudio2015.android.yunmeet.utilz.UploadNewTopicTask;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * Created by Ulrich on 1/29/2016.
 */
public class LaunchChatTopicActivity extends AppCompatActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener{

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.ed_new_topic)
    EmojiconEditText ed_text_zone;

    @Bind(R.id.rel_pic_container)
    RelativeLayout empty_pic_container;

    @Bind(R.id.gv_img)
    RecyclerView pickRecyclerview;

    @Bind(R.id.add_emoji)
    ImageView iv_add_emoji;

    @Bind(R.id.emojicons)
    LinearLayout emoji_fragment;

    @Bind(R.id.btn_finish_launch)
    Button btn_finish_launch;

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
                /* 这个时候当前空间已经自己设计好他的width 和height ， 可以获取了*/
              /* 提前获取的话可能会出现一个为0的情况*/
                empty_pic_container.setMinimumHeight(8 * empty_pic_container.getWidth() / 9); //height is ready
            }
        });
        pickRecyclerview.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                  /* 这个时候当前空间已经自己设计好他的width 和height ， 可以获取了*/
                /* 提前获取的话可能会出现一个为0的情况*/
                pickRecyclerview.setMinimumHeight(4 * getResources().getDisplayMetrics().widthPixels / 5); //height is ready
            }
        });

        /* 添加 emoticons */
        iv_add_emoji.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) LaunchChatTopicActivity.this
                        .getSystemService(Context.INPUT_METHOD_SERVICE);

                if (emoji_fragment.getVisibility() == View.VISIBLE)
                    emoji_fragment.setVisibility(View.GONE);
                else
                    emoji_fragment.setVisibility(View.VISIBLE);
                if (imm.isAcceptingText()) {
                    if (v != null) {
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                }
            }
        });

        iv_add_emoji.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    emoji_fragment.setVisibility(View.GONE);
                mT("emojin hasfocus " + hasFocus);
            }
        });

        ed_text_zone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emoji_fragment.getVisibility() == View.VISIBLE)
                    emoji_fragment.setVisibility(View.GONE);
            }
        });

        emoji_fragment.setVisibility(View.GONE);

    }


    @OnClick(R.id.btn_finish_launch)
    public void launchChatTopic () {

        if (ed_text_zone != null && !"".equals(ed_text_zone.getText().toString().trim())) {


            String content = ed_text_zone.getText().toString();
            String[] imgPathz = (String[]) adapter.getData().toArray(); // get file paths list

            i_showProgressDialog();
            // start uploading
            (new UploadNewTopicTask("", content, imgPathz)).execute(new UploadFinishCallBack() {
                @Override
                public void uploadDone() {
                    i_dismissProgressDialog();
                    mSnack(getString(R.string.upload_success));
                }

                @Override
                public void uploadfailed() {
                    i_dismissProgressDialog();
                    mSnack(getString(R.string.upload_failure));
                }
            });
        } else {

            mSnack(getString(R.string.canbenull));
        }
    }

    private void mSnack(String s) {
        Snackbar.make(getWindow().getDecorView(), s, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {

        if (emoji_fragment.getVisibility() == View.VISIBLE) {
            emoji_fragment.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }

    private void mT(String s) {
        Toast.makeText(LaunchChatTopicActivity.this, s, Toast.LENGTH_SHORT).show();
    }

    LoadingDialog dialog;
    public void i_showProgressDialog() {
        dialog = new LoadingDialog(this);
        dialog.show();
    }

    public void i_showProgressDialog(String mess) {
        dialog = new LoadingDialog(this, mess);
        dialog.show();
    }

    public void i_dismissProgressDialog () {
        if (dialog != null) {
            dialog.cancel();
            dialog.dismiss();
            dialog = null;
        }
    }

    @OnClick(R.id.add_emoji)
    public void add_emoji() {

        /* 隐藏掉键盘 */

        /* 显示或隐藏带着emoticons的viewpager */
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_OK){
            return;
        }
        if(requestCode==PickConfig.PICK_REQUEST_CODE){
            ArrayList<String> pick = data.getStringArrayListExtra(PickConfig.EXTRA_STRING_ARRAYLIST);
            if (adapter != null) {
                adapter.clearAdapter();
                adapter.updateData(pick);
                empty_pic_container.setVisibility(View.GONE);
                pickRecyclerview.setVisibility(View.VISIBLE);
            }
        }
        /* 如果是从浏览已选的照片返回来，就来看看状态 */
    }


    @Override    @OnClick(R.id.iv_back)
    public void finish() {
        super.finish();
        this.overridePendingTransition(android.R.anim.slide_in_left, R.anim.noanim);
    }


    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(ed_text_zone);
    }


    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
//        ed_text_zone.append(emojicon.getEmoji());
        EmojiconsFragment.input(ed_text_zone, emojicon);
    }
}
