package com.kratos.game.herphone.helperMessage.entity;

import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class HelperMessageOptionAnswerEntity {

	@Id
	private int messageId; //消息ID
	private String messageContent;//消息内容
	private List<Integer> optionId; //选项
	private Map<Integer,Integer> answerId; //答案

	public HelperMessageOptionAnswerEntity() {
	}
	
	public HelperMessageOptionAnswerEntity(int messageId, String messageContent, List<Integer> optionId,Map<Integer, Integer> answerId) {
		this.messageId = messageId;
		this.messageContent = messageContent;
		this.optionId = optionId;
		this.answerId = answerId;
	}
}
