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
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import yunstudio2015.android.yunmeet.customviewz.SquareImageView;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.entityz.Imagee;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsItemFragment;
import yunstudio2015.android.yunmeet.interfacez.TriggerLoadMore;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * Created by Ulrich on 3/18/2016.
 */
public class ChatTopicRecyclerviewAdapter extends RecyclerView.Adapter<ChatTopicRecyclerviewAdapter.ChatTopicViewHolder> {


    private final TriggerLoadMore lm;
    List<ChatTopicEntity> data = null;

    // view types
    private final int TYPE_LOADER = 111, TYPE_ITEM = 222;
    private Drawable placeholder = null;
    private static LayoutInflater inf;
    private Context ctx  = null;
    private static Gson gson;
    public static int rowCount = 3;

    public ChatTopicRecyclerviewAdapter(List<ChatTopicEntity> data, TriggerLoadMore lm) {

        this.data = data;
        this.lm = lm;
        gson = new Gson();
    }

    public void setData (List<ChatTopicEntity> data) {

        this.data.addAll(data);
        notifyDataSetChanged();
    }


    public void append(List<ChatTopicEntity> images) {
        int positionStart = data.size();
        int itemCount = images.size();
        data.addAll(images);
        if (positionStart > 0 && itemCount > 0) {
            notifyItemRangeInserted(positionStart, itemCount);
        } else {
            notifyDataSetChanged();
        }
    }


    @Override
    public ChatTopicViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chattopic_list_item, parent, false);
        return (new ChatTopicViewHolder(view));
    }



    @Override
    public void onBindViewHolder(ChatTopicViewHolder hld, final int position) {

        final ChatTopicEntity entity = data.get(position);
        ChatTopicViewHolder holder = (ChatTopicViewHolder) hld;
        holder.lny_model.setActivated(true);
        if (holder.tmpd == null)
            holder.tmpd = entity.image;
        if (inf == null)
            inf = LayoutInflater.from(holder.grid_recycler_view.getContext());
        if (ctx == null)
            ctx = holder.grid_recycler_view.getContext();
        if (entity.image != null && entity.image.length > 1) {
            holder.iv_unique.setVisibility(View.GONE);
//            holder.grid_recycler_view.setHasFixedSize(true);
            GridLayoutManAger gr = new GridLayoutManAger(ctx, rowCount);
            holder.grid_recycler_view.setLayoutManager(gr);
            //  set up an adapter
            holder.grid_recycler_view.setAdapter(new GridInnerAdapter(UtilsFunctions.getImagesLink(entity.image)));
            holder.grid_recycler_view.setVisibility(View.VISIBLE);
        } else { // 当某个说说只有一张图片的时候

            holder.grid_recycler_view.setVisibility(View.GONE);
            if (entity.image != null && entity.image.length==1) {

                ImageLoader.getInstance().displayImage(entity.image[0].url.replace("t_256", "t_800"), holder.iv_unique);
                holder.iv_unique.setVisibility(View.VISIBLE);
                holder.iv_unique.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                                UtilsFunctions.encodedPath(gson.toJson(entity.image[0].url.replace("t_256", "t_800"))));
                        ((ChatTopicsItemFragment.OnFragmentInteractionListener) ctx).onFragmentInteraction(uri, null);
                    }
                });
            } else { // 当某个说说没有图片时
                holder.iv_unique.setVisibility(View.GONE);
            }
        }
        ImageLoader.getInstance().displayImage(entity.face, holder.iv_launcher);

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

    public List<ChatTopicEntity> getData() {
        return data;
    }


    public   static class ChatTopicViewHolder extends  RecyclerView.ViewHolder {

        @Bind(R.id.lny_model)
        public LinearLayout lny_model;

        @Bind(R.id.iv_launcher_pic)
        public ImageView iv_launcher;

        @Bind(R.id.tv_username)
        public EmojiconTextView tv_username;

        @Bind(R.id.tv_topic)
        public  EmojiconTextView tv_topic;

        @Bind(R.id.iv_unique)
        public ImageView iv_unique;

        @Bind(R.id.grid_recycler_view)
        public RecyclerView grid_recycler_view;

        @Bind(R.id.lny_comments)
        public LinearLayout lny_comments;



        @Bind(R.id.lny_picture_content)
        RelativeLayout lny_picture_content;

        public Imagee[] tmpd = null;

        public ChatTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            grid_recycler_view.addItemDecoration(new GridItemDecoration(3));
            // set up different patterns for different size of data array
            lny_picture_content.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

                 /*   RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) lny_picture_content.getLayoutParams();
                    layoutParams.height = ctx.getResources().getDisplayMetrics().heightPixels/3;
                    lny_picture_content.setLayoutParams(layoutParams);*/
                }
            });
        }
    }


    public static class GridInnerAdapter extends RecyclerView.Adapter {

        private String[] imgd = null;

        public GridInnerAdapter(String[] imgd) {
            this.imgd = imgd;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new InnerViewHolder(inf.inflate(R.layout.row_item, parent, false));
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

            ((InnerViewHolder)holder).iv.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {

//                  int w = ctx.getResources().getDisplayMetrics().widthPixels/5;
                    int w =  ((InnerViewHolder) holder).iv.getWidth();
                    ((InnerViewHolder) holder).iv.setMaxHeight(w);
//                    ((InnerViewHolder) holder).iv.setMaxWidth(w);

               /*     int w,h;
                    w = h = ctx.getResources().getDisplayMetrics().widthPixels/5;
                    RecyclerView.LayoutParams layoutparams = new  RecyclerView.LayoutParams(w,h);
                    ((InnerViewHolder) holder).iv.setLayoutParams(layoutparams);*/
                }
            });

            ((InnerViewHolder)holder).iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(imgd[position], ((InnerViewHolder)holder).iv);
            ((InnerViewHolder)holder).iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                            UtilsFunctions.encodedPath(gson.toJson(imgd[position])));
                    ((ChatTopicsItemFragment.OnFragmentInteractionListener) ((InnerViewHolder) holder).iv.getContext()).onFragmentInteraction(uri, imgd);
                }
            });
        }

        @Override
        public int getItemCount() {
            return imgd.length;
        }

        public class InnerViewHolder extends RecyclerView.ViewHolder {

            @Bind(R.id.image)
            public SquareImageView iv;

            public InnerViewHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }

    public static class GridItemDecoration extends RecyclerView.ItemDecoration {

        private final int space;

        public GridItemDecoration(int space) {
            this.space =space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
         /*   outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;*/

            L.d("xxx", "parent position "+parent.getChildLayoutPosition(view));
            if (parent.getChildLayoutPosition(view) / rowCount != 0) {
                outRect.top = space;
            }
            if (parent.getChildLayoutPosition(view) % rowCount != 2) {
                outRect.right = space;
            }

            // Add top margin only for the first item to avoid double space between items
           /* if (parent.getChildLayoutPosition(view)%rowCount != 0) {
                outRect.top = space;
            }*//* else {
                outRect.top = 0;
            }*/
        }
    }

}
