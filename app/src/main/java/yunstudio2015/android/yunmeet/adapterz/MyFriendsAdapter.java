package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.SimpleFriendItemEntity;

/**
 * Created by lizhaotailang on 2016/2/19.
 */
public class MyFriendsAdapter extends RecyclerView.Adapter<MyFriendsAdapter.MyFriendsViewHolder> {

    private final List<SimpleFriendItemEntity> friends;
    private final Context context;
    private final LayoutInflater inflater;
    private OnFriendItemClickListener mListener;

    public MyFriendsAdapter(Context context,List<SimpleFriendItemEntity> friends){
        this.context = context;
        this.friends = friends;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyFriendsViewHolder(inflater.inflate(R.layout.friends_item,parent,false),context,mListener);
    }

    @Override
    public void onBindViewHolder(MyFriendsViewHolder holder, int position) {
        SimpleFriendItemEntity friend = friends.get(position);
        holder.tvFriendsName.setText(friend.getName());
        if (friend.getIntroduction() != null){
            holder.tvFriendsIntroduction.setText(friend.getIntroduction());
        } else {
            holder.tvFriendsIntroduction.setText("这个人太懒，什么都没有写");
        }
        Glide.with(context).load(friend.getFace()).into(holder.ivFace);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyFriendsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private ImageView ivFace;
        private TextView tvFriendsName;
        private TextView tvFriendsIntroduction;
        private LinearLayout itemLayout;
        private OnFriendItemClickListener listener;

        public MyFriendsViewHolder(View itemView,Context context,OnFriendItemClickListener listener) {
            super(itemView);

            ivFace = (ImageView) itemView.findViewById(R.id.iv_friend_face);
            tvFriendsName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            tvFriendsIntroduction = (TextView) itemView.findViewById(R.id.tv_friend_introduction);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.friend_item);

            this.listener = listener;
            itemLayout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null){
                listener.onItemClick(v,getLayoutPosition());
            }
        }
    }

    public void setOnItemListener(OnFriendItemClickListener listener){
        this.mListener = listener;
    }

    public interface OnFriendItemClickListener {
        void onItemClick(View view,int position);
    }
}
