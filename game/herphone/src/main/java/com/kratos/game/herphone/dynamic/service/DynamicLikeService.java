package com.kratos.game.herphone.dynamic.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.dynamic.entity.DynamicLikeEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
@Deprecated
@Service
public class DynamicLikeService extends BaseService {
	// 点赞
	public void addLike(String dynamicId) {
		if (dynamicId == null || "".equals(dynamicId)) {
			throw new BusinessException("参数为空");
		}
		DynamicEntity dynamic = dynamicDao.findByID(dynamicId);
		if (dynamic == null) {
			throw new BusinessException("点赞的评论不存在");
		}
		Player player = PlayerContext.getPlayer();
		if(dynamic.getFromPlayerId()==player.getId()){
			throw new BusinessException("不能给自己点赞");
		}
		String id = createId( player.getId(),dynamicId);
		DynamicLikeEntity entity = dynamicLikeDao.findByID(id);
		if (entity != null) {
			return;
		}
		entity = new DynamicLikeEntity(id, player.getId(), dynamicId);
		dynamicLikeDao.save(entity);
		
		dynamicDao.addLikeNum(dynamicId);//增加点赞数量
		//添加 送出点赞数量
		playerDynamicDao.addToLikeNum(player.getId(),1);
		//taskService.playerScheduleUpdate(CommonCost.TackType.giveLike.ordinal(),1); //任务接口(任务类型,完成次数)
	}

	private String createId( long playerId,String dynamicId) {
		return playerId + "_" + dynamicId;
	}
	
	public List<DynamicLikeEntity> findByDynamicIds(long playerId, List<String> dynamicIds) {
		List<String> likeIds = new ArrayList<>();
		for (String dynamicId : dynamicIds) {
			likeIds.add(createId(playerId,dynamicId));
		}
		return dynamicLikeDao.findByIds(likeIds);
	}
	
    /**
     * 查詢已经点赞过的评论。
     */
	public List<String> findLikeDynamicIds(long playerId,List<String> dynamicIds) {
		List<String> list = new ArrayList<>();
		for (DynamicLikeEntity obj:findByDynamicIds(playerId, dynamicIds)) {
			list.add(obj.getDynamicId());
		}
		return list;
	}
}
