package com.kratos.game.herphone.discuss.bean;

import java.util.List;

import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;

@Data
public class DiscussReplyListRes {
	List<DiscussBean> replys;
	public DiscussReplyListRes(List<DiscussBean> replys) {
		this.replys = replys;
	}
	
}
