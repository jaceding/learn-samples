package per.jaceding.demo.heartbeat.proto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息类型枚举
 *
 * @author jaceding
 * @date 2020/9/9
 */
@SuppressWarnings("unused")
@Getter
@AllArgsConstructor
public enum MessageEnum {

    /**
     * Ping
     */
    PING(0x02, Ping.class),

    /**
     * PONG
     */
    PONG(0x04, Pong.class);

    private final Integer code;

    private final Class<?> clazz;

    public static Integer getCodeByClass(Class<?> aClass) {
        for (MessageEnum messageEnum : MessageEnum.values()) {
            if (messageEnum.getClazz().equals(aClass)) {
                return messageEnum.getCode();
            }
        }
        return null;
    }

    public static Class<?> getClassByCode(Integer code) {
        for (MessageEnum messageEnum : MessageEnum.values()) {
            if (messageEnum.getCode().equals(code)) {
                return messageEnum.getClazz();
            }
        }
        return null;
    }
}
