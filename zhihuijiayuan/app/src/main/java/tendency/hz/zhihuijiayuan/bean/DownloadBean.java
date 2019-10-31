package tendency.hz.zhihuijiayuan.bean;

/**
 * Author：Libin on 2019/6/26 11:00
 * Description：下载文件
 */
public class DownloadBean {
    private int id;
    private String file_name = "file_name";  //文件名
    private String file_path = "file_path";  //文件路径
    private String file_type = "file_type";  //文件类型
    private String file_size = "file_size";  //文件大小
    private String file_url = "file_size";  //文件下载地址
    private String user_id = "user_id";  //用户id
    private String card_id = "card_id";  //卡片id


    public String getFile_url() {
        return file_url;
    }

    public void setFile_url(String file_url) {
        this.file_url = file_url;
    }

    @Override
    public String toString() {
        return "DownloadBean{" +
                "id=" + id +
                ", file_name='" + file_name + '\'' +
                ", file_path='" + file_path + '\'' +
                ", file_type='" + file_type + '\'' +
                ", file_size='" + file_size + '\'' +
                ", file_url='" + file_url + '\'' +
                ", user_id='" + user_id + '\'' +
                ", card_id='" + card_id + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFile_name() {
        return file_name;
    }

    public DownloadBean() {
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public String getFile_path() {
        return file_path;
    }

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public String getFile_type() {
        return file_type;
    }

    public void setFile_type(String file_type) {
        this.file_type = file_type;
    }

    public String getFile_size() {
        return file_size;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCard_id() {
        return card_id;
    }

    public void setCard_id(String card_id) {
        this.card_id = card_id;
    }
}
