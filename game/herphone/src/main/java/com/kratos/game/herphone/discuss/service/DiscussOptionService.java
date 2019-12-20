package com.kratos.game.herphone.discuss.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.discuss.bean.DiscussListReq;
import com.kratos.game.herphone.discuss.entity.DiscussOptionEntity;
import com.kratos.game.herphone.util.StringUtil;
@Service
public class DiscussOptionService extends BaseService{
 
	/**
	 * 查找 多个选项的 评论数量 
	 */
	public List<DiscussOptionEntity> findDiscussCounts(List<DiscussListReq> list) {
		ArrayList<String> arr=new ArrayList<>();
		for(DiscussListReq req:list){
			String id=discussDynamicId(req.getGameId(), req.getChatId(), req.getTalkId(), req.getOptionIndex());
			arr.add(id);
		}		
		return discussOptionDao.findByIds(arr);
	}
	/**
	 * 查找单个选项的 评论数量
	 * @param gameId
	 * @param chatId
	 * @param talkId
	 * @param optionIndex
	 * @return
	 */
	public int findDiscussCount(int gameId, int chatId, int talkId, int optionIndex){
		 DiscussOptionEntity entity= load(gameId, chatId, talkId, optionIndex);
		 if(entity==null){
			 return 0;
		 }
		 return entity.getDiscussNum();
	}
	
	public int findDiscussCount(int gameId, int chatId){
		 DiscussOptionEntity entity= load(gameId, chatId);
		 if(entity==null){
			 return 0;
		 }
		 return entity.getDiscussNum();
	}
	/**
	 * 查询剧本下的评论数量
	 * @param gameId 剧本ID
	 * @return
	 */
	public int findDiscussCount(int gameId){
		 return load(gameId);
	}
	
	
	private String discussDynamicId(int gameId, int chatId, int talkId, int optionIndex){
		return StringUtil.appendString(gameId,chatId,talkId,optionIndex);
	}
	
	private String discussDynamicId(int gameId, int chatId){
		return StringUtil.appendString(gameId,chatId);
	}
	
	public DiscussOptionEntity load(int gameId, int chatId, int talkId, int optionIndex){
		 String dynamicId=discussDynamicId(gameId, chatId, talkId, optionIndex);
		 DiscussOptionEntity entity= discussOptionDao.findByID(dynamicId);
		 if(entity!=null){
			 return entity;
		 }
		 entity=new DiscussOptionEntity(dynamicId, gameId, chatId, talkId, optionIndex, 0);
		 discussOptionDao.save(entity);
		 return entity;
	}
	
	
	public DiscussOptionEntity load(int gameId, int chatId){
		 String dynamicId=discussDynamicId(gameId, chatId);
		 DiscussOptionEntity entity= discussOptionDao.findByID(dynamicId);
		 if(entity!=null){
			 return entity;
		 }
		 entity=new DiscussOptionEntity(dynamicId, gameId, chatId, 0);
		 discussOptionDao.save(entity);
		 return entity;
	}
	
	
	public int load(int gameId){
		List<DiscussOptionEntity> disscussOptions = discussOptionDao.findCountByGameId(gameId);
		int num = 0;
		if (disscussOptions.size() == 0) {
			return num;
		}

		for (DiscussOptionEntity discussOptionEntity : disscussOptions) {
			num += discussOptionEntity.getDiscussNum();
		}
		return num;
	}
}
