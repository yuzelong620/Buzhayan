package com.kratos.game.herphone.score.service;


import org.springframework.stereotype.Service;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.score.domain.ScoreRewardEntity;

@Service
public class ScoreRewardServiceImpl extends BaseService implements ScoreRewardService{
	
	
	@Override
	//领取奖励     0 添加奖励 1已领取过奖励
	public int jugeGetScoreReward(int gameId) {
		Player player = PlayerContext.getPlayer();
		ScoreRewardEntity scoreRewardEntity = scoreRewardDao.findByID(player.getId());
		if (scoreRewardEntity == null) {
			scoreRewardEntity = new ScoreRewardEntity();
			scoreRewardEntity.setPlayerId(player.getId());
		}
		for (Integer itemgameid:scoreRewardEntity.getLtemGameId()) {
			if ( itemgameid == gameId) {	
				return 1;
			}
		}
		scoreRewardEntity.getLtemGameId().add(gameId);	
		player.addPower(20, true);
		playerServiceImpl.cacheAndPersist(player.getId(), player);
		scoreRewardDao.save(scoreRewardEntity);
		return 0;
	}
	
	//查询是否领取评分奖励 0未领取 1已领取
	public int selectScoreReward(int gameId) {
		Player player = PlayerContext.getPlayer();
		ScoreRewardEntity scoreRewardEntity = scoreRewardDao.findByID(player.getId());
		if (scoreRewardEntity == null) {
			return 0;
		}
		for (Integer itemgameid:scoreRewardEntity.getLtemGameId()) {
			if ( itemgameid == gameId) {	
				return 1;
			}
		}
		return 0;
	}
}

