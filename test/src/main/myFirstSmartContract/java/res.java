import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFTransactionCacheRequest;
import cn.bif.model.request.BIFTransactionGetInfoRequest;
import cn.bif.model.response.BIFTransactionCacheResponse;
import cn.bif.model.response.BIFTransactionGetInfoResponse;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/31 11:43
 * @DESCRIPTION:
 */

public class res {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    public static void main(String[] args) {

            String TransactionHash = "ff347e8419cf314d27e7476aa2ab2e243022295e5f9509814f22a2f061864436";
            BIFTransactionCacheRequest cacheRequest = new BIFTransactionCacheRequest(); //交易池
            BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();  //交易

            cacheRequest.setHash(TransactionHash);
            request.setHash(TransactionHash);


            BIFTransactionCacheResponse response = sdk.getBIFTransactionService().getTxCacheData(cacheRequest);
            System.out.println(JsonUtils.toJSONString(response));

//        if (response.getErrorCode() == 0) {
            BIFTransactionGetInfoResponse transactionInfo = sdk.getBIFTransactionService().getTransactionInfo(request);
            System.out.println(JsonUtils.toJSONString(transactionInfo));
//        }



    }
}
