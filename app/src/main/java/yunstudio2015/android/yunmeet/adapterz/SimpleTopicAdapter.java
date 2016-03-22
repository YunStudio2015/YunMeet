package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.SimpleTopicItem;

/**
 * Created by lizhaotailang on 2016/2/29.
 */
public class SimpleTopicAdapter extends RecyclerView.Adapter<SimpleTopicAdapter.SimpleTopicViewHolder> {

    private final LayoutInflater inflater;
    private final Context context;
    private final List<SimpleTopicItem> data;

    private SimpleActivityAdapter.OnRecyclerViewItemClickListener mListener;

    public SimpleTopicAdapter(Context context,List<SimpleTopicItem> list){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;

    }
    @Override
    public SimpleTopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.simple_topic_item,parent,false);
        SimpleTopicViewHolder holder = new SimpleTopicViewHolder(view,context, mListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(SimpleTopicViewHolder holder, int position) {
        SimpleTopicItem item = data.get(position);
        holder.tvPubtime.setText(item.getPubtime());
        holder.tvContent.setText(item.getContent());
        Glide.with(context).load(item.getLogoUrl()).centerCrop().into(holder.ivLogo);
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setOnClickListener(SimpleActivityAdapter.OnRecyclerViewItemClickListener listener){
        this.mListener = listener;
    }

    public static class SimpleTopicViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView tvContent;
        private TextView tvPubtime;
        private ImageView ivLogo;
        private SimpleActivityAdapter.OnRecyclerViewItemClickListener listener;

        public SimpleTopicViewHolder(View itemView, final Context context,SimpleActivityAdapter.OnRecyclerViewItemClickListener listener) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_content);
            tvPubtime = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_pubtime);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_simple_list_logo);

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

}
