import lombok.Data;


/**
 * @author rui.wang
 * @ Version: 1.0
 * @ Time: 2022/10/10 17:28
 * @ Description:
 */
@Data
public class ContractInvokeParam {

    /**
     * senderAddress
     */
    private String senderAddress;
    /**
     * 合约地址
     */
    private String contractAddress;
    /**
     * amount
     */
    private Long amount;
    /**
     * 备注
     */
    private String remarks;
    /**
     * 私钥
     */
    private String privateKey;
    /**
     * input
     */
    private Object input;

    public ContractInvokeParam() {
    }

    public ContractInvokeParam(String senderAddress, String contractAddress, Long amount, String remarks, String privateKey, Object input) {
        this.senderAddress = senderAddress;
        this.contractAddress = contractAddress;
        this.amount = amount;
        this.remarks = remarks;
        this.privateKey = privateKey;
        this.input = input;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getContractAddress() {
        return contractAddress;
    }

    public void setContractAddress(String contractAddress) {
        this.contractAddress = contractAddress;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }

    public Object getInput() {
        return input;
    }

    public void setInput(Object input) {
        this.input = input;
    }
}
