package com.kratos.game.herphone.playerOnline.bean;

import lombok.Data;

@Data
public class PlayerTimeState {

	private int state;// 未读message红点
	private long data;//时间
	int unreadReplyReadPoint;//评论回复红点
	
	public PlayerTimeState() {
	}
	@Deprecated
	public PlayerTimeState(int state) {
		this.state = state;
		this.data = System.currentTimeMillis();
	}
    /**
     * @param state  未读message红点
     * @param time 系统时间
     * @param unreadReplyReadPoint 评论回复红点
     */
	public PlayerTimeState(int state, long time,int unreadReplyReadPoint){		 
		this.state = state;
		this.data = time;
		this.unreadReplyReadPoint=unreadReplyReadPoint;
	}
	
	
}
