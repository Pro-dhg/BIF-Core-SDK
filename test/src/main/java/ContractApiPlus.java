import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractInvokeRequest;
import cn.bif.model.response.BIFBaseResponse;
import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * @USER: dhg
 * @DATE: 2022/12/13 11:14
 * @DESCRIPTION:
 */
public interface ContractApiPlus {

    /**
     * 调用合约之前获取blob
     * @param param
     * @return
     * @throws JsonProcessingException
     */
    String getInvokeBlob(ContractInvokeParam param) throws JsonProcessingException;

    /**
     * 调用合约之前获取Blob
     * @param param
     * @return
     */
    String getCreateBlob(ContractCreateParam param) throws JsonProcessingException;

    /**
     * 调用合约，获取返回值
     *
     * @param param ContractCreateParam
     * @return BIFBaseResponse
     */
    String execution(ContractTransactionParam param);

    default long defaultGasPrice() {
        return 0L;
    }

    default long defaultFeeLimit() {
        return 30000L;
    }

    default BIFContractInvokeRequest createBIFContractInvokeRequest (ContractInvokeParam param) {
        BIFContractInvokeRequest bifContractInvokeRequest = new BIFContractInvokeRequest();
        bifContractInvokeRequest.setSenderAddress(param.getSenderAddress());
        bifContractInvokeRequest.setPrivateKey(param.getPrivateKey());
        bifContractInvokeRequest.setContractAddress(param.getContractAddress());
        bifContractInvokeRequest.setBIFAmount(param.getAmount());
        bifContractInvokeRequest.setRemarks(param.getRemarks());
        bifContractInvokeRequest.setInput(JsonUtils.toJSONString(param.getInput()));
        return bifContractInvokeRequest;
    }
}
