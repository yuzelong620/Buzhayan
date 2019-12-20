package com.kratos.game.herphone.rank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.rank.bean.ResClueRank;
import com.kratos.game.herphone.rank.bean.ResListRank;
import com.kratos.game.herphone.rank.entity.CluesRankEntity;


@Service
public class CluesRankService extends BaseService{
	
	/**查看自己的排名和线索值和排行榜*/
	public ResListRank getMyselfRank() {
		ResListRank resListClueRank = new ResListRank();
		Player player = PlayerContext.getPlayer();
		int page = 1;
		List<ResClueRank> list = listCluesRank(page);
		CluesRankEntity cluesRankEntity = cluesRankDao.getMyselfRank(player.getId());
		if (cluesRankEntity == null) {
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
			resListClueRank.setValue(playerDynamicEntity.getClue());
			resListClueRank.setMyselfrank(-1);
			resListClueRank.setList(list);
			return resListClueRank;
		}
		resListClueRank.setValue(cluesRankEntity.getPlayerDynamicEntity().getClue());
		resListClueRank.setMyselfrank(cluesRankEntity.getId());
		resListClueRank.setList(list);
		return resListClueRank;	
	}
	/**查看排行榜*/
	public List<ResClueRank> listCluesRank(int page) {			
		int count = 50;
		List<CluesRankEntity> list = cluesRankDao.listCluesRank(page, count);
		List<ResClueRank> resClueRanks = new ArrayList<>();
		if (list.size() == 0||list == null) {
			return resClueRanks;
		}
		for (CluesRankEntity cluesRankEntity : list) {
			ResClueRank resClueRank = new ResClueRank(cluesRankEntity);
			resClueRanks.add(resClueRank);
		}
		return resClueRanks;
	}		
}
