package tendency.hz.zhihuijiayuan.model.modelInter;

/**
 * 卡片相关操作接口
 * Created by JasonYao on 2018/3/28.
 */

public interface CardModelInter {
    /**
     * 卡片发现列表
     *
     * @param netCode
     * @param title
     * @param districtID
     * @param pageIndex
     */
    void findCardList(int netCode, String title, String districtID, String pageIndex);

    /**
     * 个人卡片列表
     *
     * @param netCode
     * @param serviceTypeID
     * @param pageIndex
     */
    void myCardList(int netCode, String serviceTypeID, String pageIndex);

    /**
     * 卡片排序
     *
     * @param netCode
     * @param cardIds
     */
    void sortCard(int netCode, String cardIds);

    /**
     * 未登录卡片排序
     *
     * @param netCode
     * @param cardIds
     */
    void anonymousCardSort(int netCode, String cardIds);

    /**
     * 个人卡片添加
     *
     * @param netCode
     * @param cardItem
     */
    void cardAttentionAdd(int netCode, Object cardItem);

    /**
     * 信息同步
     *
     * @param netCode
     * @param CardIDs
     */
    void infoSync(int netCode, String CardIDs);

    /**
     * 卡片预览接口
     *
     * @param netCode
     * @param CardID
     */
    void previewCard(int netCode, String CardID);

    /**
     * 个人卡片删除接口
     *
     * @param netCode
     * @param ID
     * @param CardID
     */
    void deleteCard(int netCode, String ID, String CardID);

    /**
     * 卡片点击次数记录
     *
     * @param netCode
     * @param cardId
     * @param distinctId
     */
    void cardClickVolume(int netCode, String cardId, String uniqueID, String distinctId);

    /**
     * 获取历史卡片
     *
     * @param netCode
     */
    void getHisCard(int netCode);

    /**
     * 获取最受欢迎的卡列表
     *
     * @param netCode
     */
    void getPopularCard(int netCode, String num);

    /**
     * 匿名关注卡
     *
     * @param netCode
     * @param cardItem
     */
    void anonymousFocus(int netCode, Object cardItem);

    /**
     * 匿名取消关注卡
     *
     * @param netCode
     * @param cardId
     */
    void anonymousCancel(int netCode, String cardId);

    /**
     * 获取推荐卡列表
     *
     * @param netCode
     */
    void recommendCard(int netCode);

    /**
     * 获取应用卡信息
     *
     * @param netCode
     * @param cardId
     */
    void getAppCardInfo(int netCode, String cardId);

    /**
     * 校验卡片有效性
     *
     * @param netCode
     * @param cardId
     */
    void checkCanOperate(int netCode, String cardId);

    /**
     * 个人卡片列表
     *
     * @param netCode
     * @param serviceTypeID
     * @param pageIndex
     */
    void anonymousList(int netCode, String serviceTypeID, String pageIndex);

    /**
     * 获取推荐卡片
     *
     * @param netCode
     */
    void getRecommendCard(int netCode);

    /**
     * 获取精选卡片
     *
     * @param netCode
     */
    void getChoiceCard(int netCode);

    /**
     * 获取Banner
     *
     * @param netCode
     */
    void getBanner(int netCode);

    /**
     * 获取Banner详情
     *
     * @param netCode
     * @param id
     */
    void getBannerDetail(int netCode, String id);

    /**
     * 自动关注卡片
     *
     * @param netCode
     */
    void authFocusCard(int netCode);

    /**
     * 获取卡片二维码
     *
     * @param netCode
     * @param cardId
     */
    void getCardQrCodeUrl(int netCode, String cardId);

    /**
     * 获取为你推荐卡
     *
     * @param netCode
     */
    void getChoiceRecommendCard(int netCode);

    /**
     * 获取按主题拉取卡片接口
     *
     * @param netCode
     * @param eachCount
     */
    void getChoiceCardTheme(int netCode, String eachCount);

    /**
     * 卡片搜索
     *
     * @param netCode
     * @param themeVal
     * @param title
     * @param pageIndex
     */
    void getChoiceSreach(int netCode, String themeVal, String title, String pageIndex);

    /**
     * 获取卡片热搜榜
     *
     * @param netCode
     */
    void getCardHotSearch(int netCode);

    /**
     * 获取卡片Code
     *
     * @param netCode
     */
    void getCardCode(int netCode,String cardId);
}
