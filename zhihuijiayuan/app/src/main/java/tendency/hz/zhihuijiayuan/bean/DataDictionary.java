package tendency.hz.zhihuijiayuan.bean;

/**
 * 数据字典
 * Created by JasonYao on 2018/3/28.
 */

public class DataDictionary {
    private String ID;
    private String DicType;
    private String DicName;
    private String DicValue;
    private String OrderBy;
    private String Status;

    @Override
    public String toString() {
        return "DataDictionary{" +
                "ID='" + ID + '\'' +
                ", DicType='" + DicType + '\'' +
                ", DicName='" + DicName + '\'' +
                ", DicValue='" + DicValue + '\'' +
                ", OrderBy='" + OrderBy + '\'' +
                ", Status='" + Status + '\'' +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDicType() {
        return DicType;
    }

    public void setDicType(String dicType) {
        DicType = dicType;
    }

    public String getDicName() {
        return DicName;
    }

    public void setDicName(String dicName) {
        DicName = dicName;
    }

    public String getDicValue() {
        return DicValue;
    }

    public void setDicValue(String dicValue) {
        DicValue = dicValue;
    }

    public String getOrderBy() {
        return OrderBy;
    }

    public void setOrderBy(String orderBy) {
        OrderBy = orderBy;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }
}
