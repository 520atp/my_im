package top.mangod.springwebsocket.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 文本聊天对话体
 */
@Data
public class MsgBody implements Serializable {
    /**
     * 操作类型：1麦克风操作
     */
    private Integer operateType;
    /**
     * 操作内容：1打开，2关闭
     */
    private String operateContent;

    /**
     * 消息内容
     */
    private String message;
}