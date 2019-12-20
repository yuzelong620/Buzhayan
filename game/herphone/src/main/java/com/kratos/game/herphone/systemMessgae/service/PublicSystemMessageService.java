package com.kratos.game.herphone.systemMessgae.service;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.systemMessgae.entity.PublicSystemMessageEntity;

@Service
public class PublicSystemMessageService extends BaseService{
	
	public PublicSystemMessageEntity load(long playerId) {
		PublicSystemMessageEntity publicSystemMessageEntity = publicSystemMessageDao.findByID(playerId);
		if (publicSystemMessageEntity == null) {
			publicSystemMessageEntity = new PublicSystemMessageEntity();
			publicSystemMessageEntity.setPlayerId(playerId);
			publicSystemMessageDao.save(publicSystemMessageEntity);
		}
		return publicSystemMessageEntity;
	}
}
