package com.kratos.game.herphone.fandom.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service; 
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
import com.kratos.game.herphone.fandom.entity.FandomLikeEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;

@Service
@Deprecated
public class FandomLikeService extends BaseService {
	// 点赞
	public void addLike(String dynamicId) {
		if (dynamicId == null || "".equals(dynamicId)) {
			throw new BusinessException("参数为空");
		}
		FandomEntity dynamic = fandomDao.findByID(dynamicId);
		if (dynamic == null) {
			throw new BusinessException("点赞的评论不存在");
		}
		Player player = PlayerContext.getPlayer();
		if(dynamic.getFromPlayerId()==player.getId()){
			throw new BusinessException("不能给自己点赞");
		}
		String id = createId( player.getId(),dynamicId);
		FandomLikeEntity entity = fandomLikeDao.findByID(id);
		if (entity != null) {
			return;
		}
		entity = new FandomLikeEntity(id, player.getId(), dynamicId);
		fandomLikeDao.save(entity);
		
		fandomDao.addLikeNum(dynamicId,1);//增加点赞数量
		//添加 送出点赞数量
		playerDynamicDao.addToLikeNum(player.getId(),1);
		//taskService.playerScheduleUpdate(CommonCost.TackType.giveLike.ordinal(),1); //任务接口(任务类型,完成次数)
	}

	private String createId( long playerId,String fandomId) {
		return playerId + "_" + fandomId;
	}
	
	public List<FandomLikeEntity> findByDynamicIds(long playerId, List<String> dynamicIds) {
		List<String> likeIds = new ArrayList<>();
		for (String dynamicId : dynamicIds) {
			likeIds.add(createId(playerId,dynamicId));
		}
		return fandomLikeDao.findByIds(likeIds);
	}
	
    
	public List<String> findLikeFandomIds(long playerId,List<String> dynamicIds) {
		List<String> list = new ArrayList<>();
		for (FandomLikeEntity obj:findByDynamicIds(playerId, dynamicIds)) {
			list.add(obj.getFandomId());
		}
		return list;
	}
}
