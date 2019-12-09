package tendency.hz.zhihuijiayuan.bean.base;

import android.os.Environment;

import java.io.File;

import tendency.hz.zhihuijiayuan.application.MyApplication;

/**
 * 定义本地数据库和数据表
 * Created by JasonYao on 2018/3/5.
 */

public class Field {

    /**
     * 内部数据库路径
     */
    public static final String DB_PATH = File.separator + "data"
            + Environment.getDataDirectory().getAbsolutePath() + File.separator
            + MyApplication.getInstance().getPackageName() + File.separator + "databases" + File.separator;

    /**
     * 外部数据库路径
     */
    public static final String DB_PATH_SD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "tdr11" + File.separator;

    /**
     * 缓存卡片的表名和字段名（匿名登录时使用）
     */
    public static final class CacheCard {
        public static final String dbName = "CacheCard"; //表名称
        public static final String card_id = "CardId"; //卡ID
        public static final String card_area = "CardArea"; //卡地址
        public static final String card_introduction = "CardIntroduction"; //卡介绍
        public static final String card_bg = "CardBg"; //卡背景颜色
    }

    /**
     * 保存匿名登录状态下用户添加的卡
     */
    public static final class CacheMyCard {
        public static final String dbName = "CacheMyCard";

        public static final String card_id = "CardID";
        public static final String poster = "Poster";
        public static final String logo = "Logo";
        public static final String title = "Title";
        public static final String subTitle = "SubTitle";
        public static final String qrCodeImg = "QRCodeImg";
        public static final String endTime = "EndTime";
        public static final String areaName = "AreaName";
        public static final String cardUrl = "CardUrl";
        public static final String posterUrl = "PosterUrl";
        public static final String LogoUrl = "LogoUrl";
        public static final String qrCodeUrl = "QRCodeUrl";
        public static final String serviceTypeID = "ServiceTypeID";
        public static final String cardType = "CardType";
    }

    /**
     * 缓存发现页面卡片的表名和字段名
     */
    public static final class FindCacheCard {
        public static final String dbName = "FindCacheCard";  //表名称

        public static final String card_id = "CardID";
        public static final String poster = "Poster";
        public static final String logo = "Logo";
        public static final String title = "Title";
        public static final String subTitle = "SubTitle";
        public static final String qrCodeImg = "QRCodeImg";
        public static final String endTime = "EndTime";
        public static final String areaName = "AreaName";
        public static final String focusStatus = "FocusStatus";
    }

    /**
     * 三级行政区划的表名和字段名
     */
    public static final class Area {
        public static final String dbName = "Area"; //表名称

        public static final String area_id = "ID"; //地域ID
        public static final String area_parent_id = "ParentId"; //父级ID
        public static final String area_name = "Name"; //地域Name
        public static final String area_shortname = "ShortName"; //地域名字简称
    }

    /**
     * 基础配置信息的表名和字段名
     */
    public static final class DataDictionary {
        public static final String dbName = "DataDictionary"; //表名称

        public static final String data_id = "ID"; //配置ID
        public static final String data_dictype = "DicType"; //配置dicType
        public static final String data_dicName = "DicName"; //配置dicName
        public static final String data_dicValue = "DicValue"; //配置dicValue
        public static final String data_orderBy = "OrderBy";
        public static final String data_status = "Status";
    }

    /**
     * 我的消息
     */
    public static final class Message {
        public static final String dbName = "Message"; //表名称

        public static final String message_content = "Content"; //消息内容
        public static final String message_dateTime = "DateTime"; //消息时间
        public static final String message_url = "Url"; //消息Url
        public static final String message_card_url = "CardUrl"; //消息CardUrl
        public static final String message_CardID = "CardID"; //消息CardID
        public static final String message_CardName = "CardName"; //消息CardName
        public static final String message_CardLogoUrl = "CardLogoUrl";  //消息LogoUrl
        public static final String message_status = "status";  //消息状态
    }

    public static final class Address {
        public static final String dbName = "address_dict";

        public static final String area_id = "id";
        public static final String area_parent_id = "parentId";
        public static final String area_code = "code";
        public static final String area_name = "name";
    }

    /**
     * 搜索历史
     */
    public static final class SreachHis {
        public static final String dbName = "SreachHis";  //表名称

        public static final String content = "content";  //搜索内容
    }


    /**
     * 下载
     */
    public static final class DownLoad {
        public static final String dbName = "download_file";  //表名称
        public static final String file_name = "file_name";  //文件名
        public static final String file_path = "file_path";  //文件路径
        public static final String file_type = "file_type";  //文件类型
        public static final String file_size = "file_size";  //文件大小
        public static final String file_url = "file_url";  //文件下载地址
        public static final String user_id = "user_id";  //用户id
        public static final String card_id = "card_id";  //卡片id


    }

    /**
     * 时间格式
     */
    public static final class DateType {
        public static final String standard_time_format = "yyyy-MM-dd HH:mm:ss";  //标准时间格式

        public static final String year_month_day = "yyyy-MM-dd";  //年月日
        public static final String month = "MM";  //年月日
        public static final String year_month = "yyyy-MM";  //年月
    }

}
