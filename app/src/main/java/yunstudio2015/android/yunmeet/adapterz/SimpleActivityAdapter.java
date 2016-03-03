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
        return new SimpleActivityViewHolder(inflater.inflate(R.layout.simple_activity_item,parent,false));
    }

    @Override
    public void onBindViewHolder(SimpleActivityViewHolder holder, int position) {
        SimpleActivityItem item = list.get(position);
        holder.tvDetail.setText(item.getDetail());
        holder.tvTheme.setText(item.getTheme());
        L.d(item.getDetail()+item.getTheme());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SimpleActivityViewHolder extends RecyclerView.ViewHolder{

        TextView tvTheme;
        TextView tvDetail;
        ImageView ivImage;

        public SimpleActivityViewHolder(View itemView) {
            super(itemView);

            tvTheme = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_theme);
            tvDetail = (TextView) itemView.findViewById(R.id.tv_simple_activity_item_detail);
        }
    }
}
