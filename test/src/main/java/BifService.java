//import cn.bif.api.BIFSDK;
//import cn.bif.common.Constant;
//import cn.bif.common.JsonUtils;
//import cn.bif.exception.SDKException;
//import cn.bif.exception.SdkError;
//import cn.bif.model.request.BIFAccountGetNonceRequest;
//import cn.bif.model.request.BIFContractInvokeRequest;
//import cn.bif.model.request.BIFTransactionSerializeRequest;
//import cn.bif.model.request.operation.BIFBaseOperation;
//import cn.bif.model.request.operation.BIFContractInvokeOperation;
//import cn.bif.model.response.BIFAccountGetNonceResponse;
//import cn.bif.model.response.BIFBaseResponse;
//import cn.bif.model.response.BIFTransactionSerializeResponse;
//import cn.bif.module.blockchain.BIFTransactionService;
//import lombok.extern.slf4j.Slf4j;
//
//
///**
// * @author rui.wang
// * @ Version: 1.0
// * @ Time: 2022/10/10 18:35
// * @ Description:
// */
//@Slf4j
//public class BifService {
//
//    private String chainUrl;
//
//    public static volatile BIFSDK sdk;
//
//    private void init() {
//        if (sdk == null) {
//            synchronized (BifService.class) {
//                if (sdk == null) {
//                    log.info("XingHuo chain sdk url:{}", chainUrl);
//                    sdk = BIFSDK.getInstance(chainUrl);
//                }
//            }
//        }
//    }
//
//    public BIFBaseResponse validResponse(BIFBaseResponse response) {
//        if (!SdkError.SUCCESS.getCode().equals(response.getErrorCode())) {
//            throw new RuntimeException(response.getErrorDesc());
//        }
//        return response;
//    }
//
//    protected long nextNonce(String senderAddress) {
//        BIFAccountGetNonceRequest bifAccountGetNonceRequest = new BIFAccountGetNonceRequest();
//        bifAccountGetNonceRequest.setAddress(senderAddress);
//        BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(bifAccountGetNonceRequest);
//        if (0 == response.getErrorCode()) {
//            return response.getResult().getNonce();
//        }else {
//            return 1;
//        }
//    }
//
//    protected String bifSerialize2(BIFContractInvokeRequest bifContractInvokeRequest,
//                                  String senderAddress, long nonce, long feeLimit, long gasPrice) {
//        BIFContractInvokeSerializer serializer = new BIFContractInvokeSerializer(
//                sdk.getBIFTransactionService(),
//                senderAddress,
//                nonce,
//                feeLimit,
//                gasPrice
//        );
//        BIFContractInvokeOperation operation = new BIFContractInvokeOperation();
//        operation.setContractAddress(bifContractInvokeRequest.getContractAddress());
//        operation.setBIFAmount(bifContractInvokeRequest.getBIFAmount());
//        operation.setInput(JsonUtils.toJSONString(bifContractInvokeRequest));
//
//        return serializer.serializable(operation);
//    }
//
//    protected <O extends BIFBaseOperation> String bifSerialize(O operation, String senderAddress, long nonce, long feeLimit, long gasPrice) {
//        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
//        serializeRequest.setSourceAddress(senderAddress);
//        serializeRequest.setNonce(nonce + 1);
//        serializeRequest.setFeeLimit(feeLimit);
//        serializeRequest.setGasPrice(gasPrice);
//        serializeRequest.setOperation(operation);
//
//        BIFTransactionSerializeResponse serializeResponse = sdk.getBIFTransactionService().BIFSerializable(serializeRequest);
//        validResponse(serializeResponse);
//        return serializeResponse.getResult().getTransactionBlob();
//    }
//
//
//    interface BifSerializer <O extends BIFBaseOperation>{
//
//        String serializable(O operation);
//    }
//
//    private static abstract class AbstractBIFSerializer<O extends BIFBaseOperation> implements BifSerializer<O> {
//
//        private final BIFTransactionService service;
//
//        private final BIFTransactionSerializeRequest serializeRequest;
//
//        public AbstractBIFSerializer(BIFTransactionService service, String senderAddress, long nonce, long feeLimit, long gasPrice) {
//            this.service = service;
//            serializeRequest = new BIFTransactionSerializeRequest();
//            serializeRequest.setSourceAddress(senderAddress);
//            serializeRequest.setNonce(nonce + 1);
//            serializeRequest.setFeeLimit(feeLimit);
//            serializeRequest.setGasPrice(gasPrice);
//        }
//
//        @Override
//        public String serializable(O operation) {
//            serializeRequest.setOperation(operation);
//            BIFTransactionSerializeResponse serializeResponse = service.BIFSerializable(serializeRequest);
//            if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
//                throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
//            }
//            return serializeResponse.getResult().getTransactionBlob();
//        }
//    }
//
//    private static class BIFContractInvokeSerializer extends AbstractBIFSerializer<BIFContractInvokeOperation>  {
//
//        private BIFContractInvokeOperation operation;
//
//        private BIFContractInvokeSerializer(BIFTransactionService service, String senderAddress, long nonce, long feeLimit, long gasPrice) {
//            super(service, senderAddress, nonce, feeLimit, gasPrice);
//        }
//
//        public BIFContractInvokeSerializer setOperation(BIFContractInvokeOperation operation) {
//            this.operation = operation;
//            return this;
//        }
//    }
//}
