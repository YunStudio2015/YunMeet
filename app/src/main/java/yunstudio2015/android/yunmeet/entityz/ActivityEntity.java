package yunstudio2015.android.yunmeet.entityz;

/**
 * Created by Ultima on 2015/11/25.
 */
public class ActivityEntity {

    /*
    *  {
	            "id": "10",
	            "pepnum": "1",
	            "theme": "activity10",
	            "detail": "detail10",
	            "time": "11-08-15 16:26:45",
	            "pubtime": "01-01-70 08:00:00",
	            "cost": "0",
	            "status": "0",
	            "image_id": [
	                "http://121.42.160.23/Yunmeet/Public/upload/2.jpg",
	                "http://121.42.160.23/Yunmeet/Public/upload/4.jpg"
	            ],
	            "user_id": "10",
	            "category": "健康",
	            "username": "nick9",
	            "face": "http://121.42.160.23/Yunmeet/Public/upload/4.jpg",
	            "sex": "1",
	            "birth": "01-01-70 08:00:00",
	            "isvip": "0"
	        },
    *
    * */

    public int id, pepnum;
    public String theme, detail, time, pubtime;
    public double cost;
    public int status;
    public String[] image_id;
    public int user_id;
    public String category;
    public String username;
    public String face;
    public int sex;
    public String birth;
    public String isvip;



}
