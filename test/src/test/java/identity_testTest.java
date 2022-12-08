import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;
import cn.bif.model.response.result.BIFContractCallResult;
import org.junit.Before;
import org.junit.Test;

public class identity_testTest {

    private static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    private static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    private static String cTxHash = "27f3056d22574c1145c1c7698b8d34470b83623706b85740c198ece49efd211c"; //合约hash

    private static final String address = "did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c";
    private static final String privateKey = "priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL";

    private static final String registerContractAddress= "did:bid:efuFKXUfbpBrKZLEaCHebEf1j66qKHdS";
    private static final String securityContractAddress= "did:bid:efWRw14pBx83E3oX3BGHBvGgGmBqFcdH";
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

    @Test
    public void getMember() {
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

    @Test
    public void initCommissions() {
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
    }

    @Test
    public void isOkIncommissions() {
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

    @Test
    public void issue() {
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