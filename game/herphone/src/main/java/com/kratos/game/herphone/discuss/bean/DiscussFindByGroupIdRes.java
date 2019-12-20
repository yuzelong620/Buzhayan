package com.kratos.game.herphone.discuss.bean;

import java.util.List;

import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;

@Data
public class DiscussFindByGroupIdRes {
	int page;
	int count;
	List<DiscussBean> list;
	public DiscussFindByGroupIdRes(int page, int count, List<DiscussBean> list) {	 
		this.page = page;
		this.count = count;
		this.list = list;
	}
	
}
