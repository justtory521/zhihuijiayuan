package tendency.hz.zhihuijiayuan.units;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cjt2325.cameralibrary.util.LogUtil;

import tendency.hz.zhihuijiayuan.application.MyApplication;
import tendency.hz.zhihuijiayuan.bean.base.App;
import tendency.hz.zhihuijiayuan.bean.base.Field;

/**
 * Created by JasonYao on 2018/3/28.
 */

public class DataDbHelper extends SQLiteOpenHelper {
    private static DataDbHelper dbHelper = null;  //单例对象


    /**
     * 私有化构造方法
     */
    private DataDbHelper() {
        super(MyApplication.getInstance(), App.DB.dbName, null, App.DB.version);
    }

    /**
     * 单例获取
     *
     * @return
     */
    public static DataDbHelper getInstance() {
        if (dbHelper == null) {
            synchronized (DataDbHelper.class) {
                if (dbHelper == null) {
                    dbHelper = new DataDbHelper();
                }
            }
        }

        return dbHelper;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(createFindCacheCard);
        sqLiteDatabase.execSQL(createMyCacheCard);
        onUpgrade(sqLiteDatabase, App.DB.firstVersion, App.DB.version);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        for (int index = i; index < i1; index++) {
            switch (index) {
                case 1:
                    sqLiteDatabase.execSQL(createMessage);
                    break;
                case 2:
                    String sql = "alter table " + Field.Message.dbName + " add column " + Field.Message.message_card_url + " varchar";
                    sqLiteDatabase.execSQL(sql);
                    break;
                case 3:
                    sqLiteDatabase.execSQL(createSreachHis);
                    break;
                case 4:
                    sqLiteDatabase.execSQL(createDownload);
                    break;
                default:
                    break;
            }
        }
    }

    private String createFindCacheCard = "create table " + Field.FindCacheCard.dbName + "(" +
            "_id integer primary key autoincrement," +
            Field.FindCacheCard.card_id + "," +
            Field.FindCacheCard.poster + "," +
            Field.FindCacheCard.logo + "," +
            Field.FindCacheCard.title + "," +
            Field.FindCacheCard.subTitle + "," +
            Field.FindCacheCard.qrCodeImg + "," +
            Field.FindCacheCard.endTime + "," +
            Field.FindCacheCard.areaName + "," +
            Field.FindCacheCard.focusStatus + ")";

    private String createMyCacheCard = "create table " + Field.CacheMyCard.dbName + "(" +
            "_id integer primary key autoincrement," +
            Field.CacheMyCard.card_id + "," +
            Field.CacheMyCard.poster + "," +
            Field.CacheMyCard.logo + "," +
            Field.CacheMyCard.title + "," +
            Field.CacheMyCard.subTitle + "," +
            Field.CacheMyCard.qrCodeImg + "," +
            Field.CacheMyCard.endTime + "," +
            Field.CacheMyCard.areaName + "," +
            Field.CacheMyCard.cardUrl + "," +
            Field.CacheMyCard.posterUrl + "," +
            Field.CacheMyCard.LogoUrl + "," +
            Field.CacheMyCard.qrCodeUrl + "," +
            Field.CacheMyCard.serviceTypeID + "," +
            Field.CacheMyCard.cardType + ")";

    private String createMessage = "create table " + Field.Message.dbName + "(" +
            "_id integer primary key autoincrement," +
            Field.Message.message_content + "," +
            Field.Message.message_dateTime + "," +
            Field.Message.message_url + "," +
            Field.Message.message_CardID + "," +
            Field.Message.message_CardName + "," +
            Field.Message.message_CardLogoUrl + ")";

    private String createSreachHis = "create table " + Field.SreachHis.dbName + "(" +
            "_id integer primary key autoincrement," +
            Field.SreachHis.content + ")";


    private String createDownload = "create table " + Field.DownLoad.dbName + "(" +
            "_id integer primary key autoincrement," +
            Field.DownLoad.file_name + "," +
            Field.DownLoad.file_path + "," +
            Field.DownLoad.file_type + "," +
            Field.DownLoad.file_size + "," +
            Field.DownLoad.file_url + "," +
            Field.DownLoad.user_id + "," +
            Field.DownLoad.card_id + ")";

    /**
     * 向数据库中插入数据
     *
     * @param sql
     * @param arr
     */
    public void insert(String sql, Object[] arr) {
        dbHelper = getInstance();

        //执行插入操作，没有表会自动创建
        dbHelper.getReadableDatabase().execSQL(sql, arr);

        dbHelper.close();
    }

    /**
     * 从数据库中获取Cursor
     *
     * @param sql
     * @param arr
     * @return
     */
    public Cursor get(String sql, String[] arr) {
        dbHelper = getInstance();

        Cursor cursorGet = dbHelper.getReadableDatabase().rawQuery(sql, arr);

        return cursorGet;
    }


}
