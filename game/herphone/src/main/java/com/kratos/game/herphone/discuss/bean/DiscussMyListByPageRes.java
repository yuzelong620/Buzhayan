package com.kratos.game.herphone.discuss.bean;

import java.util.List;

import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;

@Data
public class DiscussMyListByPageRes {
	List<DiscussBean> list;
	int page;
	int count;

	public DiscussMyListByPageRes(List<DiscussBean> list, int page, int count) {
		this.list = list;
		this.page = page;
		this.count = count;
	}

}
