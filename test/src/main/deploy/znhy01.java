import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCreateRequest;
import cn.bif.model.response.BIFContractCreateResponse;

/**
 * 部署成功会返回一个交易hash
 *
 * 部署账号要有足够的XHT
 */
public class znhy01 {

    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    public static final String address = "did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c";
    public static final String privateKey = "priSPKdmZA2jsa8hzetan3H315HfTWftsueEkZJAxtheKPYhJL";

    public static void main(String[] args) {
        //部署合约
        //合约代码，注意转义
        String contractCode = "\"use strict\";function queryById(id) {    let data = Chain.load(id);    return data;}function query(input) {    input = JSON.parse(input);    let id = input.id;    let object = queryById(id);    return object;}function main(input) {    input = JSON.parse(input);    Chain.store(input.id, input.data);}function init(input) {    return;}";

        BIFContractCreateRequest createCReq = new BIFContractCreateRequest();

        //创建方地址和私钥
        createCReq.setSenderAddress(address);
        createCReq.setPrivateKey(privateKey);

        //合约初始balance，一般为0
        createCReq.setInitBalance(1L);

        //合约代码
        createCReq.setPayload(contractCode);

        //标记和type，javascript合约type为0
        createCReq.setRemarks("create contract");
        createCReq.setType(0);

        //交易耗费上限
        createCReq.setFeeLimit(300000000L);

        //调用SDK创建该合约
        BIFContractCreateResponse createCRsp = sdk.getBIFContractService().contractCreate(createCReq);

        if (createCRsp.getErrorCode() == 0) {
            System.out.println(JsonUtils.toJSONString(createCRsp.getResult()));
        } else {
            System.out.println(JsonUtils.toJSONString(createCRsp));
        }
    }
}
