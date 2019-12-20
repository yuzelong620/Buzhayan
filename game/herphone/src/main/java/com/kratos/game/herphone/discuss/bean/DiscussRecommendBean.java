package com.kratos.game.herphone.discuss.bean;


import lombok.Data;

@Data
public class DiscussRecommendBean {

    String discussId;//动态id 
    long recommendPlayerId;//推荐者playerId
    String recommendPlayerNick;//推荐人昵称
    String discussContent;//评论内容
    long recommendTime;//推荐时间
    int handlerState;//0未处理，1，同意，2拒绝
    
    int handlerAdminId;//处理的管理员id
    String handlerAdminName;//处理的管理员 名字
    String roleid;//推荐人ID
    String dramaName;//剧本名称
    String c_id;//剧本评论内容ID
    
    
	public DiscussRecommendBean(String discussId, long recommendPlayerId, String recommendPlayerNick, String discussContent,
			long recommendTime, int handlerState, int handlerAdminId, String handlerAdminName, String roleid,String dramaName,String c_id) {
		this.discussId = discussId;
		this.recommendPlayerId = recommendPlayerId;
		this.recommendPlayerNick = recommendPlayerNick;
		this.discussContent = discussContent;
		this.recommendTime = recommendTime;
		this.handlerState = handlerState;
		this.handlerAdminId = handlerAdminId;
		this.handlerAdminName = handlerAdminName;
		this.roleid = roleid;
		this.dramaName = dramaName;
		this.c_id = c_id;
	}

	public DiscussRecommendBean() {
		
	}

	public DiscussRecommendBean(String discussId, long recommendPlayerId, String recommendPlayerNick, String discussContent,
			long recommendTime, int handlerState, int handlerAdminId, String handlerAdminName) {
		this.discussId = discussId;
		this.recommendPlayerId = recommendPlayerId;
		this.recommendPlayerNick = recommendPlayerNick;
		this.discussContent = discussContent;
		this.recommendTime = recommendTime;
		this.handlerState = handlerState;
		this.handlerAdminId = handlerAdminId;
		this.handlerAdminName = handlerAdminName;
	}

	
    		
}
