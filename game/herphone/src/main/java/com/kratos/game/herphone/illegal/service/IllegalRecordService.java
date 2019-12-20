package com.kratos.game.herphone.illegal.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.illegal.entity.IllegalRecordEntity;

@Component
public class IllegalRecordService extends BaseService {
	
	/**
	 * 添加违规记录
	 */
	public void addIllegalRecord(int type,long playerId,long bannedDuration) {
		String id = UUID.randomUUID().toString().replace("-", "");
		IllegalRecordEntity illegalRecordEntity = new IllegalRecordEntity(
				id,playerId,type,System.currentTimeMillis(),bannedDuration);
		illegalRecordDao.save(illegalRecordEntity);
	}
	
	/**
	 * 根据ID获取指定玩家违规记录
	 */
	public List<IllegalRecordEntity> getIllegalRecord(long playerId,int page,int count){
		return illegalRecordDao.getIllegalRecord(playerId,page,count);
	}

	/**
	 * 查询指定玩家违规记录总数
	 */
	public long getIllegalRecordCount(long playerId) {
		return illegalRecordDao.getIllegalRecordCount(playerId);
	}
}
