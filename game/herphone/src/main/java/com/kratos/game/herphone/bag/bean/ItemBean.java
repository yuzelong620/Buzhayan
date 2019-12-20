package com.kratos.game.herphone.bag.bean;

public class ItemBean {
	
	public ItemBean() { 
	}

	public ItemBean(int itemId, int itemNum) { 
		this.itemId = itemId;
		this.itemNum = itemNum;
	}

	int itemId;
	int itemNum;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getItemNum() {
		return itemNum;
	}

	public void setItemNum(int itemNum) {
		this.itemNum = itemNum;
	}

}
