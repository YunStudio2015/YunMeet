package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import butterknife.ButterKnife;
import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.ActivityEntity;
import yunstudio2015.android.yunmeet.utilz.ImageLoadOptions;

/**
 * Created by Ultima on 2015/11/27.
 */
public class MyActivitizAdapter extends RecyclerView.Adapter<MyActivitizAdapter.ViewHolder> {
    private List<ActivityEntity> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView iv_activity_bg;
        public ImageView iv_profile;

        public ViewHolder(View v) {
            super(v);
            //bg image
            iv_activity_bg = ButterKnife.findById(v, R.id.iv_activity_bg);
            iv_profile = ButterKnife.findById(v, R.id.iv_activity_owner);
//                mTextView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyActivitizAdapter(List<ActivityEntity> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyActivitizAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        // create a new view
//            View v = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.my_text_view, parent, false);
//            // set the view's size, margins, paddings and layout parameters
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_item_xml, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

          /*    private void addFakeElement(LayoutInflater inflater, LinearLayout lny_container) {

        View view = inflater.inflate(R.layout.activity_item_xml, lny_container, false);
        RelativeLayout relative = ButterKnife.findById(view, R.id.relative);
        lny_container.addView(view);
        ImageView iv = (ImageView) view.findViewById(R.id.iv_activity_bg);
//        Bitmap bm = ((BitmapDrawable) iv.getDrawable()).getBitmap();
//        iv.setImageDrawable(new RoundedDrawable(bm, bm.getScaledWidth(metrics)));
        // set up the width of the iv
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
//        layoutParams.height = layoutParams.width;
        iv.setLayoutParams(layoutParams);
        String link = "http://www.csw333.com/upload_files/qibosoft_news_/135/83555_20141120091105_8uctj.jpg";//"http://p5.img.cctvpic.com/nettv/newgame/2011/1118/20111118105936548.jpg";
        ImageLoader.getInstance().displayImage(link, iv, ImageLoadOptions.getDisplaySlightlyRoundedImageOptions(getContext()));
    }*/

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
//            holder.mTextView.setText(mDataset[position]);
        ActivityEntity tmp = mDataset.get(position);

        if (holder.iv_profile.getTag() == null)
            holder.iv_profile.setTag(random(1));
        if (holder.iv_activity_bg.getTag() == null)
            holder.iv_activity_bg.setTag(random(2));
        ImageLoader.getInstance().displayImage(/*tmp.image_id[0]*/
                (String) holder.iv_profile.getTag(),  holder.iv_profile, ImageLoadOptions.getDisplayImageOptions(mContext));
        ImageLoader.getInstance().displayImage(/*tmp.image_id[0]*/
                (String) holder.iv_activity_bg.getTag(),  holder.iv_activity_bg, ImageLoadOptions.getDisplaySlightlyRoundedImageOptions(mContext));
    }

    private String random(int i) {

        String[] profile = {"http://juto8.com/uploads/allimg/150913/1-150913194I5T3.jpg",
                "http://p0.qhimg.com/t0197e2e47f7bd1bb2b.png",
                "http://img.wdjimg.com/mms/icon/v1/d/52/16ba429f6f4466e67c7ee690c670552d_256_256.png",
                "http://a0.att.hudong.com/30/24/14300000865308127553247022251.jpg",
                "http://i4.ce.cn/fashion/news/200901/04/W020090104278934579910.jpg",
                "http://img.name2012.com/uploads/allimg/2015-06/30-023131_451.jpg",
                "http://img5.duitang.com/uploads/item/201510/11/20151011153554_vKSTB.png",
                "http://img3.imgtn.bdimg.com/it/u=189762072,3298944579&fm=21&gp=0.jpg",
                "http://h.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=44073b4417ce36d3a2518b340fc316b1/2f738bd4b31c8701be397578257f9e2f0608ffa5.jpg",
                "http://img3.imgtn.bdimg.com/it/u=2550601649,1539322084&fm=21&gp=0.jpg",
                "http://img1.touxiang.cn/uploads/20131103/03-034229_273.jpg"};
        String[] bg = {
                "http://img.taopic.com/uploads/allimg/120405/9129-120405001027100.jpg",
                "http://i-7.vcimg.com/trim/134bd948aff10aeac0d9568e2cc15882239718/trim.jpg",
                "http://img5.imgtn.bdimg.com/it/u=2060251905,1883324774&fm=21&gp=0.jpg",
                "http://img14.360buyimg.com/n0/g14/M00/02/0A/rBEhVVNng2QIAAAAAATjV_vw0HcAANB9QNaAUYABONv768.jpg",
                "http://pic27.nipic.com/20130302/10039264_111459206000_2.jpg",
                "http://img.sucai.redocn.com/attachments/images/201001/20100109/Redocn_2009123109450430.jpg",
                "http://img.sucai.redocn.com/attachments/images/200912/20091222/Redocn_2009122116175514.jpg",
                "http://img3.redocn.com/20100303/Redocn_2010030220410863.jpg",
                "http://uimgs.jiatx.com/mall/2012_02/23/product/1329987844774_000.jpg",
                "http://img3.redocn.com/20100121/20100121_a31c0df0fb4438d16270kBD8cXqs1moO.jpg",
                "http://img.mypsd.com.cn/y/d/%e5%9f%ba%e7%a1%80%e5%9b%be%e5%ba%93/%e9%a4%90%e9%a5%ae%e7%be%8e%e9%a3%9f/%e5%85%b6%e4%bb%96/a/jpg/GP46027.jpg"
        };
        int indice = (int) (Math.random() * ( i == 2 ? bg.length - 1 : profile.length -1));
        return  i == 2 ? bg[indice] : profile[indice];
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}

