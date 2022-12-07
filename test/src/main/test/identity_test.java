import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;
import cn.bif.model.response.result.BIFContractCallResult;
import org.mortbay.util.ajax.JSON;

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
    public static String cTxHash = "27f3056d22574c1145c1c7698b8d34470b83623706b85740c198ece49efd211c";

    public static final String address = "did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c";
    public static final String privateKey = "priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL";

    public static final String registerContractAddress= "did:bid:efuFKXUfbpBrKZLEaCHebEf1j66qKHdS";
    public static final String securityContractAddress= "did:bid:efWRw14pBx83E3oX3BGHBvGgGmBqFcdH";

    public static void main(String[] args) {
//        initCommissions(hashDetail(cTxHash));
//        getMember(hashDetail(cTxHash));
//        isOkIncommissions(hashDetail(cTxHash));
//        issue(hashDetail(cTxHash));
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
                               +" "
                               +"获取合约地址为："+JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress())
                               +" ");
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
            System.out.println(JsonUtils.toJSONString(result.getQueryRets()));
        } else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }
    /**
     * 智能合约查询
     * @param cAddr
     */
    public static void getMember(String cAddr){

        String callInput = "{\"method\": \"getMember\",\"params\": {\"id\": \"1004\"}}";    //查询input

        BIFContractCallRequest cCallReq = new BIFContractCallRequest();             //查询请求
        cCallReq.setContractAddress(cAddr); // cAddr为 使用交易hash值，获取的合约地址
        cCallReq.setInput(callInput);

        BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq); //查询

        if (cCallRsp.getErrorCode() == 0) {
            BIFContractCallResult result = cCallRsp.getResult();
            System.out.println(JsonUtils.toJSONString(result.getQueryRets()));
        } else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }

    /**
     * 添加别的合约需要用到的信息
     * {"security_commission": {"contractAddress": "did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w"},"register_commission": {"contractAddress": "did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w"}}
     */
    public static void initCommissions(String cAddr){
        String callInput = "{ " +
                " \"method\": \"initCommissions\", " +
                " \"params\": { " +
                "  \"owner\": \"did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6d\", " +
                "  \"security_commission\": { " +
                "   \"contractAddress\": \""+securityContractAddress+"\", " +
                "   \"method\": \"initCommissions\", " +
                "   \"params\": {  " +
                "    \"commission\": { " +
                "     \"members\": [{ " +
                "      \"id\": \"1111\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"2345\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"4312\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"6454\", " +
                "      \"createdAt\": 123 " +
                "     }] " +
                "    } " +
                "   } " +
                "  }, " +
                "  \"register_commission\": { " +
                "   \"contractAddress\": \""+registerContractAddress+"\", " +
                "   \"method\": \"initCommissions\", " +
                "   \"params\": { " +
                "    \"commission\": { " +
                "     \"members\": [{ " +
                "      \"id\": \"1111\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"2341\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"5432\", " +
                "      \"createdAt\": 123 " +
                "     }, { " +
                "      \"id\": \"3452\", " +
                "      \"createdAt\": 123 " +
                "     }] " +
                "    } " +
                "   } " +
                "  } " +
                " } " +
                "}";

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

//        BIFContractCallRequest cCallReq = new BIFContractCallRequest();
//        cCallReq.setContractAddress(cAddr); // cAddr为 使用交易hash值，获取的合约地址
//        cCallReq.setInput(callInput);
//        cCallReq.setSourceAddress(address);
//
//        BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq); //查询
//
//        if (cCallRsp.getErrorCode() == 0) {
//            BIFContractCallResult result = cCallRsp.getResult();
//            System.out.println(JsonUtils.toJSONString(result.getQueryRets()));
//        } else {
//            System.out.println(cCallRsp.getErrorDesc());
//        }

    }

    /**
     * 智能合约获取权限
     * @param cAddr
     */
    public static void isOkIncommissions(String cAddr){
        String callInput = "{\"method\": \"isOkIncommissions\",\"params\":\"0001\"}";    //查询input

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
     * 智能合约issue 发行
     * @param cAddr
     */
    public static void issue(String cAddr){
        String callInput = "{\"method\":\"issue\",\"params\":{\"entity\":{\"subject\":\""+address+"\",\"id\":\"1004\",\"createdAt\":\"\"}}}";    //查询input

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
