package com.kratos.game.herphone.clue.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.clue.bean.ReqClueData;
import com.kratos.game.herphone.clue.entity.PlayerClueDataEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;

@Service
public class PlayerClueDataService extends BaseService{
	
	public PlayerClueDataEntity load(long playerId,ReqClueData req){

		String id =playerId+"_"+req.getGameId()+"_"+req.getChapterId();
		PlayerClueDataEntity playerClueDataEntity = playerClueDataDao.findByID(id);
		if(playerClueDataEntity == null){
			playerClueDataEntity = new PlayerClueDataEntity(id,
					playerId,req.getGameId(),req.getChapterId(),new HashSet<Integer>());
			playerClueDataDao.save(playerClueDataEntity);
		}
		return playerClueDataEntity;
	}


	/**
	 * 记录线索
	 */
	public void recordClue(ReqClueData req) {
		//获取玩家ID
		Player player = PlayerContext.getPlayer();
		if(req == null){
			throw new BusinessException("参数错误");
		}
		if(req.getGameId() == 0){
			throw new BusinessException("参数错误");
		}
		//查询该玩家获取的线索信息
		PlayerClueDataEntity playerClueDataEntity = load(player.getId(),req);
		//获取该玩家在该剧本下的线索集合
		Set<Integer> cluesIdList = playerClueDataEntity.getCluesIdList();
		//将该线索加入到该玩家的线索集合中
		cluesIdList.add(req.getClueId());
		//数据写入
		playerClueDataEntity.setCluesIdList(cluesIdList);
		playerClueDataDao.save(playerClueDataEntity);
	}


	/**
	 * 根据剧本ID和章节ID查询玩家所有线索
	 */
	public Set<Integer> findClue(ReqClueData req) {
		Player player = PlayerContext.getPlayer();
		if(req == null){
			throw new BusinessException("参数错误");
		}
		if(req.getGameId() == 0){
			throw new BusinessException("参数错误");
		}
		PlayerClueDataEntity playerClueDataEntity = load(player.getId(), req);
		return playerClueDataEntity.getCluesIdList();
	}
}
