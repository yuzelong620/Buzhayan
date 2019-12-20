package com.kratos.game.herphone.bag.bean;

import java.util.List;

public class BagInfo {

	public BagInfo(List<ItemBean> items) {
		this.items = items;
	}

	public BagInfo() { 
	}

	List<ItemBean> items;

	public List<ItemBean> getItems() {
		return items;
	}

	public void setItems(List<ItemBean> items) {
		this.items = items;
	}
}
