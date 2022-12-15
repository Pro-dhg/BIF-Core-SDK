//import cn.bif.common.Constant;
//import cn.bif.common.JsonUtils;
//import cn.bif.exception.SDKException;
//import cn.bif.model.request.*;
//import cn.bif.model.request.operation.BIFContractInvokeOperation;
//import cn.bif.model.response.BIFAccountGetNonceResponse;
//import cn.bif.model.response.BIFTransactionSerializeResponse;
//import cn.bif.model.response.BIFTransactionSubmitResponse;
//import cn.bif.module.encryption.key.PublicKeyManager;
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yamu.bns.bif.api.ContractApiPlus;
//import com.yamu.bns.bif.config.BifService;
//import com.yamu.bns.bif.param.ContractCreateParam;
//import com.yamu.bns.bif.param.ContractInvokeParam;
//import com.yamu.bns.bif.param.ContractTransactionParam;
//import com.yamu.bns.skeleton.aop.BusinessException;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
///**
// * @USER: dhg
// * @DATE: 2022/12/13 11:14
// * @DESCRIPTION:
// */
//
//@Slf4j
//@Component
//public class ContractApiPlusImpl extends BifService implements ContractApiPlus {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Override
//    public String getInvokeBlob(ContractInvokeParam param) throws JsonProcessingException {
//        // 获取nonce
//        long nextNonce = nextNonce(param.getSenderAddress()) + 1;
//        // 序列化
//        return bifSerialize(
//                createBIFContractInvokeRequest(param), // 核心参数，包含了xxxxx
//                param.getSenderAddress(),
//                nextNonce,
//                defaultFeeLimit(), // 默认30000
//                defaultGasPrice()
//        );
//    }
//
//    @Override
//    public String getCreateBlob(ContractCreateParam param) throws JsonProcessingException {
//        BIFAccountGetNonceRequest bifAccountGetNonceRequest = new BIFAccountGetNonceRequest();
//        bifAccountGetNonceRequest.setAddress(param.getSenderAddress());
//        BIFAccountGetNonceResponse response = sdk.getBIFAccountService().getNonce(bifAccountGetNonceRequest);
//
//        Long nonce=0L;
//        if (0 == response.getErrorCode()) {
//            nonce=response.getResult().getNonce();
//        }else {
//            throw new BusinessException("nonce value is null");
//        }
//
//        BIFContractCreateRequest bifContractCreateRequest = new BIFContractCreateRequest();
//        bifContractCreateRequest.setSenderAddress(param.getSenderAddress());
//        bifContractCreateRequest.setPrivateKey(param.getPrivateKey());
//        bifContractCreateRequest.setInitBalance(param.getInitBalance());
//        bifContractCreateRequest.setPayload(param.getPayload());
//        bifContractCreateRequest.setRemarks(param.getRemarks());
//        bifContractCreateRequest.setType(param.getType());
//        bifContractCreateRequest.setFeeLimit(param.getFeeLimit());
//
//        BIFContractInvokeOperation operation = new BIFContractInvokeOperation();
//        operation.setContractAddress(param.getContractAddress());
//        operation.setBIFAmount(param.getAmount());
//        operation.setInput(String.valueOf(bifContractCreateRequest));
//
//        BIFTransactionSerializeRequest serializeRequest = new BIFTransactionSerializeRequest();
//        serializeRequest.setSourceAddress(param.getSenderAddress());
//        serializeRequest.setNonce(nonce + 1);
//        serializeRequest.setFeeLimit(300000000L);
//        serializeRequest.setGasPrice(0L);
//        serializeRequest.setOperation(operation);
//
//        BIFTransactionSerializeResponse serializeResponse = sdk.getBIFTransactionService().BIFSerializable(serializeRequest);
//        if (!serializeResponse.getErrorCode().equals(Constant.SUCCESS)) {
//            throw new SDKException(serializeResponse.getErrorCode(), serializeResponse.getErrorDesc());
//        }
//        String transactionBlob = serializeResponse.getResult().getTransactionBlob();
//
//        return transactionBlob ;
//    }
//
//    @Override
//    public String execution(ContractTransactionParam param) {
//        if (PublicKeyManager.isPublicKeyValid(param.getSenderPublicKey())){
//            throw new BusinessException("Invalid publicKey");
//        }
//        BIFTransactionSubmitRequest submitRequest = new BIFTransactionSubmitRequest();
//        submitRequest.setSerialization(param.getBlob());
//        submitRequest.setPublicKey(param.getSenderPublicKey());
//        submitRequest.setSignData(param.getSignMessage());
//        BIFTransactionSubmitResponse transactionSubmitResponse = sdk.getBIFTransactionService().BIFSubmit(submitRequest);
//        String transactionHash=transactionSubmitResponse.getResult().getHash();
//
//        return transactionHash;
//    }
//
//
//}
//
