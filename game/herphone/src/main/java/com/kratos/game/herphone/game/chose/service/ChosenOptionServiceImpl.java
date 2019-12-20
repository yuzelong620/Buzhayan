package com.kratos.game.herphone.game.chose.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.engine.framework.common.utils.IdGenerator;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.engine.framework.crud.Param;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.game.chose.domain.ChosenOption;
import com.kratos.game.herphone.game.chose.domain.PlayerExploration;
import com.kratos.game.herphone.game.chose.message.ReqPlayerChose;
import com.kratos.game.herphone.game.chose.message.ResEachExplorationRank;
import com.kratos.game.herphone.game.chose.message.ResGameProgress;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.player.service.PlayerService;
import com.kratos.game.herphone.recentGame.service.RecentGameService;
import com.kratos.game.herphone.scheduled.ScheduledService;
import com.kratos.game.herphone.util.StringUtil;

import lombok.extern.log4j.Log4j;

@Log4j
@Service
public class ChosenOptionServiceImpl extends BaseCrudService<Long, ChosenOption> implements ChosenOptionService {
//    @Autowired
//    private ConfigResourceRegistry configResourceRegistry;
    @Autowired
    private PlayerExplorationService playerExplorationService;
    @Lazy
    @Autowired
    private PlayerService playerService;
    @Autowired
    private RecentGameService recentGameService;
    @Autowired
	private ScheduledService scheduledService;

    @Override
    public void chose(ReqPlayerChose request) {
        Player player = PlayerContext.getPlayer();
        String key = StringUtil.appendString(player.getId(),request.getGameId(),request.getChatId(),request.getTalkId());
        boolean b = JedisUtils.getInstance().setexpire(key, "1", 30);
        if (!b) {
        	log.error("key存在 ,set失败"+key);
			return;
		}
        ChosenOption chosenOption = new ChosenOption();
        chosenOption.setId(IdGenerator.getNextId());
        chosenOption.setGameId(request.getGameId());
        chosenOption.setPlayerId(player.getId());
        chosenOption.setChatId(request.getChatId());
        chosenOption.setTalkId(request.getTalkId());
        chosenOption.setOptionIndex(request.getOptionIndex());
        List<ChosenOption> chosenOptions = findByParams(
                Param.equal("playerId", player.getId()),
                Param.equal("gameId", request.getGameId()),
                Param.equal("chatId", request.getChatId()),
                Param.equal("talkId", request.getTalkId())
        );
        recentGameService.updateRecentGameEntity(player. getId(), chosenOption.getGameId());
        if(chosenOptions != null && !chosenOptions.isEmpty()) {
            return;
        }
        try {
            // 有新选项则修改用户探索度
            this.editPlayerExploration(player, chosenOption);
        } catch (Exception e) {
            log.error("修改用户探索度", e);
        }
        this.cacheAndPersist(chosenOption.getId(), chosenOption);
    }

    @Override
    public ResGameProgress getAllProgress() {
        ResGameProgress resGameProgress = new ResGameProgress();
//        List<GameCatalog_Json> list = configResourceRegistry.getConfig(GameCatalogConfig.class).getList();
        List<GameCatalog_Json> list =JsonCacheManager.getCache(GameCatalogCache.class).getList();
        Map<Integer, Long> result = new HashMap<>();
        for (GameCatalog_Json gameCatalogConfig : list) {
            result.put(gameCatalogConfig.getId(), this.getProgress(gameCatalogConfig.getId()));
        }
        resGameProgress.setProgress(result);
        return resGameProgress;
    }

    @SuppressWarnings("unchecked")
    public ResEachExplorationRank getAllRankOld() {
        ResEachExplorationRank resEachExplorationRank = JedisUtils.getInstance().get(AppCache.eachExplorationList, ResEachExplorationRank.class);
        if (resEachExplorationRank != null) {
            Player player = PlayerContext.getPlayer();

            resEachExplorationRank.getRank().forEach((key, list) -> {
                int explorationRate = 0;
                List<PlayerExploration> playerExplorations = playerExplorationService.findByParams(
                        Param.equal("playerId", player.getId()), Param.equal("gameId", key));
                if(playerExplorations != null && !playerExplorations.isEmpty()) {
                    explorationRate = playerExplorations.get(0).getExplorationRate();
                }
                if(list.parallelStream()
                        .anyMatch(resRankPlayer -> Long.valueOf(resRankPlayer.getPlayerId()) == player.getId())) {
                    for (ResRankPlayer resRankPlayer : list) {
                        if(Long.valueOf(resRankPlayer.getPlayerId()) == player.getId()) {
                            resRankPlayer.setAchievement(explorationRate);
                            resRankPlayer.setLastAddAchievementTime(player.getLastAddExplorationTime());
                            break;
                        }
                    }
                    list.sort((a, b) -> b.getAchievement() - a.getAchievement());
                } else if(list.size() > 0 && list.get(list.size() - 1).getAchievement() < explorationRate) {
                    ResRankPlayer resRankPlayerTemp = new ResRankPlayer(player, 0);
                    resRankPlayerTemp.setAchievement(explorationRate);
                    resRankPlayerTemp.setLastAddAchievementTime(player.getLastAddExplorationTime());
                    list.add(resRankPlayerTemp);
                    list.sort((a, b) -> b.getAchievement() - a.getAchievement());
                }
            });
            return resEachExplorationRank;
        }
        resEachExplorationRank = new ResEachExplorationRank();
        Map<Integer, List<ResRankPlayer>> result = new HashMap<>();
//        List<GameCatalog_Json> list = configResourceRegistry.getConfig(GameCatalogConfig.class).getList();
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
                	Integer achievenment = ((Integer) arr[1]*100)/gameCatalogConfig.getSelectionCount();
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

    @Override
    public Long getProgress(int gameId) {
        Player player = PlayerContext.getPlayer();
        List<ChosenOption> chosenOptions = findByParams(
                Param.equal("playerId", player.getId()),
                Param.equal("gameId", gameId)
        );
        if(chosenOptions != null) {
            return Long.valueOf(chosenOptions.size() + "");
        }
        return null;
    }

    public void editPlayerExploration(Player player, ChosenOption chosenOption) {
    	GameCatalogCache cache=JsonCacheManager.getCache(GameCatalogCache.class);
    	GameCatalog_Json json=cache.getData(chosenOption.getGameId());
    	if(json==null) {
    		log.error("gameId不存在："+chosenOption.getGameId());
    		throw new BusinessException("Gameid不存在：");
    	}
        ResGameProgress resGameProgress = this.getAllProgress();
//        List<GameCatalog_Json> list = configResourceRegistry.getConfig(GameCatalogConfig.class).getList();
        List<GameCatalog_Json> list = JsonCacheManager.getCache(GameCatalogCache.class).getList();
        Long count;
        int total = 0;
        int singleCount;
        PlayerExploration playerExploration;
        List<PlayerExploration> result;
        for (GameCatalog_Json gameCatalogConfig : list) {
            count = resGameProgress.getProgress().get(gameCatalogConfig.getId());
            if(gameCatalogConfig.getId().equals(chosenOption.getGameId())) {
                count += 1;
            }
            if (gameCatalogConfig.getSelectionCount() == 0) {
            	singleCount =100;
			}else {
				singleCount = new BigDecimal(count)
						.divide(new BigDecimal(gameCatalogConfig.getSelectionCount()), 2, BigDecimal.ROUND_DOWN)
						.multiply(new BigDecimal(100)).intValue();			
			}
            result = playerExplorationService.findByParams(
                    Param.equal("playerId", player.getId()),
                    Param.equal("gameId", gameCatalogConfig.getId()));
            if(singleCount > 0 && gameCatalogConfig.getId().equals(chosenOption.getGameId())) {
                if(result == null || result.isEmpty()) {
                    playerExploration = new PlayerExploration();
                    playerExploration.setId(IdGenerator.getNextId());
                    playerExploration.setGameId(gameCatalogConfig.getId());
                    playerExploration.setPlayerId(player.getId());
                } else {
                    playerExploration = result.get(0); 
                }
                playerExploration.setExplorationRate(singleCount);
                playerExploration.setExplorationCount(count.intValue());
                playerExploration.setLastAddExplorationTime(System.currentTimeMillis());
                playerExplorationService.persist(playerExploration);
            }
            total += count.intValue();
        }

        player.setExploration(total);
        player.setLastAddExplorationTime(System.currentTimeMillis());
        playerService.cacheAndPersist(player.getId(), player);
    }
    
    @Override
    public ResEachExplorationRank getAllRank() {
        ResEachExplorationRank resEachExplorationRank = JedisUtils.getInstance().get(AppCache.eachExplorationList, ResEachExplorationRank.class);
        if (resEachExplorationRank != null) {
			return resEachExplorationRank;
		}
        log.error("getAllRank()方法未从redis里获取到对象");
        return scheduledService.getAllRank();
    }  
}
