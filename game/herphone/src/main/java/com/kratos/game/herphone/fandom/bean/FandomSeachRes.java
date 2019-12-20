package com.kratos.game.herphone.fandom.bean;

import java.util.List;

import lombok.Data;
@Data
@Deprecated
public class FandomSeachRes {
	String authorId;
	List<FandomBean> list;

	public FandomSeachRes(String authorId, List<FandomBean> list) {
		super();
		this.authorId = authorId;
		this.list = list;
	}

	public FandomSeachRes() {
	}

}