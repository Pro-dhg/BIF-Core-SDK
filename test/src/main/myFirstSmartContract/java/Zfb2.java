import java.util.Calendar;
import java.util.UUID;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/11 11:23
 * @DESCRIPTION:
 */

public class Zfb2 {
    public static void main(String[] args) {
//        Goods build = Goods.builder()
//                .outTradeNo("123")
//                .subject("123123")
//                .totalAmount("12312331")
//                .build();
//        System.out.println(build);

//        System.out.println(System.currentTimeMillis() + "   "+UUID.randomUUID().toString().replaceAll("-", "").substring(0, 6));

        Calendar calendar = Calendar.getInstance();
        //设置前一个小时，超过一小时认为没有结果，将数据信息更改为超时！
        calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) - 1);
        System.out.println(calendar.getTime());
    }
}
