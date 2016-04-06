package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.rockerhieu.emojicon.EmojiconTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.customviewz.GridLayoutManAger;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsMainFragment;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * Created by Ulrich on 3/18/2016.
 */
public class ChatTopicRecyclerviewAdapter extends RecyclerView.Adapter<ChatTopicRecyclerviewAdapter.ViewHolder> {


    List<ChatTopicEntity> data = null;

    // view types
    private final int TYPE_LOADER = 111, TYPE_ITEM = 222;


    private String[] tmpdata = new String[]{
            "http://img3.imgtn.bdimg.com/it/u=2185556379,1542027630&fm=21&gp=0.jpg",
            "http://e.hiphotos.baidu.com/zhidao/pic/item/08f790529822720e68560ffe78cb0a46f21fab1e.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/d439b6003af33a870b354c87c45c10385243b5d7.jpg",
            "http://img4.duitang.com/uploads/item/201203/02/20120302210557_KSWNP.thumb.600_0.jpeg",
            "http://a.hiphotos.baidu.com/zhidao/pic/item/bd3eb13533fa828b26108612ff1f4134970a5a0b.jpg",
            "http://pica.nipic.com/2007-12-25/2007122515616115_2.jpg",
            "http://img4.imgtn.bdimg.com/it/u=20230736,313391552&fm=21&gp=0.jpg",
            "http://imgstore.cdn.sogou.com/app/a/100540002/710338.jpg",
            "http://c.hiphotos.baidu.com/zhidao/pic/item/d788d43f8794a4c22f0aafe10cf41bd5ac6e39ca.jpg",
            "http://g.hiphotos.baidu.com/zhidao/pic/item/77c6a7efce1b9d16eb0ad811f2deb48f8d5464f4.jpg",
            "http://img3.3lian.com/2014/c2/88/d/79.jpg",
            "http://f.hiphotos.baidu.com/zhidao/pic/item/9213b07eca806538514d9b1f96dda144ad348212.jpg"
    };

    private String[] profilepics = new String[] {
            "http://img4.imgtn.bdimg.com/it/u=227263614,2029134844&fm=21&gp=0.jpg",
            "http://img0.imgtn.bdimg.com/it/u=3904141079,82773253&fm=21&gp=0.jpg",
            "http://ww2.sinaimg.cn/crop.0.0.1536.1536.1024/73213515jw8eswynq2pm8j216o16otce.jpg",
            "http://cdn.duitang.com/uploads/item/201409/25/20140925132115_yrR8V.thumb.700_0.jpeg",
            "http://ww2.sinaimg.cn/crop.0.0.1080.1080.1024/8ddfc6e6jw8emfii0u05aj20u00u0782.jpg",
            "http://cdn.duitang.com/uploads/item/201507/29/20150729184755_3PEkC.jpeg",
            "http://bcs.kuaiapk.com/rbpiczy/Wallpaper/2013/9/18/ce3ce7b02b1d4a769e27946b1d8f69f8.jpg"
    };


    private Drawable placeholder = null;
    private LayoutInflater inf;
    private Context ctx  = null;
    private Gson gson;

    public ChatTopicRecyclerviewAdapter(List<ChatTopicEntity> data) {

        this.data = data;
        gson = new Gson();
    }

    public void setData (List<ChatTopicEntity> data) {

        this.data.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
//        return super.getItemViewType(position);
       /* if (position < data.size()){
            return TYPE_ITEM;
        } else {
            return TYPE_LOADER;
        }*/
        return TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chattopic_list_item, parent, false);
        return (new ChatTopicViewHolder(view));
    }


    @Override
    public void onBindViewHolder(ViewHolder hld, int position) {

        ChatTopicEntity entity = data.get(position);
        ChatTopicViewHolder holder = (ChatTopicViewHolder) hld;
        holder.lny_model.setActivated(true);
        if (holder.tmpd == null)
            holder.tmpd = entity.image;
        holder.grid_recycler_view.setHasFixedSize(true);
        if (inf == null)
            inf = LayoutInflater.from(holder.grid_recycler_view.getContext());
        if (ctx == null)
            ctx = holder.grid_recycler_view.getContext();
        if (entity.image != null && entity.image.length > 1) {
            holder.iv_unique.setVisibility(View.GONE);
            int rowCount = 0;
            if (entity.image.length == 1 || entity.image.length == 2) {
                rowCount = entity.image.length;
            } else if (entity.image.length > 2) {
                rowCount = 3;
            }
            GridLayoutManAger gr = new GridLayoutManAger(ctx, rowCount);
            holder.grid_recycler_view.setLayoutManager(gr);
            //  set up an adapter
            holder.grid_recycler_view.setAdapter(new GridInnerAdapter(entity.image));
            holder.grid_recycler_view.setVisibility(View.VISIBLE);
        } else {
            holder.grid_recycler_view.setVisibility(View.GONE);
            if (entity.image != null && entity.image.length==1) {
                L.d("setting one img for position "+position);
                Glide.with(((ChatTopicViewHolder) hld).iv_launcher.getContext())
                        .load(entity.image[0])
//                        .centerCrop()
//                    .thumbnail(0.3f)
                        .error(me.crosswall.photo.pick.R.drawable.default_error)
                        .into(holder.iv_unique);
                holder.iv_unique.setVisibility(View.VISIBLE);
            } else {
                holder.iv_unique.setVisibility(View.GONE);
            }
        }
        Glide.with(((ChatTopicViewHolder) hld).iv_launcher.getContext())
                .load(entity.face)
                .centerCrop()
//                    .thumbnail(0.3f)
                .error(me.crosswall.photo.pick.R.drawable.default_error)
                .into(holder.iv_launcher);
        holder.iv_launcher.setImageResource(R.drawable.rowitem_bg);
        // set the others
        holder.tv_username.setText(entity.nickname);
        holder.tv_topic.setText(entity.content);
    }

    private int getRandomInf(int length) {

        // get random value between 0 and length
        return showRandomInteger(0, length-1, new Random());
    }


    private static int showRandomInteger(int aStart, int aEnd, Random aRandom){
        if (aStart > aEnd) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = (long)aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        int randomNumber =  (int)(fraction + aStart);
        return randomNumber;
    }


    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void addEntry(ChatTopicEntity entity) {
        if (data == null)
            data = new ArrayList<>();
        data.add(entity);
    }

    private class LoaderLayoutHolder extends ViewHolder {

        public LoaderLayoutHolder(View view) {
            super(view);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }

    public class ChatTopicViewHolder extends  ViewHolder {

        @Bind(R.id.lny_model)
        LinearLayout lny_model;

        @Bind(R.id.iv_launcher_pic)
        public ImageView iv_launcher;

        @Bind(R.id.tv_username)
        public EmojiconTextView tv_username;

        @Bind(R.id.tv_topic)
        public  EmojiconTextView tv_topic;

        @Bind(R.id.iv_unique)
        ImageView iv_unique;

        @Bind(R.id.grid_recycler_view)
        public RecyclerView grid_recycler_view;

        @Bind(R.id.lny_comments)
        LinearLayout lny_comments;

        public String[] tmpd = null;

        public ChatTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            grid_recycler_view.addItemDecoration(new GridItemDecoration(5));
            // set up different patterns for different size of data array
        }
    }


    public class GridInnerAdapter extends RecyclerView.Adapter {

        private String[] imgd = null;

        public GridInnerAdapter(String[] imgd) {
            this.imgd = imgd;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InnerViewHolder(inf.inflate(R.layout.row_item, null));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            ((InnerViewHolder)holder).iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewGroup.LayoutParams layoutParams = ((InnerViewHolder) holder).iv.getLayoutParams();
                    ((InnerViewHolder)holder).iv.setLayoutParams(layoutParams);
                }
            });
            Glide.with(ctx)
                    .load(imgd[position])
                    .placeholder(placeholder)
                    .centerCrop()
//                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(((InnerViewHolder)holder).iv);
            ((InnerViewHolder)holder).iv.setImageResource(R.drawable.rowitem_bg);
            ((InnerViewHolder)holder).iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                            UtilsFunctions.encodedPath(gson.toJson(imgd[position])));
                    ((ChatTopicsMainFragment.OnFragmentInteractionListener) ctx).onFragmentInteraction(uri, imgd);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imgd.length;
        }

        public class InnerViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.image)
            public ImageView iv;

            public InnerViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public class GridItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public GridItemDecoration(int space) {
            this.space =space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;

            // Add top margin only for the first item to avoid double space between items
            if (parent.getChildLayoutPosition(view) == 0) {
                outRect.top = space;
            } else {
                outRect.top = 0;
            }
        }
    }

}
