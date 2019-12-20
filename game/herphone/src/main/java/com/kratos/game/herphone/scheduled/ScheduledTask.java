package com.kratos.game.herphone.scheduled;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.rank.entity.AchievementRankEntity;
import com.kratos.game.herphone.rank.entity.CluesRankEntity;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class ScheduledTask extends BaseService{	
	@Autowired
	private ScheduledService scheduledService;
	
	//更新成就排行榜
	@Deprecated
	@Scheduled(fixedDelay = 29 * 60 * 1000)
    public void getRank() {		
		long vtime = JedisUtils.getInstance().ttl(AppCache.achievementList);
		log.info("定时器getRank()方法启动 ，redis的有效时间剩余："+vtime);
		scheduledService.getRank();
    } 
	//更新探索度排行榜
	@Deprecated
	@Scheduled(fixedDelay = 29 * 60 * 1000)
    public void getExploration() {
		long vtime = JedisUtils.getInstance().ttl(AppCache.explorationList);
		log.info("定时器getExploration()方法启动 ，redis的有效时间剩余："+vtime);
		scheduledService.getExploration();
    } 
	//更新每个游戏的探索度前三排行榜
	@Deprecated
	@Scheduled(fixedDelay = 29 * 60 * 1000)
    public void getAllRank() {
		long vtime = JedisUtils.getInstance().ttl(AppCache.eachExplorationList);
		log.info("定时器getAllRank()方法启动 ，redis的有效时间剩余："+vtime);
		scheduledService.getAllRank();
    } 
	//更新评分
	@Scheduled(fixedDelay = 29 * 60 * 1000)
    public void getAllScore() {
		long vtime = JedisUtils.getInstance().ttl(AppCache.gameScore);
		log.info("定时器getAllScore()方法启动 ，redis的有效时间剩余："+vtime);
		scheduledService.getAllScore();
    } 
	//定时更新线索排行
	@Scheduled(fixedDelay =2 * 60 * 60 * 1000)
	public void setCluesRank() {
		List<PlayerDynamicEntity> list = playerDynamicDao.getPlayerDynamicByClue();
		for (int i = 0; i < list.size(); i++) {
			CluesRankEntity cluesRankEntity = new CluesRankEntity();
			cluesRankEntity.setId(i+1);
			cluesRankEntity.setPlayerDynamicEntity(list.get(i));
			cluesRankDao.save(cluesRankEntity);
		}
	 } 
	//定时更新成就排行
	@Scheduled(fixedDelay =2 * 60 * 60 * 1000)
	public void setAchievementRank() {
		List<PlayerDynamicEntity> list = playerDynamicDao.getPlayerDynamicByAchievementDebris();
		for (int i = 0; i < list.size(); i++) {
			AchievementRankEntity achievementRankEntity = new AchievementRankEntity();
			achievementRankEntity.setId(i+1);
			achievementRankEntity.setPlayerDynamicEntity(list.get(i));
			achievementRankDao.save(achievementRankEntity);
		}
	} 
	/*//更新护眼大队称号达成玩家
	@Scheduled(initialDelay=1 * 60 * 1000,fixedDelay  = 60 * 60 * 1000)
    public void updateTitile() {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		//获取探索度前五十玩家id
		List<ResRankPlayer> resRankPlayerList = JedisUtils.getInstance().getList(AppCache.explorationList, ResRankPlayer.class);
		//获取成就值前五十玩家id
		List<ResRankPlayer> resRankPlayerList1 = JedisUtils.getInstance().getList(AppCache.achievementList, ResRankPlayer.class);	
		List<Long> playerIds = new ArrayList<Long>();		
		//获取满足条件的玩家id
		for (ResRankPlayer resRankPlayer : resRankPlayerList) {
			for (ResRankPlayer resRankPlayer1 : resRankPlayerList1) {			
					if (resRankPlayer.getPlayerId() .equals(resRankPlayer1.getPlayerId())) {
						playerIds.add(Long.valueOf(resRankPlayer1.getPlayerId()));
				}
			}
		}
		int size = playerIds.size();
		List<Long> playerIdList = new ArrayList<Long>(size);
		//获取满足在线时间玩家的id
		playerIdList = playerOnlineService.getPlayerIds(playerIds);
		//获取玩过10个游戏以上的玩家
		playerIds = playerExplorationService.listPlayerId(playerIdList);
		//玩家粉丝数量大于80人
		List<PlayerDynamicEntity> playerDynamiclist = playerDynamicDao.getFansCoutGte(playerIds);
		//获取热评数量大于50的人	
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamiclist) {
			if (playerDynamicEntity.getHotDiscussCount() >= gameParams_Json.getTitleHotDiscussCount().intValue()) {
					if (!playerDynamicEntity .getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
						playerDynamicEntity.getItemTitle().add(gameParams_Json.getProtectEyesTitleId());
						Player player =playerService.get(playerDynamicEntity.getPlayerId());
						commonService.resetPlayerExtra(player, playerDynamicEntity);								
						playerDynamicDao.save(playerDynamicEntity);
					}
			}
		}
	}	*/
	
}
