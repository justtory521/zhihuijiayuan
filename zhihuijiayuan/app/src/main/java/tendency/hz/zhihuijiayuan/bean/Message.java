package tendency.hz.zhihuijiayuan.bean;

/**
 * Created by JasonYao on 2018/11/19.
 */
public class Message {
    private String id;
    private String Content;
    private String DateTime;
    private String Url;
    private String CardUrl;
    private String CardID;
    private String CardName;
    private String CardLogoUrl;
    private boolean isChecked = false;
    private int status = 0;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id='" + id + '\'' +
                ", Content='" + Content + '\'' +
                ", DateTime='" + DateTime + '\'' +
                ", Url='" + Url + '\'' +
                ", CardUrl='" + CardUrl + '\'' +
                ", CardID='" + CardID + '\'' +
                ", CardName='" + CardName + '\'' +
                ", CardLogoUrl='" + CardLogoUrl + '\'' +
                ", isChecked=" + isChecked +
                '}';
    }

    public String getCardUrl() {
        return CardUrl;
    }

    public void setCardUrl(String cardUrl) {
        CardUrl = cardUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getCardID() {
        return CardID;
    }

    public void setCardID(String cardID) {
        CardID = cardID;
    }

    public String getCardName() {
        return CardName;
    }

    public void setCardName(String cardName) {
        CardName = cardName;
    }

    public String getCardLogoUrl() {
        return CardLogoUrl;
    }

    public void setCardLogoUrl(String cardLogoUrl) {
        CardLogoUrl = cardLogoUrl;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
