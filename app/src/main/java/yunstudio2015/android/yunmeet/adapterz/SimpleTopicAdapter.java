package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.commonLogs.L;
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
        return new SimpleTopicViewHolder(inflater.inflate(R.layout.simple_topic_item,parent,false));
    }

    @Override
    public void onBindViewHolder(SimpleTopicViewHolder holder, int position) {
        SimpleTopicItem item = data.get(position);
        holder.tvPubtime.setText(item.getPubtime());
        holder.tvContent.setText(item.getContent());
        L.d(item.getContent()+item.getPubtime());
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class SimpleTopicViewHolder extends RecyclerView.ViewHolder{

        TextView tvContent;
        TextView tvPubtime;
        ImageView ivFace;

        public SimpleTopicViewHolder(View itemView) {
            super(itemView);

            tvContent = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_content);
            tvPubtime = (TextView) itemView.findViewById(R.id.tv_simple_topic_item_pubtime);
        }
    }

}
