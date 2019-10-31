package tendency.hz.zhihuijiayuan.bean;

/**
 * Created by JasonYao on 2018/7/26.
 */
public class Order {
    private String SellerId;
    private String ProductDesc;
    private String OrderSubject;
    private String Amount;
    private String OutTradeNo;
    private String AsyncNotifyUrl;
    private String BizKey;

    private String out_trade_no;
    private String trade_no;
    private String app_id;
    private String total_amount;
    private String seller_id;
    private String timestamp;
    private String code;
    private String resultStatus;
    private String ExtendInfo;
    private Integer Timeout;

    public Integer getTimeout() {
        return Timeout;
    }

    public void setTimeout(Integer timeout) {
        Timeout = timeout;
    }

    //返回的微信订单号
    private String appid;
    private String noncestr;
    private String partnerid;
    private String prepayid;
    private String mypackage;
    private String sign;

    private String OrderNum;
    private String CardId;

    @Override
    public String toString() {
        return "Order{" +
                "SellerId='" + SellerId + '\'' +
                ", ProductDesc='" + ProductDesc + '\'' +
                ", OrderSubject='" + OrderSubject + '\'' +
                ", Amount='" + Amount + '\'' +
                ", OutTradeNo='" + OutTradeNo + '\'' +
                ", AsyncNotifyUrl='" + AsyncNotifyUrl + '\'' +
                ", BizKey='" + BizKey + '\'' +
                ", out_trade_no='" + out_trade_no + '\'' +
                ", trade_no='" + trade_no + '\'' +
                ", app_id='" + app_id + '\'' +
                ", total_amount='" + total_amount + '\'' +
                ", seller_id='" + seller_id + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", code='" + code + '\'' +
                ", resultStatus='" + resultStatus + '\'' +
                ", ExtendInfo='" + ExtendInfo + '\'' +
                ", appid='" + appid + '\'' +
                ", noncestr='" + noncestr + '\'' +
                ", partnerid='" + partnerid + '\'' +
                ", prepayid='" + prepayid + '\'' +
                ", mypackage='" + mypackage + '\'' +
                ", sign='" + sign + '\'' +
                ", OrderNum='" + OrderNum + '\'' +
                ", CardId='" + CardId + '\'' +
                '}';
    }

    public String getExtendInfo() {
        return ExtendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        ExtendInfo = extendInfo;
    }

    public String getBizKey() {
        return BizKey;
    }

    public void setBizKey(String bizKey) {
        BizKey = bizKey;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(String orderNum) {
        OrderNum = orderNum;
    }

    public String getCardId() {
        return CardId;
    }

    public void setCardId(String cardId) {
        CardId = cardId;
    }

    public String getSellerId() {
        return SellerId;
    }

    public void setSellerId(String sellerId) {
        SellerId = sellerId;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public String getOrderSubject() {
        return OrderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        OrderSubject = orderSubject;
    }

    public String getAmount() {
        return Amount;
    }

    public void setAmount(String amount) {
        Amount = amount;
    }

    public String getOutTradeNo() {
        return OutTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        OutTradeNo = outTradeNo;
    }

    public String getAsyncNotifyUrl() {
        return AsyncNotifyUrl;
    }

    public void setAsyncNotifyUrl(String asyncNotifyUrl) {
        AsyncNotifyUrl = asyncNotifyUrl;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(String resultStatus) {
        this.resultStatus = resultStatus;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getPrepayid() {
        return prepayid;
    }

    public void setPrepayid(String prepayid) {
        this.prepayid = prepayid;
    }

    public String getMypackage() {
        return mypackage;
    }

    public void setMypackage(String mypackage) {
        this.mypackage = mypackage;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
