package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public SimpleTopicAdapter(Context context,List<SimpleTopicItem> list){

        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.data = list;

    }
    @Override
    public SimpleTopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleTopicViewHolder(inflater.inflate(R.layout.simple_topic_item,parent,false),context);
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

    public static class SimpleTopicViewHolder extends RecyclerView.ViewHolder{

        TextView tvContent;
        TextView tvPubtime;
        ImageView ivLogo;

        public SimpleTopicViewHolder(View itemView, final Context context) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_content);
            tvPubtime = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_pubtime);
            ivLogo = (ImageView) itemView.findViewById(R.id.iv_simple_list_logo);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

}
