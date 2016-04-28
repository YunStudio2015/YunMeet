package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.SimpleActivityItem;

/**
 * Created by lizhaotailang on 2016/3/3.
 */
public class SimpleActivityAdapter extends RecyclerView.Adapter<SimpleActivityAdapter.SimpleActivityViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private final List<SimpleActivityItem> list;
    private OnRecyclerViewItemClickListener mListener;

    public SimpleActivityAdapter(Context context,List<SimpleActivityItem> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public SimpleActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simple_activity_item,parent,false);
        SimpleActivityViewHolder holder = new SimpleActivityViewHolder(view,context, mListener);
        return  holder;
    }

    @Override
    public void onBindViewHolder(SimpleActivityViewHolder holder, int position) {
        SimpleActivityItem item = list.get(position);
        holder.tvDetail.setText(item.getDetail());
        holder.tvTheme.setText(item.getTheme());
        String s = item.getImage();
        s = s.replace("[","");
        s = s.replace("]","");
        s = s.replace("\\","");
        s = s.replace("\"","");
        Glide.with(context).load(s).centerCrop().into(holder.ivImage);
        Log.d("img",s);

        setAnimation(holder.layoutItem,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }

    public static class SimpleActivityViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvTheme;
        private TextView tvDetail;
        private ImageView ivImage;
        private OnRecyclerViewItemClickListener listener;
        private LinearLayout layoutItem;

        public SimpleActivityViewHolder(View itemView, final Context context,OnRecyclerViewItemClickListener listener) {
            super(itemView);

            tvTheme = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_theme);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_detail);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_simple_activtiy_image);
            layoutItem = (LinearLayout) itemView.findViewById(R.id.simple_activity_item);

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


    public static interface OnRecyclerViewItemClickListener{
        void onItemClick(View view,int position);
    }

    private void setAnimation(View viewToAnimation,int position){
        if (position > -1){
            Animation animation = AnimationUtils.loadAnimation(context,android.R.anim.slide_in_left);
            viewToAnimation.startAnimation(animation);
        }
    }
}
