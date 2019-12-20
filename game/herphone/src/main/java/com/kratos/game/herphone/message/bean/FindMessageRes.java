package com.kratos.game.herphone.message.bean;

import java.util.List;

import lombok.Data;
@Data
public class FindMessageRes {
	int page;
	int count;
	List<MessageBean> list;
	
	public FindMessageRes(int page, int count, List<MessageBean> list) {
		this.page = page;
		this.count = count;
		this.list = list;
	}
	
}
