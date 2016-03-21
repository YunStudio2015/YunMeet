package yunstudio2015.android.yunmeet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.florent37.hollyviewpager.HollyViewPagerBus;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.google.gson.Gson;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.ActivityDownloadEntity;

/**
 * Created by florentchampigny on 07/08/15.
 */
public class ScrollViewFragment extends Fragment {

    @Bind(R.id.scrollView)
    ObservableScrollView scrollView;
    private Context context;


    public static ScrollViewFragment newInstance(ActivityDownloadEntity tag){
        Bundle args = new Bundle();
        args.putString("tag", (new Gson()).toJson(tag));
        ScrollViewFragment fragment = new ScrollViewFragment();
        fragment.setArguments(args);
        return fragment;
    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_scroll, container, false);
        context = rootview.getContext();
        String json_data = (String) getArguments().get("tag");
        Gson gson = new Gson();
        ActivityDownloadEntity entity = gson.fromJson(json_data, ActivityDownloadEntity.class);

        /* set up the data inside the fragment */
        ViewHolder vh = new ViewHolder (rootview);

        inflateData(vh, entity);

        return rootview;
    }

    private void inflateData(ViewHolder vh, ActivityDownloadEntity entity) {

        /* inflate pictures */
      /*  Glide.with(context)
                .load(entity.image)
                .thumbnail(0.5f)
                .error(me.crosswall.photo.pick.R.drawable.default_error)
                .into(vh.iv_activity_bg);*/ // inflate background image
        Glide.with(context)
                .load(entity.face)
                .thumbnail(0.5f)
                .error(me.crosswall.photo.pick.R.drawable.default_error)
                .into(vh.iv_profile);

        /* inflate text'z */
        vh.tv_act_date_time.setText(entity.time);
        vh.tv_act_launch_time.setText(entity.pubtime);
        vh.tv_act_launcher_name.setText(entity.nickname);
        vh.tv_act_paymode.setText(entity.cost);
        vh.tv_act_people_count.setText(entity.pepnum+getResources().getString(R.string.meeting_people));
        vh.tv_activity_description.setText(entity.detail);
        vh.tv_activity_place.setText(entity.place);
        vh.tv_activity_title.setText(entity.theme);
        vh.tv_sex_ic.setText(entity.sex);
        vh.tv_comment_count.setText(entity.commentnum+getResources().getString(R.string.commentcount));
//        vh.tv_takepart_count.setText(entity.apply);
    }


    public class ViewHolder {

//        public ImageView iv_activity_bg;

        @Bind(R.id.iv_activity_owner)
        public ImageView iv_profile;
        /* textviews */

        // each data item is just a string in this case
        @Bind(R.id.tv_comment_count)
        public TextView tv_comment_count;

        @Bind(R.id.tv_takepart_count)
        public TextView tv_takepart_count;

        @Bind(R.id.tv_viewed_count)
        public TextView tv_viewed_count;

        @Bind(R.id.tv_activity_description)
        public TextView tv_activity_description;

        @Bind(R.id.tv_activity_place)
        public TextView tv_activity_place;

        @Bind(R.id.tv_act_date_time)
        public TextView tv_act_date_time;

        @Bind(R.id.tv_act_people_count)
        public TextView tv_act_people_count;

        @Bind(R.id.tv_act_paymode)
        public TextView tv_act_paymode;

        @Bind(R.id.tv_act_sex_ic)
        public TextView tv_sex_ic;

        @Bind(R.id.tv_act_launch_time)
        public TextView tv_act_launch_time;

        @Bind(R.id.tv_act_launcher_name)
        public TextView tv_act_launcher_name;

        @Bind(R.id.tv_activity_title)
        public TextView tv_activity_title;

        public ViewHolder(View v) {

            /* inflating */
            ButterKnife.bind(this, v);
        }
    }



    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        HollyViewPagerBus.registerScrollView(getActivity(), scrollView);
    }
}
