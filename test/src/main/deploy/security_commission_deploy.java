import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFContractCreateRequest;
import cn.bif.model.response.BIFContractCreateResponse;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 部署成功会返回一个交易hash
 *
 * 部署账号要有足够的XHT
 */
public class security_commission_deploy {
    /**
     * 测试网址
     */
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    /**
     * 部署用户
     */
    public static final String address = "did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh";
    public static final String privateKey = "priSPKrSftQVRWM33dWxxSmwhRX7ArgyUmwV3pXun79QKsQkW2";

    /**
     * 部署代码文件位置
     */
    public static String fileName = "D:\\air\\BIF-Core-SDK\\test\\src\\main\\data\\security_commission.js";
    /**
     * identity初始化参数 owner设置
     */
    public static String registerCommissionInitInput = "{\"commission\": {\"members\": [{\"id\":\"0001\",\"createdAt\": 123},{\"id\": \"1002\",\"createdAt\": 123},{\"id\": \"1003\",\"createdAt\": 123},{\"id\": \"1004\",\"createdAt\": 123}]},\"owner\":\"did:bid:ef28Wz8twCynVe6PAnamLYCAFJYgJSnMh\"}";


    public static void main(String[] args) throws Exception {


        //如果是JDK11用上面的方法，如果不是用这个方法也很容易
        byte[] bytes = Files.readAllBytes(Paths.get(fileName));

        String content = new String(bytes, StandardCharsets.UTF_8);
        System.out.println("下面是需要部署的代码：\n"
                +"*******************************************************************************************"
                +"\n"
                +content
                +"\n"
                +"*******************************************************************************************"
        );

        //部署合约
        String contractCode = content;

        BIFContractCreateRequest createCReq = new BIFContractCreateRequest();

        //创建方地址和私钥
        createCReq.setSenderAddress(address);
        createCReq.setPrivateKey(privateKey);
        createCReq.setInitInput(registerCommissionInitInput);

        //合约初始balance，一般为0
        createCReq.setInitBalance(1L);

        //合约代码
        createCReq.setPayload(contractCode);

        //标记和type，javascript合约type为0
        createCReq.setRemarks("identity");
        createCReq.setType(0);

        //交易耗费上限
        createCReq.setFeeLimit(300000000L);

        System.out.println("开始部署....");
        //调用SDK创建该合约
        BIFContractCreateResponse createCRsp = sdk.getBIFContractService().contractCreate(createCReq);

        if (createCRsp.getErrorCode() == 0) {
            System.out.println("deployment succeeded ! there is return hash :"
                    +"\n"
                    +"     "+JsonUtils.toJSONString(createCRsp.getResult()));
        } else {
            System.out.println("deployment failed ! there is return reason :"
                    +"\n"
                    +"     "+JsonUtils.toJSONString(createCRsp));
        }
    }
}
