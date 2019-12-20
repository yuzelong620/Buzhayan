package com.kratos.game.herphone.player.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.globalgame.auto.json.Item_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.bag.bean.ItemBean;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.ExchangeRewardEntity;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.domain.RewardEntity;
import com.kratos.game.herphone.util.RandomUtil;

import lombok.extern.log4j.Log4j;

@Log4j
@Component
public class RewardServiceImpl  extends BaseService implements RewardService{

	@Override
	public List<RewardEntity> generateReward(int num,List<ItemBean> itemBeans) {
		ItemCache cache=JsonCacheManager.getCache(ItemCache.class);
		for (ItemBean itemBean : itemBeans) {
			Item_Json json=cache.getData(itemBean.getItemId());
			if (json == null) {
				return null;
			}
		}
		long timeStamp = System.currentTimeMillis();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String ts = simpleDateFormat.format(timeStamp);
		List<RewardEntity> list = new ArrayList<RewardEntity>();
		int n = 1;
		try {
			while (n <= num) {
				RewardEntity reward = new RewardEntity();
				reward.setCode(RandomUtil.generateString(8));
				if (RewardDao.findByID(reward.getCode()) != null) {
					continue;
				}
				reward.setGroupId(timeStamp/1000);
				reward.setState(0);
				reward.setTimeStamp(ts);
				reward.setList(itemBeans);
				RewardDao.save(reward);
				list.add(reward);
				n++;
			}
			return list;
		} catch (Exception e) {
			log.error("err:"+e);
		}			
		return list;
	}

	@Override
	public List<ItemBean> exchangeCode(String code) {
		Player player = PlayerContext.getPlayer();
		RewardEntity reward = RewardDao.findByID(code);
		if (reward == null ) {
			throw new BusinessException("激活码不存在");
		}
		if (reward.getState() == 1) {
			throw new BusinessException("激活码已被领取");
		}
		//查找用户领取奖励列表
		ExchangeRewardEntity exchangeReward = ExchangeRewardDao.findByID(player.getRoleId());
		//没有查到用户 创建对象并添加奖励
		if (exchangeReward == null) {
			exchangeReward = new ExchangeRewardEntity();
			exchangeReward.setRoleid(player.getRoleId());
		}
		//判断该批次奖励是否被领取
		for (long groupId : exchangeReward.getGroupIds()) {
			if (groupId == reward.getGroupId()) {
				throw new BusinessException("不能重复领取");
			}
		}
		List<ItemBean> lItemBeans = reward.getList();
		exchangeReward.getGroupIds().add(reward.getGroupId());
		bagService.add(lItemBeans);
		reward.setState(1);
		RewardDao.save(reward);
		ExchangeRewardDao.save(exchangeReward);
		return lItemBeans;
	}

}
