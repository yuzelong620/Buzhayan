package com.kratos.game.herphone.playerOnline.service;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.playerOnline.entity.PlayerLoginTimeEntity;
import com.kratos.game.herphone.util.DateUtil;

@Service
public class PlayerLoginTimeService extends BaseService{
	
	public PlayerLoginTimeEntity load(long playerId) {
		PlayerLoginTimeEntity playerLoginTimeEntity  = playerLoginTimeDao.findByID(playerId);
		if (playerLoginTimeEntity == null) {
			playerLoginTimeEntity = new PlayerLoginTimeEntity();
			playerLoginTimeEntity.setPlayerId(playerId);
			playerLoginTimeEntity.setLastLoginTime(System.currentTimeMillis());
			playerLoginTimeEntity.setTotalLogin(1);
			playerLoginTimeEntity.setMaxLogin(1);
			playerLoginTimeEntity.setContinuousLogin(1);
			playerLoginTimeDao.save(playerLoginTimeEntity);
		}
		return playerLoginTimeEntity;
	}
	public void updatePlayerLogin(long playerId) {
		PlayerLoginTimeEntity playerLoginTimeEntity = load(playerId);
		long nowTime = System.currentTimeMillis();	
		if (!DateUtil.isSameDate(nowTime,playerLoginTimeEntity.getLastLoginTime())) {
			if (DateUtil.ComparingDate(-1, playerLoginTimeEntity.getLastLoginTime())) {
				//昨天登录过
				int ContinuousLogin = playerLoginTimeEntity.getContinuousLogin();
				playerLoginTimeEntity.setContinuousLogin(ContinuousLogin + 1);
				if ((ContinuousLogin + 1) > playerLoginTimeEntity.getMaxLogin()) {
					playerLoginTimeEntity.setMaxLogin(ContinuousLogin + 1);
				}
				
			}else {
				playerLoginTimeEntity.setContinuousLogin(1);
			}
			playerLoginTimeEntity.setLastLoginTime(nowTime);
			playerLoginTimeEntity.setTotalLogin(playerLoginTimeEntity.getTotalLogin() + 1 );
			playerLoginTimeDao.save(playerLoginTimeEntity);
		}
	}
}
