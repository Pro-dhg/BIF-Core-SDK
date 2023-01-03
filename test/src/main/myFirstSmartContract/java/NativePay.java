import com.wechat.pay.contrib.apache.httpclient.WechatPayHttpClientBuilder;
import com.wechat.pay.contrib.apache.httpclient.auth.AutoUpdateCertificatesVerifier;
import com.wechat.pay.contrib.apache.httpclient.auth.PrivateKeySigner;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Credentials;
import com.wechat.pay.contrib.apache.httpclient.auth.WechatPay2Validator;
import com.wechat.pay.contrib.apache.httpclient.util.PemUtil;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.PrivateKey;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/3 16:41
 * @DESCRIPTION:
 */

public class NativePay {
    public static void main(String[] args) throws Exception {
        /**
         * 加载商户私钥（privateKey：私钥字符串）
         * 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
         */
        String privateKey = null;
        String apiV3Key = null;

        String mchId = null;
        String mchSerialNo = null;
        /**
         * 1.搭建和配置开发环境
         */
        // 加载商户私钥（privateKey：私钥字符串）
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

        // 加载平台证书（mchId：商户号,mchSerialNo：商户证书序列号,apiV3Key：V3密钥）
        AutoUpdateCertificatesVerifier verifier = new AutoUpdateCertificatesVerifier(
                new WechatPay2Credentials(mchId, new PrivateKeySigner(mchSerialNo, merchantPrivateKey)),apiV3Key.getBytes("utf-8"));

        // 初始化httpClient
        CloseableHttpClient httpClient = WechatPayHttpClientBuilder.create()
                .withMerchant(mchId, mchSerialNo, merchantPrivateKey)
                .withValidator(new WechatPay2Validator(verifier)).build();

        /**
         * 2.Native下单
         *  生成支付二维码
         */
        CreateOrder(httpClient);

        /**
         * 3.查询订单
         */
        QueryOrder(httpClient,mchId);

        /**
         * 4.关闭订单
         */
        CloseOrder(httpClient,mchId);

        /**
         * 5.申请交易账单
         */
        TradeBill(httpClient);

        /**
         * 6.下载账单
         */
        String download_url = null ; //通过申请交易账单接口获取到账单下载地址（download_url）
        DownloadUrl(httpClient, download_url, privateKey , mchId , mchSerialNo);


        /**
         * httpClient.close();
         */

    }

    public static void CreateOrder(CloseableHttpClient httpClient) throws Exception{
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
        // 请求body参数
        String reqdata = "{"
                + "\"time_expire\":\"2018-06-08T10:34:56+08:00\","
                + "\"amount\": {"
                + "\"total\":100,"
                + "\"currency\":\"CNY\""
                + "},"
                + "\"mchid\":\"1230000109\","
                + "\"description\":\"Image形象店-深圳腾大-QQ公仔\","
                + "\"notify_url\":\"https://www.weixin.qq.com/wxpay/pay.php\","
                + "\"out_trade_no\":\"1217752501201407033233368018\","
                + "\"goods_tag\":\"WXG\","
                + "\"appid\":\"wxd678efh567hg6787\","
                + "\"attach\":\"自定义数据说明\","
                + "\"detail\": {"
                + "\"invoice_id\":\"wx123\","
                + "\"goods_detail\": ["
                + "{"
                + "\"goods_name\":\"iPhoneX 256G\","
                + "\"wechatpay_goods_id\":\"1001\","
                + "\"quantity\":1,"
                + "\"merchant_goods_id\":\"商品编码\","
                + "\"unit_price\":828800"
                + "},"
                + "{"
                + "\"goods_name\":\"iPhoneX 256G\","
                + "\"wechatpay_goods_id\":\"1001\","
                + "\"quantity\":1,"
                + "\"merchant_goods_id\":\"商品编码\","
                + "\"unit_price\":828800"
                + "}"
                + "],"
                + "\"cost_price\":608800"
                + "},"
                + "\"scene_info\": {"
                + "\"store_info\": {"
                + "\"address\":\"广东省深圳市南山区科技中一道10000号\","
                + "\"area_code\":\"440305\","
                + "\"name\":\"腾讯大厦分店\","
                + "\"id\":\"0001\""
                + "},"
                + "\"device_id\":\"013467007045764\","
                + "\"payer_client_ip\":\"14.23.150.211\""
                + "}"
                + "}";
        StringEntity entity = new StringEntity(reqdata,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) { //处理成功
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) { //处理成功，无返回Body
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

    public static void QueryOrder(CloseableHttpClient httpClient, String mchId) throws Exception {

        //请求URL
        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/pay/transactions/id/4200000745202011093730578574");
        uriBuilder.setParameter("mchid", mchId);

        //完成签名并执行请求
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

    public static void CloseOrder(CloseableHttpClient httpClient, String mchId) throws Exception {

        //请求URL
        HttpPost httpPost = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/out-trade-no/sdkphp12345678920201028112429/close");
        //请求body参数
        String reqdata ="{\"mchid\": \""+mchId+"\"}";

        StringEntity entity = new StringEntity(reqdata,"utf-8");
        entity.setContentType("application/json");
        httpPost.setEntity(entity);
        httpPost.setHeader("Accept", "application/json");

        //完成签名并执行请求
        CloseableHttpResponse response = httpClient.execute(httpPost);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

    public static void TradeBill(CloseableHttpClient httpClient) throws Exception {

        //请求URL
        URIBuilder uriBuilder = new URIBuilder("https://api.mch.weixin.qq.com/v3/bill/tradebill");
        uriBuilder.setParameter("bill_date", "2020-11-09");
        uriBuilder.setParameter("bill_type", "ALL");

        //完成签名并执行请求
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");
        CloseableHttpResponse response = httpClient.execute(httpGet);

        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }

    public static void DownloadUrl(CloseableHttpClient httpClient,String download_url,String privateKey ,String mchId ,String mchSerialNo) throws Exception{
        PrivateKey merchantPrivateKey = PemUtil.loadPrivateKey(new ByteArrayInputStream(privateKey.getBytes("utf-8")));

        //初始化httpClient
        //该接口无需进行签名验证、通过withValidator((response) -> true)实现
        httpClient =  WechatPayHttpClientBuilder.create().withMerchant(mchId, mchSerialNo, merchantPrivateKey).withValidator((response) -> true).build();

        //请求URL
        //账单文件的下载地址的有效时间为30s
        URIBuilder uriBuilder = new URIBuilder(download_url);
        HttpGet httpGet = new HttpGet(uriBuilder.build());
        httpGet.addHeader("Accept", "application/json");

        //执行请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        try {
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                System.out.println("success,return body = " + EntityUtils.toString(response.getEntity()));
            } else if (statusCode == 204) {
                System.out.println("success");
            } else {
                System.out.println("failed,resp code = " + statusCode+ ",return body = " + EntityUtils.toString(response.getEntity()));
                throw new IOException("request failed");
            }
        } finally {
            response.close();
        }
    }
}
