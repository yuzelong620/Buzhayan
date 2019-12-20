package com.kratos.game.herphone.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.clue.entity.PlayerClueEntity;
import com.kratos.game.herphone.common.bean.PlayerExtraBean;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.mind.core.util.IntTuple;

@Service
public class CommonService extends BaseService {
	
	public static final String exp_key = "exp";// 经验
	public static final String currency_key = "currency"; // 眨眼星币
   
	public static final int exp = 2002;// 经验
	public static final int currency = 2001; // 眨眼星币
	
	public static final int key_type_property=2;
	
	/**
	 * 取模1000 获得大的类型
	 * @param key
	 * @return
	 */
	public int  getType(int key){
		return key/1000;
	}

	/**
	 * 添加方法
	 * 
	 * @param list
	 */
	public void add(long playerId,List<IntTuple> list) {
       HashMap<String,Integer> propertys=new HashMap<>();
       HashMap<Integer,Integer> items=new HashMap<>();
       for(IntTuple obj:list){
    	   switch(getType(obj.getKey())){
    	   case key_type_property:
    		   addPropery(propertys,obj.getKey(),obj.getValue());
    		   break;
    	   default:
    		   addItem(items,obj.getKey(),obj.getValue());
    		   break;
    	   }
       }
       if(propertys.size()>0){
    	   playerDynamicDao.updateMoney(playerId, propertys);
       }
       if(items.size()>0){
    	   bagDao.updateNum(playerId, items);
       }
	}

	private void addItem( HashMap<Integer, Integer> map, int key, int value) {
		Integer num=map.get(key);
		if(num==null){
			num=0;
		}
		num+=value;
		map.put(key, value);
	}

	private void addPropery(HashMap<String, Integer> map, int key, int value) {
		switch(key){
		case exp:
			addValue(map, exp_key,value);
			break;
		case currency:
			addValue(map,currency_key, value); 
			break;
			default:
				throw new RuntimeException("無法識別的類型");
		}
	}

	private void addValue(HashMap<String, Integer> map,String key, int value) {
		Integer num=map.get(key);
		if(num==null){
			num=0;
		}
		num+=value;
		map.put(key,num);
	}
 

	/**
	 * 添加方法
	 * 
	 * @param list
	 */
	public void cost(long playerId,List<IntTuple> list) {

	}
	
	/**
	 * 檢查小號的資源是否 足夠
	 * @return
	 */
	public boolean checkCost(long playerId,List<IntTuple> list){
		return false;
	}

	/**
	 * 重新设置额外加成
	 * 
	 * @param player
	 * @param playerDynamic
	 */
	public void resetPlayerExtra(Player player, PlayerDynamicEntity playerDynamic) {
		BagEntity bag = bagService.load(player.getId());
		resetPlayerExtra(player, playerDynamic, bag);
	}

	public void resetPlayerExtra(Player player, BagEntity bag) {
		PlayerDynamicEntity playerDynamic = playerDynamicService.load(player.getId());
		resetPlayerExtra(player, playerDynamic, bag);
	}

	public void resetPlayerExtra(Player player, PlayerDynamicEntity playerDynamic, BagEntity bag) {
		PlayerExtraBean temp = new PlayerExtraBean();

		// 添加背包相关
		PlayerExtraBean bagInfo = bagService.getExtra(bag);
		temp.setExtraPowerLimit(temp.getExtraPowerLimit() + bagInfo.getExtraPowerLimit());
		temp.setExtraRecoverRote(temp.getExtraRecoverRote() + bagInfo.getExtraRecoverRote());

		// 添加称号的相关---------------
		PlayerExtraBean dynamicInfo = playerDynamicService.getExtra(playerDynamic);

		temp.setExtraPowerLimit(temp.getExtraPowerLimit() + dynamicInfo.getExtraPowerLimit());
		temp.setExtraRecoverRote(temp.getExtraRecoverRote() + dynamicInfo.getExtraRecoverRote());

		// 重新设置player
		player.setExtraPowerLimit(temp.getExtraPowerLimit());
		player.setExtraRecoverRote(temp.getExtraRecoverRote());

		playerService.cacheAndPersist(player.getId(), player);
	}

	/** 重新计算探索 */
	public int recountClues(Player player, PlayerDynamicEntity playerDynamicEntity, PlayerClueEntity playerClue) {
		int sumClue = playerClueService.getClueValue(playerClue);
		playerDynamicDao.setClue(player.getId(), sumClue); // 保存到数据库
		return sumClue;
	}

	/** 重新计算成就 */
	public int recountAchievement(Player player, PlayerDynamicEntity playerDynamicEntity) {
		int achievement = achievementService.getAchievementNum(player.getId());
		playerDynamicDao.setAchievementDebris(player.getId(), achievement);
		return achievement;
	}

}
