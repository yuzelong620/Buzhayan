package com.kratos.game.herphone.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.CostCurrency_Json;
import com.globalgame.auto.json.Item_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.CostCurrencyCache;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.order.bean.ReqOrderBean;
import com.kratos.game.herphone.order.entity.OrderEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class OrderService extends BaseService{
	/**购买商品*/
	public void buyItem(ReqOrderBean reqOrderBean) {
		Item_Json item_Json= JsonCacheManager.getCache(ItemCache.class).getData(reqOrderBean.getItemId());
		if (item_Json == null) {
			throw new BusinessException("参数错误");
		}
		Player player = PlayerContext.getPlayer();
		BagEntity bagEntity = bagService.load(player.getId());
		PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(player.getId());
		CostCurrency_Json costCurrency_Json = JsonCacheManager.getCache(CostCurrencyCache.class).getData(reqOrderBean.getItemId());
		if (costCurrency_Json == null) {
			throw new BusinessException("参数错误");
		}
		//判断背包里有没有这个id
		Integer item = bagEntity.getBagItems().get(reqOrderBean.getItemId());
		int cost = costCurrency_Json.getCostNum() * reqOrderBean.getItemNum();
		if (item == null || item == 0) {
			//判断购买数量是否大于最大值
			if (item_Json.getMax() < reqOrderBean.getItemNum()) {
				throw new BusinessException("购买次数上限");
			}	
			creatOrderAndAddItem(reqOrderBean,bagEntity,cost,playerDynamicEntity,player,item_Json);
		}else {
			//背包有这个id
			//查看购买数量+背包里的数量是否大于最大值
			if ((item_Json.getMax() -item )< reqOrderBean.getItemNum() ) {
				throw new BusinessException("本商品只能购买 "+ item_Json.getMax() + "次");
			}
			creatOrderAndAddItem(reqOrderBean,bagEntity,cost,playerDynamicEntity,player,item_Json);
		}
	}
	//生成订单 并 添加商品
	private void creatOrderAndAddItem(ReqOrderBean reqOrderBean,BagEntity bagEntity,int cost,PlayerDynamicEntity playerDynamicEntity,
			Player player, Item_Json item_Json ) {
		//判断钱够不够买
		if (playerDynamicEntity.getCurrency() < cost) {
			throw new BusinessException("剩余眨眼星币不足");
		}
		//生成订单
		OrderEntity orderEntity = new OrderEntity();
		orderEntity.setId(UUID.randomUUID().toString());
		orderEntity.setPlayerId(playerDynamicEntity.getPlayerId());
		orderEntity.setItemId(reqOrderBean.getItemId());
		orderEntity.setCost(cost);
		orderEntity.setCurrentTime(System.currentTimeMillis());
		orderDao.save(orderEntity);		
		//添加商品
		Integer sum = bagEntity.getBagItems().get(reqOrderBean.getItemId());
		if(sum == null) {
			sum = 0;
		}
		//物品id
		int itemId = reqOrderBean.getItemId();
		if(itemId == 1002){ //聚能环  直接使用
			int extra_power_limit=0;
			extra_power_limit += item_Json.getValue();
			player.setExtraPowerLimit(extra_power_limit);
		} 
		if(itemId == 1003){ //沙漏 直接使用
			player.setExtraRecoverRote(item_Json.getValue());
		} 
		sum = sum + reqOrderBean.getItemNum();
		bagEntity.getBagItems().put(reqOrderBean.getItemId(), sum);
		bagDao.save(bagEntity);
		
		playerService.cacheAndPersist(player.getId(), player);//保存更新
		//扣除货币
		playerDynamicDao.updateCurrency(playerDynamicEntity.getPlayerId(),0-cost);
	}
}
