package yunstudio2015.android.yunmeet.entityz;

/**
 * Created by lizhaotailang on 2016/2/19.
 */
public class SimpleFriendItemEntity {

    private String ID;
    private String face;
    private String name;
    private String introduction;

    public SimpleFriendItemEntity(String id,String face,String name,String intro){
        this.ID = id;
        this.face = face;
        this.name = name;
        this.introduction = intro;
    }

    public String getID() {
        return ID;
    }

    public String getFace() {
        return face;
    }

    public String getName() {
        return name;
    }

    public String getIntroduction() {
        return introduction;
    }
}
