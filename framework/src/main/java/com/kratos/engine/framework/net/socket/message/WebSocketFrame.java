package com.kratos.engine.framework.net.socket.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.SimpleDateFormatSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class WebSocketFrame {
	private String id;
	private String msg;

    private static SerializeConfig mapping = new SerializeConfig();

    static {
        mapping.put(Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Date.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Timestamp.class, new SimpleDateFormatSerializer("yyyy-MM-dd HH:mm:ss"));
        mapping.put(java.sql.Time.class, new SimpleDateFormatSerializer("HH:mm:ss"));
    }

    public static WebSocketFrame valueOf(Message message) {
        WebSocketFrame frame = new WebSocketFrame();
        frame.id = String.format("%s_%s", message.getModule(), message.getCmd());
        frame.msg = JSON.toJSONString(message, mapping, SerializerFeature.DisableCircularReferenceDetect);
        return frame;
    }

    public short getModule() {
        return Short.parseShort(id.split("_")[0]);
    }

    public short getCmd() {
        return Short.parseShort(id.split("_")[1]);
    }
}
