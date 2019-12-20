package com.kratos.game.herphone.message.bean;

import java.util.Collection;
import lombok.Data;

@Data
public class FindMessageInfoRes {
	Collection<MessageInfoBean> list;// 未读消息

	public FindMessageInfoRes(Collection<MessageInfoBean> collection) {
		this.list = collection;
	}
}
