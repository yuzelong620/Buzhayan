package com.kratos.game.herphone.rank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.rank.bean.AchievementRankAndMyRanKing;
import com.kratos.game.herphone.rank.bean.ResAchievementRank;
import com.kratos.game.herphone.rank.bean.ResListRank;
import com.kratos.game.herphone.rank.entity.AchievementRankEntity;


@Service
public class AchievementRankService extends BaseService{
	
	/**查看自己的排名和线索值和排行榜*/
	public ResListRank getMyselfRank() {
		ResListRank resListClueRank = new ResListRank();
		Player player = PlayerContext.getPlayer();
		int page = 1;
		AchievementRankAndMyRanKing achievementRankAndMyRanKing = listAchievementRank(player.getId(),page);
		AchievementRankEntity achievementRankEntity = achievementRankDao.getMyselfRank(player.getId());
//		if (achievementRankEntity == null) {
//			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
//			resListClueRank.setValue(playerDynamicEntity.getAchievementDebris());
//			resListClueRank.setMyselfrank(-1);
//			resListClueRank.setList(list);
//			return resListClueRank;
//		}
		resListClueRank.setValue(achievementRankEntity.getPlayerDynamicEntity().getAchievementDebris());
		resListClueRank.setMyselfrank(achievementRankAndMyRanKing.getMyRanKing());
		resListClueRank.setList(achievementRankAndMyRanKing.getResAchievementRankList());
		return resListClueRank;	
	}
	/**查看排行榜*/
	public AchievementRankAndMyRanKing listAchievementRank(long playerId,int page) {		
		int count = 50;
		List<AchievementRankEntity> list = achievementRankDao.listAchievementRank(page, count);
		List<ResAchievementRank> resAchievementRanks = new ArrayList<>();
		AchievementRankAndMyRanKing achievementRankAndMyRanKing = new AchievementRankAndMyRanKing();
		if (list.size() == 0||list == null) {
			achievementRankAndMyRanKing.setResAchievementRankList(resAchievementRanks);
			return achievementRankAndMyRanKing;
		}
		int myRanKing = 0;
		for (AchievementRankEntity achievementRankEntity : list) {
			myRanKing += 1; 
			ResAchievementRank resAchievementRank = new ResAchievementRank(achievementRankEntity);
			resAchievementRanks.add(resAchievementRank);
			if(playerId == achievementRankEntity.getPlayerDynamicEntity().getPlayerId()){
				achievementRankAndMyRanKing.setMyRanKing(myRanKing);
			}
		}
		achievementRankAndMyRanKing.setResAchievementRankList(resAchievementRanks);
		return achievementRankAndMyRanKing;
	}		
}
