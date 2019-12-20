package com.kratos.game.herphone.bag.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.Item_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.bag.bean.BagInfo;
import com.kratos.game.herphone.bag.bean.ItemBean;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.ItemType;
import com.kratos.game.herphone.common.bean.PlayerExtraBean;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.service.PlayerServiceImpl;

@Service
public class BagService extends BaseService {

	public BagEntity load(long id) {
		BagEntity bag = bagDao.findByID(id);
		if (bag == null) {
			bag = new BagEntity();
			bag.setPlayerId(id);
			bagDao.save(bag);
			//测试代码
//		 ArrayList<ItemBean> list=new ArrayList<>();
//		 list.add(new ItemBean(1001, 1));
//		 list.add(new ItemBean(1002, 1));
//		 list.add(new ItemBean(1003, 1));
//	     add(list);
		}
		return bag;
	}

	public BagInfo getBagInfo() {
		Player player = PlayerContext.getPlayer();
		BagEntity bag = load(player.getId());
		List<ItemBean> items = new ArrayList<>();
		for (Entry<Integer, Integer> entry : bag.getBagItems().entrySet()) {
			items.add(new ItemBean(entry.getKey(), entry.getValue()));
		}
		return new BagInfo(items);
	}
	/**
	 *使用一次，添加体力的道具
	 */
	public void  useAddPowerValueItem(int itemId,int itemNum){
		ItemCache cache=JsonCacheManager.getCache(ItemCache.class);
		Item_Json json=cache.getData(itemId);
		if(itemNum>999||itemNum<1){
	    	throw new BusinessException("物品数量错误！");
	    }
	    if(json==null){
	    	throw new BusinessException("物品id错误！");
	    }
		Player player = PlayerContext.getPlayer();
		BagEntity bag = load(player.getId());
		Integer num=bag.getBagItems().get(itemId);
        if(num<itemNum){
        	throw new BusinessException("物品数量不足！");
		}
        if(json.getType()==ItemType.addPowerValue.ordinal()){//添加体力道具
    	   int value= (int)(json.getValue()*itemNum);
    	   player.addPower(value,true);
    	   playerServiceImpl.cacheAndPersist(player.getId(),player);
        }
        else{
    	   throw new BusinessException("道具无法使用！");
        }       
        //保存物品个数
        num-=itemNum;
        if(num==0){
        	bag.getBagItems().remove(itemId);
        }
        else{
        	bag.getBagItems().put(itemId,num);
        }
        bagDao.save(bag);
	}
	
	public void add(List<ItemBean> items){
		Player player = PlayerContext.getPlayer(); 
		BagEntity bag = load(player.getId());
		ItemCache cache=JsonCacheManager.getCache(ItemCache.class);
		boolean exitPowerItem=false;
		for(ItemBean item:items){
			Item_Json json=cache.getData(item.getItemId());
			if(json==null){
				throw new RuntimeException("无法添加不存在的道具id:"+item.getItemId());
			}
			
			Integer num=bag.getBagItems().get(item.getItemId());
			if(num==null){
				num=0;
			}
			//限制最大叠加数量
			if(num>=json.getMax()){
				continue;
			}
			num+=item.getItemNum();
			if(num>=json.getMax()){
				num=json.getMax();
			}
			bag.getBagItems().put(item.getItemId(), num);
			//需要重置，玩家体力加成的道具
			if(json.getType()==ItemType.addPowerMax.ordinal()
					||json.getType()==ItemType.addPowerStep.ordinal()){
				exitPowerItem=true;
			} 
		}
		if(exitPowerItem){			
			commonService.resetPlayerExtra(player, bag);
			resetItemExtra(player, bag, cache); 
			playerServiceImpl.cacheAndPersist(player.getId(),player);
		}
		bagDao.save(bag);
	}
	
	
	public PlayerExtraBean  getExtra(long playerId){
		BagEntity bag=load(playerId);
		return getExtra(bag);
	}
	public PlayerExtraBean  getExtra(BagEntity bag){
		double extra_recover_rote=0;
		int    extra_power_limit=0;
		ItemCache cache=JsonCacheManager.getCache(ItemCache.class);
		for(Entry<Integer, Integer> entry:bag.getBagItems().entrySet()){
			Item_Json json=cache.getData(entry.getKey());
			if(json.getType()==ItemType.addPowerMax.ordinal()){
				extra_power_limit+=json.getValue();
			}
			else if(json.getType()==ItemType.addPowerStep.ordinal()){
				extra_recover_rote+=json.getValue();
			}
		}
		return new PlayerExtraBean(extra_power_limit, extra_recover_rote);
	}
    /**
     * @deprecated
     * 重设物品加成
     * @param player
     * @param bag
     * @param cache
     */
	private void resetItemExtra(Player player, BagEntity bag, ItemCache cache ) {
		double extra_recover_rote=0;
		int    extra_power_limit=0;
		for(Entry<Integer, Integer> entry:bag.getBagItems().entrySet()){
			Item_Json json=cache.getData(entry.getKey());
			if(json.getType()==ItemType.addPowerMax.ordinal()){
				extra_power_limit+=json.getValue();
			}
			else if(json.getType()==ItemType.addPowerStep.ordinal()){
				extra_recover_rote+=json.getValue();
			}
		}
		//修改加成 并保存
		player.setExtraPowerLimit(extra_power_limit);
		player.setExtraRecoverRote(extra_recover_rote); 
	}
	

}
