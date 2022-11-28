import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCallRequest;
import cn.bif.model.request.BIFContractGetAddressRequest;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFContractCallResponse;
import cn.bif.model.response.BIFContractGetAddressResponse;
import cn.bif.model.response.BIFContractInvokeResponse;

/**
 * 根据交易hash 获取合约地址
 *
 * 合约成功部署并且获取到合约地址后, 就可以通过SDK发送交易调用合约接口, 我们存储一个Key-Value对到合约里:
 */
public class znhy01_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);
    public static String cTxHash = "3c61aaff1bc3170c249104587d488f31bdc4f1128c2e9d8373a9d42e395818db";

    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";

    public static void main(String[] args) {
        hashDetail(cTxHash);
//        test01("did:bid:efsLgSysvVR1iUwARB2bRVYhwiJP7xT3");
        test02("did:bid:ef8TqstyTi5uggUX15V1Sj9ntRz6bK2w");
        test02("did:bid:efsLgSysvVR1iUwARB2bRVYhwiJP7xT3");

    }

    /**
     * 使用部署智能合约交易hash值，来获取合约地址
     * @param cTxHash
     * ps: 只能处理智能合约的hash值
     */
    public static void hashDetail(String cTxHash){
        //查询交易hash 详细信息
        BIFContractGetAddressRequest cAddrReq = new BIFContractGetAddressRequest();
        cAddrReq.setHash(cTxHash);

        BIFContractGetAddressResponse cAddrRsp = sdk.getBIFContractService().getContractAddress(cAddrReq);
        if (cAddrRsp.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(cAddrRsp));
//            String cAddr = JsonUtils.toJSONString(cAddrRsp.getResult().getContractAddressInfos().get(0).getContractAddress()); //根据交易hash 获取合约地址
//            test02(cAddr) ;
        } else {
            System.out.println(cAddrRsp.getErrorDesc());
        }
    }

    /**
     * 智能合约调用
     * @param cAddr
     */
    public static void test01(String cAddr){
        //转义后input
        String input = "{\"id\":\"test\", \"data\": \"duangetest\"}";

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

    /**
     * 智能合约查询
     * @param cAddr
     */
    public static void test02(String cAddr){
        BIFContractCallRequest cCallReq = new BIFContractCallRequest();             //查询请求

        String callInput = "{\"id\":\"test\"}";                                     //查询input

        cCallReq.setContractAddress(cAddr); // cAddr为 上述生成的 did:bid:efexVGPgx8Brxmv68TnTic9TU8kAA9Hd
        cCallReq.setInput(callInput);

        BIFContractCallResponse cCallRsp = sdk.getBIFContractService().contractQuery(cCallReq); //查询

        if (cCallRsp.getErrorCode() == 0) {
//            System.out.println(JsonUtils.toJSONString(cCallRsp.getResult()));
            System.out.println(JsonUtils.toJSONString(cCallRsp));
        } else {
            System.out.println(cCallRsp.getErrorDesc());
        }
    }


}
