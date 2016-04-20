package yunstudio2015.android.yunmeet.entityz;

import java.util.List;

/**
 * Created by Ultima on 2015/11/25.
 */

public class UploadActivityEntity {


	/*
    * - token 用户token  您还未登陆，请先登陆
  - isrec 是否为推荐活动   0-->不是  1-->是
当值不是0或1  活动只有推荐和普通类型
当值为1但用户不是认证用户  联系我们成为认证用户，就能发布推荐活动
- pepnum 活动参与人数  参与人数必须大于0
- time 活动开始时间  活动开始时间必须大于当前时间
- category_id 活动分类id  没有这个活动分类
- theme 活动主题  必须填写1到37个字符的活动主题
- detail 活动详情  必须填写活动详情
- cost 活动费用类型  没有这个费用选项
- place 活动地点  必须填写活动地点
    *
    * */
	public String theme, detail, place, time;
	public	boolean isrec;
	public	String pepnum;
	public	String category_id;
	public	String cost;
	public	List<String> pic_paths;

	public UploadActivityEntity(String theme, String detail, String place, String time, boolean isrec, String pepnum, String category_id, String cost, List<String> pic_paths) {
		this.theme = theme;
		this.detail = detail;
		this.place = place;
		this.time = time;
		this.isrec = isrec;
		this.pepnum = pepnum;
		this.category_id = category_id;
		this.cost = cost;
		this.pic_paths = pic_paths;
	}

	public UploadActivityEntity() {

	}
}