package tendency.hz.zhihuijiayuan.bean;

/**
 * Created by JasonYao on 2018/8/1.
 */
public class PayResult {
    private String Type;
    private Object Source;
    private String Result;

    @Override
    public String toString() {
        return "{" +
                "Type='" + Type + '\'' +
                ", Source='" + Source + '\'' +
                ", Result='" + Result + '\'' +
                '}';
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public Object getSource() {
        return Source;
    }

    public void setSource(Object source) {
        Source = source;
    }

    public String getResult() {
        return Result;
    }

    public void setResult(String result) {
        Result = result;
    }
}
