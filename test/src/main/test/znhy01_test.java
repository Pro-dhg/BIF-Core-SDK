import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;

/**
 * 根据交易hash 获取合约地址
 */
public class znhy01_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "07723a0d3bde2ee166a7dd158ef79003233ea6451b73b2b6f229c69de812eb7e";

    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";

    public static void main(String[] args) {
        BIFContractGetAddressRequest cAddrReq = new BIFContractGetAddressRequest();
        cAddrReq.setHash(cTxHash);

        BIFContractGetAddressResponse cAddrRsp = sdk.getBIFContractService().getContractAddress(cAddrReq);
        if (cAddrRsp.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(cAddrRsp.getResult()));
            test01("did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w") ;
        } else {
            System.out.println(cAddrRsp.getErrorDesc());
        }

    }

    public static void test01(String cAddr){
        //转义后input
        String input = "{\"id\":\"test\", \"data\": \"test\"}";

        BIFContractInvokeRequest cIvkReq = new BIFContractInvokeRequest();

        //调用者地址和私钥
        cIvkReq.setSenderAddress(address);
        cIvkReq.setPrivateKey(privateKey);

       //合约地址
        cIvkReq.setContractAddress(cAddr); // cAddr为 上述生成的 did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w

        //调用交易XHT金额
        cIvkReq.setBIFAmount(0L);

        //标记
        cIvkReq.setRemarks("contract invoke");

        //调用input
        cIvkReq.setInput(input);

        BIFContractInvokeResponse cIvkRsp = sdk.getBIFContractService().contractInvoke(cIvkReq);
        if (cIvkRsp.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(cIvkRsp.getResult()));
        } else {
            System.out.println(cIvkRsp.getErrorDesc());
        }
    }


}
