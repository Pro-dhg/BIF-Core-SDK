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
public class identity_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "70dc1b9d35ec481dbe035a4067344f4fa75f4d2c9b7f29c4fb2a7cc96ad11c0d";

    public static final String address = "did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c";
    public static final String privateKey = "priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL";

    public static void main(String[] args) {
        get(hashDetail(cTxHash));
//        initCommissions(hashDetail(cTxHash));
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

        String callInput = "{\"method\": \"get\",\"params\": {\"id\": \"123123123123\"}}";    //查询input

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
     * 添加别的合约需要用到的信息
     * {"security_commission": {"contractAddress": "did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w"},"register_commission": {"contractAddress": "did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w"}}
     */
    public static void initCommissions(String cAddr){
        String callInput = "{\"method\": \"initCommissions\",\"params\":{\"owner\": \"did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c\",\"security_commission\": {\"contractAddress\": \"did:bid:efH9jQta3JCwe3EYreZFzN7ZtwMRE3qH\"},\"register_commission\": {\"contractAddress\": \"did:bid:efH9jQta3JCwe3EYreZFzN7ZtwMRE3qH\"}}}";    //查询input

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

//        BIFContractCallRequest cCallReq = new BIFContractCallRequest();             //查询请求
//
//
//        cCallReq.setSourceAddress(address);
//
//        cCallReq.setContractAddress(cAddr); // cAddr为 使用交易hash值，获取的合约地址
//        cCallReq.setInput(callInput);
//
//        BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq); //查询
//
//        if (cCallRsp.getErrorCode() == 0) {
//            BIFContractCallResult result = cCallRsp.getResult();
//            System.out.println(JsonUtils.toJSONString(result));
//        } else {
//            System.out.println(cCallRsp.getErrorDesc());
//        }

    }


}
