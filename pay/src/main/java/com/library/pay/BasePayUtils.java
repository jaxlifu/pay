package com.library.pay;

import android.app.Activity;

import com.library.pay.model.PayParams;

/**
 * Created by Jax on 2016/9/13.
 * Description :如果有其他的支付方式可以继承这个类做些其他操作
 * Version : V1.0.0
 */
public abstract class BasePayUtils {
    protected String TAG = getClass().getSimpleName();
    protected Activity mActivity;
    public static final int TYPE_WALLET = 0;
    public static final int TYPE_ORDER = 1;
    //根据需求而定,此处支付类型是指的钱包充值,或者是订单支付,回调是不一样的,如果没有需要可以不管
    protected int mPayType;

    /**
     * @param params 请以json格式传过来请求参数
     */
    public abstract void pay(PayParams params);

    /**
     * 这个方法作用对于支付宝作用不大,支付宝会直接调用H5Pay微信的话只能提示用户下载最新班的微信咯
     */
    public abstract boolean checkSupportPay();

    /**
     * 设置支付类型,对应后台的支付回调,钱包充值 type = 0 订单付款 type = 1
     * AliPayUtil重写了这个方法,wxPayUtils 不需要重写这个方法
     */
    public void setPayType(int type) {
        mPayType = type;
    }
}
