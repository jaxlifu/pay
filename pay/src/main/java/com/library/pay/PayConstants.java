package com.library.pay;

/**
 * Created by Jax on 2016/9/13.
 * Description :
 * Version : V1.0.0
 */
public class PayConstants {

    public static boolean PAY_DEBUG_MODE = true;
    public static final String WECHAT_PARAMS_URL = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
    /**
     * ----------微信支付相关参数-------------------
     */
    public static String WECHAT_APP_ID = ""; // 微信APPID
    public static String WECHAT_PARTNER_ID = "";//商户号
    /**
     * ----------支付宝相关参数-------------------
     */
    public static String ALIPAY_APPID = "";//新版本的需要AppID PayTask payV2 接口
    public static String ALIPAY_NOTIFY_URL = "";//订单支付的回调
    public static String ALIPAY_RSA_PRIVATE = ""; // 商户私钥，pkcs8格式

    /**
     * @param appId     微信APPID
     * @param partnerId 商户号 (商户号在后台看到的可能有公众号和app的注意不要弄错了)
     */
    public static void initWechat(String appId, String partnerId) {
        WECHAT_APP_ID = appId;
        WECHAT_PARTNER_ID = partnerId;
    }

    /**
     * @param appId 新版本的需要AppID PayTask payV2 接口
     * @param pkcs8 商户私钥，pkcs8格式(用支付宝给得工具来生成的)
     */
    public static void initAliPay(String appId, String pkcs8, String notifyUrl) {
        ALIPAY_APPID = appId;
        ALIPAY_RSA_PRIVATE = pkcs8;
        ALIPAY_NOTIFY_URL = notifyUrl;
    }
}
