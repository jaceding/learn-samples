package per.jaceding.demo.heartbeat.proto;

import lombok.*;

/**
 * @author jaceding
 * @date 2020/9/14
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Ping extends AbstractMessage {

    private String msg;
}
