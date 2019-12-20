package com.kratos.game.herphone.gift.bean;

import lombok.Data;

@Data
public class GiftBean {

	int itemId;//物品ID
	int authorId;//作者ID
	int giftNum;//礼物数量
	
	public GiftBean() {
		
	}

	public GiftBean(int itemId, int authorId, int giftNum) {
		this.itemId = itemId;
		this.authorId = authorId;
		this.giftNum = giftNum;
	}
	
}
