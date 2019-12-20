package com.kratos.game.herphone.systemMessgae.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import com.kratos.engine.framework.net.socket.exception.BusinessException;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.common.CommonCost.SendType;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.systemMessgae.bean.FindMessagaeBean;
import com.kratos.game.herphone.systemMessgae.bean.ResPlayerSystemMessagae;
import com.kratos.game.herphone.systemMessgae.bean.ResSystemMessgae;
import com.kratos.game.herphone.systemMessgae.bean.SystemMessgaeInfoBean;
import com.kratos.game.herphone.systemMessgae.entity.PublicSystemMessageEntity;
import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeEntity;
import com.kratos.game.herphone.user.entity.UserEntity;


@Service
public class SystemMessgaeService extends BaseService{
	private  static SystemMessgaeService instance;
	public SystemMessgaeService() {
		instance = this;
	}
	
	public static SystemMessgaeService getInstance() {
		return instance;
	}
	public  void init() {
		List<SystemMessgaeEntity> list = systemMessgaeDao.distinctFindList();			
		for (int i = 0; i < list.size(); i++) {
			 Object object = list.get(i);
			 String id =object.toString();
			long playerId = Long.valueOf(id);
			SystemMessgaeEntity systemMessgaeEntity = systemMessgaeDao.findLastSystemMessgaeEntity(playerId);
			systemMessageLastService.updateSystemMessageLast(playerId, systemMessgaeEntity);
		}			
	}
	
	/**玩家向系统发送消息*/
	public void playerSendMessgae(String content,int contentType) {
		int contentLimit=GameParamsCache.getGameParams_Json().getMessageContentLimit();
		if(content.length()>contentLimit){
			throw new BusinessException("内容过长");
		}
		Player player = PlayerContext.getPlayer();
		SystemMessgaeEntity systemMessgaeEntity = new SystemMessgaeEntity();
		systemMessgaeEntity.setId(UUID.randomUUID().toString().replace("-", ""));
		systemMessgaeEntity.setPlayerId(player.getId());
		systemMessgaeEntity.setNickname(player.decodeName());
		systemMessgaeEntity.setReadState(ReadState.read.ordinal());
		systemMessgaeEntity.setSendType(SendType.fromplayer.ordinal());
		systemMessgaeEntity.setContentType(contentType);
		systemMessgaeEntity.setContent(content);
		systemMessgaeEntity.setAnnouncement(0);
		systemMessgaeEntity.setCreateTime(System.currentTimeMillis());
		systemMessgaeDao.save(systemMessgaeEntity);
		systemMessageLastService.updateSystemMessageLast(player.getId(), systemMessgaeEntity);
	}
	/**系统向玩家发送消息*/
	public void systemSendMessgae(String content,int contentType,long ...playerIds) {
		for (long playerId : playerIds) {
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
			SystemMessgaeEntity systemMessgaeEntity = new SystemMessgaeEntity();
			systemMessgaeEntity.setId(UUID.randomUUID().toString().replace("-", ""));
			systemMessgaeEntity.setPlayerId(playerId);
			systemMessgaeEntity.setNickname(playerDynamicEntity.getNickName());
			systemMessgaeEntity.setReadState(ReadState.unread.ordinal());
			systemMessgaeEntity.setSendType(SendType.toplayer.ordinal());
			systemMessgaeEntity.setContentType(contentType);
			systemMessgaeEntity.setContent(content);
			systemMessgaeEntity.setAnnouncement(0);
			systemMessgaeEntity.setCreateTime(System.currentTimeMillis());
			systemMessgaeDao.save(systemMessgaeEntity);
			systemMessageLastService.updateSystemMessageLast(playerId, systemMessgaeEntity);
		}
		if(playerIds.length > 1) {
			GMDataChange.recordChange("通过多选玩家发送消息 ","---");
		}
	}
	/**系统向玩家发送公共消息*/
	public void systemSendAnnouncement(String content,int contentType) {			
			SystemMessgaeEntity systemMessgaeEntity = new SystemMessgaeEntity();
			systemMessgaeEntity.setId(UUID.randomUUID().toString().replace("-", ""));
			systemMessgaeEntity.setPlayerId(0);
			systemMessgaeEntity.setNickname("");
			systemMessgaeEntity.setReadState(ReadState.unread.ordinal());
			systemMessgaeEntity.setSendType(SendType.toplayer.ordinal());
			systemMessgaeEntity.setContentType(contentType);
			systemMessgaeEntity.setContent(content);
			systemMessgaeEntity.setAnnouncement(1);
			systemMessgaeEntity.setCreateTime(System.currentTimeMillis());
			systemMessgaeDao.save(systemMessgaeEntity);
			GMDataChange.recordChange("向玩家发送公共消息\t消息ID为",systemMessgaeEntity.getId());
	}
	/**玩家获取系统消息列表*/
	public FindMessagaeBean listSystemMessge(int page) {
		int count = GameParamsCache.getGameParams_Json().getMessagePageCount();
		Player player = PlayerContext.getPlayer();
		List<SystemMessgaeEntity> list = systemMessgaeDao.findByPlayerIdLimit(page, count, player.getId());
		systemMessgaeDao.updateFromGm(player.getId(), ReadState.unread.ordinal(), ReadState.read.ordinal());
		PublicSystemMessageEntity publicSystemMessageEntity = publicSystemMessageService.load(player.getId());
		List<ResPlayerSystemMessagae> reslist = new ArrayList<>();
		for (SystemMessgaeEntity systemMessgaeEntity : list) {
			//如果消息类型是公共消息且PublicSystemMessageEntity实体类中没有这个消息id，将此消息添加到公共系统消息类里
			if (systemMessgaeEntity.getAnnouncement() == 1 && !publicSystemMessageEntity.getSystemMessageSet().contains(systemMessgaeEntity.getId())) {
				publicSystemMessageDao.addSystemMessageSet(player.getId(), systemMessgaeEntity.getId());
			}
			ResPlayerSystemMessagae resPlayerSystemMessagae = new ResPlayerSystemMessagae(systemMessgaeEntity);
			reslist.add(resPlayerSystemMessagae);
		}		
		FindMessagaeBean findMessagaeBean = new FindMessagaeBean();
		findMessagaeBean.setPage(page);
		findMessagaeBean.setCount(count);
		findMessagaeBean.setList(reslist);
		return findMessagaeBean; 
	}
	/**玩家获取系统消息未读数量*/	
	public SystemMessgaeInfoBean getSystemMessge() {
		Player player = PlayerContext.getPlayer();
		List<SystemMessgaeEntity> list = systemMessgaeDao.findUnReadState(player.getId(), ReadState.unread.ordinal());	
		SystemMessgaeInfoBean systemMessgaeInfoBean = new SystemMessgaeInfoBean();
		if (list == null||list.size() == 0) {
			SystemMessgaeEntity LastSystemMessgaeEntity = systemMessgaeDao.findLastSystemMessgaeEntity(player.getId());
			if (LastSystemMessgaeEntity == null) {
				return systemMessgaeInfoBean;
			}
			systemMessgaeInfoBean.setContent(LastSystemMessgaeEntity.getContent());
			systemMessgaeInfoBean.setUnreadNum(0);
			systemMessgaeInfoBean.setUpdateTime(LastSystemMessgaeEntity.getCreateTime());
			return systemMessgaeInfoBean;
		}
		PublicSystemMessageEntity publicSystemMessageEntity = publicSystemMessageService.load(player.getId());
		//公共消息里有没有已读消息		
		int size = list.size();
			for (SystemMessgaeEntity systemMessgaeEntity : list) {
				if (publicSystemMessageEntity.getSystemMessageSet().contains(systemMessgaeEntity.getId())) {
					size--;
				}
			}		
		if (size == 0) {
			SystemMessgaeEntity LastSystemMessgaeEntity = systemMessgaeDao.findLastSystemMessgaeEntity(player.getId());
			if (LastSystemMessgaeEntity == null) {
				return systemMessgaeInfoBean;
			}
			systemMessgaeInfoBean.setContent(LastSystemMessgaeEntity.getContent());
			systemMessgaeInfoBean.setUnreadNum(0);
			systemMessgaeInfoBean.setUpdateTime(LastSystemMessgaeEntity.getCreateTime());
			return systemMessgaeInfoBean;
		}
		systemMessgaeInfoBean.setUnreadNum(size);
		systemMessgaeInfoBean.setContent(list.get(size-1).getContent());
		systemMessgaeInfoBean.setUpdateTime(list.get(size-1).getCreateTime()); 
		return systemMessgaeInfoBean;
		
	}	
	/**系统获取指定玩家消息*/
	public List<ResSystemMessgae> getPlayerMessgeByPlayerId(long playerId) {
		List<SystemMessgaeEntity> list = systemMessgaeDao.findByPlayerId(playerId);
		List<ResSystemMessgae> resList = new ArrayList<ResSystemMessgae>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (SystemMessgaeEntity systemMessgaeEntity : list) {
			ResSystemMessgae resSystemMessgae = new ResSystemMessgae();
			resSystemMessgae.setPlayerId(String.valueOf(systemMessgaeEntity.getPlayerId()));
			resSystemMessgae.setContent(systemMessgaeEntity.getContent());
			if (systemMessgaeEntity.getSendType() == SendType.toplayer.ordinal()) {
				resSystemMessgae.setFromUser("超级管理员");
				resSystemMessgae.setToUser(systemMessgaeEntity.getNickname());
				resSystemMessgae.setSendTime(sdf.format(systemMessgaeEntity.getCreateTime()));
			}
			if (systemMessgaeEntity.getSendType() == SendType.fromplayer.ordinal()) {
				resSystemMessgae.setFromUser(systemMessgaeEntity.getNickname());
				resSystemMessgae.setToUser("超级管理员");
				resSystemMessgae.setSendTime(sdf.format(systemMessgaeEntity.getCreateTime()));
			}
			resList.add(resSystemMessgae);
		}
		return resList;
	}
	
	/**根据手机号发送消息*/
	public boolean systemSendMessgaeByPhone(String content,int contentType,String phone) {
		UserEntity userEntity = userDao.findByID(phone);
		if (userEntity == null) {
			return false;
		}
		systemSendMessgae(content,contentType,userEntity.getPlayerId());
		GMDataChange.recordChange("通过手机号给玩家发送消息  手机号为",phone);
		return true;
	}
	/**根据角色ID发送消息*/
	public boolean systemSendMessgaeByRoleId(String content,int contentType,String roleId) {
		Long playerId = playerServiceImpl.getPlayerByRoleId(roleId);
		if (playerId == null) {
			return false;
		}
		systemSendMessgae(content,contentType,playerId);
		GMDataChange.recordChange("通过roleId给玩家发送消息  roleId为",roleId);
		return true;
	}
	
	/**
	 *  玩家是否有未读消息 
	 * @param toPlayerId
	 * @return 返回玩家消息状态 ： 1=有，0没有
	 */
	public int getPlayerUnreadState(long playerId) {
		SystemMessgaeEntity systemMessgaeEntity = systemMessgaeDao.findOneUnreadReply(playerId);
		if (systemMessgaeEntity == null) {
			return 0;
		} 
		return 1;
	}	
}
