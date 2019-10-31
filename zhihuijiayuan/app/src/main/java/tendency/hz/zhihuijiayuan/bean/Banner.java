package tendency.hz.zhihuijiayuan.bean;

/**
 * Created by JasonYao on 2018/10/10.
 */
public class Banner {
    String ID;
    String Title;
    String OuterImg;
    String Content;
    String InnerImg;

    @Override
    public String toString() {
        return "Banner{" +
                "ID='" + ID + '\'' +
                ", Title='" + Title + '\'' +
                ", OuterImg='" + OuterImg + '\'' +
                ", Content='" + Content + '\'' +
                ", InnerImg='" + InnerImg + '\'' +
                '}';
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOuterImg() {
        return OuterImg;
    }

    public void setOuterImg(String outerImg) {
        OuterImg = outerImg;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getInnerImg() {
        return InnerImg;
    }

    public void setInnerImg(String innerImg) {
        InnerImg = innerImg;
    }
}
