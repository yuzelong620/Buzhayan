package com.kratos.game.herphone.game.chose.service;

import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.game.chose.domain.PlayerExploration;
import com.kratos.game.herphone.player.bean.PlayerAstrictBean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import org.springframework.stereotype.Service;

@Service
public class PlayerExplorationServiceImpl extends BaseCrudService<Long, PlayerExploration> implements PlayerExplorationService {
	
	@Override
	public List<Integer> getGameid(long playId) {   	
   	 Query query = em.createNativeQuery("SELECT game_id FROM `player_exploration` WHERE `player_id` = ? ORDER BY last_add_exploration_time DESC LIMIT 0, 6");
        query.setParameter(1, playId);
        List resultList = query.getResultList();
        if (resultList == null||resultList.size()==0) {
			return null;
		}
       List<Integer> list = new ArrayList<>();
      for (Object integer: resultList) {   	     	   
   	   list.add(Integer.parseInt(integer.toString()));
      }
   	return list;
	}
	/**玩家玩过剧本的数量*/
	@Override
	public int getGameCount(long playId) {
		 Query query = em.createNativeQuery("SELECT game_id FROM `player_exploration` WHERE `player_id` = ?");
         query.setParameter(1, playId);
         List resultList = query.getResultList();
         if (resultList == null) {
			return 0;
		}
		return resultList.size();
	}
	@Override
	public  Map<Long, Integer> getGameCount(List<Long> playerIds) {
		Map<Long, Integer> map = new HashMap<>();
		if(playerIds.size() == 0) {
			return map;
		} else {
			String hql = "SELECT COUNT(gameId)  ,playerId  FROM PlayerExploration WHERE playerId in (:playerIds) GROUP BY playerId";
			Query query = em.createQuery(hql);
			query.setParameter("playerIds", playerIds);
	         List<Object []> resultList = query.getResultList();
	         if (resultList == null) {
				return map;
			}
	         for (Object[] objects : resultList) {
	        	 map.put(Long.parseLong(objects[1].toString()),Integer.parseInt(objects[0].toString()));	
			}
			return map;
		}
	}
	@Override
	public List<Long> listPlayerId(List<Long> playerIds) {
		if (playerIds.size() == 0) {
			return playerIds;
		}
		Query query = em.createQuery("select playerId from PlayerExploration where playerId in (:playerIds) group by playerId having count(gameId) >= 1");//?改成:
		query.setParameter("playerIds", playerIds);
		 List resultList = query.getResultList();
		 List<Long> list = new ArrayList<>();
		 if (resultList == null||resultList.size()==0) {
				return list;
			}
		 for (Object object: resultList) {   	     	    
		   	   list.add(Long.valueOf(object.toString()));
		      }
	   	return list;
	}

	@SuppressWarnings({ "unchecked"})
	@Override
	public Map<Integer, Integer> getGamePlayerNum() {
		Map<Integer, Integer> resmap = JedisUtils.getInstance().get(AppCache.gamePeopleNum, HashMap.class);
		if (resmap == null) {
			resmap = new HashMap<Integer, Integer>();
			Query playerNum = em.createNativeQuery("SELECT  game_id,COUNT(player_id) FROM player_exploration GROUP BY game_id");
			List<Object[]> ResultList = playerNum.getResultList();			
			if (ResultList == null ||ResultList.size() == 0) {				
				return resmap;
			}
			for (Object[] obj : ResultList) {
				resmap.put(Integer.parseInt(obj[0].toString()), Integer.parseInt(obj[1].toString()));
			}
			JedisUtils.getInstance().set(AppCache.gamePeopleNum, resmap);
		}		
		return resmap;
	}
}
