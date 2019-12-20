package com.kratos.game.herphone.dynamic.entity;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kratos.game.herphone.dynamic.bean.ResBean;

import lombok.Data;
/**
 * 评论推荐
 * @author Administrator
 *
 */
@Data
@Document
public class NominateBestEntity {
	@Id
    String dynamicId;//动态id
    long recommendPlayerId;//推荐者playerId
    String recommendPlayerNick;//推荐者昵称
    String recommendRoleId;//角色id
    List<ResBean> resList;//评论内容
    long recommendTime;//推荐时间
    int handlerState;//0未处理，1，同意，2拒绝 
    
    int handlerAdminId;//处理的管理员id
    String handlerAdminName;//处理的管理员 名字
    List<Integer> tags=new ArrayList<Integer>();
	public NominateBestEntity(String dynamicId, long recommendPlayerId, String recommendPlayerNick,
			List<ResBean> resList, long recommendTime, int handlerState, int handlerAdminId, 
			String handlerAdminName,String recommendRoleId,List<Integer> tags) {
		this.dynamicId = dynamicId;
		this.recommendPlayerId = recommendPlayerId;
		this.recommendPlayerNick = recommendPlayerNick;
		this.resList = resList;
		this.recommendTime = recommendTime;
		this.handlerState = handlerState;
		this.handlerAdminId = handlerAdminId;
		this.handlerAdminName = handlerAdminName;
		this.recommendRoleId=recommendRoleId;
		this.tags=tags;
	}
	public NominateBestEntity() {
		
	}
}
