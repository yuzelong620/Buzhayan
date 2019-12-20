package com.kratos.game.herphone.clue.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.Clue_Json;
import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.clue.bean.ClueAddRes;
import com.kratos.game.herphone.clue.entity.PlayerClueEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.ClueCache;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
@Service
public class PlayerClueService extends BaseService{
	
	public  PlayerClueEntity load(long playerId){
		PlayerClueEntity obj=playerClueDao.findByID(playerId);
		if(obj!=null){
			return obj;
		}
		obj=new PlayerClueEntity(playerId,new HashSet<>());
		playerClueDao.save(obj);
		return obj;
	}
	
	/**
	 *获取线索
	 * @param gameId
	 * @param chatId
	 * @param cId
	 */
	public ClueAddRes add(int id){
		ClueCache cache=JsonCacheManager.getCache(ClueCache.class);
		Clue_Json json=cache.getData(id);
		if(json==null){
			throw new BusinessException("参数错误！");
		}
		Player player=PlayerContext.getPlayer();
		//记录获得的线索id
		PlayerClueEntity entity=load(player.getId());
		PlayerDynamicEntity playerDynamic=playerDynamicService.load(player.getId());
		if(entity.getRewardClueIds().contains(json.getId())){			
			return new ClueAddRes(0, playerDynamic.getClue());//提示+0
		}		
		playerClueDao.addBlack(player.getId(), json.getId());
	    //重新计算线索值
		taskService.playerScheduleUpdate(CommonCost.TackType.clue.ordinal(),json.getValue()); //任务接口(任务类型,完成次数)
		//更新阶段任务进度，调用阶段任务接口，获得线索
		stageTaskService.updateTaskValue(CommonCost.StageTaskType.clue.ordinal(), 1);
		return new ClueAddRes(json.getValue(), commonService.recountClues(player, playerDynamic,entity));
	}
	
	/**
	 * 重新 线索值
	 * @param playerId
	 * @return
	 */
	public int  getClueValue(PlayerClueEntity entity){
		ClueCache cache=JsonCacheManager.getCache(ClueCache.class);
		int sumClue=0;
		for(int clueId:entity.getRewardClueIds()){
			Clue_Json json=cache.getData(clueId);
			sumClue+=json.getValue();
		}
		return sumClue;
	}

	public List<Integer> getMyClues(int gameId) {
		GameCatalogCache cache=JsonCacheManager.getCache(GameCatalogCache.class);
		GameCatalog_Json game=cache.getData(gameId);
		if(game==null){
			throw new BusinessException("参数错误");
		}
		ClueCache clueCache=JsonCacheManager.getCache(ClueCache.class);
		ArrayList<Integer> list=new ArrayList<>();
		Player player=PlayerContext.getPlayer();
		PlayerClueEntity clues= load(player.getId());
		for(int clueId:clues.getRewardClueIds()){
			Clue_Json json=clueCache.getData(clueId);
			if(json==null){
				continue;
			}
			if(json.getGameid()==gameId){
				list.add(clueId);
			}
		}
		return list;
	}

}
