package com.kratos.game.herphone.systemNotice.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.ReadState;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.message.bean.MessageInfoBean;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.systemMessgae.entity.PublicSystemMessageEntity;
import com.kratos.game.herphone.systemNotice.bean.ResSystemNoticeInfo;
import com.kratos.game.herphone.systemNotice.entity.SystemNoticeEntity;

@Service
public class SystemNoticeService extends BaseService{
	/**玩家获取系统通知*/
	public List<ResSystemNoticeInfo> getSystemNotice(int page) {
		int count = GameParamsCache.getGameParams_Json().getMessagePageCount();
		Player player = PlayerContext.getPlayer();
		List<SystemNoticeEntity> list = systemNoticeDao.findByPlayerIdLimit(player.getId(),count,page);
		List<ResSystemNoticeInfo> reslist = new ArrayList<ResSystemNoticeInfo>();
		if (list == null|| list.size() == 0) {
			return reslist;
		}
		PublicSystemMessageEntity publicSystemMessageEntity = publicSystemMessageService.load(player.getId());
		for (SystemNoticeEntity systemNoticeEntity : list) {
		//如果消息类型是公共通知且PublicSystemMessageEntity实体类中没有这个消息id，将此消息添加到公共系统消息类里
			if (systemNoticeEntity.getAnnouncement() == 1 && !publicSystemMessageEntity.getSystemNotice().contains(systemNoticeEntity.getId())) {
				publicSystemMessageDao.addSystemNotice(player.getId(), systemNoticeEntity.getId());
			}
			ResSystemNoticeInfo resSystemNoticeInfo = new ResSystemNoticeInfo();
			resSystemNoticeInfo.setContent(systemNoticeEntity.getContent());
			resSystemNoticeInfo.setCreatTime(systemNoticeEntity.getCreatTime());
			resSystemNoticeInfo.setType(systemNoticeEntity.getType());
			resSystemNoticeInfo.setReadState(systemNoticeEntity.getReadState());
			resSystemNoticeInfo.setNoticeId(systemNoticeEntity.getId());
			reslist.add(resSystemNoticeInfo);	
		}
			
	        Comparator<ResSystemNoticeInfo> comparator = new Comparator<ResSystemNoticeInfo>() {			
				@Override
				public int compare(ResSystemNoticeInfo o1, ResSystemNoticeInfo o2) {
					long sub = o2.getCreatTime() - o1.getCreatTime();
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
		systemNoticeDao.updateSystemNoticeUnread(player.getId());
		return reslist;
	}
	/**获取系统通知未读条数*/
	public long getSystemNoticeUnReadNum() {
		Player player = PlayerContext.getPlayer();
		List<SystemNoticeEntity> list = systemNoticeDao.findUnReadState(player.getId(), ReadState.unread.ordinal());
		if (list == null||list.size() == 0) {			
			return 0;
		}
		PublicSystemMessageEntity publicSystemMessageEntity = publicSystemMessageService.load(player.getId());
		int count =list.size();
		for (SystemNoticeEntity systemNoticeEntity : list) {
			if (publicSystemMessageEntity.getSystemNotice().contains(systemNoticeEntity.getId())) {		
				count--;
			}
		}
		return count;
	}
	/**向玩家发送系统通知*/
	public void sendSystemNotice(String content,int type,long ...playerIds) {
		for (long playerId : playerIds) {
			PlayerDynamicEntity playerDynamicEntity = playerDynamicService.load(playerId);
			SystemNoticeEntity systemNoticeEntity = new SystemNoticeEntity();
			systemNoticeEntity.setId(UUID.randomUUID().toString().replace("-", ""));
			systemNoticeEntity.setPlayerId(playerDynamicEntity.getPlayerId());
			systemNoticeEntity.setNickName(playerDynamicEntity.getNickName());
			systemNoticeEntity.setRoleId(playerDynamicEntity.getRoleId());
			systemNoticeEntity.setType(type);
			systemNoticeEntity.setContent(content);
			systemNoticeEntity.setAnnouncement(0);
			systemNoticeEntity.setReadState(0);
			systemNoticeEntity.setCreatTime(System.currentTimeMillis());
			systemNoticeDao.save(systemNoticeEntity);
		}
	}
	/**向玩家发送系统公共通知*/
	public void sendSystemNoticeAnnouncement(String content,int type) {		
			SystemNoticeEntity systemNoticeEntity = new SystemNoticeEntity();
			systemNoticeEntity.setId(UUID.randomUUID().toString().replace("-", ""));
			systemNoticeEntity.setPlayerId(0);
			systemNoticeEntity.setNickName("");
			systemNoticeEntity.setRoleId("");
			systemNoticeEntity.setType(type);
			systemNoticeEntity.setContent(content);
			systemNoticeEntity.setAnnouncement(1);
			systemNoticeEntity.setReadState(0);
			systemNoticeEntity.setCreatTime(System.currentTimeMillis());
			systemNoticeDao.save(systemNoticeEntity);
	}
	
	/**
	 * 设置玩家消息已读状态
	 */
	public void setReadState(Set<String> systemNoticeIds) {
		// TODO Auto-generated method stub
		Player player = PlayerContext.getPlayer();
		systemNoticeDao.setReadState(player.getId(),systemNoticeIds);
	}
}
