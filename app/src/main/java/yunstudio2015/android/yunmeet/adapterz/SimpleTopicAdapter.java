package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;

/**
 * Created by lizhaotailang on 2016/2/29.
 */
public class SimpleTopicAdapter extends RecyclerView.Adapter<SimpleTopicAdapter.SimpleTopicViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private   List<ChatTopicEntity> data;

    private SimpleActivityAdapter.OnRecyclerViewItemClickListener mListener;

    public SimpleTopicAdapter(Context context,ChatTopicEntity[]  list){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        data = new ArrayList<>();
        for (ChatTopicEntity dt:  list) {
            data.add(dt);
        }
    }


    @Override
    public SimpleTopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simple_topic_item,parent,false);
        SimpleTopicViewHolder holder = new SimpleTopicViewHolder(view,context, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleTopicViewHolder holder, int position) {
        ChatTopicEntity item = data.get(position);
        holder.tvPubtime.setText(item.pubtime);
        holder.tvContent.setText(item.content);
//        L.d("img is "+item.getLogoUrl());
        if (item.image != null && item.image.length >0) {
            try {
                Glide.with(context).load(item.image[0].url).error(R.drawable.error_img).centerCrop().into(holder.ivLogo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            holder.ivLogo.setVisibility(View.GONE);
        }
        setAnimation(holder.layoutItem,position);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener(SimpleActivityAdapter.OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }

    public void append(ChatTopicEntity[] dd) {
        if (data == null)
            data = new ArrayList<>();
        for (ChatTopicEntity dt:
                dd) {
            data.add(dt);
        }
        notifyDataSetChanged();
    }

    public static class SimpleTopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvContent;
        private TextView tvPubtime;
        private ImageView ivLogo;
        private LinearLayout layoutItem;
        private SimpleActivityAdapter.OnRecyclerViewItemClickListener listener;

        public SimpleTopicViewHolder(View itemView, final Context context,SimpleActivityAdapter.OnRecyclerViewItemClickListener listener) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_content);
            tvPubtime = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_pubtime);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_simple_list_logo);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.simple_topic_item);

            this.listener = listener;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    private void setAnimation(View viewToAnimation,int position){
        if (position > -1){
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            viewToAnimation.startAnimation(animation);
        }
    }

}
