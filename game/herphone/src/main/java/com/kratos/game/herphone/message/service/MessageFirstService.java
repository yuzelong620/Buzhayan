package com.kratos.game.herphone.message.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.message.entity.MessageEntity;
import com.kratos.game.herphone.message.entity.MessageFirstEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player; 

@Service
public class MessageFirstService extends BaseService{
	
	/**
	 * 查找玩家的私聊回话列表。 
	 * @param page
	 * @return
	 */
	public List<MessageFirstEntity> find(long playerId,int page,int count){ 
		return messageFirstDao.find(playerId, page, count);
		
    }
	
	public void updateMessageFirst(MessageEntity message){
		String id=createId(message.getFromPlayerId(), message.getToPlayerId());
		MessageFirstEntity entity=messageFirstDao.findByID(id);
		long lastUpdateTime=System.currentTimeMillis();
		if(entity==null){
			ArrayList<Long> playerIds=new ArrayList<>();//关联玩家的id
			playerIds.add(message.getFromPlayerId());
			playerIds.add(message.getToPlayerId());
			entity=new MessageFirstEntity(id, playerIds, message.getId(), lastUpdateTime);
			messageFirstDao.save(entity);
			return;
		}
		messageFirstDao.updateInfo(id,message.getId(),lastUpdateTime);		
	}
	/**
	 * 拼接id  。  id1_id2 ，id1的值比 id2小
	 * @param fromPlayerId
	 * @param toPlayerId
	 * @return
	 */
	private String createId(long fromPlayerId,long toPlayerId){
		if(fromPlayerId>toPlayerId){
			return toPlayerId+"_"+fromPlayerId;
		}
		return fromPlayerId+"_"+toPlayerId;
	}
    /**
     *"删除" 与他的聊天记录（我不想看到）
     * @param toPlyayer
     */
	public void setLimitTime(long toPlayerId) {
		Player player=PlayerContext.getPlayer();
		String id=createId(player.getId(), toPlayerId);
		messageFirstDao.setLimitTime(id,player.getId(),System.currentTimeMillis());
		
	}
}
