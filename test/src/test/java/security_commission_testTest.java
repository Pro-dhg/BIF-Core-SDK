import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;
import cn.bif.model.response.result.BIFContractCallResult;
import cn.bif.model.response.result.BIFContractInvokeResult;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class security_commission_testTest {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "8e770e2d12b877fbc22d52aa643b6ccf97fb7da7d77ca8ea542c3b0f7b8cc5ae";

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
    public void get() {
        String callInput = "{\"method\": \"get\"}";    //查询input

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

    @Test
    public void getMember() {
        String callInput = "{\"method\":\"getMember\",\"params\":{\"id\":\"1004\"}}";    //查询input

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

    @Test
    public void addMember() {
        String callInput = "{\"method\":\"addMember\",\"params\":{\"entity\":{\"id\":\"1004\",\"createdAt\":\"\"}}}";    //查询input

        cCallInvokeReq.setBIFAmount(1l);
        cCallInvokeReq.setSenderAddress(address);
        cCallInvokeReq.setPrivateKey(privateKey);
        cCallInvokeReq.setInput(callInput);
        cCallInvokeReq.setContractAddress(cAddr);
        BIFContractInvokeResponse cCallRsp = sdk.getBIFContractService().contractInvoke(cCallInvokeReq);
        if (cCallRsp.getErrorCode() == 0 ){
            BIFContractInvokeResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        }else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }

    @Test
    public void expelMember() {
        String callInput = "{\"method\":\"expelMember\",\"params\":{\"id\":\"1004\"}}";    //查询input

        cCallInvokeReq.setBIFAmount(1l);
        cCallInvokeReq.setSenderAddress(address);
        cCallInvokeReq.setPrivateKey(privateKey);
        cCallInvokeReq.setInput(callInput);
        cCallInvokeReq.setContractAddress(cAddr);
        BIFContractInvokeResponse cCallRsp = sdk.getBIFContractService().contractInvoke(cCallInvokeReq);
        if (cCallRsp.getErrorCode() == 0 ){
            BIFContractInvokeResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        }else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }

    @Test
    public void updateMemberInfo() {
        String callInput = "{\"method\":\"updateMemberInfo\",\"params\":{\"entity\":{\"id\":\"1004\",\"createdAt\":\"\",\"emailAddress\":\"123@yamu.com\",\"phoneNumber\":\"1001011\"}}}";    //查询input

        cCallInvokeReq.setBIFAmount(1l);
        cCallInvokeReq.setSenderAddress(address);
        cCallInvokeReq.setPrivateKey(privateKey);
        cCallInvokeReq.setInput(callInput);
        cCallInvokeReq.setContractAddress(cAddr);
        BIFContractInvokeResponse cCallRsp = sdk.getBIFContractService().contractInvoke(cCallInvokeReq);
        if (cCallRsp.getErrorCode() == 0 ){
            BIFContractInvokeResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        }else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }

    @Test
    public void initCommissions() {
        String callInput = "{\"method\":\"initCommissions\",\"params\":{\"contractAddress\":\"did:bid:efBZK3SSPrukRpaMkUtZD1gDn7QMubjq\",\"owner\":\"did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh\",\"commission\":{\"members\":[{\"id\":\"2001\",\"createdAt\":123},{\"id\":\"0002\",\"createdAt\":123},{\"id\":\"0003\",\"createdAt\":123},{\"id\":\"0004\",\"createdAt\":123}]}}}";    //查询input

        cCallInvokeReq.setContractAddress(cAddr);
        cCallInvokeReq.setSenderAddress(address);
        cCallInvokeReq.setBIFAmount(1l);
        cCallInvokeReq.setPrivateKey(privateKey);
        cCallInvokeReq.setInput(callInput);
        BIFContractInvokeResponse res = sdk.getBIFContractService().contractInvoke(cCallInvokeReq);
        if (res.getErrorCode()==0){
            System.out.println(JsonUtils.toJSONString(res.getResult()));
        }else {
            System.out.println(res.getErrorDesc());
        }
    }
}