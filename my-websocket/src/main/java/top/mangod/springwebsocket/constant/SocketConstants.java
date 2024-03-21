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

    /**
     * 业务类型
     */
    interface BizType {
        /**
         *  单聊
         */
        int SINGLE_CHAT = 0;

        /**
         *  群聊
         */
        int GROUP_CHAT = 1;

        /**
         *  系统消息
         */
        int SYSTEM_MSG = 2;

    }

    /**
     * 模块操作类型
     */
    interface OperateType {
        /**
         * 麦克风操作
         */
        int MICRO_PHONE = 1;
    }

}
