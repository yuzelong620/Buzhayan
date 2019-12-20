package com.kratos.game.herphone.dynamic.bean;

import java.util.List;
import com.kratos.game.herphone.dynamic.entity.NominateBestEntity;

import lombok.Data;
@Data
@Deprecated
public class NominateBestBean {
 
    String dynamicId;//动态id
    long recommendPlayerId;//推荐者playerId
    String recommendPlayerNick;//推荐者昵称
    List<ResBean> resList;//评论内容
    long recommendTime;//推荐时间
    int handlerState;//0未处理，1，同意，2拒绝 
    int handlerAdminId;//处理的管理员id
    String handlerAdminName;//处理的管理员 名字
    List<Integer> tags;
    
	public NominateBestBean(NominateBestEntity obj) {
		this.dynamicId = obj.getDynamicId();
		this.recommendPlayerId = obj.getRecommendPlayerId();
		this.recommendPlayerNick = obj.getRecommendPlayerNick();
		this.resList = obj.getResList();
		this.recommendTime = obj.getRecommendTime();
		this.handlerState = obj.getHandlerState();
		this.handlerAdminId = obj.getHandlerAdminId();
		this.handlerAdminName = obj.getHandlerAdminName();
		this.tags=obj.getTags();
	}
}
