package tendency.hz.zhihuijiayuan.bean;

import java.io.Serializable;

/**
 * 卡片订单
 * Created by JasonYao on 2018/8/1.
 */
public class CardOrder implements Serializable {
    private String productDesc;
    private String orderSubject;
    private String amount;
    private String outTrageNo;
    private PayType alipay;
    private PayType wxPay;
    private String callback;
    private String ExtendInfo;
    private Integer Timeout;

    public Integer getTimeout() {
        return Timeout;
    }

    public void setTimeout(Integer timeout) {
        Timeout = timeout;
    }

    @Override
    public String toString() {
        return "CardOrder{" +
                "productDesc='" + productDesc + '\'' +
                ", orderSubject='" + orderSubject + '\'' +
                ", amount='" + amount + '\'' +
                ", outTrageNo='" + outTrageNo + '\'' +
                ", alipay=" + alipay +
                ", wxPay=" + wxPay +
                ", callback='" + callback + '\'' +
                ", ExtendInfo='" + ExtendInfo + '\'' +
                '}';
    }

    public String getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(String productDesc) {
        this.productDesc = productDesc;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        this.orderSubject = orderSubject;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getOutTrageNo() {
        return outTrageNo;
    }

    public void setOutTrageNo(String outTrageNo) {
        this.outTrageNo = outTrageNo;
    }

    public PayType getAlipay() {
        return alipay;
    }

    public void setAlipay(PayType alipay) {
        this.alipay = alipay;
    }

    public PayType getWxPay() {
        return wxPay;
    }

    public void setWxPay(PayType wxPay) {
        this.wxPay = wxPay;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }

    public String getExtendInfo() {
        return ExtendInfo;
    }

    public void setExtendInfo(String extendInfo) {
        ExtendInfo = extendInfo;
    }

    public class PayType implements Serializable {
        private String sellerId = "";
        private String asyncNotifyUrl = "";
        private String bizKey = "";

        @Override
        public String toString() {
            return "PayType{" +
                    "SellerId='" + sellerId + '\'' +
                    ", AsyncNotifyUrl='" + asyncNotifyUrl + '\'' +
                    ", BizKey='" + bizKey + '\'' +
                    '}';
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getAsyncNotifyUrl() {
            return asyncNotifyUrl;
        }

        public void setAsyncNotifyUrl(String asyncNotifyUrl) {
            this.asyncNotifyUrl = asyncNotifyUrl;
        }

        public String getBizKey() {
            return bizKey;
        }

        public void setBizKey(String bizKey) {
            this.bizKey = bizKey;
        }
    }
}
