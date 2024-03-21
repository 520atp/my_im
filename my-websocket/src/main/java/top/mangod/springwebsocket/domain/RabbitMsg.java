package top.mangod.springwebsocket.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class RabbitMsg implements Serializable {

    /**
     * 用户id
     */
    private String userId;

    /**
     * 消息
     */
    private String msg;

}
