package com.kratos.game.herphone.order.entity;

import javax.persistence.Id;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class OrderEntity {
	@Id
	private String id;   //订单Id
	private long playerId; //购买玩家Id
	private int itemId;	//购买物品Id
	private int itemNum;	//购买物品数量
	private int cost;		//消费金额
	private long currentTime;	//订单生成时间
	
}
