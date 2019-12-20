package com.kratos.game.herphone.message.service;

import java.util.*;
import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.blackList.entity.BlackListEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.message.bean.FindMessageInfoRes;
import com.kratos.game.herphone.message.bean.FindMessageRes;
import com.kratos.game.herphone.message.bean.MessageBean;
import com.kratos.game.herphone.message.bean.MessageInfoBean;
import com.kratos.game.herphone.message.entity.MessageEntity;
import com.kratos.game.herphone.message.entity.MessageFirstEntity;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
@Service
public class MessageService extends BaseService{
     /***
      * 更新用户信息 
      * @param info
      */
	public void updatePlayerInfo(PlayerDynamicEntity info){
		messageDao.updateToPlayer(info.getPlayerId(), info.getNickName(), info.getHeadImgUrl());
		messageDao.updateFromPlayer(info.getPlayerId(), info.getNickName(), info.getHeadImgUrl());
	}
    
	public void setLimitTime(long toPlyayer){
		messageFirstService.setLimitTime(toPlyayer);
	}
	/**
	 * 分页查询 ，私聊回话列表
	 * @param page
	 * @return
	 */
	public FindMessageInfoRes findMessageFirstInfo(int page){
		Player player=PlayerContext.getPlayer();
		int count=GameParamsCache.getGameParams_Json().getMessagePageCount();//查询最新条数
 		List<MessageFirstEntity> firsts=messageFirstService.find(player.getId(), page, count);
		List<MessageInfoBean> infos=new ArrayList<MessageInfoBean>();//返回的数据
		List<String> messageIds=new ArrayList<>();
		HashMap<String,MessageInfoBean> tempMap=new HashMap<>();//临时的映射集合
		for(MessageFirstEntity obj:firsts){
			messageIds.add(obj.getMessageId());
			Long limitTime=obj.getLimitTime().get(player.getId());
			MessageInfoBean bean= new MessageInfoBean(0, "", "", 0, 0,0,0);
			if(limitTime!=null&&limitTime>=obj.getLastUpdateTime()){//我已经“删除”这个聊天会话
				bean.setIsDelete(1);
			}
			infos.add(bean);
			tempMap.put(obj.getMessageId(), bean);
		}
		List<MessageEntity> messages=messageDao.findByIds(messageIds);
		List<Long> playerIds=new ArrayList<Long>();//发给我的玩家id
		for(MessageEntity obj:messages){
			boolean toMe=obj.getToPlayerId()==player.getId();//发给我的
        	MessageInfoBean info=tempMap.get(obj.getId());
        	if(toMe){//发给我的
        	    info.reset(obj.getFromPlayerId(), obj.getFromNickName(), obj.getFromAvatarUrl(), 0,obj.getCreateTime(),
        	    		obj.getFromAchievementTags(),obj.getFromAvatarFrame());
        	    playerIds.add(obj.getFromPlayerId());
        	}
        	else{//我发出去的
        		info.reset(obj.getToPlayerId(), obj.getToNickName(), obj.getToAvatarUrl(), 0,obj.getCreateTime(),
        				obj.getToAchievementTags(),obj.getToAvatarFrame());
        	}
        	info.resetFirst(new MessageBean(obj));//设置第一个消息
		}
		setUnreadNum(player, infos, playerIds);
		return new FindMessageInfoRes(infos);
	}

	private void setUnreadNum(Player player, List<MessageInfoBean> infos, List<Long> playerIds) {
		List<MessageEntity> unreadMessages=messageDao.findToMyUnreadMessages(player.getId(),playerIds);
		for(MessageEntity obj:unreadMessages){
			 for(MessageInfoBean info:infos){
				 if(Long.parseLong(info.getPlyaerId())==obj.getFromPlayerId()){
					info.setUnreadNum(info.getUnreadNum()+1);//未读消息数量
					break;
				 }
			 }
		}
	}
	
	public FindMessageInfoRes newMessageInfo() {
 		Player player=PlayerContext.getPlayer();
 		int count=GameParamsCache.getGameParams_Json().getMessagePageCount();//查询最新条数
		int page=1;
        List<MessageEntity> list=messageDao.findUnreadMessageInfo(player.getId(),page,count);
        Map<Long,MessageInfoBean>  temp=new HashMap<Long, MessageInfoBean>();
        for(MessageEntity obj:list){
        	boolean toMe=obj.getToPlayerId()==player.getId();//发给我的
        	MessageInfoBean info=null;
        	long toPlayerId=0;
        	if(toMe){
        		toPlayerId=obj.getFromPlayerId();
        	}
        	else{
        		toPlayerId=obj.getToPlayerId();
        	}
        	info=temp.get(toPlayerId);
        	if(info==null){
        		if(toMe){//发给我的
        		    info=new MessageInfoBean(obj.getFromPlayerId(), obj.getFromNickName(), obj.getFromAvatarUrl(), 0,obj.getCreateTime(),
        		    		obj.getFromAchievementTags(),obj.getFromAvatarFrame());
        		}
        		else{//我发出去的
        			info=new MessageInfoBean(obj.getToPlayerId(), obj.getToNickName(), obj.getToAvatarUrl(), 0,obj.getCreateTime(),
        					obj.getFromAchievementTags(),obj.getFromAvatarFrame());
        		}
        	    temp.put(toPlayerId, info);
        	}
        	//如果 是未读，并且是发给我的。增加未读消息数量
        	if(toMe&&obj.getState()==ReadState.unread.ordinal()){
        		info.setUnreadNum(info.getUnreadNum()+1);//未读消息数量
        	}
        	if(info.getFirstMessage()==null){
        		info.resetFirst(new MessageBean(obj));
        	}
        }
        ArrayList<MessageInfoBean> reslist = new ArrayList<>(temp.values());
        Comparator<MessageInfoBean> comparator = new Comparator<MessageInfoBean>() {			
			@Override
			public int compare(MessageInfoBean o1, MessageInfoBean o2) {
				long sub = o2.getUpdateTime() - o1.getUpdateTime();
				if (sub > 0) {
					return 1;
				}else if (sub == 0) {
					return 0;
				}else {
					return -1;
				}
			}
		};
        Collections.sort(reslist,comparator);
        return new FindMessageInfoRes(reslist);
	}
	
	public static List<MessageBean> toMessageBean(List<MessageEntity> list){
		List<MessageBean> temp=new ArrayList<MessageBean>();
		if(list!=null){
			for(MessageEntity obj:list){
				temp.add(new MessageBean(obj));
			}
		}
		return temp;
	}
	
	public FindMessageRes findMessage(long fromPlayerId, int page) {
		Player player= PlayerContext.getPlayer();
		int count=GameParamsCache.getGameParams_Json().getMessagePageCount();
		if(fromPlayerId==player.getId()) {
			throw new BusinessException("自己不能私信自己");
		}
		List<MessageEntity> list=messageDao.findMessage(fromPlayerId,player.getId(), page, count);
		return new FindMessageRes(page,count, toMessageBean(list));
	}
	
	public void sendMessage(long toPlayerId,String content,int messageType,int audioTime){
		if(content==null||content.trim().equals("")){
			throw new BusinessException("消息不能为空");
		}
		int contentLimit=GameParamsCache.getGameParams_Json().getMessageContentLimit();
		if(content.length()>contentLimit){
			throw new BusinessException("内容过长");
		}
		Player player= PlayerContext.getPlayer();
		PlayerDynamicEntity fromPlayer = playerDynamicDao.findByID(player.getId());
		BlackListEntity blackListEntity = blackListService.load(toPlayerId);
		if (blackListEntity.getBlackList().contains(player.getId())) {
			throw new BusinessException("对方设置了权限 您无法进行沟通~");
		}
		BlackListEntity fromblackListEntity = blackListService.load(player.getId());
		if (fromblackListEntity.getBlackList().contains(toPlayerId)) {
			throw new BusinessException("对方已在你黑名单中");
		}
		PlayerDynamicEntity toPlayer=playerDynamicService.load(toPlayerId);
		String id=UUID.randomUUID().toString().replace("-", "");
		int state=ReadState.unread.ordinal();
		long createTime=System.currentTimeMillis();			
		MessageEntity message=new MessageEntity(id, state, createTime, content, player.getId(), player.decodeName(), player.getAvatarUrl(), toPlayer.getPlayerId(), toPlayer.getNickName(), toPlayer.getHeadImgUrl(),messageType,audioTime,fromPlayer.getAchievementTags(),fromPlayer.getAvatarFrame(),toPlayer.getAchievementTags(),toPlayer.getAvatarFrame());
	    messageDao.save(message);
	    //更新 私聊会话相关消息
	    messageFirstService.updateMessageFirst(message);
	    //通知玩家消息
	    systemService.sendMessage(message);
	}

	public void setReadState(Set<String> messageIds){ 
		messageDao.setReadState(messageIds);
	}
	
	public void setReadState(long playerId){ 
		List<MessageEntity> list = messageDao.setReadState(playerId);
		Set<String> messageIds = new HashSet<>();
		for (MessageEntity messageEntity : list) {
			messageIds.add(messageEntity.getId());
		}
		setReadState(messageIds);
	}
	/**
	 *  玩家是否有未读消息 
	 * @param toPlayerId
	 * @return 返回玩家消息状态 ： 1=有，0没有
	 */
	public int getPlayerUnreadState(long toPlayerId) {
		MessageEntity messageEntity = messageDao.findOneUnreadReply(toPlayerId); 
		if (messageEntity == null) {
			return 0;
		} 
		return 1;
	}

}
