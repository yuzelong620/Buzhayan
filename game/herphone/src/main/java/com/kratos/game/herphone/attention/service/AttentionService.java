package com.kratos.game.herphone.attention.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.attention.bean.AttentionBean;
import com.kratos.game.herphone.attention.entity.AttentionAuthorEntity;
import com.kratos.game.herphone.attention.entity.AttentionEntity;
import com.kratos.game.herphone.blackList.entity.BlackListEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@Service
public class AttentionService extends BaseService{
	/**
	 * 更新 用户信息
	 * @param info
	 */
	public void updatePlayerInfo(PlayerDynamicEntity info){
		attentionDao.updateToPlayer(info.getPlayerId(), info.getNickName(), info.getHeadImgUrl(), info.getSignature(),info.getAchievementTags(),info.getAvatarFrame());
		attentionDao.updateFromPlayer(info.getPlayerId(), info.getNickName(), info.getHeadImgUrl(), info.getSignature(),info.getAchievementTags(),info.getAvatarFrame());
	}
	 /**
	  * 关注
	  */
	 public void attentionPlayer(long toPlayerId){
		 PlayerDynamicEntity toPlayer=playerDynamicService.load(toPlayerId);
         if(toPlayer==null){
        	throw new BusinessException("用户不存在");
		 }
		 Player player=PlayerContext.getPlayer();
		 BlackListEntity toblackListEntity = blackListService.load(toPlayerId);
			if (toblackListEntity.getBlackList().contains(player.getId())) {
				throw new BusinessException("对方设置了权限，不能关注了对方哦~");
			}
		 BlackListEntity fromblackListEntity = blackListService.load(player.getId());
		 if (fromblackListEntity.getBlackList().contains(toPlayerId)) {
				throw new BusinessException("对方已在你黑名单中");
			}	
		 PlayerDynamicEntity fromPlayer=playerDynamicService.load(player.getId());
		 long userId=player.getId();
		 if(userId==toPlayerId) {
			 throw new BusinessException("不能自己关注自己~");
		 }
		 String id=createId(toPlayerId, userId);
		 AttentionEntity entity=attentionDao.findByID(id);
		 if(entity!=null){
			 throw new BusinessException("您已经关注了");
		 }
		 entity=new AttentionEntity(id, userId,player.decodeName(), toPlayerId,toPlayer.getNickName(),toPlayer.getHeadImgUrl(),player.getAvatarUrl(),toPlayer.getSignature(),fromPlayer.getSignature(),fromPlayer.getAchievementTags(),fromPlayer.getAvatarFrame(),toPlayer.getAchievementTags(),toPlayer.getAvatarFrame());
		 
 		 attentionDao.save(entity);//保存关注数据
		  
 		playerDynamicDao.incAttentionNum(userId,1);//我的关注数 +1
 		playerDynamicDao.incFansNum(toPlayerId, 1);//对方粉丝数+1 
 		
 		//更新阶段任务进度，调用阶段任务接口，关注用户
 		stageTaskService.updateTaskValue(CommonCost.StageTaskType.attentionUser.ordinal(), 1);
	 }
 
	 
	 /**查询我关注的用户  */
	 public List<AttentionBean> seachMyAttentionPlayers(int page){
		 Player player=PlayerContext.getPlayer();
		 long userId=player.getId();
		 int count=30;
		 //查找我关注的人列表
		 List<AttentionEntity> attentions=attentionDao.seachAttentions(userId,page,count);
		 
		 List<String> ids=new ArrayList<>();
		 for(AttentionEntity entity:attentions){
			 ids.add(createId(userId, entity.getToPlayerId()));
		 }
		 List<AttentionEntity> attFans=attentionDao.seachAttentions(ids);		 
		 
		 List<AttentionBean> list=new ArrayList<>();
		 for(AttentionEntity att:attentions){
			 int isAttention=0;
			 for(AttentionEntity obj:attFans){
				 if(obj.getFromPlayerId()==att.getToPlayerId()){	
					 isAttention=1;
				     break;
				 }
			 }
			 list.add(new AttentionBean(att.getToPlayerId(), att.getToPlayerNick(),att.getToPlayerAvatarUrl(),isAttention,att.getToPlayerSignature(),att.getToPlayerAchievementTags(),att.getToPlayerAvatarFrame()));
		 }
		 return list;
	 }
	 
	 /**查找关注我的粉丝 */
	 public List<AttentionBean>  seachFansPlayers(int page){
		 Player player=PlayerContext.getPlayer();
		 int count=30;
		 return seachFansPlayers(player.getId(), page,count);
	 }
	 
	 /**查找关注我的粉丝 */
	 public  List<AttentionBean>  seachFansPlayers( long playerId , int page,int count){ 
		
		 List<AttentionEntity> attentions=attentionDao.seachFans(playerId,page,count);
		 //查询我是否关注了粉丝 
		 List<String> ids=new ArrayList<>();
		 for(AttentionEntity entity:attentions){
			 ids.add(createId(entity.getFromPlayerId(), playerId));
		 }
		 List<AttentionEntity> attFans=attentionDao.seachAttentions(ids);		 
		 
		 List<AttentionBean> list=new ArrayList<>();
		 for(AttentionEntity att:attentions){
			 int isAttention=0;
			 for(AttentionEntity obj:attFans){
				 if(obj.getToPlayerId()==att.getFromPlayerId()){
				     isAttention=1;
				     break;
				 }
			 }
			 list.add(new AttentionBean(att.getFromPlayerId(),att.getFromPlayerNick(),att.getFromPlayerAvatarUrl(),isAttention,att.getFromPlayerSignature(),att.getFromPlayerAchievementTags(),att.getFromPlayerAvatarFrame()));
		 }
		return list;
	 }
	 
	 /**
	  * @param toPlayerId 
	  * @param userId
	  * @return
	  */
	 public String createId(long toPlayerId,long userId){
		 return new StringBuffer().append(userId).append("_").append(toPlayerId).toString();
	  }
	 
	  public List<AttentionEntity> findAttentions(List<Long> toPlayerIds,long userId){
		  List<String> ids=new ArrayList<>();
		  for(long toPlayerId:toPlayerIds) {
			  ids.add(createId(toPlayerId,userId));
		  }
		  return  attentionDao.seachAttentions(ids);
      }
	  
	 public AttentionEntity findAttention(long toPlayerId,long userId){
		return attentionDao.findByID(createId(toPlayerId, userId));
	 }
   /**
    * 取消关注
    * @param req
    */
	public void attentionCancel(long toPlayerId) {
		 Player player=PlayerContext.getPlayer();
		 long userId=player.getId();
		 String id=createId(toPlayerId, userId);
		 AttentionEntity entity=attentionDao.findByID(id);
		 if(entity==null){
			 throw new BusinessException("您还没有关注他"); 
		 }
		 attentionDao.deleteById(id);
		 playerDynamicDao.incAttentionNum(userId,-1);//我关注数 -1
		 playerDynamicDao.incFansNum( toPlayerId,-1);//对方 粉丝数-1
	}

	/**
	 * 关注作者
	 */
	
	 public void attentionAuthor(long authorId){
		 //查询作者是否存在
		PlayerDynamicEntity author=playerDynamicDao.findByID(authorId);
		//如果不存在，抛出异常
        if(author==null){
       	throw new BusinessException("作者不存在");
		 }
         //获取用户信息
		 Player player=PlayerContext.getPlayer();
		 //获取用户Id
		 long playerId = player.getId();
		 //long playerId = 2815133567330811917L;
		 //按照规则创建主键id
		 String id=createId(authorId, playerId);
		 //查询是否已关注
		 AttentionAuthorEntity authorEntity=attentionAuthorDao.findByID(id);
		 if(authorEntity!= null){
			 throw new BusinessException("您已经关注了");
		 }
		 authorEntity=new AttentionAuthorEntity(id, playerId, authorId, System.currentTimeMillis());
		//保存关注数据
		 attentionAuthorDao.save(authorEntity);
		//我的关注数 +1
		playerDynamicDao.incAttentionNum(playerId,1);
		//对方粉丝数+1 
		playerDynamicDao.incFansNum(authorId, 1);
		
		//更新阶段任务进度，调用阶段任务接口，关注作者
 		stageTaskService.updateTaskValue(CommonCost.StageTaskType.attentionAuthor.ordinal(), 1);
	 }
	 
	 /**
	  * 取消对作者关注
	  */
	 public void attentionAuthorCancel(long authorId) {
		 //获取用户
		 Player player=PlayerContext.getPlayer();
		 //获取用户id
		 long userId=player.getId();
		 //long userId = 2815133567330811917L;
		 String id=createId(authorId, userId);
		 //根据用户id查询关注作者
		 AttentionAuthorEntity entity=attentionAuthorDao.findByID(id);
		 //判断是否关注该作者
		 if(entity==null){
			 throw new BusinessException("您还没有关注该作者"); 
		 }
		 //删除关注信息
		 attentionAuthorDao.deleteById(id);
		 //我关注数 -1
		 playerDynamicDao.incAttentionNum(userId,-1);
		 //对方 粉丝数-1
		 playerDynamicDao.incFansNum( authorId,-1);
	}
	
	 /**
	  * 查询粉丝关注的作者
	  */
	 public List<Long> findAttentionAuthor(){
		 //获取用户信息
		 Player player=PlayerContext.getPlayer();
		 //获取用户id
		 long userId=player.getId();
		 //long userId = 2815133567330811917L;
		 //根据用户id获取该用户关注的作者信息
		 List<AttentionAuthorEntity> authorList = attentionAuthorDao.findAttentionAuthor(userId);
		 //创建集合，存放作者id
		 List<Long> authorIdList = new ArrayList<Long>();
		 //遍历作者信息集合
		 for (AttentionAuthorEntity author : authorList) {
			 //提取作者信息中的id，存放到作者id集合中
			authorIdList.add(author.getAuthorId());
		}
		 return authorIdList;
	 }
	 
}
