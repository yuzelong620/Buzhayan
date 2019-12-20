package com.kratos.game.herphone.discuss.bean;
 

import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;
 
@Data
public class MainDiscussBean extends DiscussBean{   
	public MainDiscussBean(DiscussEntity obj) {
		super(obj);
	}
	int unreadNum;//未读回复数量
	DiscussBean firstReply;//第一个回复的
}
