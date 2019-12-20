package com.kratos.game.herphone.homePage.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.gameHistory.entity.PlayHistoryEntity;
import com.kratos.game.herphone.homePage.bean.GamePageNum;
import com.kratos.game.herphone.homePage.bean.ResHomePageInfo;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class HomePageService extends BaseService {
	
	
	/**
	 * 获取主页信息：
	 * 			1.玩家金币
	 * 			2.剧本播放数量
	 * 			3.剧本评论数量
	 * 			4.电量值
	 * 			5.电量上限
	 */
	
	public ResHomePageInfo getPageInfo() {
		ResHomePageInfo pageInfo = new ResHomePageInfo();
		pageInfo.setGamePageNum(new ArrayList<GamePageNum>());
		
		 //获取用户信息
		 Player player=PlayerContext.getPlayer();
		 //获取玩家金币信息
		 PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		 if(playerDynamicEntity == null){ //是游客
			 pageInfo.setMoneyNum(0);
		 } else {
			 //将玩家金币存入实体类
			 pageInfo.setMoneyNum(playerDynamicEntity.getCurrency());
		 }
		 //回复电量接口
		 playerService.recoverPower(player);
		 //获取玩家电量
		 pageInfo.setPower(player.getPower());
		 //获取玩家电量上限
		 pageInfo.setPowerLimit(player.getExtraPowerLimit() + 100);
		 //获取所有副本播放次数
		 List<PlayHistoryEntity> playHistorys = gameHistoryService.findAll();
		 //循环所有副本
		 for (PlayHistoryEntity playHistoryEntity : playHistorys) {
			 //实例化游戏页面数据对象
			GamePageNum gamePageNum = new GamePageNum();
			//通过副本ID查询该副本被评论次数
			int num = discussOptionService.load(playHistoryEntity.getGameId());
			//设置副本ID
			gamePageNum.setGameId(playHistoryEntity.getGameId());
			//设置副本播放次数
			gamePageNum.setPlayNum(playHistoryEntity.getShowNum());
			//设施副本评论次数
			gamePageNum.setDiscussNum(num);
			//追加副本实体
			pageInfo.getGamePageNum().add(gamePageNum);
		}
		 return pageInfo;
	}
}
