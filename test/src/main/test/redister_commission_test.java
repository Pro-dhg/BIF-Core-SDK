import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;
import cn.bif.model.response.result.BIFContractCallResult;

/**
 * 根据交易hash 获取合约地址
 *
 * 合约成功部署并且获取到合约地址后, 就可以通过SDK发送交易调用合约接口, 我们存储一个Key-Value对到合约里
 *
 * input = {"method": "get", "params": {"address": "did:bid:efi9eJga7RS9HHwTmw9c3nHEniqB6FGq"}}
 */
public class redister_commission_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "3a0f001e1b8cd20f22ad907be6be5fb75cc646fdb377f911a3952bc0e5c8c5ac";

    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";

    public static void main(String[] args) {
//        initCommissions(hashDetail(cTxHash));
        get(hashDetail(cTxHash));
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
        BIFContractCallRequest cCallReq = new BIFContractCallRequest();             //查询请求

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

    /**
     * 测试联盟委员会成员初始化
     * @param cAddr
     */
    public static void initCommissions(String cAddr){
        String callInput = "{\"method\":\"initCommissions\",\"params\":{\"contractAddress\":\"did:bid:efBZK3SSPrukRpaMkUtZD1gDn7QMubjq\",\"owner\":\"did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh\",\"commission\":{\"members\":[{\"id\":\"2001\",\"createdAt\":123},{\"id\":\"0002\",\"createdAt\":123},{\"id\":\"0003\",\"createdAt\":123},{\"id\":\"0004\",\"createdAt\":123}]}}}";    //查询input

        BIFContractInvokeRequest cCallReq = new BIFContractInvokeRequest();
        cCallReq.setContractAddress(cAddr);
        cCallReq.setSenderAddress(address);
        cCallReq.setBIFAmount(1l);
        cCallReq.setPrivateKey(privateKey);
        cCallReq.setInput(callInput);
        BIFContractInvokeResponse res = sdk.getBIFContractService().contractInvoke(cCallReq);
        if (res.getErrorCode()==0){
            System.out.println(JsonUtils.toJSONString(res.getResult()));
        }else {
            System.out.println(res.getErrorDesc());
        }
    }




}
