package per.jaceding.demo.decimal;

import java.math.BigDecimal;

/**
 * @author jaceding
 * @date 2021/6/7
 */
public class BigDecimalTest {

    @SuppressWarnings("AlibabaBigDecimalAvoidDoubleConstructor")
    public static void main(String[] args) {
        BigDecimal a = new BigDecimal(1.01);
        BigDecimal b = new BigDecimal(1.02);
        BigDecimal c = new BigDecimal("1.01");
        BigDecimal d = new BigDecimal("1.02");
        System.out.println(a.add(b));
        System.out.println(c.add(d));
        System.out.println(0.1 + 0.2);
    }
}
