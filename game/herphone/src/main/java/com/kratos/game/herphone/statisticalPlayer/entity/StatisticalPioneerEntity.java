package com.kratos.game.herphone.statisticalPlayer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class StatisticalPioneerEntity {
	@Id
	private String id;	//唯一索引 （date_playerId)拼接
	@Indexed
	private int date;
	@Indexed
	private long playerId;
	private String roleId;
	private long creatTime;
	private String nickName;
	private String sex;			
	private int dealNum;		//治理次数
	private int succcessNum;	//成功治理次数
	private int isEyeshield;	//是否护眼大队
}
