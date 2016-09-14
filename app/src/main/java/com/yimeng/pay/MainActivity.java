package com.yimeng.pay;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.library.pay.BasePayUtils;
import com.library.pay.PayUtilsFactory;

public class MainActivity extends AppCompatActivity {
    private BasePayUtils mPayUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    public void wechat(View view) {
        mPayUtils = PayUtilsFactory.getPayUtils(this, PayUtilsFactory.PAY_TYPE_WECHAT);
        if (mPayUtils.checkSupportPay()) mPayUtils.pay(null);
    }

    public void alipay(View view) {
        mPayUtils = PayUtilsFactory.getPayUtils(this, PayUtilsFactory.PAY_TYPE_ALIPAY);
        if (mPayUtils.checkSupportPay()) mPayUtils.pay(null);
    }


}
