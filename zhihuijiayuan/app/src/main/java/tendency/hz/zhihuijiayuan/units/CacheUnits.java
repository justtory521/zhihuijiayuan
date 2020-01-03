package tendency.hz.zhihuijiayuan.units;

import android.database.Cursor;
import android.util.Log;

import com.cjt2325.cameralibrary.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

import tendency.hz.zhihuijiayuan.bean.CardItem;
import tendency.hz.zhihuijiayuan.bean.DownloadBean;
import tendency.hz.zhihuijiayuan.bean.Message;
import tendency.hz.zhihuijiayuan.bean.base.Field;

/**
 * 缓存操作工具类
 * Created by JasonYao on 2018/3/28.
 */

public class CacheUnits {
    private static final String TAG = "CacheUnits---";
    public static CacheUnits mInstance;

    private CacheUnits() {
    }

    public static CacheUnits getInstance() {
        if (mInstance == null) {
            synchronized (CacheUnits.class) {
                if (mInstance == null) {
                    mInstance = new CacheUnits();
                }
            }
        }

        return mInstance;
    }

    /**
     * 获取缓存的发现页面的卡片
     *
     * @return
     */
    public List<CardItem> getFindCacheCard() {
        List<CardItem> cardItems = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.FindCacheCard.dbName, new String[]{});

        if (cursor.getCount() <= 0) {
            return cardItems;
        } else {
            while (cursor.moveToNext()) {
                CardItem cardItem = new CardItem();
                cardItem.setCardID(cursor.getString(1));
                cardItem.setPoster(cursor.getString(2));
                cardItem.setLogo(cursor.getString(3));
                cardItem.setTitle(cursor.getString(4));
                cardItem.setSubTitle(cursor.getString(5));
                cardItem.setQRCodeImg(cursor.getString(6));
                cardItem.setEndTime(cursor.getString(7));
                cardItem.setAreaName(cursor.getString(8));
                cardItem.setFocusStatus(cursor.getString(9));
                cardItems.add(cardItem);
            }
        }

        return cardItems;
    }

    /**
     * 清空缓存的发现页面的卡片
     */
    public void clearFindCacheCard() {
        DataDbHelper.getInstance().insert("delete from " + Field.FindCacheCard.dbName, new String[]{});
    }

    /**
     * 将发现页面的卡片缓存
     */
    public void insertFindCacheCard(List<CardItem> cardItems) {
        for (CardItem cardItem : cardItems) {
            String sqlIns = "insert into " + Field.FindCacheCard.dbName + " values(null,?,?,?,?,?,?,?,?,?)";
            String[] arr = {
                    cardItem.getCardID(), cardItem.getPoster(), cardItem.getLogo(), cardItem.getTitle(), cardItem.getSubTitle(), cardItem.getQRCodeImg(),
                    cardItem.getEndTime(), cardItem.getAreaName(), cardItem.getFocusStatus()
            };

            DataDbHelper.getInstance().insert(sqlIns, arr);
        }
    }

    /**
     * 将卡片缓存至我的卡片下
     *
     * @param cardItem
     */
    public void insertMyCacheCard(CardItem cardItem) {
        if (cardItem == null) return;
        String sqlIns = "insert into " + Field.CacheMyCard.dbName + " values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        String[] arr = {
                cardItem.getCardID(), cardItem.getPoster(), cardItem.getLogo(), cardItem.getTitle(), cardItem.getSubTitle(), cardItem.getQRCodeImg(),
                cardItem.getEndTime(), cardItem.getAreaName(), cardItem.getCardUrl(), cardItem.getPosterUrl(), cardItem.getLogoUrl(),
                cardItem.getQRCodeUrl(), cardItem.getServiceTypeID(), String.valueOf(cardItem.getCardType())
        };

        DataDbHelper.getInstance().insert(sqlIns, arr);
    }

    /**
     * 将卡片缓存至我的卡片下
     *
     * @param cardItems
     */
    public void insertMyCacheCards(List<CardItem> cardItems) {
        for (CardItem cardItem : cardItems) {
            String sqlIns = "insert into " + Field.CacheMyCard.dbName + " values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String[] arr = {
                    cardItem.getCardID(), cardItem.getPoster(), cardItem.getLogo(), cardItem.getTitle(), cardItem.getSubTitle(), cardItem.getQRCodeImg(),
                    cardItem.getEndTime(), cardItem.getAreaName(), cardItem.getCardUrl(), cardItem.getPosterUrl(), cardItem.getLogoUrl(),
                    cardItem.getQRCodeUrl(), cardItem.getServiceTypeID(), String.valueOf(cardItem.getCardType())
            };

            DataDbHelper.getInstance().insert(sqlIns, arr);
        }
    }


    /**
     * 检查该卡是否已被添加
     *
     * @param cardItem
     * @return
     */
    public boolean singleCacheCard(CardItem cardItem) {
        //先查询该卡是否已经被添加，被添加的情况下不允许重复添加
        String sqlSel = "select * from " + Field.CacheMyCard.dbName
                + " where " + Field.CacheMyCard.card_id + "='" + cardItem.getCardID() + "'";
        Cursor cursor = DataDbHelper.getInstance().get(sqlSel, new String[]{});
        Log.e(TAG, cardItem.getCardID());
        Log.e(TAG, cursor.getCount() + "");
        return cursor.getCount() != 0;
    }

    /**
     * 将我的卡片缓存清空
     */
    public void deleteMyCacheCard() {
        DataDbHelper.getInstance().insert("delete from " + Field.CacheMyCard.dbName, new String[]{});
    }

    /**
     * 根据ID将我的卡片缓存清空
     *
     * @param cardId
     */
    public void deleteMyCacheCardById(String cardId) {
        DataDbHelper.getInstance().insert("delete from " + Field.CacheMyCard.dbName + " where " + Field.CacheMyCard.card_id + "='" + cardId + "'", new String[]{});
    }

    /**
     * 获取缓存的卡片IDs
     *
     * @return
     */
    public String getMyCacheCardIds() {
        StringBuffer stringBuffer = new StringBuffer();
        String sqlSel = "select " + Field.CacheMyCard.card_id + " from " + Field.CacheMyCard.dbName;
        Cursor cursor = DataDbHelper.getInstance().get(sqlSel, new String[]{});

        if (cursor.getCount() <= 0) {
            return stringBuffer.toString();
        } else {
            while (cursor.moveToNext()) {
                stringBuffer.append(cursor.getString(0) + ",");
            }


            return stringBuffer.substring(0, stringBuffer.length() - 1);
        }
    }

    /**
     * 读取缓存卡片
     *
     * @param serviceId
     * @return
     */
    public List<CardItem> getMyCacheCards(String serviceId) {
        List<CardItem> cardItems = new ArrayList<>();
        String sqlSel;
        if (FormatUtils.getInstance().isEmpty(serviceId)) {  //serviceId为空，表示获取全部缓存卡片
            sqlSel = "select * from " + Field.CacheMyCard.dbName + " order by _id";
        } else {
            sqlSel = "select * from " + Field.CacheMyCard.dbName + " where " + Field.CacheMyCard.serviceTypeID + "=" + serviceId + "order by _id";
        }

        Cursor cursor = DataDbHelper.getInstance().get(sqlSel, new String[]{});

        if (cursor.getCount() <= 0) {
            return cardItems;
        } else {
            while (cursor.moveToNext()) {
                CardItem cardItem = new CardItem();
                cardItem.setCardID(cursor.getString(1));
                cardItem.setPoster(cursor.getString(2));
                cardItem.setLogo(cursor.getString(3));
                cardItem.setTitle(cursor.getString(4));
                cardItem.setSubTitle(cursor.getString(5));
                cardItem.setQRCodeImg(cursor.getString(6));
                cardItem.setEndTime(cursor.getString(7));
                cardItem.setAreaName(cursor.getString(8));
                cardItem.setCardUrl(cursor.getString(9));
                cardItem.setPosterUrl(cursor.getString(10));
                cardItem.setLogoUrl(cursor.getString(11));
                cardItem.setQRCodeUrl(cursor.getString(12));
                cardItem.setServiceTypeID(cursor.getString(13));
                cardItem.setCardType(cursor.getString(14));
                cardItems.add(cardItem);
            }

            Log.e(TAG, cardItems.toString());
            return cardItems;
        }
    }

    /**
     * 清空我的缓存卡片
     */
    public void refreshMyCards(List<CardItem> cardItems) {
        for (CardItem cardItem : cardItems) {
            String sqlIns = "insert into " + Field.CacheMyCard.dbName + " values(null,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            String[] arr = {
                    cardItem.getCardID(), cardItem.getPoster(), cardItem.getLogo(), cardItem.getTitle(), cardItem.getSubTitle(), cardItem.getQRCodeImg(),
                    cardItem.getEndTime(), cardItem.getAreaName(), cardItem.getCardUrl(), cardItem.getPosterUrl(), cardItem.getLogoUrl(),
                    cardItem.getQRCodeUrl(), cardItem.getServiceTypeID(), String.valueOf(cardItem.getCardType())
            };

            DataDbHelper.getInstance().insert(sqlIns, arr);
        }
    }

    /**
     * 插入Message
     *
     * @param messages
     */
    public void insertMessage(List<Message> messages) {
        for (Message message : messages) {
            String sqlIns = "insert into " + Field.Message.dbName + " values(null,?,?,?,?,?,?,?,?)";
            Object[] arr = {
                    message.getContent(), message.getDateTime(), message.getUrl(), message.getCardID(),
                    message.getCardName(), message.getCardLogoUrl(), message.getCardUrl(), 0
            };

            DataDbHelper.getInstance().insert(sqlIns, arr);
        }
    }

    /**
     * 获取Message
     *
     * @return
     */
    public List<Message> getMessage() {
        List<Message> messageList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.Message.dbName + " order by _id desc", new String[]{});
        if (cursor.getCount() <= 0) {
            return messageList;
        } else {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setId(cursor.getString(0));
                message.setContent(cursor.getString(1));
                message.setDateTime(cursor.getString(2));
                message.setUrl(cursor.getString(3));
                message.setCardID(cursor.getString(4));
                message.setCardName(cursor.getString(5));
                message.setCardLogoUrl(cursor.getString(6));
                message.setCardUrl(cursor.getString(7));
                message.setStatus(cursor.getInt(8));
                messageList.add(message);
            }
        }

        return messageList;
    }

    /**
     * 按照cardId分组获取Message，取最新一条
     *
     * @return
     */
    public List<Message> getMessageGroupByCardId() {
        List<Message> messageList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.Message.dbName
                + " group by " + Field.Message.message_CardID + " order by " +
                Field.Message.message_CardID + " = '0' desc , _id desc", new String[]{});
        if (cursor.getCount() <= 0) {
            return messageList;
        } else {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setId(cursor.getString(0));
                message.setContent(cursor.getString(1));
                message.setDateTime(cursor.getString(2));
                message.setUrl(cursor.getString(3));
                message.setCardID(cursor.getString(4));
                message.setCardName(cursor.getString(5));
                message.setCardLogoUrl(cursor.getString(6));
                message.setCardUrl(cursor.getString(7));
                message.setStatus(cursor.getInt(8));
                messageList.add(message);
            }
        }

        return messageList;
    }

    /**
     * 按照cardId分组获取Message
     *
     * @return
     */
    public List<Message> getMessageByCardId(String cardId) {
        List<Message> messageList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.Message.dbName + " where "
                + Field.Message.message_CardID + "='" + cardId + "' order by _id desc", new String[]{});
        if (cursor.getCount() <= 0) {
            return messageList;
        } else {
            while (cursor.moveToNext()) {
                Message message = new Message();
                message.setId(cursor.getString(0));
                message.setContent(cursor.getString(1));
                message.setDateTime(cursor.getString(2));
                message.setUrl(cursor.getString(3));
                message.setCardID(cursor.getString(4));
                message.setCardName(cursor.getString(5));
                message.setCardLogoUrl(cursor.getString(6));
                message.setCardUrl(cursor.getString(7));
                message.setStatus(cursor.getInt(8));
                messageList.add(message);
            }
        }

        return messageList;
    }

    /**
     * @param cardId
     * @return 获取分组未读取消息数量
     */
    public int getUnreadCountsByCardId(String cardId) {

        int count = DataDbHelper.getInstance().get("select * from " + Field.Message.dbName + " where "
                + Field.Message.message_CardID + "='" + cardId + "' and " + Field.Message.message_status + "='0'", new String[]{}).getCount();
        LogUtils.log("分组未读消息数量：" + count);
        return count;
    }

    /**
     * @return 获取所有未读取消息数量
     */
    public int getUnreadCounts() {

        int count = DataDbHelper.getInstance().get("select * from " + Field.Message.dbName +
                " where " + Field.Message.message_status + "='0'", new String[]{}).getCount();
        LogUtils.log("未读消息数量：" + count);
        return count;
    }

    /**
     * @return 批量标记消息已读
     */
    public void markAsRead(String cardId) {

        DataDbHelper.getInstance().insert("update " + Field.Message.dbName +
                " set " + Field.Message.message_status + "='1'" + " where " + Field.Message.message_CardID + "='" + cardId + "'", new String[]{});
    }


    /**
     * 清除Message
     */
    public void clearMessage() {
        DataDbHelper.getInstance().insert("delete from " + Field.Message.dbName, new String[]{});
    }

    /**
     * 获取消息条数
     *
     * @return
     */
    public int getMessageNum() {
        return (int) SPUtils.getInstance().get(SPUtils.FILE_MESSAGE, SPUtils.messageNum, 0);
    }

    /**
     * 设置消息条数
     *
     * @param messageNum
     */
    public void setMessageNum(int messageNum) {
        int num = getMessageNum() + messageNum;
        SPUtils.getInstance().put(SPUtils.FILE_MESSAGE, SPUtils.messageNum, num);
    }

    /**
     * 清除消息条数
     */
    public void clearMessageNum() {
        SPUtils.getInstance().clear(SPUtils.FILE_MESSAGE);
    }

    /**
     * 获取消息显示条数
     *
     * @return
     */
    public int getDisplayMessageNum() {
        if (((int) SPUtils.getInstance().get(SPUtils.FILE_MESSAGE, SPUtils.messageNum, 0)) == 0) {
            return -1;
        }
        return (int) SPUtils.getInstance().get(SPUtils.FILE_MESSAGE, SPUtils.messageNum, 0);
    }

    /**
     * 插入搜索历史
     *
     * @param searchHis
     */
    public void insertSearchHis(String searchHis) {
        String sqlDel = "delete from " + Field.SreachHis.dbName + " where " + Field.SreachHis.content + "='" + searchHis + "'";
        DataDbHelper.getInstance().insert(sqlDel, new String[]{});
        String sqlIns = "insert into " + Field.SreachHis.dbName + " values(null,?)";
        String[] arr = {
                searchHis
        };

        DataDbHelper.getInstance().insert(sqlIns, arr);
    }

    /**
     * 清除搜索历史
     */
    public void clearSearchHis() {
        DataDbHelper.getInstance().insert("delete from " + Field.SreachHis.dbName, new String[]{});
    }

    /**
     * 获得搜索历史
     */
    public List<String> getSearchHis() {
        List<String> hisList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.SreachHis.dbName + " order by _id desc", new String[]{});
        if (cursor.getCount() <= 0) {
        } else {
            while (cursor.moveToNext()) {
                hisList.add(cursor.getString(1));
            }
        }

        return hisList;
    }


    /**
     * 插入下载记录
     *
     * @param
     */
    public void insertDownloadFile(DownloadBean downloadBean) {
        String sqlDel = "delete from " + Field.DownLoad.dbName + " where " + Field.DownLoad.file_name + "='" + downloadBean.getFile_name() + "'";
        DataDbHelper.getInstance().insert(sqlDel, new String[]{});

        String sqlIns = "insert into " + Field.DownLoad.dbName + " values(null,?,?,?,?,?,?,?)";
        String[] arr = {
                downloadBean.getFile_name(), downloadBean.getFile_path(), downloadBean.getFile_type(),
                downloadBean.getFile_size(), downloadBean.getFile_url(), downloadBean.getCard_id(), downloadBean.getUser_id()
        };

        DataDbHelper.getInstance().insert(sqlIns, arr);
    }


    /**
     * 获取下载的文件
     *
     * @return
     */
    public List<DownloadBean> getDownloadFile() {
        List<DownloadBean> downloadList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.DownLoad.dbName + " order by _id desc", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setId(cursor.getInt(0));
                downloadBean.setFile_name(cursor.getString(1));
                downloadBean.setFile_path(cursor.getString(2));
                downloadBean.setFile_type(cursor.getString(3));
                downloadBean.setFile_size(cursor.getString(4));
                downloadBean.setFile_url(cursor.getString(5));
                downloadBean.setUser_id(cursor.getString(6));
                downloadBean.setCard_id(cursor.getString(7));

                downloadList.add(downloadBean);
            }
        }
        return downloadList;
    }

    /**
     * 获取下载的视频文件
     *
     * @return
     */
    public List<DownloadBean> getDownloadVideo() {
        List<DownloadBean> downloadList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.DownLoad.dbName
                + " where " + Field.DownLoad.file_type + " like 'video/%' order by _id desc", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setId(cursor.getInt(0));
                downloadBean.setFile_name(cursor.getString(1));
                downloadBean.setFile_path(cursor.getString(2));
                downloadBean.setFile_type(cursor.getString(3));
                downloadBean.setFile_size(cursor.getString(4));
                downloadBean.setFile_url(cursor.getString(5));
                downloadBean.setUser_id(cursor.getString(6));
                downloadBean.setCard_id(cursor.getString(7));
                downloadList.add(downloadBean);
            }
        }
        return downloadList;
    }

    /**
     * 获取下载的音频文件
     *
     * @return
     */
    public List<DownloadBean> getDownloadAudio() {
        List<DownloadBean> downloadList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.DownLoad.dbName
                + " where " + Field.DownLoad.file_type + " like 'audio/%' order by _id desc", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setId(cursor.getInt(0));
                downloadBean.setFile_name(cursor.getString(1));
                downloadBean.setFile_path(cursor.getString(2));
                downloadBean.setFile_type(cursor.getString(3));
                downloadBean.setFile_size(cursor.getString(4));
                downloadBean.setFile_url(cursor.getString(5));
                downloadBean.setUser_id(cursor.getString(6));
                downloadBean.setCard_id(cursor.getString(7));
                downloadList.add(downloadBean);
            }
        }
        return downloadList;
    }

    /**
     * 获取下载的文件
     *
     * @return
     */
    public List<DownloadBean> getDownloadPhoto() {
        List<DownloadBean> downloadList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.DownLoad.dbName +
                " where " + Field.DownLoad.file_type + " like 'image/%' order by _id desc", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setId(cursor.getInt(0));
                downloadBean.setFile_name(cursor.getString(1));
                downloadBean.setFile_path(cursor.getString(2));
                downloadBean.setFile_type(cursor.getString(3));
                downloadBean.setFile_size(cursor.getString(4));
                downloadBean.setFile_url(cursor.getString(5));
                downloadBean.setUser_id(cursor.getString(6));
                downloadBean.setCard_id(cursor.getString(7));
                downloadList.add(downloadBean);
            }
        }
        return downloadList;
    }


    /**
     * 获取下载的文档
     *
     * @return
     */
    public List<DownloadBean> getDownloadDoc() {
        List<DownloadBean> downloadList = new ArrayList<>();
        Cursor cursor = DataDbHelper.getInstance().get("select * from " + Field.DownLoad.dbName +
                " where " + Field.DownLoad.file_type +
                " in ('" + OpenFileUtils.DATA_TYPE_DOC + "','"
                + OpenFileUtils.DATA_TYPE_DOCX + "','"
                + OpenFileUtils.DATA_TYPE_XLS + "','"
                + OpenFileUtils.DATA_TYPE_XLSX + "','"
                + OpenFileUtils.DATA_TYPE_PDF + "','"
                + OpenFileUtils.DATA_TYPE_TXT + "') order by _id desc", new String[]{});
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                DownloadBean downloadBean = new DownloadBean();
                downloadBean.setId(cursor.getInt(0));
                downloadBean.setFile_name(cursor.getString(1));
                downloadBean.setFile_path(cursor.getString(2));
                downloadBean.setFile_type(cursor.getString(3));
                downloadBean.setFile_size(cursor.getString(4));
                downloadBean.setFile_url(cursor.getString(5));
                downloadBean.setUser_id(cursor.getString(6));
                downloadBean.setCard_id(cursor.getString(7));
                downloadList.add(downloadBean);
            }
        }
        return downloadList;
    }


    /**
     * 清除下载记录
     */
    public void clearDownloadFile() {
        DataDbHelper.getInstance().insert("delete from " + Field.DownLoad.dbName, new String[]{});
    }


    /**
     * 删除下载记录
     */
    public void deleteDownloadFile(int id) {
        DataDbHelper.getInstance().insert("delete from " + Field.DownLoad.dbName + " where _id=" + id, new String[]{});
    }
}
