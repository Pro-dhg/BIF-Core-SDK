import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFTransactionCacheRequest;
import cn.bif.model.request.BIFTransactionGetInfoRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFTransactionCacheResponse;
import cn.bif.model.response.BIFTransactionGetInfoResponse;

/**
 * @USER: dhg
 * @DATE: 2022/12/26 17:12
 * @DESCRIPTION:
 */

public class wallte {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
//    public static final String NODE_URL = "http://192.168.20.211:27002";  //星火链私链地址

    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    public static void main(String[] args) {

        BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();
        request.setHash("9216fd2be9841727e12234098bdf039afd3afba79c18a9c635085fbc280b9936");
        BIFTransactionGetInfoResponse response = sdk.getBIFTransactionService().getTransactionInfo(request);
        if (response.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(response.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response));
        }

        BIFTransactionCacheRequest cacheRequest=new BIFTransactionCacheRequest();
        cacheRequest.setHash("9216fd2be9841727e12234098bdf039afd3afba79c18a9c635085fbc280b9936");
        BIFTransactionCacheResponse response2 = sdk.getBIFTransactionService().getTxCacheData(cacheRequest);
        if (response2.getErrorCode() == 0) {
            System.out.println("txCacheData: "+JsonUtils.toJSONString(response2.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(response2));
        }
    }
}
