package com.kratos.game.herphone.intimacy.service;

import org.springframework.stereotype.Service;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.intimacy.entity.NoteEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
@Service
public class NoteService extends BaseService{
	NoteEntity find(int num) {
		Player player=PlayerContext.getPlayer();
		long fromPlayerId=player.getId();
		String id = createId(fromPlayerId,num);
		return noteDao.findByID(id);
	}
	public void add(int num) {
		Player player=PlayerContext.getPlayer();
		long fromPlayerId=player.getId();
		String id = createId(fromPlayerId,num);
		NoteEntity noteEntity=new NoteEntity(id,num);
		noteDao.save(noteEntity);
	}
	/**
	 * 加載用戶所有的親密度相关记录信息
	 * 
	 * @param userId
	 * @return
	 */
	public  String createId(long fromPlayerId,int num ){
		return fromPlayerId + "_" + num;
	}
}
