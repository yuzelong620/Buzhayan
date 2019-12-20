package com.kratos.game.herphone.discuss.bean;

import java.util.List;
 
import com.kratos.game.herphone.message.bean.DiscussBean;

import lombok.Data;

@Data
public class DiscussListRes {
    int page;//最新评论当前页
    int count;//最新评论当前页 显示的数量
    List<TopDiscussBean> recentCommentList;//最新评论 
    List<TopDiscussBean> topCommentList;//精品评论
    
	public DiscussListRes(int page, int count, List<TopDiscussBean> recentCommentList,
			List<TopDiscussBean> topCommentList) {
		this.page = page;
		this.count = count;
		this.recentCommentList = recentCommentList;
		this.topCommentList = topCommentList;
	}
	
}
