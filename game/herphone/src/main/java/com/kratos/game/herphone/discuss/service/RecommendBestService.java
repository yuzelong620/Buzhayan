package com.kratos.game.herphone.discuss.service;

import java.util.ArrayList;
import java.util.List;
import com.kratos.game.herphone.discuss.entity.RecommendBestEntity;
import com.kratos.game.herphone.gamemanager.service.GmContext;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

import org.springframework.stereotype.Service;

import com.globalgame.auto.json.GameCatalog_Json;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.HandlerState;
import com.kratos.game.herphone.discuss.bean.DiscussRecommendBean;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
@Service
public class RecommendBestService extends BaseService{
	/**
	 * 推荐审评
	 * @param entity
	 */
	public void recommendBest(DiscussEntity entity){
		Player player=PlayerContext.getPlayer();
		String discussId = entity.getDynamicId();
		if(recommendBestDao.findByID(discussId)!=null){
			return;
		}
		String discussContent = entity.getContent();
		Long recommendPlayerId = player.getId();
		String recommendPlayerNick = player.decodeName(player.getNickName());
		long recommendTime = System.currentTimeMillis();
        int handlerAdminId = 0;
		String handlerAdminName =GmContext.getGm();
		int handlerState  = HandlerState.undeal.ordinal();
		RecommendBestEntity bean = new RecommendBestEntity(discussId, recommendPlayerId, recommendPlayerNick, discussContent, recommendTime, handlerState, handlerAdminId, handlerAdminName);
		//存入数据库
		recommendBestDao.insert(bean);
		playerDynamicDao.recommendBestNum(recommendPlayerId,1);
	}

	/**
	 * 查询推荐评论未处理状态列表
	 * */
	public List<DiscussRecommendBean> findByUndeal(int page,int count){
		GameCatalogCache cache = JsonCacheManager.getCache(GameCatalogCache.class);
		List<RecommendBestEntity> recommendBests = recommendBestDao.findByUndeal(page, count);
		List<DiscussRecommendBean> list = new ArrayList<>();
		List<Long> playerIds = new ArrayList<Long>();
		List<String> discussIds=new ArrayList<>();
		for(int i = 0;i<recommendBests.size();i++){
			RecommendBestEntity object=recommendBests.get(i);
			DiscussRecommendBean discuss = new DiscussRecommendBean(object.getDiscussId(),object.getRecommendPlayerId()
					,object.getRecommendPlayerNick(),object.getDiscussContent(),object.getRecommendTime(),
					object.getHandlerState(),object.getHandlerAdminId(),object.getHandlerAdminName());
			list.add(discuss);
			playerIds.add(object.getRecommendPlayerId());
			discussIds.add(object.getDiscussId());

		}
			
		List<PlayerDynamicEntity> arrayplayerDynamicEntity = playerDynamicDao.findByIds(playerIds);		
		for (PlayerDynamicEntity playerDynamicEntity : arrayplayerDynamicEntity) {
			for (DiscussRecommendBean discussPlayerId : list) {
				if(discussPlayerId.getRecommendPlayerId()==playerDynamicEntity.getPlayerId()){			
					discussPlayerId.setRoleid(playerDynamicEntity.getRoleId());	
				}			
			}			
		}
		
		List<DiscussEntity> discusses=discussDao.findByIds(discussIds);
		for(DiscussRecommendBean obj:list){
			for(DiscussEntity discuss:discusses){
				if(discuss.getId().equals(obj.getDiscussId())){
					//获取  gameId
				    String dynamnicId=discuss.getDynamicId();
					int index = dynamnicId.indexOf("_");
					String str = dynamnicId.substring(0,index);
					int gameId = Integer.parseInt(str);
					GameCatalog_Json gameCatalog_Json=cache.getData(gameId); 
					obj.setC_id(Integer.toString(gameId));
					obj.setDramaName(gameCatalog_Json.getName());
					obj.setC_id(gameCatalog_Json.getId().toString());
				}
			}
		}
        return list;
 }

	/**
	 * 推荐评论设置成已处理
	 * */
	public void recommendBestDeal(String discussId){
		recommendBestDao.changeHandlerState(discussId,HandlerState.deal);
		discussDao.setIsBest(discussId,System.currentTimeMillis());//设置 评论神评
		GMDataChange.recordChange("通过剧本评论ID设置神评\t剧本评论ID为",discussId);
	}

	/**
	 * 推荐评论设置成搁置
	 * */
	public void recommendBestHold(String discussId){
		GMDataChange.recordChange("通过剧本评论ID删除评论\t剧本评论ID为",discussId);
		recommendBestDao.changeHandlerState(discussId, HandlerState.hold);
	}
	
	/**
	 * 推荐评论设置成未处理状态
	 * */
	public void  recommendBestUndeal(String discussId){
		GMDataChange.recordChange("通过剧本评论ID恢复评论\t剧本评论ID为",discussId);
		recommendBestDao.changeHandlerState(discussId, HandlerState.undeal);
	}
	
	
	/**
	 *查询推荐评论总条数
	 * */
	public long findCount(){
		return recommendBestDao.findCount();
	}
	
	/**
	 *查询搁置状态的评论
	 * */
	public List<DiscussRecommendBean> findByhold(int page,int count){
		GameCatalogCache cache = JsonCacheManager.getCache(GameCatalogCache.class);
		List<RecommendBestEntity> list = recommendBestDao.findByhold(page, count);
		List<DiscussRecommendBean> arraydiscussRecommendBean = new ArrayList<>();
		List<Long> playerIds = new ArrayList<Long>();
		List<String> discussIds=new ArrayList<>();

		for(int i = 0;i<list.size();i++){
			RecommendBestEntity object=list.get(i);
			DiscussRecommendBean discuss = new DiscussRecommendBean(object.getDiscussId(),object.getRecommendPlayerId(),object.getRecommendPlayerNick(),
			object.getDiscussContent(),object.getRecommendTime(),object.getHandlerState(),object.getHandlerAdminId(),object.getHandlerAdminName());
			arraydiscussRecommendBean.add(discuss);			
			playerIds.add(object.getRecommendPlayerId());
			discussIds.add(object.getDiscussId());
		}
		
		List<PlayerDynamicEntity> arrayplayerDynamicEntity = playerDynamicDao.findByIds(playerIds);
		for (PlayerDynamicEntity playerDynamicEntity : arrayplayerDynamicEntity) {
			for (DiscussRecommendBean discussPlayerId : arraydiscussRecommendBean) {
				if(discussPlayerId.getRecommendPlayerId()==playerDynamicEntity.getPlayerId()){			
					discussPlayerId.setRoleid(playerDynamicEntity.getRoleId());				
				}			
			}			
		}
		
		List<DiscussEntity> discusses=discussDao.findByIds(discussIds);
		for(DiscussRecommendBean obj:arraydiscussRecommendBean){
			for(DiscussEntity discuss:discusses){
				if(discuss.getId().equals(obj.getDiscussId())){
					//获取  gameId
				    String dynamnicId=discuss.getDynamicId();
					int index = dynamnicId.indexOf("_");
					String str = dynamnicId.substring(0,index);
					int gameId = Integer.parseInt(str);
					GameCatalog_Json gameCatalog_Json=cache.getData(gameId); 
					obj.setDramaName(gameCatalog_Json.getName());
					obj.setC_id(gameCatalog_Json.getId().toString());
				}
			}
		}

		
		return arraydiscussRecommendBean;	
	}
	
	/**
	 *查询搁置状态的评论总条数
	 * */
	public long findcountByhold(){
		return recommendBestDao.findcountByhold();		
	}

	public void setRecommendUtd(String discussId) {
		recommendBestDao.setRecommendUtd(discussId);
		GMDataChange.recordChange("通过剧本评论ID取消神评\t剧本评论ID为",discussId);
	}
	
}
