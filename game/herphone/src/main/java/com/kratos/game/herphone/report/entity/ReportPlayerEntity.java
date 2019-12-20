package com.kratos.game.herphone.report.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 举报玩家类
 * @author Administrator
 *
 */
@Data
@Document
public class ReportPlayerEntity {
	@Id
	private String id;
	@Indexed
	private String reportPlayerDynamicId; 	//动态id(fromPlayerId_toPlayerId)
	@Indexed
	private long fromPlayerId; //举报人
	private String fromPlayerNickName;	//举报人名字
	private long toPlayerId;	//被举报人	
	private String toPlayerNickName;	//被举报人名字
	private String content;		//举报内容
	private int isTitle;		//举报人是否为护眼大队 0不是，1是
	private long timeStamp;		//举报时间戳
	private int state;			//处理状态 0未处理，1已处理，2搁置
}
