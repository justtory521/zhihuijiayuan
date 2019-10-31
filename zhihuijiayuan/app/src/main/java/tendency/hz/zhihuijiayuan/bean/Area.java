package tendency.hz.zhihuijiayuan.bean;

import java.util.List;

/**
 * 地区相关Bean
 * Created by JasonYao on 2018/3/27.
 */

public class Area {
    private String ID;
    private String ParentId;
    private String Name;
    private String ShortName;
    private List<Area> Next;
    private String Id;

    @Override
    public String toString() {
        return "Area{" +
                "ID='" + ID + '\'' +
                ", ParentId='" + ParentId + '\'' +
                ", Name='" + Name + '\'' +
                ", ShortName='" + ShortName + '\'' +
                ", Next=" + Next +
                ", Id='" + Id + '\'' +
                '}';
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public List<Area> getNext() {
        return Next;
    }

    public void setNext(List<Area> next) {
        Next = next;
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
