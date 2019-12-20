package com.kratos.game.herphone.report.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 举报信息类
 * @author Administrator
 *
 */
@Data
@Document
public class ReportInfoEntity {
	@Id
	private String id;
	@Indexed
	private String reportInfoDynamicId; 	//动态id(fromPlayerId_discussId)
	@Indexed
	private long fromPlayerId; //举报人
	@Indexed
	private String discussId;	//评论id
	private int type;			//举报评论的类型 0剧本1广场2粉圈
	private int isTitle;		//举报人是否为护眼大队 0不是，1是
	private long timeStamp;		//举报时间戳
	private int state;			//处理状态 0未处理，1已处理，2搁置
}
