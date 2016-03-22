package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

    public MyFriendsAdapter(Context context,List<SimpleFriendItemEntity> friends){
        this.context = context;
        this.friends = friends;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public MyFriendsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyFriendsViewHolder(inflater.inflate(R.layout.friends_item,parent,false));
    }

    @Override
    public void onBindViewHolder(MyFriendsViewHolder holder, int position) {
        SimpleFriendItemEntity friend = friends.get(position);
        holder.tvFriendsName.setText(friend.getName());
        Log.d("friend","name " + friend.getName());
        if (friend.getIntroduction() != null){
            holder.tvFriendsIntroduction.setText(friend.getIntroduction());
        } else {
            holder.tvFriendsIntroduction.setText("这个人太懒，什么都没有写");
        }
        Log.d("friend","intro " + friend.getIntroduction());

        Log.d("friend","img " + friend.getFace());
        Glide.with(context).load(friend.getFace()).into(holder.ivFace);
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class MyFriendsViewHolder extends RecyclerView.ViewHolder{

        private ImageView ivFace;
        private TextView tvFriendsName;
        private TextView tvFriendsIntroduction;

        public MyFriendsViewHolder(View itemView) {
            super(itemView);

            ivFace = (ImageView) itemView.findViewById(R.id.iv_friend_face);
            tvFriendsName = (TextView) itemView.findViewById(R.id.tv_friend_name);
            tvFriendsIntroduction = (TextView) itemView.findViewById(R.id.tv_friend_introduction);
        }
    }
}
