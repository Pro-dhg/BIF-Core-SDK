import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @AUTHOR: dhg
 * @DATE: 2023/1/11 14:59
 * @DESCRIPTION:
 */

@Data
@Builder
public class Goods  {

    /**
     * 商户订单号，需要保证商家系统不重复。
     */
    private String outTradeNo;
    /**
     * 订单金额。
     */
    private String totalAmount;
    /**
     * 商品的标题/交易标题/订单标题/订单关键字等。不可使用特殊字符，如 /，=，& 等。
     */
    private String subject;
}
