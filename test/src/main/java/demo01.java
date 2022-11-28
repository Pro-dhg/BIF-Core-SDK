import cn.bif.model.crypto.KeyPairEntity;

/**
 * demo 验证sdk可用
 */
public class demo01 {
    public static void main(String[] args) {
        KeyPairEntity entity = KeyPairEntity.getBidAndKeyPair();                //离线创建一个新账号
        System.out.printf("BID address %s\n", entity.getEncAddress());          //账户地址, 可以公开
        System.out.printf("privatekey %s\n", entity.getEncPrivateKey());      //账户私钥, 请妥善保管
    }
}
