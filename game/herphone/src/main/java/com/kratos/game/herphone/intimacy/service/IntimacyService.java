package com.kratos.game.herphone.intimacy.service;
 
import java.util.List;
import org.springframework.stereotype.Service;
import com.globalgame.auto.json.Intimacy_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.intimacy.entity.IntimacyEntity;
import com.kratos.game.herphone.intimacy.entity.NoteEntity;
import com.kratos.game.herphone.json.datacache.IntimacyCache;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
@Service
public class IntimacyService extends BaseService {

	/**
	 * 添加好亲密度
	 * 
	 * @param session
	 * @param dynamicId
	 * @param toPlayerId
	 * @param num
	 */
	public void addInimacy(int id) {
		IntimacyCache cache =JsonCacheManager.getCache(IntimacyCache.class);
		Intimacy_Json  intimacy_Json=cache.getData(id);
		if(intimacy_Json==null){
  		   throw new BusinessException("参数错误！");
		}
		IntimacyEntity intimacyEntity= load(intimacy_Json.getAvatarid());
		NoteEntity noteEntity=noteService.find(intimacy_Json.getNumber());
		if(noteEntity!=null)
		{
			return ;
		}
		noteService.add(intimacy_Json.getNumber());
		int val=intimacyEntity.getIntimacyValue()+intimacy_Json.getValue();
		intimacyEntity.setIntimacyValue(val);
		intimacyDao.save(intimacyEntity);
	}
 
	IntimacyEntity load(long toPlayerId) {
		Player player=PlayerContext.getPlayer();
		long fromPlayerId=player.getId();
		String id = createId(fromPlayerId, toPlayerId);
		IntimacyEntity entity =intimacyDao.findByID(id);;//
		if (entity != null) {// 已经存在
			return entity;
		}
		entity = new IntimacyEntity(id, fromPlayerId, toPlayerId, 0);
		intimacyDao.save(entity);
		return entity;
	}
	/**
	 * 加載用戶所有的親密度信息
	 * 
	 * @param userId
	 * @return
	 */
	public List<IntimacyEntity> findAll(int page) {
		Player player=PlayerContext.getPlayer();
		int count=25;
		List<IntimacyEntity> list = intimacyDao.findByPlayerId(	player.getId(),page,count);
		return list;
	}
	private String createId(long fromPlayerId, long toPlayerId) {
		return fromPlayerId + "_" + toPlayerId;
	}
	
}
