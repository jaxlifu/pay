package com.yimeng;

import android.app.Application;

import com.library.pay.PayConstants;

/**
 * Created by Jax on 2016/9/14.
 * Description :
 * Version : V1.0.0
 */
public class GlobalApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        initPayInfo();

    }

    private void initPayInfo() {
        PayConstants.PAY_DEBUG_MODE = false;
        PayConstants.initWechat("you wechat appid", "wechat partner id");
        PayConstants.initAliPay("you alipay appid", "alipay rsa private of pkcs8", "alipay notify url");
    }
}
