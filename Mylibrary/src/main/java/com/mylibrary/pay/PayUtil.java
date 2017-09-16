package com.mylibrary.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mylibrary.common.AppConfig;
import com.mylibrary.info.WXInfo;
import com.mylibrary.manager.Log;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * author:jjj
 * time:2017/3/9 17:25
 * TODO:支付类
 */
public class PayUtil {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;

    public static String union_Model = "00";// 00-正式 01-测试(银联)

    private static PayListener mPayListener;

    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            Log.e("zfb---", "" + msg.obj);

            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    if (msg.obj instanceof Map) {
                        PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                        String resultStatus = payResult.getResultStatus();

                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            String totalAmount = null;
                            try {
                                JSONObject o = new JSONObject(payResult.getResult());
                                if (!o.isNull("alipay_trade_app_pay_response")) {
                                    totalAmount = o.getJSONObject("alipay_trade_app_pay_response").getString("total_amount");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            mPayListener.onResultSuccess(totalAmount);
                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                mPayListener.onResultFail("支付结果确认中,请耐心等待");

                            } else {
                                mPayListener.onResultFail("支付失败");
                            }
                        }
                    } else {
                        mPayListener.onResultFail("支付失败");
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    mPayListener.onResultFail((String) msg.obj);
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * 银联支付
     *
     * @param activity
     * @param id       交易订单号，由银联生成
     */
    public static void wayToYinlian(Activity activity, String id) {
//        UPPayAssistEx.startPay(activity, null, null, id, union_Model);
    }

    /**
     * 微信支付
     *
     * @param context appId        公众账号ID
     *                partnerId    商户号
     *                prepayId     预支付交易会话ID
     *                packageValue 扩展字段 暂填写固定值Sign=WXPay
     *                nonceStr     随机字符串，不长于32位。推荐随机数生成算法
     *                timeStampe   时间戳
     *                sign         签名
     */
    public static void wayToWX(Context context, WXInfo wxInfo) {
        IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
        msgApi.registerApp(AppConfig.WX_APPID);

        boolean isPaySupported = msgApi.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
        if (!isPaySupported) {
            Toast.makeText(context, "请先打开微信", Toast.LENGTH_SHORT).show();
            return;
        }

        PayReq request = new PayReq();
        request.appId = wxInfo.getAppid();
        request.partnerId = wxInfo.getMch_id();
        request.prepayId = wxInfo.getPrepay_id();
        request.nonceStr = wxInfo.getNonce_str();
        request.timeStamp = wxInfo.getTimestamp();
        request.packageValue = "Sign=WXPay";
        request.sign = wxInfo.getSign();
        msgApi.sendReq(request);
    }

    /**
     * 支付宝支付
     */
    public static void wayToZFB(final Context context, final String payInfo, PayListener payListener) {
        mPayListener = payListener;
        //       //此只用于沙箱环境
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        try {
            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask((Activity) context);
                    // 调用支付接口，获取支付结果
//                    String result = alipay.pay(payInfo, true);
                    Map<String, String> result = alipay.payV2(payInfo, true);
                    Message msg = new Message();
                    msg.what = SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                }
            };

            // 必须异步调用
            Thread payThread = new Thread(payRunnable);
            payThread.start();
        } catch (Exception e) {
            mPayListener.onResultFail(e.getMessage());
        }
    }

    public interface PayListener {
        /**
         * 支付成功
         */
        void onResultSuccess(String totalAmount);

        /**
         * 支付失败
         *
         * @param msg
         */
        void onResultFail(String msg);
    }
}
