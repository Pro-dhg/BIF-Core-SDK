import cn.bif.api.BIFSDK;
import cn.bif.common.JsonUtils;
import cn.bif.model.request.BIFAccountGetInfoRequest;
import cn.bif.model.response.BIFAccountGetInfoResponse;

/**
 * 查看账户状态
 */
public class demo02 {
    public static final String NODE_URL = "http://test.bifcore.bitfactory.cn";  //星火链测试网RPC地址
//    public static final String NODE_URL = "http://1.15.66.155:27002/getLedger";  //星火链测试网RPC地址

    public static BIFSDK sdk = BIFSDK.getInstance(NODE_URL);

    public static void main(String[] args) {
        String address = "did:bid:efJmpzPvG76ktDykMtzKVMAEForBiw6c";

        //构建查看账户请求
        BIFAccountGetInfoRequest infoReq = new BIFAccountGetInfoRequest();
        //要查看账户的地址
        infoReq.setAddress(address);

        //发出查询请求
        BIFAccountGetInfoResponse infoRsp = sdk.getBIFAccountService().getAccount(infoReq);

        if (infoRsp.getErrorCode() == 0) {
            //查询成功
            System.out.println(JsonUtils.toJSONString(infoRsp.getResult()));
        } else {
            //查询失败
            System.out.println(infoRsp.getErrorDesc());
        }

    }
}
