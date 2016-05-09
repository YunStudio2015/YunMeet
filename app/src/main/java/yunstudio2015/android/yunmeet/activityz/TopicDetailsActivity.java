package yunstudio2015.android.yunmeet.activityz;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.CircleImageView;
import yunstudio2015.android.yunmeet.customviewz.GridLayoutManAger;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsItemFragment;
import yunstudio2015.android.yunmeet.fragments.ShowPicturesFragment;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

import static yunstudio2015.android.yunmeet.adapterz.ChatTopicRecyclerviewAdapter.*;

/**
 * Created by lizhaotailang on 2016/3/22.
 */
public class TopicDetailsActivity extends AppCompatActivity implements
        ChatTopicsItemFragment.OnFragmentInteractionListener,
        ShowPicturesFragment.OnFragmentInteractionListener{

    private CircleImageView circleImageView;
    private TextView tvUserName;
    private TextView tvTopic;
    private TextView tvTime;
    private TextView tvLike;
    private TextView tvComment;

    private Gson gson;
    private ChatTopicViewHolder viewholder;

    private static final String FRG_SHOWPICTURE = "FRG_SHOWPICTURE";
    private ShowPicturesFragment showpictureFragment;


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (showpictureFragment != null && showpictureFragment.isVisible()) {
            // hide it
            hideShowPictureFragment();
        } else {
//            moveTaskToBack(true);
            super.onBackPressed();
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fullscreen_chattopic_list_item);


        if (gson == null) {
            gson = new Gson();
        }

        initViews(((ViewGroup)findViewById(android.R.id.content)).getChildAt(0));
        this.setTranslucentStatusColor(this, R.color.actionbar_color);

        Intent i = getIntent();
        ChatTopicEntity chattopic = (ChatTopicEntity) i.getSerializableExtra("topic");

        bindView(viewholder, chattopic);
    }

    // inflate the view
    private void updateView(ChatTopicEntity chatTopicEntity) {

        // get in this activity with all the informations necessary

        //

        tvTopic.setText(chatTopicEntity.content);
        tvUserName.setText(chatTopicEntity.nickname);
        tvTime.setText(chatTopicEntity.pubtime);
        String s = chatTopicEntity.for_num + "赞";
        tvLike.setText(s);
        s = chatTopicEntity.comment_num + "评论";
        tvComment.setText(s);
        Glide.with(TopicDetailsActivity.this)
                .load(chatTopicEntity.face)
                .centerCrop()
                .into(circleImageView);
    }


    public void mT(String mess) {
        Toast.makeText(this, mess, Toast.LENGTH_SHORT).show();
    }

    private void initViews(View view) {

     /*   circleImageView = (CircleImageView) findViewById(R.id.iv_launcher_pic);
        tvUserName = (TextView) findViewById(R.id.tv_username);
        tvTopic = (TextView) findViewById(R.id.tv_topic);
        tvTime = (TextView) findViewById(R.id.tv_publish_time);
        tvLike = (TextView) findViewById(R.id.tv_like);
        tvComment = (TextView) findViewById(R.id.tv_comment);
*/
        viewholder = new ChatTopicViewHolder(view);
    }

    private void bindView(final ChatTopicViewHolder holder, final ChatTopicEntity entity) {

        LayoutInflater inf = getLayoutInflater();
//        holder.lny_model.setActivated(true);
        if (holder.tmpd == null)
            holder.tmpd = entity.image;
        if (inf == null)
            inf = LayoutInflater.from(holder.grid_recycler_view.getContext());

        if (entity.image != null && entity.image.length > 1) {
            holder.iv_unique.setVisibility(View.GONE);
//            holder.grid_recycler_view.setHasFixedSize(true);
            GridLayoutManAger gr = new GridLayoutManAger(this, ChatTopicRecyclerviewAdapter.rowCount);
            holder.grid_recycler_view.setLayoutManager(gr);
            //  set up an adapter
            holder.grid_recycler_view.setAdapter(new ChatTopicRecyclerviewAdapter.GridInnerAdapter(UtilsFunctions.getImagesLink(entity.image)));
            holder.grid_recycler_view.setVisibility(View.VISIBLE);
        } else { // 当某个说说只有一张图片的时候

            holder.grid_recycler_view.setVisibility(View.GONE);
            if (entity.image != null && entity.image.length==1) {

                ImageLoader.getInstance().displayImage(entity.image[0].url.replace("t_256", "t_800"), holder.iv_unique);
                holder.iv_unique.setVisibility(View.VISIBLE);
               holder.iv_unique.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                                UtilsFunctions.encodedPath(gson.toJson(entity.image[0].url.replace("t_256", "t_800"))));
                        ((ChatTopicsItemFragment.OnFragmentInteractionListener) holder.iv_unique.getContext()).onFragmentInteraction(uri, null);
                    }
                });
            } else { // 当某个说说没有图片时
                holder.iv_unique.setVisibility(View.GONE);
            }
        }
        ImageLoader.getInstance().displayImage(entity.face, holder.iv_launcher);

        holder.iv_launcher.setImageResource(R.drawable.rowitem_bg);
        // set the others
        holder.tv_username.setText(entity.nickname);
        holder.tv_topic.setText(entity.content);
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

    private void hideShowPictureFragment() {

        if (showpictureFragment != null) {
//            mT ("hide n null");
            FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
//            trans.hide(showpictureFragment);
            trans.remove(showpictureFragment);
            showpictureFragment = null;
            trans.commit();
        }
    }


    @Override
    public void onFragmentInteraction(Uri uri, @Nullable String[] themelinks) {

        mT("dopskfoasjfoiasnd");

        showpictureFragment = (ShowPicturesFragment) getSupportFragmentManager().findFragmentByTag(FRG_SHOWPICTURE);

        /* so it is an image, then,   @Bind(R.id.toolbar)
    Toolbar toolbar at we should do is show it... then try to show the bigger picture */

        if (uri.getScheme().equals(AppConstants.scheme_photo)) {
            /* show the small picture and load for the bigger */
        }
        if (uri.getScheme().equals(AppConstants.scheme_ui)) {

            if (gson == null) {
                gson = new Gson();
            }
            String imgz = null;
            try {
                imgz = gson.fromJson(UtilsFunctions.decodedPath(uri.getPath()), String.class);
            } catch (Exception e) {
                L.e("isnt showing object");
            }
            //
            if (imgz != null) {
//                mT ("clicked on != null");
//                showpictureFragment.updateData(imgz, themelinks);
                FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                if (showpictureFragment == null) {
//                    mT ("create n  show");
                    showpictureFragment = ShowPicturesFragment.newInstance(imgz, themelinks);
                    trans.add(R.id.frame_picture_shower, showpictureFragment, FRG_SHOWPICTURE);
                }
                if (showpictureFragment.isVisible()) {
                    hideShowPictureFragment();
                }
                trans.commit();
            }
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        onFragmentInteraction(uri, null);
    }
}
