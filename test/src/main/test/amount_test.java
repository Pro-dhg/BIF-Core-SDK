import cn.bif.api.BIFSDK;
import cn.bif.common.Constant;
import cn.bif.common.JsonUtils;
import cn.bif.exception.SDKException;
import cn.bif.model.request.*;
import cn.bif.model.request.operation.BIFContractInvokeOperation;
import cn.bif.model.response.*;
import cn.bif.module.encryption.key.PrivateKeyManager;
import cn.bif.utils.hex.HexFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @USER: dhg
 * @DATE: 2022/12/13 13:34
 * @DESCRIPTION:
 */

public class amount_test {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    /**
     * 获取账号nonce
     * @param param
     * @return
     */
    public static Long nonce(ContractInvokeParam param){
        BIFAccountGetNonceRequest request = new BIFAccountGetNonceRequest();
        request.setAddress(param.getSenderAddress());
        BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(request);

        // 调用getNonce接口
        Long nonce=0L;
        if (0 == response.getErrorCode()) {
            nonce=response.getResult().getNonce();
            System.out.println("Account nonce:" + response.getResult().getNonce());
        }else {
            System.out.println(JsonUtils.toJSONString(response));
        }

        return nonce;
    }

    public static String invoke(ContractInvokeParam param) throws JsonProcessingException {
        Long nonce = nonce(param);

        BIFContractInvokeRequest request = new BIFContractInvokeRequest();
        request.setSenderAddress(param.getSenderAddress());
        request.setPrivateKey(param.getPrivateKey());
        request.setContractAddress(param.getContractAddress());
        request.setBIFAmount(param.getAmount());
        request.setRemarks(param.getRemarks());
        request.setInput(new ObjectMapper().writeValueAsString(param.getInput()));

        //构建操作对象
        BIFContractInvokeOperation operation = new BIFContractInvokeOperation(); //合约调用对象
        operation.setContractAddress(param.getContractAddress()); //合约地址
        operation.setBIFAmount(param.getAmount()); //转账金额
        operation.setInput(String.valueOf(request)); //待触发的合约的main()入参

        //序列化对象
        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest(); // 初始化请求参数
        serializeRequest.setSourceAddress(param.getSenderAddress());
        serializeRequest.setNonce(nonce + 1);//1.1获取
        serializeRequest.setFeeLimit(300000000L);
        serializeRequest.setGasPrice(0L);
        serializeRequest.setOperation(operation);

        BIFTransactionSerializeResponse serializeResponse = sdk.getBIFTransactionService().BIFSerializable(serializeRequest); // 调用buildBlob接口
        if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
            throw new SDKException(serializeResponse.getErrorCode(),
                    serializeResponse.getErrorDesc());
        }
        String transactionBlob = serializeResponse.getResult().getTransactionBlob(); //获取blob

        return transactionBlob ;
    }
    public static void main(String[] args) throws JsonProcessingException {
//        ContractInvokeParam param = new ContractInvokeParam();
//        param.setAmount(0L);
//        param.setInput("{\"method\": \"get\",\"params\": {\"id\": \"123123123123\"}}");
//        param.setRemarks("测试");
//        param.setContractAddress("did:bid:efuFKXUfbpBrKZLEaCHebEf1j66qKHdS");
//        param.setSenderAddress("did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c");
//        param.setPrivateKey("priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL");
//
//        String transactionBlob = invoke(param);
//        System.out.println(transactionBlob);


        String transactionBlob = "0A286469643A6269643A65664A6D707A50764737366B7444796B4D747A4B564D4145466F72426977366310EB022266080752620A286469643A6269643A6566346166555A476844514B41646B46754A393775394274756B6D67487248381A36636E2E6269662E6D6F64656C2E726571756573742E424946436F6E7472616374496E766F6B65526571756573744032383061356136303080C6868F01";
        String signBytes = "f0c4fd6bb877f94cf8ee254aaa9b65ca5ca4f8aabfcc63169b9c270f3d2706f86bc44440b055e1a7ba8bd4fcf77b89c81697062bcaed61aa5cc92a6d2f860705";

        String publicKey = PrivateKeyManager.getEncPublicKey("priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL");
        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
        submitRequest.setSerialization(transactionBlob);
        submitRequest.setPublicKey(publicKey);
        submitRequest.setSignData(signBytes); //这个是前端调用钱包后，获取的签名
// 调用bifSubmit接口
        BIFTransactionSubmitResponse transactionSubmitResponse = sdk.getBIFTransactionService().BIFSubmit(submitRequest);
//交易hash
        String transactionHash=transactionSubmitResponse.getResult().getHash();
        System.out.println(transactionHash);
        System.out.println(JsonUtils.toJSONString(transactionSubmitResponse));

        //查询交易池,交易池中没有当前交易，才可以查询交易
        BIFTransactionCacheRequest cacheRequest=new BIFTransactionCacheRequest();
        cacheRequest.setHash(transactionHash);
        BIFTransactionCacheResponse response = sdk.getBIFTransactionService().getTxCacheData(cacheRequest);

        //查询交易  交易池中不存在时，查询交易上链结果
        BIFTransactionGetInfoRequest request = new BIFTransactionGetInfoRequest();
        request.setHash(transactionHash);
        BIFTransactionGetInfoResponse response2 = sdk.getBIFTransactionService().getTransactionInfo(request);

        System.out.println(JsonUtils.toJSONString(response)+"  <--交易池  交易-->  "+JsonUtils.toJSONString(response2));
    }
}
