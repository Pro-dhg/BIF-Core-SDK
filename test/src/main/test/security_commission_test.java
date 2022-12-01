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

/**
 * 根据交易hash 获取合约地址
 *
 * 合约成功部署并且获取到合约地址后, 就可以通过SDK发送交易调用合约接口, 我们存储一个Key-Value对到合约里
 *
 * input = {"method": "get", "params": {"address": "did:bid:efi9eJga7RS9HHwTmw9c3nHEniqB6FGq"}}
 */
public class security_commission_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "ba725d163da24bd67a1a58ff1e447436f9b35aefefa301e095703d8a426974b5";

    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";

    public static void main(String[] args) {
        get(hashDetail(cTxHash));
//        addMember(hashDetail(cTxHash));
    }

    /**
     * 使用部署智能合约交易hash值，来获取合约地址
     * @param cTxHash
     * ps: 只能处理智能合约的hash值
     */
    public static String hashDetail(String cTxHash){
        //查询交易hash 详细信息
        BIFContractGetAddressRequest cAddrReq = new BIFContractGetAddressRequest();
        cAddrReq.setHash(cTxHash);

        BIFContractGetAddressResponse cAddrRsp = sdk.getBIFContractService().getContractAddress(cAddrReq);
        if (cAddrRsp.getErrorCode() == 0) {
            System.out.println("交易hash值："+cTxHash
                               +"\n"
                               +"获取合约地址为："+JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress())
                               +"\n");
            return JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress()) ;
        } else {
            System.out.println(cAddrRsp.getErrorDesc());
        }
        return "false";
    }


    /**
     * 智能合约查询
     * @param cAddr
     */
    public static void get(String cAddr){
//        String callInput = "{\"method\": \"get\",\"params\":{\"id\":\"security_commission\"}}";    //查询input
        String callInput = "{\"method\": \"get\"}";    //查询input

//        BIFContractInvokeRequest cCallReq = new BIFContractInvokeRequest();
//        cCallReq.setBIFAmount(1l);
//        cCallReq.setSenderAddress(address);
//        cCallReq.setPrivateKey(privateKey);
//        cCallReq.setInput(callInput);
//        cCallReq.setContractAddress(cAddr);
//        BIFContractInvokeResponse cCallRsp = sdk.getBIFContractService().contractInvoke(cCallReq);
//        if (cCallRsp.getErrorCode() == 0 ){
//            BIFContractInvokeResult result = cCallRsp.getResult();
//            System.out.println(JsonUtils.toJSONString(result));
//        }else {
//            System.out.println(cCallRsp.getErrorDesc());
//        }

        BIFContractCallRequest cCallReq = new BIFContractCallRequest();             //查询请求


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

    /**
     * 智能合约查询
     * @param cAddr
     */
    public static void addMember(String cAddr){
        String callInput = "{\"method\":\"addMember\",\"params\":{\"entity\":{\"id\":\"8547\",\"createdAt\":\"\"}}}";    //查询input

        BIFContractInvokeRequest cCallReq = new BIFContractInvokeRequest();
        cCallReq.setBIFAmount(1l);
        cCallReq.setSenderAddress(address);
        cCallReq.setPrivateKey(privateKey);
        cCallReq.setInput(callInput);
        cCallReq.setContractAddress(cAddr);
        BIFContractInvokeResponse cCallRsp = sdk.getBIFContractService().contractInvoke(cCallReq);
        if (cCallRsp.getErrorCode() == 0 ){
            BIFContractInvokeResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result));
        }else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }


}
