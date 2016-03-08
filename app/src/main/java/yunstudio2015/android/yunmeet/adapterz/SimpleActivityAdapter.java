package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    public SimpleActivityAdapter(Context context,List<SimpleActivityItem> list){
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
    }

    @Override
    public SimpleActivityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SimpleActivityViewHolder(inflater.inflate(R.layout.simple_activity_item,parent,false),context);
    }

    @Override
    public void onBindViewHolder(SimpleActivityViewHolder holder, int position) {
        SimpleActivityItem item = list.get(position);
        holder.tvDetail.setText(item.getDetail());
        holder.tvTheme.setText(item.getTheme());
        Glide.with(context).load(item.getImage()).centerCrop().into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SimpleActivityViewHolder extends RecyclerView.ViewHolder{

        TextView tvTheme;
        TextView tvDetail;
        ImageView ivImage;

        public SimpleActivityViewHolder(View itemView, final Context context) {
            super(itemView);

            tvTheme = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_theme);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_detail);
            ivImage = (ImageView) itemView.findViewById(R.id.iv_simple_activtiy_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context,String.valueOf(getLayoutPosition()),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
