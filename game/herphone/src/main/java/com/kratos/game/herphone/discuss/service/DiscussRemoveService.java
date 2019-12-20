package com.kratos.game.herphone.discuss.service;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.discuss.entity.DiscussRemoveEntity;

@Service
public class DiscussRemoveService extends BaseService{
	
	/**
	 * 增加删除的评论信息  1代表增加成功 
	 */
	public int addDeletedDiscuss(DiscussRemoveEntity discussRemoveEntity) {
		discussRemoveDao.save(discussRemoveEntity);
		return 1;
	}
}
