package yunstudio2015.android.yunmeet.adapterz;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import yunstudio2015.android.yunmeet.R;
import yunstudio2015.android.yunmeet.entityz.FriendEntity;

/**
 * Created by lizhaotailang on 2016/2/19.
 */
public class FriendsAdapter extends ArrayAdapter<FriendEntity> {

    private int resourceId;

    public FriendsAdapter(Context context, int resource, List<FriendEntity> objects) {
        super(context, resource, objects);
        resourceId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendEntity friend = getItem(position);//获取当前FriendEntity的实例
        View view;//使用LayoutInflater来为子项加载传入的布局

        ViewHolder viewHolder;

        /**
         * 将之前加载好的布局进行缓存，以便重新启用
         * 在getview()方法进行判断，如果convertView为空，则使用LayoutInflater加载布局
         * 如果不为空则直接对convertView进行重用
         * 当convertView为空时，创建一个ViewHolder对象，并将控件的实例都存放在ViewHolder中
         * 然后调用View的setTag()方法，把ViewHolder重新取出
         */
        if (convertView == null){
            view = LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder = new ViewHolder();
            viewHolder.ivFriendFace = (ImageView) view.findViewById(R.id.iv_friend_face);
            viewHolder.tvFriendName = (TextView) view.findViewById(R.id.tv_friend_name);
            viewHolder.tvFriendIntroduction = (TextView) view.findViewById(R.id.tv_friend_introduction);
            view.setTag(viewHolder);//将viewholder存储在view中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.ivFriendFace.setImageBitmap(friend.getFace());
        viewHolder.tvFriendName.setText(friend.getName());
        viewHolder.tvFriendIntroduction.setText(friend.getIntroduction());

        return view;
    }

    //内部类，用于对控件的示例进行缓存
    class ViewHolder{
        ImageView ivFriendFace;
        TextView tvFriendName;
        TextView tvFriendIntroduction;
    }
}
