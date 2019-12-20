package com.kratos.game.herphone.pioneer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
/**护眼先锋列表*/
@Data
@Document
public class PioneerEntity {
	@Id
	private long playerId;		
	private long creatTime;		//加入护眼先锋时间
	private int dealNum;		//处理次数
	private int successNum;		//成功处理次数
	private int allocationNum; 	//分配举报信息个数
}
