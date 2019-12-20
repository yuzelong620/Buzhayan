package com.kratos.game.herphone.discuss.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
/**
 * 评论推荐
 * @author Administrator
 *
 */
@Data
@Document
public class RecommendBestEntity {
	@Id
    String discussId;//动态id
    long recommendPlayerId;//推荐者playerId
    String recommendPlayerNick;//推荐者昵称
    String discussContent;//评论内容
    long recommendTime;//推荐时间
    int handlerState;//0未处理，1，同意，2拒绝
    
    int handlerAdminId;//处理的管理员id
    String handlerAdminName;//处理的管理员 名字
	public RecommendBestEntity(String discussId, long recommendPlayerId, String recommendPlayerNick,
			String discussContent, long recommendTime, int handlerState, int handlerAdminId, String handlerAdminName) {
		this.discussId = discussId;
		this.recommendPlayerId = recommendPlayerId;
		this.recommendPlayerNick = recommendPlayerNick;
		this.discussContent = discussContent;
		this.recommendTime = recommendTime;
		this.handlerState = handlerState;
		this.handlerAdminId = handlerAdminId;
		this.handlerAdminName = handlerAdminName;
	}
	public RecommendBestEntity() {
		
	}
}
