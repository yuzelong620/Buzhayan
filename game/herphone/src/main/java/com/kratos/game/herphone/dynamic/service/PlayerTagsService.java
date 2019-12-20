package com.kratos.game.herphone.dynamic.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.Tag_Json;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.dynamic.entity.PlayerTagsEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.TagCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;

import lombok.extern.log4j.Log4j;
@Log4j
@Service
@Deprecated
public class PlayerTagsService extends BaseService {
	
	public PlayerTagsEntity load(long playerId) {
		PlayerTagsEntity obj = playerTagsDao.findByID(playerId);
		if (obj == null) {
			obj = new PlayerTagsEntity(playerId, new HashSet<>());
			playerTagsDao.save(obj);
		}
		return obj;
	}
	/**
	 * 当用户 获得小标签的时候，会活动关联的tag
	 * @param tagId
	 */
	public void  add(int tagId){
		Player player=PlayerContext.getPlayer();
		TagCache cache=JsonCacheManager.getCache(TagCache.class);
		Tag_Json json=cache.getData(tagId);
		if(json==null){
			log.error("tag 添加错误，tagId 无法识别："+tagId);
			return;
		}
		PlayerTagsEntity tags=load(player.getId());
		if(tags.getTags().contains(tagId)){//已经有了这个tagId了
			return;
		}
		playerTagsDao.addToSet(player.getId(),tagId);
	}
	/**
	 * 当用户 获得成就碎片的时候，会活动关联的tag
	 * @param tagId
	 */
	public void  add(int tagId,Player player) {
		TagCache cache=JsonCacheManager.getCache(TagCache.class);
		Tag_Json json=cache.getData(tagId);
		if(json==null){
			log.error("tag 添加错误，tagId 无法识别："+tagId);
			return;
		}
		PlayerTagsEntity tags=load(player.getId());
		if(tags.getTags().contains(tagId)){//已经有了这个tagId了
			return;
		}
		playerTagsDao.addToSet(player.getId(),tagId);
	}
	/**
     * 用户查询标签
     */
	public Set<Integer> findDynamciTags() {
		Player player = PlayerContext.getPlayer();
		PlayerTagsEntity playerTagsEntity = playerTagsDao.findByID(player.getId());
		Set<Integer> tags = new HashSet<>();
		if(playerTagsEntity != null) {
			tags = playerTagsEntity.getTags();
		}
		return tags;
	}
}