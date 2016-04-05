package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adhamenaya.androidmosaiclayout.listeners.OnItemClickListener;
import com.adhamenaya.androidmosaiclayout.views.BlockPattern;
import com.adhamenaya.androidmosaiclayout.views.MosaicLayout;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.Bind;
import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.app.AppConstants;
import yunstudio2015.android.yunmeet.commonLogs.L;
import yunstudio2015.android.yunmeet.entityz.ChatTopicDownloadEntity;
import yunstudio2015.android.yunmeet.entityz.ChatTopicEntity;
import yunstudio2015.android.yunmeet.fragments.ChatTopicsMainFragment;
import yunstudio2015.android.yunmeet.utilz.UtilsFunctions;

/**
 * Created by Ulrich on 3/18/2016.
 */
public class ChatTopicRecyclerviewAdapter extends RecyclerView.Adapter<ChatTopicRecyclerviewAdapter.ViewHolder> {


    List<Object> data = null;

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

    public BlockPattern.BLOCK_PATTERN[][] patternz = {AppConstants.pattern1, AppConstants.pattern2,
            AppConstants.pattern3, AppConstants.pattern4,
            AppConstants.pattern5, AppConstants.pattern6,
            AppConstants.pattern7, AppConstants.pattern8};

    private ColorDrawable placeholder = null;

    public ChatTopicRecyclerviewAdapter(List<Object> data) {

        this.data = data;
    }

    public void setData (List<Object> data) {

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

            ChatTopicEntity entity = (ChatTopicEntity) data.get(position);

            ChatTopicViewHolder holder = (ChatTopicViewHolder) hld;
            if (holder.tmpd == null)
                holder.tmpd = entity.image;
            /*new String[]{
                        tmpdata[getRandomInf(tmpdata.length)],  tmpdata[getRandomInf(tmpdata.length)],
                        tmpdata[getRandomInf(tmpdata.length)],  tmpdata[getRandomInf(tmpdata.length)]
                        ,  tmpdata[getRandomInf(tmpdata.length)]};*/
            MosaicLayoutAdapter mAdapater = new MosaicLayoutAdapter(holder.iv_launcher.getContext(), holder.tmpd);
            if (holder.tmpd.length >= 1 && holder.tmpd.length <= 8) {
                holder.mosaicLayout.addPattern(patternz[holder.tmpd.length-1]);
            }
            holder.mosaicLayout.setAdapter(mAdapater);
            holder.mosaicLayout.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onClick(int position) {
                    L.d("clicked on nb "+position);
                }
            });

            Glide.with(((ChatTopicViewHolder) hld).iv_launcher.getContext())
                    .load(entity.face)
                    .centerCrop()
//                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(holder.iv_launcher);

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


        @Bind(R.id.iv_launcher_pic)
        public ImageView iv_launcher;

        @Bind(R.id.tv_username)
        public TextView tv_username;

        @Bind(R.id.tv_topic)
        public  TextView tv_topic;

        @Bind(R.id.mosaic_layout)
        public   MosaicLayout mosaicLayout;

        @Bind(R.id.lny_comments)
        LinearLayout lny_comments;

        public String[] tmpd = null;

        public ChatTopicViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            // set up different patterns for different size of data array
        }
    }


    public class MosaicLayoutAdapter extends ArrayAdapter<Object> {

        private Context context;
        private String[] values;
        private Gson gson;


        public MosaicLayoutAdapter(Context context, String[] values) {
            super(context, R.layout.row_item);
            this.context = context;
            this.values = values;
            gson = new Gson();
        }

        @Override
        public int getCount() {
            return values.length;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.row_item, parent, false);
            final ImageView image = (ImageView) rowView.findViewById(R.id.image);
            if (placeholder == null) {
                placeholder = new ColorDrawable(context.getResources().getColor(R.color.gray));
            }

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Uri uri = Uri.parse(AppConstants.scheme_ui + "://" + AppConstants.authority + "/" +
                            UtilsFunctions.encodedPath(gson.toJson(values[position])));
                    ((ChatTopicsMainFragment.OnFragmentInteractionListener) context).onFragmentInteraction(uri, values);
                }
            });

            Glide.with(context)
                    .load(values[position])
                    .centerCrop()
                    .placeholder(placeholder)
//                    .thumbnail(0.3f)
                    .error(me.crosswall.photo.pick.R.drawable.default_error)
                    .into(image);
            return rowView;
        }
    }

    public static class DividerItemDecoration extends RecyclerView.ItemDecoration {

        private static final int[] ATTRS = new int[]{
                android.R.attr.listDivider
        };

        public static final int HORIZONTAL_LIST = LinearLayoutManager.HORIZONTAL;

        public static final int VERTICAL_LIST = LinearLayoutManager.VERTICAL;

        private Drawable mDivider;

        private int mOrientation;

        public DividerItemDecoration(Context context, int orientation) {
            final TypedArray a = context.obtainStyledAttributes(ATTRS);
            mDivider = a.getDrawable(0);
            a.recycle();
            setOrientation(orientation);
        }

        public void setOrientation(int orientation) {
            if (orientation != HORIZONTAL_LIST && orientation != VERTICAL_LIST) {
                throw new IllegalArgumentException("invalid orientation");
            }
            mOrientation = orientation;
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent) {

            /* if it is the last dont draw anything. */
            if (mOrientation == VERTICAL_LIST) {
                drawVertical(c, parent);
            } else {
                drawHorizontal(c, parent);
            }

        }

        public void drawVertical(Canvas c, RecyclerView parent) {
            final int left = parent.getPaddingLeft();
            final int right = parent.getWidth() - parent.getPaddingRight();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                final int top = child.getBottom() + params.bottomMargin;
                final int bottom = top + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        public void drawHorizontal(Canvas c, RecyclerView parent) {
            final int top = parent.getPaddingTop();
            final int bottom = parent.getHeight() - parent.getPaddingBottom();

            final int childCount = parent.getChildCount();
            for (int i = 0; i < childCount; i++) {
                final View child = parent.getChildAt(i);
                final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                        .getLayoutParams();
                int customRightMargin = 20;
                final int left = child.getRight() + customRightMargin/*params.rightMargin*/;
                final int right = left + mDivider.getIntrinsicHeight();
                mDivider.setBounds(left, top, right, bottom);
                mDivider.draw(c);
            }
        }

        @Override
        public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
            if (mOrientation == VERTICAL_LIST) {
                outRect.set(10, 0, 10, mDivider.getIntrinsicHeight());
            } else {
                outRect.set(0, 0, mDivider.getIntrinsicWidth(), 0);
            }
        }
    }


}
