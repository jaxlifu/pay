package com.library.pay.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.library.pay.PayConstants;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * Created by Jax on 2016/9/13.
 * Description :
 * Version : V1.0.0
 */
public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, PayConstants.WECHAT_APP_ID);
        api.registerApp(PayConstants.WECHAT_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }


    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String result = "";
            switch (resp.errCode) {
                case BaseResp.ErrCode.ERR_OK:
                    result = "微信支付成功";
                    //支付成功发送广播刷新余额
                    break;
                case BaseResp.ErrCode.ERR_COMM:
                    result = "支付发生异常";
                    break;
                case BaseResp.ErrCode.ERR_USER_CANCEL:
                    result = "已取消微信支付";
                    break;
            }
            Toast.makeText(this, result, Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
