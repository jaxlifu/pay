package com.library.pay.wechat;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.library.pay.BasePayUtils;
import com.library.pay.HttpRequestUtils;
import com.library.pay.PayConstants;
import com.library.pay.model.PayParams;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;


/**
 * 微信支付工具类
 * 微信支付前先配置微信登录部分,确保能进行微信登录(部分情况下有可能是微信缓存导致不能调用微信客户端)
 * 支付只需正确配置 mAppId, mPartnerId,(这2个参数客户端配置) mPrepayId, mNonceStr, mTimeStamp, mPackageValue,mSign;(这几个参数后台返回)
 * 如果提示==>"支付发生异常" 而errCode = -1 可能是微信缓存问题,这个时候清理下微信的缓存重试下
 */
public class WxPayUtils extends BasePayUtils {
    private IWXAPI api;
    private Handler mHandler;
    private String mAppId, mPartnerId, mPrepayId, mNonceStr, mTimeStamp, mPackageValue, mSign;

    /**
     * 充值构造
     */
    public WxPayUtils(Activity activity) {
        super();
        this.mActivity = activity;
        this.mHandler = new Handler();
        api = WXAPIFactory.createWXAPI(mActivity, "PayConstants.WECHAT_APP_ID");
        if (!PayConstants.PAY_DEBUG_MODE) {
            if (TextUtils.isEmpty(PayConstants.WECHAT_APP_ID) || TextUtils.isEmpty(PayConstants.WECHAT_PARTNER_ID)) {
                throw new RuntimeException("请配置微信APPID以及商户号!");
            }
        }
    }

    public boolean checkSupportPay() {
        boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(mActivity, "请安装最新版本微信!", Toast.LENGTH_SHORT).show();
        }
        return isPaySupported;
    }

    public void pay(PayParams params) {
        //测试状态下使用微信的默认数据
        if (PayConstants.PAY_DEBUG_MODE) {
            debugPay();
            return;
        }
        sendPayReq(params);
    }

    private void debugPay() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String result = HttpRequestUtils.httpRequest(PayConstants.WECHAT_PARAMS_URL);
                sendPayReq(PayParams.getInstance4Json(result));
            }
        }).start();
    }

    private void sendPayReq(PayParams params) {
        if (PayConstants.PAY_DEBUG_MODE) {
            mAppId = params.getAppid();
            mPartnerId = params.getPartnerid();
        } else {//发布状态这个2个值直接在客户端配置
            mAppId = PayConstants.WECHAT_APP_ID;
            mPartnerId = PayConstants.WECHAT_PARTNER_ID;
        }
        mPrepayId = params.getPrepayId();
        mNonceStr = params.getNoncestr();
        mTimeStamp = params.getTimestamp();
        mPackageValue = params.getPackages();
        mSign = params.getSign();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                PayReq req = new PayReq();
                req.appId = mAppId;
                req.partnerId = mPartnerId;
                req.prepayId = mPrepayId;
                req.nonceStr = mNonceStr;
                req.timeStamp = mTimeStamp;
                req.packageValue = mPackageValue;
                req.sign = mSign;
                api.registerApp(PayConstants.WECHAT_APP_ID);
                api.sendReq(req);
            }
        });
    }
}
