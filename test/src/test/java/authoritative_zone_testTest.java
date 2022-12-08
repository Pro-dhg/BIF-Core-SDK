import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.result.BIFContractCallResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class authoritative_zone_testTest {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "cf0ee530e7d9d2dcd2c794ce3ef3b5d16471b00439dad8d60478dd46a540605f";

    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";
    private static final BIFContractCallRequest cCallReq = new BIFContractCallRequest(); //可打印
    private static final BIFContractInvokeRequest cCallInvokeReq = new BIFContractInvokeRequest(); //不可打印
    private static String cAddr = "";

    @Before
    public void hashDetail() {
        BIFContractGetAddressRequest cAddrReq = new BIFContractGetAddressRequest();
        //查询交易hash 详细信息
        cAddrReq.setHash(cTxHash);

        BIFContractGetAddressResponse cAddrRsp = sdk.getBIFContractService().getContractAddress(cAddrReq);
        if (cAddrRsp.getErrorCode() == 0) {
            System.out.println("交易hash值："+cTxHash
                    +" "
                    +"获取合约地址为："+ JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress())
                    +" ");
            cAddr = JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress()) ;
        } else {
            System.out.println(cAddrRsp.getErrorDesc());
        }

    }

    @Test
    public void getIdentifiers() {
        String callInput = "{\"method\": \"getIdentifiers\"}";    //查询input

        cCallReq.setContractAddress(cAddr); // cAddr为 使用交易hash值，获取的合约地址
        cCallReq.setInput(callInput);

        BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq); //查询

        if (cCallRsp.getErrorCode() == 0) {
            BIFContractCallResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        } else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }
}