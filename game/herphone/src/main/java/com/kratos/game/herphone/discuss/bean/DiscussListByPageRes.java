package com.kratos.game.herphone.discuss.bean;

import java.util.List;

import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;

@Data
public class DiscussListByPageRes {
    int page;//最新评论当前页
    int count;//最新评论当前页 显示的数量
    List<DiscussBean> recentCommentList;//最新评论 
    
	public DiscussListByPageRes(int page, int count, List<DiscussBean> recentCommentList) {
		this.page = page;
		this.count = count;
		this.recentCommentList = recentCommentList; 
	}
}
