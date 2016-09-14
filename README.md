#Pay library
针对支付宝以及微信封装的支付相关功能
*  使用方法
 ```
     //在你的application中修改支付相关的信息,如果不做修改的话默认的是测试内容
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
 ```  

 ```
   //支付时候你只需要通过PayUtilsFactory获取相关的支付工具即可
    private BasePayUtils mPayUtils;
    public void wechat(View view) {
        mPayUtils = PayUtilsFactory.getPayUtils(this, PayUtilsFactory.PAY_TYPE_WECHAT);
        if (mPayUtils.checkSupportPay()) mPayUtils.pay("");
    }

    public void alipay(View view) {
        mPayUtils = PayUtilsFactory.getPayUtils(this, PayUtilsFactory.PAY_TYPE_ALIPAY);
        if (mPayUtils.checkSupportPay()) mPayUtils.pay("");
    }
 ```  

*  特别提示: 
  1. 运行Demo时如果要看效果,请将`GlobalApplication`中的`onCreate中的 initPayInfo();注释掉`
  2. 如果微信出现了第一次点击能够调起微信界面,后面不能请确认你的appid对应的信息,修改微信后台的包名以及签名信息 
