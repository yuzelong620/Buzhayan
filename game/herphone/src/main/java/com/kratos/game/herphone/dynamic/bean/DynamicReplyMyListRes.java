package com.kratos.game.herphone.dynamic.bean;

import java.util.List;

import lombok.Data;
@Data
@Deprecated
public class DynamicReplyMyListRes {
	List<DynamicBean> list;
	int page;
	int count;

	public DynamicReplyMyListRes(List<DynamicBean> list, int page, int count) {
		this.list = list;
		this.page = page;
		this.count = count;
	}

}
