package com.kratos.game.herphone.discuss.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.discuss.bean.ReqChangeLikeDiscussState;
import com.kratos.game.herphone.discuss.bean.ResLikeMyDiscussBean;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.discuss.entity.DiscussLikeEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class DiscussLikeService extends BaseService {
	// 点赞
	public void addLikeDiscuss(String discussId) {
		if (discussId == null || "".equals(discussId)) {
			throw new BusinessException("参数为空");
		}
		DiscussEntity discuss = discussDao.findByID(discussId);
		if (discuss == null) {
			throw new BusinessException("点赞的评论不存在");
		}
		Player player = PlayerContext.getPlayer();
//		if(discuss.getFromPlayerId()==player.getId()){
//			throw new BusinessException("不能给自己点赞");
//		}
		String id = createId(discussId, player.getId());
		DiscussLikeEntity entity = discussLikeDao.findByID(id);
		if (entity != null) {
			return;
		}
		//根据评论Id查询被点赞玩家
		DiscussEntity discussEntity = discussDao.findByID(discussId);
		entity = new DiscussLikeEntity(id, player.getId(), discussId,System.currentTimeMillis(),discussEntity.getFromPlayerId(),0);
		discussLikeDao.save(entity);
		discussDao.addLikeNum(discussId);//增加点赞数量
		//添加 送出点赞数量
		playerDynamicDao.addToLikeNum(player.getId(),1);
		//taskService.playerScheduleUpdate(CommonCost.TackType.giveLike.ordinal(),1); //任务接口(任务类型,完成次数)
	}
	
	// 取消点赞
		public void deLikeDiscuss(String discussId) {
			if (discussId == null || "".equals(discussId)) {
				throw new BusinessException("参数为空");
			}
			DiscussEntity discuss = discussDao.findByID(discussId);
			if (discuss == null) {
				throw new BusinessException("取消点赞的评论不存在");
			}
			Player player = PlayerContext.getPlayer();
			String id = createId(discussId, player.getId());
			DiscussLikeEntity entity = discussLikeDao.findByID(id);
			if (entity == null) {
				return;
			}
			entity = new DiscussLikeEntity(id, player.getId(), discussId);
			discussLikeDao.deleteById(id);
			discussDao.deLikeNum(discussId);//点赞数量 -1
			//送出点赞数量-1
			playerDynamicDao.addToLikeNum(player.getId(),-1);
			//taskService.playerScheduleUpdate(CommonCost.TackType.giveLike.ordinal(),1); //任务接口(任务类型,完成次数)
		}

	private String createId(String discussId, long playerId) {
		return playerId + "_" + discussId;
	}

	public List<DiscussLikeEntity> findByDiscussIds(long playerId, List<String> discussIds) {
		List<String> likeIds = new ArrayList<>();
		for (String discussId : discussIds) {
			likeIds.add(createId(discussId, playerId));
		}
		return discussLikeDao.findByIds(likeIds);
	}
    /**
     * 查詢已经点赞过的评论。
     * @param playerId
     * @param discussIds
     * @return
     */
	public List<String> findLikeDiscussIds(long playerId,List<String> discussIds) {
		List<String> list = new ArrayList<>();
		for (DiscussLikeEntity obj:findByDiscussIds(playerId, discussIds)) {
			list.add(obj.getDiscussId());
		}
		return list;
	}
	/**
     * 查询对我的点赞信息
     */
	public List<ResLikeMyDiscussBean> findLikeMyDiscuss(int page) {
		// TODO Auto-generated method stub
		int count = 30;
		//获取当前玩家信息
		Player player = PlayerContext.getPlayer();
		//实例化返回值
		List<ResLikeMyDiscussBean> result = new ArrayList<ResLikeMyDiscussBean>();
		//获取当前玩家被点赞评论的信息
		List<DiscussLikeEntity> discussLikeEntity = discussLikeDao.findByToPlayerId(player.getId(), page, count);
		//实例化List集合，保存回复玩家的评论Id，用于修改阅读状态
		List<String> readLikeDiscussIds = new ArrayList<>();
		if(discussLikeEntity != null) {
			//循环遍历
			for (DiscussLikeEntity obj : discussLikeEntity) {
				ResLikeMyDiscussBean res = new ResLikeMyDiscussBean();
				//根据点赞评论信息,查询该评论具体信息
				DiscussEntity discussEntity = discussDao.findByID(obj.getDiscussId());
				//获取评论的剧本Id和章节Id
				String[] arr=discussEntity.getDynamicId().split("_");
				//arr[0] --------剧本Id
				//arr[1] --------章节Id
				String gameId = arr[0];
				String chapterId = arr[1];
				//返回对象内容封装
				//设置剧本ID
				res.setGameId(Integer.parseInt(gameId));
				//设置章节ID
				res.setChapterId(Integer.parseInt(chapterId));
				//设置被点赞评论内容
				res.setContent(discussEntity.getContent());
				//设置评论ID
				res.setDiscussId(obj.getDiscussId());
				//设置点赞玩家id
				res.setFromPlayerId(obj.getPlayerId()+"");
				//设置点赞时间
				res.setLikeTime(obj.getLikeTime());
				//设置是否查看过点赞信息
				res.setIsRead(obj.getIsRead());
				//根据点赞玩家Id查询点赞玩家信息
				PlayerDynamicEntity toDiscussPlayer = playerDynamicDao.findByID(obj.getPlayerId());
				res.setNickName(toDiscussPlayer.getNickName());
				res.setHeadImgUrl(toDiscussPlayer.getHeadImgUrl());
				//设置点赞玩家头像框
				res.setAvatarFrame(toDiscussPlayer.getAvatarFrame());
				//设置点赞玩家徽章
				res.setAchievementTags(toDiscussPlayer.getAchievementTags());
				//如果为未读状态，将该评论点赞Id存入list集合，以便设置已读状态
				if(obj.getIsRead()==0) {
					readLikeDiscussIds.add(obj.getId());
				}
				result.add(res);
			}
			discussLikeDao.setReadState(readLikeDiscussIds);
		}
		return result;
		
		
	}

	public void setReadState(Set<ReqChangeLikeDiscussState> req) {
		// TODO Auto-generated method stub
		for (ReqChangeLikeDiscussState obj : req) {
			String id = obj.getFromPlayerId()+"_"+obj.getDiscussId();
			discussLikeDao.setReadState(id);
		}
	}
	/**
     	* 查询对我评论点赞但未读的点赞数量
     */
	public long findLikeCount() {
		//获取当前玩家信息
		Player player = PlayerContext.getPlayer();
		return discussLikeDao.findLikeCount(player);
	}
}
