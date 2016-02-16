package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import me.crosswall.photo.pick.util.UriUtil;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.activityz.BrowseSelectedPicturesActivity;
import yunstudio2015.android.yunmeet.activityz.LaunchChatTopicActivity;

/**
 * Created by Ulrich on 1/31/2016.
 */
public class LaunchTopicImageAdapter extends RecyclerView.Adapter<LaunchTopicImageAdapter.ViewHolder>{


    private ArrayList<String> data;
    private int widget;
    private Context context;

    public LaunchTopicImageAdapter(Context context, ArrayList<String> data) {
        this.data = data;
        this.context = context;
        widget = context.getResources().getDisplayMetrics().widthPixels/ LaunchChatTopicActivity.spanCount;
    }

    @Override
    public LaunchTopicImageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // inflate imageview.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo_view, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final LaunchTopicImageAdapter.ViewHolder holder, final int position) {

        holder.setData(data.get(position).toString());
        /* 删掉当前图标、在array里面删掉、并隐藏掉图片 */
        holder.delete_me_drawable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete with position
                holder.itemView.setVisibility(View.GONE);
                removeAt (position);
            }
        });

        /* 点击摸个图片都会使图片url组打开一个viewpager浏览图片 */
        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityFromPosition(position); /* 创建浏览图片的activity并从position开始浏览 */
            }
        });

    }

    private void startActivityFromPosition(int position) {

        Intent intent = new Intent(((LaunchChatTopicActivity) this.context), BrowseSelectedPicturesActivity.class);
        intent.putStringArrayListExtra("data",data);
        intent.putExtra("position", position);
        ((LaunchChatTopicActivity) this.context).startActivity(intent);
    }

    private void removeAt(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
//        notifyItemRangeChanged(position, data.size());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData (ArrayList<String> paths) {
        this.data = paths;
        notifyDataSetChanged();
    }

    public void clearAdapter() {
        if (data != null)
            data.clear ();
    }

    public void addData(ArrayList<String> pick) {
        if (data == null)
            data = new ArrayList<>();
        this.data.addAll(pick);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        ImageView iv;
        View delete_me_drawable;

        public ViewHolder(View itemView) {
            super(itemView);
            iv = (ImageView) itemView.findViewById(R.id.photo_thumbview);
            delete_me_drawable = itemView.findViewById(R.id.delete_me_drawable);
            context = iv.getContext();
        }

        public void setData(String imgPath){

            Uri uri = UriUtil.generatorUri(imgPath,UriUtil.LOCAL_FILE_SCHEME);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(widget,widget);
            params.setMargins(5,5,5,5);
            iv.setLayoutParams(params);
//            Uri uri = Uri.fromFile(new File(imgPath));
            Glide.with(context)
                    .load(uri)
                    .centerCrop()
                    .placeholder(iv.getDrawable())
                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(iv);
        }

    }

}
