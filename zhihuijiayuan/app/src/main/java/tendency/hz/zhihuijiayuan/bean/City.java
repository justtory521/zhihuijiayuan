package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;

/**
 * Created by JasonYao on 2018/4/7.
 */

public class City implements Serializable {
    private String ID;
    private String ParentId;
    private String Name;
    private String ShortName;

    public City(String name) {
        setName(name);
    }

    public City() {

    }

    public City(String ID, String name) {
        this.ID = ID;
        Name = name;
    }

    @Override
    public String toString() {
        return "City{" +
                "ID='" + ID + '\'' +
                ", ParentId='" + ParentId + '\'' +
                ", Name='" + Name + '\'' +
                ", ShortName='" + ShortName + '\'' +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getParentId() {
        return ParentId;
    }

    public void setParentId(String parentId) {
        ParentId = parentId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }
}
