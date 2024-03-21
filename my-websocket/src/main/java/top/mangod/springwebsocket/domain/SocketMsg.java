package top.mangod.springwebsocket.domain;

import lombok.Data;

/**
 * ws会话消息体
 */
@Data
public class SocketMsg<T> {

    /**
     * 消息类型：1心跳  2登录 3业务操作
     */
    private Integer msgType;

    /**
     * 发送用户ID
     */
    private String sendUserId;

    /**
     * 接受用户ID
     */
    private String receivedUserId;

    /**
     * 会话id
     */
    private String conversation_id;

    /**
     * 业务类型
     */
    private Integer bizType;

    /**
     * 消息内容
     */
    private T msgBody;
}