package com.kratos.game.herphone.fandom.service;

import java.util.ArrayList;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.FandomType;
import com.kratos.game.herphone.fandom.bean.FandomBean;
import com.kratos.game.herphone.fandom.bean.FandomSeachRes;
import com.kratos.game.herphone.fandom.entity.FandomEntity;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;

import lombok.extern.log4j.Log4j;
@Log4j
@Service
@Deprecated
public class FandomService extends BaseService{
	 
	
	private List<FandomBean> toBeans(List<FandomEntity> list){
		List<FandomBean> arr=new ArrayList<FandomBean>();
		for(FandomEntity entity:list){
			arr.add(new FandomBean(entity));
		}
		return arr;
	}
	/**  查询删除列表 */
	public  List<FandomBean> findDeleteList(int page,int count){
		return toBeans(fandomDao.findDeleteList(page, count));
	}
	/** 查询 列表 */
	public  List<FandomBean> findList(int page,int count) {
		return toBeans(fandomDao.findList(page, count));
	}	
	/**  查询数据总条数 */
	public long findCount() {
		return fandomDao.findCount();
	}
	
	/** 查询删除状态的条数 */
	public long deleteCount() {
		return fandomDao.deleteCount();
	}
	
	/**撤销删除*/
	public boolean cancelDelete(String id){
		GMDataChange.recordChange("通过粉圈评论ID恢复删除的评论\t评论ID为",id);
		return fandomDao.cancelDelete(id);
	}
	
	/**删除
	 * @param id
	 */
	public boolean delete(String id){		
		GMDataChange.recordChange("通过粉圈评论ID删除评论\t评论ID为",id);
		return fandomDao.delete(id);
	}
	
	/////----------------------------gm分界线--------------------------------------------
	
	/** 查询指定 游戏的粉丝圈列表 */
	public FandomSeachRes seach(int gameId,int page) {
		Player player=PlayerContext.getPlayer();
		int count=30;
		List<FandomEntity> list=fandomDao.findList(gameId, page, count); 
		GameCatalogCache cache=JsonCacheManager.getCache(GameCatalogCache.class);
		GameCatalog_Json json=cache.getData(gameId);
		if(json==null){
			log.error("客户端发送的gameId 无法识别，");
			throw new BusinessException("参数错误");
		}
		String authorId=json.getAuthorid().toString();
		FandomSeachRes res=new FandomSeachRes(authorId, toBeans(list));
		if(res.getList().size()>0){
		    checkAndSetPaiseState(res.getList(), player.getId());
		}
		return res;
	}
	
	/**
	 * 发送 粉圈消息
	 * @param gameGroupId 游戏组号
	 * @param gameId 游戏剧本id
	 * @param content 发送消息内容
	 * @param type 类型是否为作者
	 */
	public void  send( int gameId,String content){
		Player player=PlayerContext.getPlayer();
		long authorId= JsonCacheManager.getCache(GameCatalogCache.class).getData(gameId).getAuthorid();
		int type=FandomType.author.ordinal();//玩家发送的
		if(player.getId().longValue()==authorId){
			type=FandomType.author.ordinal();//作者发送的
		}
		if(content==null||"".equals(content)){
			throw new BusinessException("内容为空！");
		}
		String id=UUID.randomUUID().toString().replace("-", "");
		FandomEntity entity=new FandomEntity(id, 0, gameId, "", content, 0, 100,player.decodeName(), type, 0, System.currentTimeMillis(),player.getAvatarUrl());
	    fandomDao.save(entity);
	}
	
	/**
	 * 检查和设置 是否点赞状态
	 * @param items
	 * @param playerId
	 */
	private void checkAndSetPaiseState(List<FandomBean> items,long playerId){
		List<String> discussIds=new ArrayList<>();
		for (FandomBean obj:items){
			discussIds.add(obj.getId());
		}
		List<String>  likeDiscussIds= fandomLikeService.findLikeFandomIds(playerId,discussIds);
		for(FandomBean bean:items){
			if(likeDiscussIds.contains(bean.getId())){
				bean.setIsPraise(1);
			}
		}
	}
	
}
