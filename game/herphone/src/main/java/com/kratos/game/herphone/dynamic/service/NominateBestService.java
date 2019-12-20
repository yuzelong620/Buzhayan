package com.kratos.game.herphone.dynamic.service;

import java.util.ArrayList;
import java.util.List; 
import org.springframework.stereotype.Service; 
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.HandlerState; 
import com.kratos.game.herphone.dynamic.bean.NominateBestBean;
import com.kratos.game.herphone.dynamic.bean.ResBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.dynamic.entity.NominateBestEntity;
import com.kratos.game.herphone.gamemanager.service.GmContext;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange; 
@Service
@Deprecated
public class NominateBestService extends BaseService{
	/**
	 * 推荐审评
	 * @param entity
	 */
	public void nominateBest(DynamicEntity entity){
		Player player=PlayerContext.getPlayer();
		String dynamicId = entity.getId();
		if(nominateBestDao.findByID(dynamicId)!=null){
			return;
		}
		List<Integer> tags=entity.getTags();
		List<ResBean> resList = entity.getResList();
		Long recommendPlayerId = player.getId();
		String roleId=player.getRoleId();
		String recommendPlayerNick = player.decodeName(player.getNickName());
		long recommendTime = System.currentTimeMillis();
        int handlerAdminId = 0;
		String handlerAdminName =GmContext.getGm();
		int handlerState  = HandlerState.undeal.ordinal();
		NominateBestEntity bean = new NominateBestEntity(dynamicId, recommendPlayerId, recommendPlayerNick,
				resList, recommendTime, handlerState, handlerAdminId, handlerAdminName,roleId,tags);
		//存入数据库
		nominateBestDao.insert(bean);
		playerDynamicDao.recommendBestNum(recommendPlayerId,1);
	}

	/**
	 * 查询推荐评论未处理状态列表
	 * */
	public List<NominateBestBean> findByUndeal(int page,int count){ 
		List<NominateBestEntity> list = nominateBestDao.findByUndeal(page, count);
		List<NominateBestBean> results = new ArrayList<>(); 
		for(int i = 0;i<list.size();i++){
			NominateBestEntity obj=list.get(i);
			NominateBestBean discuss = new NominateBestBean(obj);
			results.add(discuss);
		}
        return results;
 }

	/**
	 * 推荐评论设置成已处理
	 * */
	public void recommendBestDeal(String discussId){
		nominateBestDao.changeHandlerState(discussId,HandlerState.deal);
		dynamicDao.setIsBest(discussId,System.currentTimeMillis());//设置 评论神评
		GMDataChange.recordChange("通过广场评论ID设置神评\t广场评论ID为",discussId);
	}

	/**
	 * 推荐评论设置成搁置
	 * */
	public void recommendBestHold(String discussId){
		nominateBestDao.changeHandlerState(discussId, HandlerState.hold);
	}
	
	/**
	 * 推荐评论设置成未处理状态
	 * */
	public void  recommendBestUndeal(String discussId){
		nominateBestDao.changeHandlerState(discussId, HandlerState.undeal);
		dynamicDao.removeIsBest(discussId); //取消神评
		GMDataChange.recordChange("通过广场评论ID取消神评\t广场评论ID为",discussId);
	}
	
	
	/**
	 *查询推荐评论总条数
	 * */
	public long findCount(){
		return nominateBestDao.findCount();
	}
	
	/**
	 *查询搁置状态的评论
	 * */
	public List<NominateBestBean> findByhold(int page,int count){ 
		List<NominateBestEntity> list = nominateBestDao.findByhold(page, count);
		List<NominateBestBean> results = new ArrayList<>();
		for(NominateBestEntity obj:list){
			results.add( new NominateBestBean(obj)); 
		}
		return results;
	}
	
	/**
	 *查询搁置状态的评论总条数
	 * */
	public long findcountByhold(){
		return nominateBestDao.findcountByhold();		
	}
   
	public void removeIsBest(String dynamicId) {
		dynamicService.removeIsBest(dynamicId);
		nominateBestDao.setRecommendUtd(dynamicId);
	}
	
}
