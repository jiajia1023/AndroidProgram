package com.mylibrary.pay;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.mylibrary.common.AppConfig;
import com.mylibrary.info.WXInfo;
import com.mylibrary.manager.Log;
import com.tencent.mm.opensdk.constants.Build;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.net.URLEncoder;

/**
 * author:jjj
 * time:2017/3/9 17:25
 * TODO:支付类
 */
public class PayUtil2 {
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    // 商户PID
    public static final String PARTNER = "2088102169742033";
    // 商户收款账号
    public static final String SELLER = "mg@hptree.cn";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCa2EgSoDfz3H6aRrdrwM43nwZ8dFErlObWv2BUEKxKCP+WsX4CNeGy1ivGzta2XfpmGa30KNQ513zDLdfY+HhkA2CRAYmSb37IUQEZn+H8mjHxjrsEocBsCZXdY0YqZtAgj+SMKxbHV62BOWQSog7HIkB8bx3UHyGEtyvT0VqjJGCs3f+cwwTmP0Fcjiv4cxPvlDZkABE0EvUrei4e2VqJBR6GLeS2E6olvy7MDwUsqgFhisfJnjcMs6gbrIB56C3+zBe283SS7uRT+oI/fgxN2JGGEiy0s5K/X1kR7m6cXmmx4JaODEwkdvOgoY3HgcTsyICZCdGlR8p20CWB4wc7AgMBAAECggEAE4+U8z1oNBc7qxOZ4jPWGTKuqBQtmNrTmndMnPIKQcQHPasoZvEVlPj3SMMiXqFam4FM91YdET2GlkV5SgCBfOjsTwUpsuUA2B9bizgmXTnBa2mDeJ9SZpODahc7aunYCvUCgy6CTyZlVbjBSpVMVp2eFdoXo49XNaPKFpUQ5SDlnJMAAAdbq//QyKJxBjpujIfbs60iaHMrT2CM2CuW1Orfcd4p7gsXzW/CuRKSvjWn5Z7rEB7pe4rkaaKug2g9c5dxn2R2PG5apDqDPiRJdE/JulTWL3ktdUN3Rasb7hvmoYvmyCVGY9L/9CYa5XdSW6eL6E5WdJDAn2oLVE91sQKBgQDh+MN8Qth47PLsRWmqAVSSWtXclzm4UyHZ5jaQWFujcxbGdyk1wtpLRHQZuHcWkpyoOEkZImV3I4sLt/Sn1pKODkMqWEjFBbE0UiKuvFae0PJJtpG5w5XqpFvDNnQ4oldbnRZrccfVUia8+Uxqj2dbBndg2nl6cf2nSs7092DqgwKBgQCva+H4ez0V90XwNc9URfxv/N/8fi1YLiqJDmncB3mSVcsVB1S2+7oSNYGfhIp8oaWCnlTdCM+g4eFmWmhnbmeAd3L3XM8PoOOMfb2+pqK5gXABDueOe8HSVK9mc0prH8m+2KF3JSvNlaIR4hzb3YbsS/12s1TqqyKlCZlkyaAy6QKBgQC5lBEHNeaNuRrviznx0lHgOBU2qkNO64b7aqMY/FeV7mif6TDA5BtegQNQNDwpH5LkXAU8TRFDPL9uik60n/WQEIp8djCdDcWwEFxHYIlKuJKikvsusj+migCKW2grMrfHl0TYTIMu397Mh3iIcukQyki0E/h75K3J/Bfm+lpvfQKBgAvNLTmjSNvGc+0SIa2gu7fpAkShVHmfOXtY/eQwmIwwu2XhxWA+FSmOcfcCWrLByTm9XyYzpoxDRf+1y1pSAiKRZqmfk8qQX16fQvmlikMCoz9e+lMKnmKBEf/Jd9M3I0J0noA+fvDoxSuA08Iy6BXTkuy+adMTylPiDnDKY72pAoGATgBfacnARYN84qd/SEhgbN4SE581CGSlWSavyp+8sIO9bnWwkoXK4YnWBOUjCf7OqDaDflb6BFGDlgskQHiDHODdJLjvTtvrHNQktQStjU3Qgnj8ekd5Fgqyxv/C5GDbu/En81pS47Ez1XULIZ2SRoI+Tj0AwoDnimPLE/8+xFk=";

    public static String union_Model = "00";// 00-正式 01-测试(银联)

    private static PayListener mPayListener;

    private static Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
//            PayResult result = new PayResult((String) msg.obj);
            Log.e("zfb---", ""+msg.obj);
//            Toast.makeText(DemoActivity.this, result.getResult(),
//                    Toast.LENGTH_LONG).show();

//            switch (msg.what) {
//                case SDK_PAY_FLAG: {
//                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
//
//                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
//                    String resultInfo = payResult.getResult();
//
//                    String resultStatus = payResult.getResultStatus();
//
//                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
//                    if (TextUtils.equals(resultStatus, "9000")) {
//                        mPayListener.resultForZhifubao(1, "支付成功");
//                    } else {
//                        // 判断resultStatus 为非“9000”则代表可能支付失败
//                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
//                        if (TextUtils.equals(resultStatus, "8000")) {
//                            mPayListener.resultForZhifubao(0, "支付结果确认中,请耐心等待");
//
//                        } else {
//                            mPayListener.resultForZhifubao(0, "支付失败");
//                        }
//                    }
//                    break;
//                }
//                case SDK_CHECK_FLAG: {
//                    mPayListener.resultForZhifubao(0, (String) msg.obj);
//                    break;
//                }
//                default:
//                    break;
//            }
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
    public static void wayToZhifubao(final Context context, String price,
                                     String pay_sn) {
        mPayListener = (PayListener) context;
        // 订单
        String orderInfo = getOrderInfo(price, pay_sn);

        try {
            // 对订单做RSA 签名
            String sign = SignUtils.sign(orderInfo, RSA_PRIVATE, true);
//            String sign = SignUtils.sign(orderInfo, RSA_PRIVATE);
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
            // 完整的符合支付宝参数规范的订单信息
            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                    + "sign_type=\"RSA\"";

            Runnable payRunnable = new Runnable() {

                @Override
                public void run() {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask((Activity) context);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo, true);

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
        }
    }

    /**
     * create the order info. 创建订单信息
     */
    private static String getOrderInfo(String price, String pay_sn) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + pay_sn + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + "小欧家商品购买_" + pay_sn + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + pay_sn + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + AppConfig.ZFB_URL
                + "method=apppayment/sync_of_alipay" + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";
        // orderInfo += "&extra_common_param=\"product_buy\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";
        return orderInfo;
    }

    public interface PayListener {
        /**
         * 支付宝返回结果
         *
         * @param state 0 失败 1 成功
         */
        public void resultForZhifubao(int state, String detail);
    }
}
