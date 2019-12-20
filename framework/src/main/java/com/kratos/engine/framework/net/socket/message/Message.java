package com.kratos.engine.framework.net.socket.message;


import com.alibaba.fastjson.annotation.JSONField;
import com.kratos.engine.framework.net.socket.annotation.MessageMeta;

/**
 * 抽象消息定义
 */
public abstract class Message {
    /**
     * messageMeta, module of message
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    public short getModule() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.module();
        }
        return 0;
    }

    /**
     * messageMeta, subType of message
     * @return
     */
    @JSONField(serialize = false, deserialize = false)
    public short getCmd() {
        MessageMeta annotation = getClass().getAnnotation(MessageMeta.class);
        if (annotation != null) {
            return annotation.cmd();
        }
        return 0;
    }
}
