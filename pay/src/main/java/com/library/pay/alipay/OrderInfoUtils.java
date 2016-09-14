package com.library.pay.alipay;

import com.library.pay.PayConstants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jax on 2016/9/13.
 * Description :
 * Version : V1.0.0
 */
public class OrderInfoUtils {
    /**
     * 生成支付宝支付订单信息
     *
     * @param body        String	否	128	对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。	Iphone6 16G
     * @param subject     subject	String	是	256	商品的标题/交易标题/订单标题/订单关键字等。	大乐透
     * @param outTradeNo  out_trade_no	String	是	64	商户网站唯一订单号	70501111111S001111119
     * @param totalAmount total_amount	String	是	9	订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]	9.00
     */
    public static String getOrderInfo(String body, String subject, String totalAmount, String outTradeNo) {
        Map<String, String> map = new HashMap<>();
        Map<String, String> params = buildOrderParamMap(body, subject, totalAmount, outTradeNo);
        map.putAll(params);
        map.put("sign", getSign(params, PayConstants.ALIPAY_RSA_PRIVATE));
        return buildOrderParam(map);
    }


    /**
     * 要求外部订单号必须唯一。
     *
     * @return
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    ///////////////////////////////////////////////////////////////////////////
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * 构造支付订单参数信息
     *
     * @param map 支付订单参数
     * @return
     */
    private static String buildOrderParam(Map<String, String> map) {
        List<String> keys = new ArrayList<>(map.keySet());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            sb.append(buildKeyValue(key, value, true));
            sb.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        sb.append(buildKeyValue(tailKey, tailValue, true));

        return sb.toString();
    }

    /**
     * 拼接键值对
     *
     * @param key
     * @param value
     * @param isEncode
     * @return
     */
    private static String buildKeyValue(String key, String value, boolean isEncode) {
        StringBuilder sb = new StringBuilder();
        sb.append(key);
        sb.append("=");
        if (isEncode) {
            try {
                sb.append(URLEncoder.encode(value, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                sb.append(value);
            }
        } else {
            sb.append(value);
        }
        return sb.toString();
    }

    /**
     * 对支付参数信息进行签名
     *
     * @param map 待签名授权信息
     * @return
     */
    private static String getSign(Map<String, String> map, String rsaKey) {
        List<String> keys = new ArrayList<>(map.keySet());
        // key排序
        Collections.sort(keys);

        StringBuilder authInfo = new StringBuilder();
        for (int i = 0; i < keys.size() - 1; i++) {
            String key = keys.get(i);
            String value = map.get(key);
            authInfo.append(buildKeyValue(key, value, false));
            authInfo.append("&");
        }

        String tailKey = keys.get(keys.size() - 1);
        String tailValue = map.get(tailKey);
        authInfo.append(buildKeyValue(tailKey, tailValue, false));

        String oriSign = SignUtils.sign(authInfo.toString(), rsaKey);
        String encodedSign = "";

        try {
            encodedSign = URLEncoder.encode(oriSign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encodedSign;
    }

    /**
     * 参数	类型	是否必填	最大长度	描述	示例值
     * body	String	否	128	对一笔交易的具体描述信息。如果是多种商品，请将商品描述字符串累加传给body。	Iphone6 16G
     * subject	String	是	256	商品的标题/交易标题/订单标题/订单关键字等。	大乐透
     * out_trade_no	String	是	64	商户网站唯一订单号	70501111111S001111119
     * timeout_express	String	否	6	该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。	90m
     * total_amount	String	是	9	订单总金额，单位为元，精确到小数点后两位，取值范围[0.01,100000000]	9.00
     * seller_id	String	否	16	收款支付宝用户ID。 如果该值为空，则默认为商户签约账号对应的支付宝用户ID	2088102147948060
     * product_code	String	是	64	销售产品码，商家和支付宝签约的产品码	QUICK_MSECURITY_PAY
     */
    private static String setBizContent(String body, String subject, String out_trade_no, String total_amount, String seller_id) {
        String bizContent = "";
        try {
            JSONObject obj = new JSONObject();
            obj.putOpt("body", body);
            obj.putOpt("subject", subject);
            obj.putOpt("out_trade_no", out_trade_no);
            obj.putOpt("timeout_express", "30m");
            obj.putOpt("total_amount", total_amount);
            obj.putOpt("seller_id", seller_id);
            obj.putOpt("product_code", "QUICK_MSECURITY_PAY");
            bizContent = obj.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return bizContent;
    }

    /**
     * 构造支付订单参数列表
     * <p/>
     * app_id    String	是	32	支付宝分配给开发者的应用ID	2014072300007148
     * method	String	是	128	接口名称	alipay.trade.app.pay
     * format	String	否	40	仅支持JSON	JSON
     * charset	String	是	10	请求使用的编码格式，如utf-8,gbk,gb2312等	utf-8
     * sign_type	String	是	10	商户生成签名字符串所使用的签名算法类型，目前支持RSA	RSA
     * sign	String	是	256	商户请求参数的签名串，详见签名	详见示例
     * timestamp	String	是	19	发送请求的时间，格式"yyyy-MM-dd HH:mm:ss"	2014-07-24 03:07:50
     * version	String	是	3	调用的接口版本，固定为：1.0	1.0
     * notify_url	String	是	256	支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https	https://api.xx.com/receive_notify.htm
     * biz_content	String	是	-	业务请求参数的集合，最大长度不限，除公共参数外所有请求参数都必须放在这个参数中传递，具体参照各产品快速接入文档
     */
    private static Map<String, String> buildOrderParamMap(String body, String subject, String totalAmount, String outTradeNo) {
        Map<String, String> keyValues = new HashMap<>();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();

        keyValues.put("app_id", PayConstants.ALIPAY_APPID);

        keyValues.put("method", "alipay.trade.app.pay");

        keyValues.put("charset", "utf-8");

        keyValues.put("format", "json");

        keyValues.put("sign_type", "RSA");

        keyValues.put("notify_url", PayConstants.ALIPAY_NOTIFY_URL);

        keyValues.put("biz_content", setBizContent(body, subject, outTradeNo, totalAmount, ""));

        keyValues.put("timestamp", format.format(date));

        keyValues.put("version", "1.0");
        return keyValues;
    }
}
