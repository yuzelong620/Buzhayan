package com.kratos.game.herphone.player.service;

import java.util.List;

import com.kratos.game.herphone.bag.bean.ItemBean;
import com.kratos.game.herphone.player.domain.RewardEntity;

public interface RewardService {
	 /**
     * 生成激活码
     * @param 生成数量
     * @return 激活码
     */
    List<RewardEntity> generateReward(int num,List<ItemBean> list);
    
    /**
     * 兑换激活码
     * @param 激活码，角色id
     * @return 兑换成功  0成功 1失败（已被领取）2（激活码错误）
     */
    List<ItemBean> exchangeCode(String code);
}
