package com.kratos.game.herphone.box.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.globalgame.auto.json.AchBadge_Json;
import com.globalgame.auto.json.Box_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.achievement.entity.AchievementEntity;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.box.until.RandomUtil;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AchBadgeCache;
import com.kratos.game.herphone.json.datacache.AchDebrisCache;
import com.kratos.game.herphone.json.datacache.BoxCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.dao.PlayerDynamicDao;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.mind.core.util.StringIntTuple;
@Service
public class BoxService extends BaseService{
	String prize = null;
	static final Integer NORMALBOXID = 4001;
	static final Integer SUPERBOXID = 4002;
	static final Integer ACHIEVEMENTBOXID = 4003;
	static final String NORMALHEAD = "1018";
	static final String COLLECTIONHEAD = "1019";
	static final String LEGENDARYHEAD = "1020";
	
	
	public Box_Json openBox(int boxKind){
		BoxCache boxCache = JsonCacheManager.getCache(BoxCache.class);
		List<Box_Json> list =boxCache.getListByBoxKind(boxKind);
		Player player = PlayerContext.getPlayer();
		switch (boxKind) {
		//普通宝箱
		case 1:
			return openNomalBox(boxKind,list,player);
		//超级宝箱
		case 2:
			return openSuperBox(boxKind, list,player);
		//成就宝箱
		case 3:
			return openAchievementBox(boxKind, list,player);
		default:
			throw new BusinessException("宝箱不存在！");
		}
		
	}
	//打开普通宝箱
	public Box_Json openNomalBox(int boxKind,List<Box_Json> list,Player player){
		Box_Json box = randomBox(boxKind, list, player);
		//System.err.println(box);
		addToBag(box,player);
		deBox(boxKind,player);
		return box;
	}
	//打开超级宝箱
	public Box_Json openSuperBox(int boxKind,List<Box_Json> list,Player player){
		Box_Json box = randomBox(boxKind, list, player);
		addToBag(box,player);
		deBox(boxKind,player);
		return box;
	}
	//打开成就宝箱
	public Box_Json openAchievementBox(int boxKind,List<Box_Json> list,Player player){
		Box_Json box = randomBox(boxKind, list, player);
		//判断抽到的是不是徽章,如果抽到徽章,徽章加入背包逻辑在抽取的时候实现,此处不需实现
		addToBag(box,player);
		//讲背包内的宝箱-1
		deBox(boxKind,player);
		return box;
	}
	
	//随机宝物
	public Box_Json randomBox(int boxKind,List<Box_Json> list,Player player ) {
		//获取玩家id
		long playerId = player.getId();
		//获取玩家背包
		BagEntity bagEntity = bagService.load(playerId);
		//获取玩家信息
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
		RandomUtil<Box_Json> randomUtil = getRandomUtil(list);
		Box_Json box=randomUtil.random();
		System.err.println(box.getPrize()+"   "+box.getType());
		Integer boxType = box.getType();
		String boxId = box.getItemId().get(0).getKey();
		switch (boxType) {
		case 5:
			//TODO 获得头像框
			Integer head = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
			if(head != null) {
				return randomBox(boxKind, list, player);
			}else {
				return box;
			}
		case 0:
			//TODO 获得金币
			return box;
		case 8:
			//TODO 获得小徽章
			int badgeId = randomBadge(player);
			if(badgeId != 0) {
				//如果得到徽章,会将奖品实体类中获得奖品ID更新为对应的徽章ID
				StringIntTuple sit = box.getItemId().get(0);
				sit.setKey(badgeId);
				return box;
			}else {
				return randomBox(boxKind, list, player);
			}
		default:
			//TODO 获得背包物品
			//判断该物品啊在背包中是否存在
			Integer item = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
			//如果背包中已存在沙漏或者聚能环，则重新抽奖
			if(item != null && ("1002".equals(box.getItemId().get(0).getKey()) || "1003".equals(box.getItemId().get(0).getKey())) ) {
				System.err.println("存在沙漏或聚能环，重新抽奖");
				return randomBox(boxKind, list, player);
			}else {
				return box;
			}
		}
		/*
		 * if("1111".equals(boxId)) { //TODO 获得是头像框
		 * 
		 * }else if("coin".equals(boxId)) { //TODO 获得金币 return box; }else
		 * if("2222".equals(boxId)) { //TODO 获得成就碎片 }else { //TODO 获得背包物品
		 * //判断该物品啊在背包中是否存在 Integer item =
		 * bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey())
		 * ); //如果背包中已存在沙漏或者聚能环，则重新抽奖 if(item != null &&
		 * ("1002".equals(box.getItemId().get(0).getKey()) ||
		 * "1003".equals(box.getItemId().get(0).getKey())) ) {
		 * System.err.println("存在沙漏或聚能环，重新抽奖"); return randomBox(boxKind, list, player);
		 * }else { return box; } }
		 */
		
	}
	
	//随机抽取徽章碎片
	public int randomBadge(Player player){
		//获取游戏存在的所有小徽章
		AchBadgeCache cache =JsonCacheManager.getCache(AchBadgeCache.class);
		HashMap<Integer,List<Integer>> map = cache.getMap();
		//获取小徽章数量
		int size = map.size();
		//随机获得其中某一个小徽章
		int random=(int)(Math.random()*size+1);
		List<Integer> list = cache.getAchBadgeId(random);
		int little = (int)(Math.random()*list.size());
		Integer badgeId = list.get(little);
		AchievementEntity  achievementEntity = achievementService.load(player.getId());
		//AchievementEntity  achievementEntity = achievementService.load(2815133990854000640L);
		//判断玩家是否拥有该小徽章
		if(achievementEntity.getBadgeIds().contains(badgeId)) {
			return 0;
		}else {
			//查询该小徽章对应的碎片
			AchDebrisCache achDebrisCache = JsonCacheManager.getCache(AchDebrisCache.class);
			List<Integer> achdebrisList = achDebrisCache.getAchDebrisId(badgeId);
			//查询该玩家拥有的碎片
			Set<Integer> achievementIds = achievementEntity.getAchievementIds();
			//将该小徽章碎片加入玩家拥有碎片中
			achievementIds.addAll(achdebrisList);
			//查询玩家拥有的小徽章
			Set<Integer> badgeIds = achievementEntity.getBadgeIds();
			//将该徽章加入玩家拥有徽章中
			badgeIds.add(badgeId);
			//将玩家徽章数据和碎片数据存入数据库
			achievementEntity.setBadgeIds(badgeIds);
			achievementEntity.setAchievementIds(achievementIds);
			achievementDao.save(achievementEntity);
			//将徽章ID返回
			return badgeId;
		}
	}
	
	//将宝物加入背包
	public void addToBag(Box_Json box,Player player){
		//获取玩家背包信息
		Integer boxType = box.getType();
		BagEntity bagEntity = bagService.load(player.getId());
		if(boxType == 5) {
			//TODO 获得是头像框
			bagEntity.getBagItems().put(Integer.parseInt(box.getItemId().get(0).getKey()),1);
			bagDao.save(bagEntity);
		}else if(boxType == 0) {
			//获得金币
			int currency = box.getItemId().get(0).getValue();
			playerDynamicDao.updateCurrency(player.getId(), currency);
		}else if(boxType == 8) {
			//获得成就碎片,在抽取时加入玩家背包
			return;
		}else {
			//获得背包物品
			Integer item = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
			if(item == null){
				bagEntity.getBagItems().put(Integer.parseInt(box.getItemId().get(0).getKey()),box.getItemId().get(0).getValue());
				bagDao.save(bagEntity);
			}else {
				Integer integer = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
				Integer sum = integer + box.getItemId().get(0).getValue();
				bagEntity.getBagItems().put(Integer.parseInt(box.getItemId().get(0).getKey()),sum);
				bagDao.save(bagEntity);
			}
		}
	}
	//消耗宝箱
	public void deBox(int boxKind,Player player){
		//获得用户背包信息
		BagEntity bagEntity = bagService.load(player.getId());
		Integer i = 0;
		Integer sum = 0;
		//判断宝箱类型
		switch (boxKind) {
		//普通宝箱
		case 1:
			i = bagEntity.getBagItems().get(NORMALBOXID);
			if (i == null) {
				i = 0;
			}
			sum = i - 1; 
			bagEntity.getBagItems().put(NORMALBOXID, sum);
			bagDao.save(bagEntity);
			break;
		//超级宝箱
		case 2:
			i = bagEntity.getBagItems().get(SUPERBOXID);
			if (i == null) {
				i = 0;
			}
			sum = i - 1; 
			bagEntity.getBagItems().put(SUPERBOXID, sum);
			bagDao.save(bagEntity);
			break;
		//成就宝箱	
		case 3:
			i = bagEntity.getBagItems().get(ACHIEVEMENTBOXID);
			if (i == null) {
				i = 0;
			}
			sum = i - 1; 
			bagEntity.getBagItems().put(ACHIEVEMENTBOXID, sum);
			bagDao.save(bagEntity);
			break;
		default:
			break;
		}
	}
	//将宝箱物品加入背包
	public void addToBag1(Box_Json box,int boxKind,List<Box_Json> list){
		Player player = PlayerContext.getPlayer();
		long id = 2815133990854000640L;
//		BagEntity bagEntity = bagService.load(player.getId());
		BagEntity bagEntity = bagService.load(id);
		if("1111".equals(box.getItemId().get(0).getKey())) {
			//TODO 获得是头像框
		}else if("9999".equals(box.getItemId().get(0).getKey())) {
			//TODO 获得金币
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
			int currency = box.getItemId().get(0).getValue();
			playerDynamicDao.updateCurrency(id, currency);
			prize = box.getPrize();
		}else if("2222".equals(box.getItemId().get(0).getKey())) {
			//TODO 获得成就碎片
		}else {
			//TODO 获得背包物品
			//判断该物品啊在背包中是否存在
			Integer item = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
			//如果背包中已存在沙漏或者聚能环，则重新抽奖
			if(item != null && ("1002".equals(box.getItemId().get(0).getKey()) || "1003".equals(box.getItemId().get(0).getKey())) ) {
				System.err.println(111);
				//openSuperBox(boxKind,list);
			}else if(item == null){
				bagEntity.getBagItems().put(Integer.parseInt(box.getItemId().get(0).getKey()),box.getItemId().get(0).getValue());
				bagDao.save(bagEntity);
			    prize = box.getPrize();
			}else {
				Integer integer = bagEntity.getBagItems().get(Integer.parseInt(box.getItemId().get(0).getKey()));
				Integer sum = integer + box.getItemId().get(0).getValue();
				bagEntity.getBagItems().put(Integer.parseInt(box.getItemId().get(0).getKey()),sum);
				bagDao.save(bagEntity);
				prize = box.getPrize();
			}
		}
		
	}
	
	
	private RandomUtil<Box_Json> getRandomUtil(List<Box_Json> list) {
		RandomUtil<Box_Json> randomUtil=new RandomUtil<>();
		for(Box_Json json:list){
			randomUtil.add(json.getOdds(), json);
		}
		return randomUtil;
	}
}
