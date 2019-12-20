package com.kratos.game.herphone.report.entity;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

/**
 * 推送给护眼先锋消息实体类
 * @author Administrator
 *
 */
@Data
@Document
public class ReportInfoDistinctEntity {
	@Id
	private String id;	//discussId_type
	private String discussId;	//评论id
	@Indexed
	private int type;	//属于评论类型	0剧本 1广场
	private long reportPlayerId;	//举报人
	private ArrayList<Long> pushPlayers = new ArrayList<>(3);  //推送人
	private ArrayList<Long> dealPlayers = new ArrayList<>(3);  //处理人
	private int dealOpinion;		//处理意见 0不处理1已处理
	private int version;	//版本号 用于校验数据一致
	private long reportTime;
	
	
}
