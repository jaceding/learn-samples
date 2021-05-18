package per.jaceding.demo.simple;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaceding
 * @date 2021/3/2
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String type;
    private String value;
}
