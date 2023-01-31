import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/9 10:32
 * @DESCRIPTION: 支付宝
 */

public class Zfb {
    public static void main(String[] args) throws Exception {
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                "2021003173639268",
                "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCF9VZK4Ww4LiaZOwk+WglYlQuj48JTIUf0oNRs7ocRf1PJS5voX6vca8tHhVAbijdVazHktJQYICm2ZdQS0/FE1yBGmMR42axSQF9OGRwjp+xqSCbZT39RGP3kClEa/YFg6bozBbkBFM643sVsZAOB9aj+y/FQo23wN0ozgKz64hMqT/vSljYfpD9b8yVSLHBEUdQkGywElhuNVGla3DwxB0Kg7HDLtssBQmAaTZa2BWKRj/URywYyjESmG9EMbEfI5xnBN0u5UcaI9nl4lFeLcNUDvBFoqtXDAYQSZRjeepJQxKIFe+7ylXGQCJ2aiF7k7XsbcR/l7z4CFxwOT2bBAgMBAAECggEAYheTBuYnDsSMsywNysC3k0wg5/QB+kmmnTWgF9xav69TNVXOZsyX1qKAinDOhjwh5MTIFkc9IqV6pfvO3bKhO92j3s5DIJOUoe5NW6z7dAilD/ahrAOQoVwBnpkHZxI3STYdiPspy+bmsW0/d+2Mu/+drma84GWsPXjfNvOEt3kBO9qT59o8OkBCR+u38+eO0F6blytSsBPpOk5hjQHbXBtmLK8cBMlkbTamuSX4DSZP81S67qUwlLPyzJ220PZ+U3/VHiETpM048vc0cwIC6NUy2ZzLqn5MtTuFkzYTzKWTKMnsOzRIQjBDJRpvlgGj152UJWJtcUJeUryK6Mh6gQKBgQDB8RWx1J3rGNvdFFowHW9NieJpynJk110sUO51EfPMfxuv5H8w42KOTM0knTeKMD9H2MTOLpuMXMhwDiNgwKZk7ve5MCrs5VGVr/7AFwfjKv7NDnEsFnYwhS8RtOrPtiF0mX6Cp26ODtlLb33E0sv7XIcVqeq8C6i1nkCf2zgCuQKBgQCw0qhyVC83UYbBuPyIAFM+O6jufwyC9oUSSDX3Yc03z8HNkFjC+bN+QFe4+idhJGhZ2gCPGARjblEL6KgSrEFkmzuwQLMrRtYHWDQ7BIC4IV7Rb5ISYS9Y6Hg/ZV0kxdh4va4eD01tOyG4ZqZebVI5NprNmUN1rpkcuW4BsTugSQKBgHTqNT3jAuA7OC/qmQPweqyvtsI3vQZ1nkGc+GOy0towtiwu/04lhcBrJDAyZszzkPJrBxmUl0jiliKXI1SsTqMCPGxzUS1GgQxP8K80MxkLTlenoSoG0HEnmNx/yB43mauE7PMupEMXSH9lUthYi4eYnAa/TDbYgHgcUrELopRhAoGAf2ZYTX+QQWgxEpTpBkhNsdIkZioN5rIdsM1sbJ//z02WUjjFaz2j8LwFOFasVtZ3hP2IV5EzYDyUkmL/7exEdWtPNy92qCCyhrorb7ZP09rfSUD82tdA2Ost8GdlE+CklmH7+NaHr8LiCc2T4wcbLfPE14usyjaRRNvpgbijwFkCgYEAoQjdQytCh6H0FlQaoRnC4/EB5soeP+gjtUQb36B9RiVyTSD1WCEZpLVNCZ9e7rMUr0ttb58yAF3q+bFmISQJh3dTYnWmK95M5b1rEaqScqotslWEK2CTcjlJevZguoev3avhq35MEZ4Xyy0WofUd61R6l1xA2oir13u/fyIwflA=",
                "json",
                "UTF-8",
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApkUoWa95rt/wFtNMr95Gc9h+t8+XY86XSaeLXl9+A/3XDr7FapJFLtGlStJlp/7M7R1bErF2t5rd2QO6WvmkqDzgA9rm9EKT3V2WYjhm09qZLWHEcFi5Csa3BFpgtIgtnmurMnxLhdIKfMbc5s72GFDCKweUtF1/w2TTy85f5lWcIIddUg++K+Bl7gU3rSRlwYWWgJayrpaID+6QB8hhGg/jnDdwcudM1n3JyL7nFJdvi7eV+Wmn0pIUTSLH5OXVXw026awYjT6/mtOLqs68HwZvv8OAnKnI+eksSyp3xdMsP2rP9okmzFNaSQw+jrDf7CziVV0UdGm1ACtr6N25kwIDAQAB",
                "RSA2");
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl("");
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", "20210817010101003");
        bizContent.put("total_amount", 0.01);
        bizContent.put("subject", "测试商品");

//// 商品明细信息，按需传入
//JSONArray goodsDetail = new JSONArray();
//JSONObject goods1 = new JSONObject();
//goods1.put("goods_id", "goodsNo1");
//goods1.put("goods_name", "子商品1");
//goods1.put("quantity", 1);
//goods1.put("price", 0.01);
//goodsDetail.add(goods1);
//bizContent.put("goods_detail", goodsDetail);

//// 扩展信息，按需传入
//JSONObject extendParams = new JSONObject();
//extendParams.put("sys_service_provider_id", "2088511833207846");
//bizContent.put("extend_params", extendParams);

//// 结算信息，按需传入
//JSONObject settleInfo = new JSONObject();
//JSONArray settleDetailInfos = new JSONArray();
//JSONObject settleDetail = new JSONObject();
//settleDetail.put("trans_in_type", "defaultSettle");
//settleDetail.put("amount", 0.01);
//settleDetailInfos.add(settleDetail);
//settleInfo.put("settle_detail_infos", settleDetailInfos);
//bizContent.put("settle_info", settleInfo);

//// 二级商户信息，按需传入
//JSONObject subMerchant = new JSONObject();
//subMerchant.put("merchant_id", "2088000603999128");
//bizContent.put("sub_merchant", subMerchant);

//// 业务参数信息，按需传入
//JSONObject businessParams = new JSONObject();
//businessParams.put("busi_params_key", "busiParamsValue");
//bizContent.put("business_params", businessParams);

//// 营销信息，按需传入
//JSONObject promoParams = new JSONObject();
//promoParams.put("promo_params_key", "promoParamsValue");
//bizContent.put("promo_params", promoParams);

        request.setBizContent(bizContent.toString());
        AlipayTradePrecreateResponse response = alipayClient.execute(request);
        if(response.isSuccess()){
            System.out.println("调用成功");
        } else {
            System.out.println("调用失败");
        }
    }

    public  static void   doPost (/*HttpServletRequest httpRequest, HttpServletResponse httpResponse*/)   throws ServletException, IOException {
        String APP_ID = "2021003173639268",
                APP_PRIVATE_KEY= "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCF9VZK4Ww4LiaZOwk+WglYlQuj48JTIUf0oNRs7ocRf1PJS5voX6vca8tHhVAbijdVazHktJQYICm2ZdQS0/FE1yBGmMR42axSQF9OGRwjp+xqSCbZT39RGP3kClEa/YFg6bozBbkBFM643sVsZAOB9aj+y/FQo23wN0ozgKz64hMqT/vSljYfpD9b8yVSLHBEUdQkGywElhuNVGla3DwxB0Kg7HDLtssBQmAaTZa2BWKRj/URywYyjESmG9EMbEfI5xnBN0u5UcaI9nl4lFeLcNUDvBFoqtXDAYQSZRjeepJQxKIFe+7ylXGQCJ2aiF7k7XsbcR/l7z4CFxwOT2bBAgMBAAECggEAYheTBuYnDsSMsywNysC3k0wg5/QB+kmmnTWgF9xav69TNVXOZsyX1qKAinDOhjwh5MTIFkc9IqV6pfvO3bKhO92j3s5DIJOUoe5NW6z7dAilD/ahrAOQoVwBnpkHZxI3STYdiPspy+bmsW0/d+2Mu/+drma84GWsPXjfNvOEt3kBO9qT59o8OkBCR+u38+eO0F6blytSsBPpOk5hjQHbXBtmLK8cBMlkbTamuSX4DSZP81S67qUwlLPyzJ220PZ+U3/VHiETpM048vc0cwIC6NUy2ZzLqn5MtTuFkzYTzKWTKMnsOzRIQjBDJRpvlgGj152UJWJtcUJeUryK6Mh6gQKBgQDB8RWx1J3rGNvdFFowHW9NieJpynJk110sUO51EfPMfxuv5H8w42KOTM0knTeKMD9H2MTOLpuMXMhwDiNgwKZk7ve5MCrs5VGVr/7AFwfjKv7NDnEsFnYwhS8RtOrPtiF0mX6Cp26ODtlLb33E0sv7XIcVqeq8C6i1nkCf2zgCuQKBgQCw0qhyVC83UYbBuPyIAFM+O6jufwyC9oUSSDX3Yc03z8HNkFjC+bN+QFe4+idhJGhZ2gCPGARjblEL6KgSrEFkmzuwQLMrRtYHWDQ7BIC4IV7Rb5ISYS9Y6Hg/ZV0kxdh4va4eD01tOyG4ZqZebVI5NprNmUN1rpkcuW4BsTugSQKBgHTqNT3jAuA7OC/qmQPweqyvtsI3vQZ1nkGc+GOy0towtiwu/04lhcBrJDAyZszzkPJrBxmUl0jiliKXI1SsTqMCPGxzUS1GgQxP8K80MxkLTlenoSoG0HEnmNx/yB43mauE7PMupEMXSH9lUthYi4eYnAa/TDbYgHgcUrELopRhAoGAf2ZYTX+QQWgxEpTpBkhNsdIkZioN5rIdsM1sbJ//z02WUjjFaz2j8LwFOFasVtZ3hP2IV5EzYDyUkmL/7exEdWtPNy92qCCyhrorb7ZP09rfSUD82tdA2Ost8GdlE+CklmH7+NaHr8LiCc2T4wcbLfPE14usyjaRRNvpgbijwFkCgYEAoQjdQytCh6H0FlQaoRnC4/EB5soeP+gjtUQb36B9RiVyTSD1WCEZpLVNCZ9e7rMUr0ttb58yAF3q+bFmISQJh3dTYnWmK95M5b1rEaqScqotslWEK2CTcjlJevZguoev3avhq35MEZ4Xyy0WofUd61R6l1xA2oir13u/fyIwflA=",
                FORMAT= "json",
                CHARSET= "UTF-8",
                ALIPAY_PUBLIC_KEY= "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApkUoWa95rt/wFtNMr95Gc9h+t8+XY86XSaeLXl9+A/3XDr7FapJFLtGlStJlp/7M7R1bErF2t5rd2QO6WvmkqDzgA9rm9EKT3V2WYjhm09qZLWHEcFi5Csa3BFpgtIgtnmurMnxLhdIKfMbc5s72GFDCKweUtF1/w2TTy85f5lWcIIddUg++K+Bl7gU3rSRlwYWWgJayrpaID+6QB8hhGg/jnDdwcudM1n3JyL7nFJdvi7eV+Wmn0pIUTSLH5OXVXw026awYjT6/mtOLqs68HwZvv8OAnKnI+eksSyp3xdMsP2rP9okmzFNaSQw+jrDf7CziVV0UdGm1ACtr6N25kwIDAQAB",
                SIGN_TYPE = "RSA2";
        AlipayClient alipayClient =  new DefaultAlipayClient(
                "https://openapi.alipay.com/gateway.do" ,
                APP_ID,
                APP_PRIVATE_KEY,
                FORMAT,
                CHARSET,
                ALIPAY_PUBLIC_KEY,
                SIGN_TYPE);  //获得初始化的AlipayClient
        AlipayTradePagePayRequest alipayRequest =  new AlipayTradePagePayRequest(); //创建API对应的request
        alipayRequest.setReturnUrl( "http://domain.com/CallBack/return_url.jsp" );
        alipayRequest.setNotifyUrl( "http://domain.com/CallBack/notify_url.jsp" ); //在公共参数中设置回跳和通知地址
        alipayRequest.setBizContent( "{"  +
                "    \"out_trade_no\":\"20150320010101001\","  +
                "    \"product_code\":\"FAST_INSTANT_TRADE_PAY\","  +
                "    \"total_amount\":88.88,"  +
                "    \"subject\":\"Iphone6 16G\","  +
                "    \"body\":\"Iphone6 16G\","  +
                "    \"passback_params\":\"merchantBizType%3d3C%26merchantBizNo%3d2016010101111\","  +
                "    \"extend_params\":{"  +
                "    \"sys_service_provider_id\":\"2088511833207846\""  +
                "    }" +
                "  }" ); //填充业务参数
        String form= "" ;
        try  {
            form = alipayClient.pageExecute(alipayRequest).getBody();  //调用SDK生成表单
        }  catch  (AlipayApiException e) {
            e.printStackTrace();
        }
        System.out.println(form);
//        httpResponse.setContentType( "text/html;charset="  + CHARSET);
//        httpResponse.getWriter().write(form); //直接将完整的表单html输出到页面
//        httpResponse.getWriter().flush();
//        httpResponse.getWriter().close();

//        System.out.println(httpResponse);
    }
}
