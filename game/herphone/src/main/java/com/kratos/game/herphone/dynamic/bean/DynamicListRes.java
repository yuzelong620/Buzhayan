package com.kratos.game.herphone.dynamic.bean;

import java.util.List;
import lombok.Data;
@Deprecated
@Data
public class DynamicListRes {
	int count;
	int page;
	List<DynamicBean> list;
	public DynamicListRes(int count, int page, List<DynamicBean> list) {		 
		this.count = count;
		this.page = page;
		this.list = list;
	}
}
