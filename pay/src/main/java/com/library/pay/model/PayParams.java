package com.library.pay.model;

import org.json.JSONObject;

/**
 * Created by Jax on 2016/9/14.
 * Description :
 * Version : V1.0.0
 */
public class PayParams {
    //alipay
    private String body;
    private String subject;
    private String price;
    private String outTradeNo;
    //wechat
    private String appid;
    private String partnerid;
    private String prepayId;
    private String noncestr;
    private String timestamp;
    private String packages;
    private String sign;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getPartnerid() {
        return partnerid;
    }

    public void setPartnerid(String partnerid) {
        this.partnerid = partnerid;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getNoncestr() {
        return noncestr;
    }

    public void setNoncestr(String noncestr) {
        this.noncestr = noncestr;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * {
     * "appid": "wxb4ba3c02aa476ea1",
     * "partnerid": "1305176001",
     * "package": "Sign=WXPay",
     * "noncestr": "a858591cb46a391bc76048bf37eb5ff8",
     * "timestamp": 1473834760,
     * "prepayid": "wx201609141432405a34b6f1010879527558",
     * "sign": "EC8BB8F8C0BBF5DAE5EBC923A311B20B"
     * }
     */
    public static PayParams getInstance4Json(String json) {
        PayParams params = new PayParams();
        try {
            JSONObject obj = new JSONObject(json);
            params.setAppid(obj.optString("appid"));
            params.setPartnerid(obj.optString("partnerid"));
            params.setPackages(obj.optString("package"));
            params.setNoncestr(obj.optString("noncestr"));
            params.setTimestamp(obj.optString("timestamp"));
            params.setPrepayId(obj.optString("prepayid"));
            params.setSign(obj.optString("sign"));
            params.setBody(obj.optString("body"));
            params.setSubject(obj.optString("subject"));
            params.setOutTradeNo(obj.optString("out_trade_no"));
            params.setPrice(obj.optString("total_amount"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return params;
    }
}
