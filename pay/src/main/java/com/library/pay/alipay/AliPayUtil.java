package com.library.pay.alipay;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.library.pay.BasePayUtils;
import com.library.pay.PayConstants;
import com.library.pay.model.PayParams;

import java.util.Map;

/**
 * 支付宝支付类
 * notify_url 要求后台给出
 * 使用时注意lib 文件用最新的否则可能出现不能直接调用的情况(加载的网页支付内容空白)
 * 使用时只需配置参数:
 * 1.商户PID 合作伙伴身份（PID）--PARTNER
 * 2.商户收款账号 --SELLER
 * 3.商户私钥，pkcs8格式(一般要求生成3个密钥,后台使用一个,前端只需要使用pkcs8格式的--去空格和--begin--及--end--部分) --RSA_PRIVATE
 */
public class AliPayUtil extends BasePayUtils {

    private static final String DEBUG_PKCS8 =
            "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBALuKz4BSECtGAC/J\n" +
                    "lLPEMJY8YZIEAvUOGbVpMBk0U87RrQzbcWxmJzy82nZKN1O14+qSpnGo9I0XbT6x\n" +
                    "jaZ3CUZU9AWKHY8Dsp80lAax3RCXjlahfiQWl5pRwhaKFahfqlWEGCG+3/b48tlZ\n" +
                    "hr0NopQOA3uhSk6wDQRXvEamSf9hAgMBAAECgYEAlLIYl8t5GnZkC3+usWGqjFPP\n" +
                    "oLAhkB7f72A5TKKamG7S4FDVe3b7QUMTi5qIh4y96uzfn1qwm3Wpnn20q/nRlV6c\n" +
                    "8DM4t3wNZIMhnBzFvpWMtAp+SrvMUgyYeZ82NHMv1ttwhbEVLLViDYcdjZRFRu29\n" +
                    "TPCMmZ5N1c/fSQbA3IkCQQD0ToGXyhRwIhaY2Eye+dCqDv38YqQ85jAj6ouOgVDR\n" +
                    "d8PNLmDXWXUDWrIquxTvjJVUtGgZng0Bw7VGkhNDXXWjAkEAxITEymzjG1v0Dnwg\n" +
                    "jn+sxXd+wv2yY0UtKZ9E8lZ+gj52RECvIYIVo4CW1dMDhKKqWrw6eJ6FKXdBJUy4\n" +
                    "8s2fKwJBANZQJCkW0dUIYlBUtupi9aSpIC+ODMuLlsF7GE+7qz0F1hMhUzy2sgpu\n" +
                    "PotzsErwRXOYCxYl6v0YROaScCmRTAcCQE6d9sQsb2aAkNBCpBudxBZzSYjkSTsh\n" +
                    "b4HZL5HrPE0Kg/GmFunGyrVQOgdslVm4YYJPDu02LXt7M9qiKNMpo2UCQQCAiCPe\n" +
                    "T4JMRHHsbWVj12mQDr9wzQzmT9zges5N/vqWBtRA4nyaiy4x1wkX6Zlcn63QPkyw\n" +
                    "J780OeGD8x7WS52J";
    private static final String DEBUG_APPID = "2016050401361308";

    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;

    private Handler mHandler;

    public AliPayUtil(Activity mActivity) {
        super();
        this.mActivity = mActivity;
        if (!PayConstants.PAY_DEBUG_MODE) {
            if (TextUtils.isEmpty(PayConstants.ALIPAY_APPID) || TextUtils.isEmpty(PayConstants.ALIPAY_RSA_PRIVATE)) {
                throw new RuntimeException("请配置支付宝APPID以及pkcs8格式商户私钥!");
            }
        } else {
            PayConstants.ALIPAY_APPID = DEBUG_APPID;
            PayConstants.ALIPAY_RSA_PRIVATE = DEBUG_PKCS8;
        }
        mHandler = new Handler(mCallback);
    }

    @Override
    public void setPayType(int type) {
        super.setPayType(type);
    }

    /**
     * 9000	订单支付成功
     * 8000	正在处理中，支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 4000	订单支付失败
     * 5000	重复请求
     * 6001	用户中途取消
     * 6002	网络连接出错
     * 6004	支付结果未知（有可能已经支付成功），请查询商户订单列表中订单的支付状态
     * 其它	其它支付错误
     */
    private Handler.Callback mCallback = new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) message.obj);
                    //打印用户操作结果
                    if (!TextUtils.isEmpty(payResult.getMemo())) {
                        Toast.makeText(mActivity, payResult.getMemo(), Toast.LENGTH_SHORT).show();
                    }
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(mActivity, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(mActivity, "支付结果确认中", Toast.LENGTH_SHORT).show();
                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(mActivity, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    boolean isisExist = (boolean) message.obj;
                    Log.d(TAG, "isisExist" + isisExist);
                    break;
                }
                default:
                    break;
            }
            return false;
        }
    };

    public void pay(PayParams params) {
        String outTradeNo, subject, body, price;
        try {
            if (PayConstants.PAY_DEBUG_MODE) {
                outTradeNo = OrderInfoUtils.getOutTradeNo();
                subject = "测试的商品";
                body = "该测试商品的详细描述";
                price = "0.01";
            } else {
                if (params == null) {
                    Log.d(TAG, "支付信息有误!");
                    Toast.makeText(mActivity, "支付失败!", Toast.LENGTH_SHORT).show();
                    return;
                }
                outTradeNo = params.getOutTradeNo();
                subject = params.getSubject();
                body = params.getBody();
                price = params.getPrice();
            }

            final String orderInfo = OrderInfoUtils.getOrderInfo(body, subject, price, outTradeNo);
            Log.d(TAG + "请求的参数", orderInfo);
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    PayTask alipay = new PayTask(mActivity);
                    /**
                     * String orderInfo	app支付请求参数字符串，主要包含商户的订单信息，key=value形式，以&连接。
                     * boolean isShowPayLoading	用户在商户app内部点击付款，是否需要一个loading做为在钱包唤起之前的过渡，
                     *         这个值设置为true，将会在调用pay接口的时候直接唤起一个loading，直到唤起H5支付页面或者唤起外部的钱包付款页面loading才消失。
                     *        （建议将该值设置为true，优化点击付款到支付唤起支付页面的过渡过程。）
                     * */
                    Map<String, String> result = alipay.payV2(orderInfo, true);
                    Log.i("msp", result.toString());

                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };
            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            // Thread的run方法是不抛出任何检查型异常(checked exception)的,
            // 但是它自身却可能因为一个异常而被终止，导致这个线程的终结。最麻烦的是，在线程中抛出的异常即使使用try...catch也无法截获，
            // 因此可能导致一些问题出现，比如异常的时候无法回收一些系统资源，或者没有关闭当前的连接等等。自定义的一个UncaughtExceptionHandler,
            payThread.setUncaughtExceptionHandler(mExceptionHandler);
            payThread.start();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(mActivity, "支付失败,请稍后重试!", Toast.LENGTH_SHORT).show();
        }
    }

    private Thread.UncaughtExceptionHandler mExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            Log.d(TAG, "This thread is:" + thread.getName() + ",Message:" + throwable.getMessage());
        }
    };

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public boolean checkSupportPay() {
        Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // 调用查询接口，获取查询结果
                boolean isExist = !TextUtils.isEmpty(getSDKVersion());
                Message msg = new Message();
                msg.what = SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.setUncaughtExceptionHandler(mExceptionHandler);
        checkThread.start();
        return true;

    }

    /**
     * get the sdk version. 获取SDK版本号
     */
    private String getSDKVersion() {
        PayTask payTask = new PayTask(mActivity);
        return payTask.getVersion();
    }

}