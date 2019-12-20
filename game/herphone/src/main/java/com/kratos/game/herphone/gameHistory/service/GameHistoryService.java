package com.kratos.game.herphone.gameHistory.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.gameHistory.entity.GameHistoryEntity;
import com.kratos.game.herphone.gameHistory.entity.PlayHistoryEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
@Service
public class GameHistoryService extends BaseService{
	
	/**
	 * 添加玩家历史记录
	 */
	public void addHistory(String gameId) {
		//获取用户信息
		 Player player=PlayerContext.getPlayer();
		 //获取用户Id
		 long playerId = player.getId();
		 //long playerId = 2815133990854000640L;
		 //查询该用户历史信息
		 GameHistoryEntity gameHistory = load(playerId);
		 //封装历史记录,key:gameId   value:当前时间戳
		 Map<Integer, Long> map = gameHistory.getGameHistory();
		 long time = System.currentTimeMillis();
		 map.put(Integer.parseInt(gameId), time);
		 gameHistory.setGameHistory(map);
		 gameHistoryDao.save(gameHistory);
	}
	
	/**
	 * 根据玩家Id获取玩家历史副本记录
	 * @param id
	 * @return
	 */
	public List<Integer> findHistoryByPlayerId(String playerId){
		long id = Long.parseLong(playerId);
		GameHistoryEntity gameHistory = load(id);
		List<Integer> gameList = sortMap(gameHistory.getGameHistory());
		return gameList;
	}
	
	/**
	 * 将map按照value值降序排序
	 * @param playerId
	 * @return
	 */
	public List<Integer> sortMap(Map<Integer,Long> map){
		List<Entry<Integer,Long>> tool = new ArrayList<Entry<Integer,Long>>(map.entrySet());
		Collections.sort(tool, new Comparator<Map.Entry<Integer,Long>>() {  
		    public int compare(Map.Entry<Integer,Long> o1,  
		            Map.Entry<Integer,Long> o2) {  
		        return (int) (o2.getValue() - o1.getValue());  
		    }  
		});  
		List<Integer> list = new ArrayList<Integer>();
		for (int i = 0; i <tool.size(); i++) {
			list.add(tool.get(i).getKey());
		}
		return list;
	}
	
	
	public GameHistoryEntity load(long playerId) {
		GameHistoryEntity gameHistory = gameHistoryDao.findByID(playerId);
		if(gameHistory == null) {
			gameHistory = new GameHistoryEntity();
			gameHistory.setPlayerId(playerId);
			gameHistoryDao.save(gameHistory);
		}
		return gameHistory;
	}
	/**
	 * 增加剧本播放次数
	 * @param gameId
	 */
	public void addGameShowNum(int gameId) {
		// TODO Auto-generated method stub
		PlayHistoryEntity playHistory = playHistoryDao.findByID(gameId);
		if(playHistory == null) {
			playHistory = new PlayHistoryEntity();
			playHistory.setGameId(gameId);
			playHistory.setShowNum(1);
			playHistoryDao.save(playHistory);
		}else {
			playHistoryDao.incShowNum(gameId, 1);
		}
	}

	public int findGameShowNum(int gameId) {
		PlayHistoryEntity playHistory = playHistoryDao.findByID(gameId);
		if(playHistory == null) {
			playHistory = new PlayHistoryEntity();
			playHistory.setGameId(gameId);
			playHistory.setShowNum(0);
			playHistoryDao.save(playHistory);
		}
		return playHistory.getShowNum();
	}
	
	public List<PlayHistoryEntity> findAll(){
		return playHistoryDao.findAll();
	}
}
