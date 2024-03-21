package top.mangod.springwebsocket.constant;

public interface SocketConstants {

    /**
     * 消息类型
     */
    interface MsgType {

        /**
         * 1心跳
         */
        int HEART_BEAT = 1;

        /*
         * 2登录
         */
        int LOGIN = 2;

        /*
         * 业务类型
         */
        int BIZ_OPERATE = 3;
    }

}
