package com.kratos.game.herphone.scheduled;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.common.utils.TimeUtil;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.game.chose.message.ResEachExplorationRank;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.player.service.PlayerService;

@Component
public class ScheduledService extends  BaseCrudService<Long, Player>{
	@Autowired
	private PlayerService playerService;
	
	public List<ResRankPlayer> getExploration() {
		   List<Player> players = null;
	        try{
	        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
	        Root<Player> root = criteriaQuery.from(Player.class);
	        criteriaQuery.select(root).orderBy(
	                criteriaBuilder.desc(root.get("exploration")));
	        TypedQuery<Player> query = em.createQuery(criteriaQuery);
	        query.setFirstResult(0);
	        query.setMaxResults(50);
	        players = query.getResultList();
	        }
	        finally{
	        	em.close();
	        }
	        List<ResRankPlayer> result = new ArrayList<>();
	        for (int i = 0; i < players.size(); i++) {
	            ResRankPlayer resRankPlayer = new ResRankPlayer(players.get(i), i + 1);
	            resRankPlayer.setAchievement(players.get(i).getExploration());
	            resRankPlayer.setLastAddAchievementTime(players.get(i).getLastAddExplorationTime());
	            result.add(resRankPlayer);
	        }
	        this.sortRank(result);
	        JedisUtils.getInstance().set(AppCache.explorationList, result);
	        return result;
	}
	 public List<ResRankPlayer> getRank() {     
	        List<Player> players =null;
	        try{
	        CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
	        CriteriaQuery<Player> criteriaQuery = criteriaBuilder.createQuery(Player.class);
	        Root<Player> root = criteriaQuery.from(Player.class);
	        criteriaQuery.select(root).orderBy(
	                criteriaBuilder.desc(root.get("achievement")));
	        TypedQuery<Player> query = em.createQuery(criteriaQuery);
	        query.setFirstResult(0);
	        query.setMaxResults(50);
	        players = query.getResultList();
	        }
	        finally{
	           em.close();
	        }
	        List<ResRankPlayer> result = new ArrayList<>();
	        for (int i = 0; i < players.size(); i++) {
	            result.add(new ResRankPlayer(players.get(i), i + 1));
	        }
	        this.sortRank(result);
	        JedisUtils.getInstance().set(AppCache.achievementList, result);
	        return result;
	    }
	 @SuppressWarnings("unchecked")
	public ResEachExplorationRank getAllRank() {	     
	        ResEachExplorationRank resEachExplorationRank = new ResEachExplorationRank();
	        Map<Integer, List<ResRankPlayer>> result = new HashMap<>();
//	        List<GameCatalog_Json> list = configResourceRegistry.getConfig(GameCatalogConfig.class).getList();
	        List<GameCatalog_Json> list=JsonCacheManager.getCache(GameCatalogCache.class).getList();
	        ResRankPlayer resRankPlayer;
	        List<ResRankPlayer> resRankPlayers;
	        for (GameCatalog_Json gameCatalogConfig : list) {
	            resRankPlayers = new ArrayList<>();
	            Query query = em.createNativeQuery("SELECT player_id, exploration_count FROM `player_exploration` WHERE `game_id` = ? ORDER BY exploration_count DESC LIMIT 0, 3");
	            query.setParameter(1, gameCatalogConfig.getId());
	            List<Object[]> resultList = query.getResultList();
	            Player player;
	            for (Object[] arr : resultList) {
	                if (arr[0] == null) {
	                    continue;
	                }
	                resRankPlayer = new ResRankPlayer();
	                player = playerService.get(((BigInteger) arr[0]).longValue());
	                if (player == null) {
	                    continue;
	                }
	                resRankPlayer.setPlayerId(player.getId().toString());
	                resRankPlayer.setNickName(player.decodeName());
	                if(arr[1] == null) {
	                    resRankPlayer.setAchievement(0);
	                } else {
	                	Integer achievenment = 0;
	                	if (gameCatalogConfig.getSelectionCount() == 0 ) {
	                		achievenment = 100;
						}else {				
							achievenment = ((Integer) arr[1]*100)/gameCatalogConfig.getSelectionCount();
							if (achievenment > 100) {
								achievenment = 100;
							}
						}
	                    resRankPlayer.setAchievement(achievenment);
	                }
	                resRankPlayer.setAvatarUrl(player.getAvatarUrl());
	                resRankPlayer.setLastAddAchievementTime(player.getLastAddExplorationTime());
	                resRankPlayers.add(resRankPlayer);
	            }
	            result.put(gameCatalogConfig.getId(), resRankPlayers);
	        }
	        resEachExplorationRank.setRank(result);
	        JedisUtils.getInstance().set(AppCache.eachExplorationList, resEachExplorationRank);
	        return resEachExplorationRank;
	    }
	 public Map<Integer, String> getAllScore() {
	        Map<Integer, String> resultMap = new HashMap<>();
//	        List<GameCatalog_Json> list = configResourceRegistry.getConfig(GameCatalog_Json.class).getList();
	        List<GameCatalog_Json> list =JsonCacheManager.getCache(GameCatalogCache.class).getList();
	        for (GameCatalog_Json gameCatalogConfig : list) {
	            Query query = em.createNativeQuery("SELECT SUM(score) FROM score WHERE game_id = ? AND score != 0");
	            query.setParameter(1, gameCatalogConfig.getId());
	            Object result = query.getSingleResult();

	            query = em.createNativeQuery("SELECT COUNT(score) FROM score WHERE game_id = ? AND score != 0");
	            query.setParameter(1, gameCatalogConfig.getId());
	            Object count = query.getSingleResult();
	            if(result == null) {
	                resultMap.put(gameCatalogConfig.getId(), "10");
	            } else {
	                BigDecimal resultDecimal = (BigDecimal) result;
	                resultMap.put(gameCatalogConfig.getId(),
	                        resultDecimal.add(new BigDecimal(5)).multiply(new BigDecimal(2))
	                                .divide(BigDecimal.valueOf(((BigInteger)count).longValue() + 1),
	                                        1, BigDecimal.ROUND_HALF_UP).stripTrailingZeros().toPlainString());
	            }

	        }
	        JedisUtils.getInstance().set(AppCache.gameScore, resultMap);
	        JedisUtils.getInstance().set(AppCache.gameScoreExpiration, System.currentTimeMillis() + TimeUtil.ONE_HOUR);
	        return resultMap;
	    }
	 
	 private void sortRank(List<ResRankPlayer> resRankPlayerList) {
	        resRankPlayerList.sort((a, b) -> {
	            if(a.getAchievement() == b.getAchievement()) {
	                int lastA = Integer.MAX_VALUE;
	                int lastB = Integer.MAX_VALUE;
	                if(a.getLastAddAchievementTime() != null) {
	                    lastA = a.getLastAddAchievementTime().intValue();
	                }
	                if(b.getLastAddAchievementTime() != null) {
	                    lastB = b.getLastAddAchievementTime().intValue();
	                }
	                return lastA - lastB;
	            }
	            return b.getAchievement() - a.getAchievement();
	        });
	        for (int i = 0; i < resRankPlayerList.size(); i++) {
	            resRankPlayerList.get(i).setRank(i + 1);
	        }
	    }
}
