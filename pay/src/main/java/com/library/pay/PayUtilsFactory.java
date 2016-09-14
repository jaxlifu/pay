package com.library.pay;

import android.app.Activity;

import com.library.pay.alipay.AliPayUtil;
import com.library.pay.wechat.WxPayUtils;

/**
 * Created by Jax on 2016/9/13.
 * Description :如果有其他的支付方式可以继承这个类做其他的操作
 * Version : V1.0.0
 */
public class PayUtilsFactory {
    public final static int PAY_TYPE_ALIPAY = 2;//支付宝支付
    public final static int PAY_TYPE_WECHAT = 3;//微信支付
    public final static int PAY_TYPE_YIPAY = 4;//翼支付
    private static BasePayUtils mPayUtils;


    /**
     * @param act     上下文对象,最好是传入Activity
     * @param payType 支付类型(支付宝 ,微信 ,翼支付,其他的可以自己修改定义)
     */
    public static BasePayUtils getPayUtils(Activity act, int payType) {
        switch (payType) {
            case PAY_TYPE_ALIPAY:
                mPayUtils = new AliPayUtil(act);
                break;
            case PAY_TYPE_WECHAT:
                mPayUtils = new WxPayUtils(act);
                break;
            case PAY_TYPE_YIPAY:
                break;
        }
        return mPayUtils;

    }
}
