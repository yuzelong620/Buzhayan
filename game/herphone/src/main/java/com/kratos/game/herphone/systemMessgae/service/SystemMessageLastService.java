package com.kratos.game.herphone.systemMessgae.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost.SendType;
import com.kratos.game.herphone.systemMessgae.bean.ResSystemMessgae;
import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeEntity;
import com.kratos.game.herphone.systemMessgae.entity.SystemMessgaeLastEntity;

@Service
public class SystemMessageLastService extends BaseService{
	/**获取列表：玩家与系统之间最后一条消息*/
		public List<ResSystemMessgae> listSystemMessageLast(int page,int count) {
			List<SystemMessgaeLastEntity> list = systemMessageLastDao.findList(page, count);
			if (list == null||list.size() == 0) {
				list = new ArrayList<SystemMessgaeLastEntity>();
			}
			List<ResSystemMessgae> resList = new ArrayList<ResSystemMessgae>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			
			for (SystemMessgaeLastEntity systemMessgaeLastEntity : list) {
				ResSystemMessgae resSystemMessgae = new ResSystemMessgae();
				resSystemMessgae.setPlayerId(String.valueOf(systemMessgaeLastEntity.getPlayerId()));
				resSystemMessgae.setContent(systemMessgaeLastEntity.getContent());
				if (systemMessgaeLastEntity.getSendType() == SendType.toplayer.ordinal()) {
					resSystemMessgae.setFromUser("超级管理员");
					resSystemMessgae.setToUser(systemMessgaeLastEntity.getPlayerName());
					resSystemMessgae.setSendTime(sdf.format(systemMessgaeLastEntity.getCreateTime()));
				}
				if (systemMessgaeLastEntity.getSendType() == SendType.fromplayer.ordinal()) {
					resSystemMessgae.setFromUser(systemMessgaeLastEntity.getPlayerName());
					resSystemMessgae.setToUser("超级管理员");
					resSystemMessgae.setSendTime(sdf.format(systemMessgaeLastEntity.getCreateTime()));
				}
				resList.add(resSystemMessgae);
			}
			return resList;
		}
		/**更新玩家与系统最后一条*/
		public void updateSystemMessageLast(long playerId,SystemMessgaeEntity systemMessgaeEntity) {
			SystemMessgaeLastEntity systemMessgaeLastEntity = load(playerId);
			systemMessgaeLastEntity.setPlayerName(systemMessgaeEntity.getNickname());
			systemMessgaeLastEntity.setContent(systemMessgaeEntity.getContent());
			systemMessgaeLastEntity.setSendType(systemMessgaeEntity.getSendType());
			systemMessgaeLastEntity.setCreateTime(systemMessgaeEntity.getCreateTime());
			systemMessageLastDao.save(systemMessgaeLastEntity);			
		}
		public SystemMessgaeLastEntity load(long playerId) {
			SystemMessgaeLastEntity systemMessgaeLastEntity = systemMessageLastDao.findByID(playerId);
			if (systemMessgaeLastEntity == null) {
				systemMessgaeLastEntity = new SystemMessgaeLastEntity();
				systemMessgaeLastEntity.setPlayerId(playerId);
			}
			return systemMessgaeLastEntity;
		}
		
		/**
		 * 查询历史通知记录总条数
		 */
		public long getInforCount() {
			return systemMessageLastDao.getInforCount();
		}
}
