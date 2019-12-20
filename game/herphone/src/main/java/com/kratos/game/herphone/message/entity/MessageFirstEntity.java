package com.kratos.game.herphone.message.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/**
 * 私聊 最后 数据
 * @author Administrator
 *
 */
@Data
@Document
public class MessageFirstEntity {
	 @Id
	 String id;// playerIds 以下划线分割 按照 排序拼接  1_2的形式 拼接
	 @Indexed
	 List<Long> playerIds=new ArrayList<>();//私聊的用户id
	 Map<Long,Long> limitTime=new HashMap<>();//限制时间，单方“删除会话后不再显示历史”
	 String messageId;//消息id
	 long lastUpdateTime;//最后更新时间
	 
	public MessageFirstEntity(String id, List<Long> playerIds, String messageId, long lastUpdateTime) {
		this.id = id;
		this.playerIds = playerIds;
		this.messageId = messageId;
		this.lastUpdateTime = lastUpdateTime;
	}
	
	public MessageFirstEntity() {
	}
	 
}
