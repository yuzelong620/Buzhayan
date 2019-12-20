package com.kratos.game.herphone.discuss.bean;

import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;
/**
 * 回复我的信息 
 *
 */
@Data
public class DiscussReplyBean extends DiscussBean{ 
	DiscussBean  toMyDiscuss;//回复我的那一条评论 
	public DiscussReplyBean(DiscussEntity obj) {		 
		super(obj);
	}
}
