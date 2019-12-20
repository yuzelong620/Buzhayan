package com.kratos.game.herphone.system.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.github.kevinsawicki.http.HttpRequest;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.dynamic.bean.DynamicBean;
import com.kratos.game.herphone.dynamic.entity.DynamicEntity;
import com.kratos.game.herphone.event.WorkManager;
import com.kratos.game.herphone.message.bean.DiscussBean;
import com.kratos.game.herphone.message.bean.MessageBean;
import com.kratos.game.herphone.message.entity.MessageEntity;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;
import com.kratos.game.herphone.system.cost.SystemCost;
import com.kratos.game.herphone.util.HttpClientUtils;
 
/**
 * 系统通知服务。
 * @author 
 *
 */
@Service
public class SystemService extends BaseService{
	@Value("${spring.centerServerHost}")
	private String centerServerHost;
	/**
	 * 发送消息私聊回复
	 * @param message
	 */
	public void sendMessage(MessageEntity message){ 
		Runnable run=new Runnable(){
			public void run(){
				MessageBean bean=new MessageBean(message);
				String json=JSONObject.toJSONString(bean);
				sendToCenterServer(SystemCost.PATH_SEND_MESSAGE,json);
			}
		};
		//异步发送给中心服务器
		WorkManager.getInstance().putNoticeTask(run);
	}
	/**
	 * 发送广场动态回复
	 * @param dynamicReply
	 */
	public void sendDynamicReply(DynamicEntity dynamicReply){
		Runnable run=new Runnable() {
			public void run() {
				DynamicBean  bean=new DynamicBean(dynamicReply);
				String json=JSONObject.toJSONString(bean);
				sendToCenterServer(SystemCost.PATH_SEND_DYNAMIC_REPLY,json);
			}
		};
		//异步发送给中心服务器
		WorkManager.getInstance().putNoticeTask(run);
	}
	/**
	 * 发送 剧本评论回复
	 * @param discussReply
	 */
	public void sendDiscussReply(DiscussEntity discussReply){
		Runnable run=new Runnable() {
			public void run() {
				DiscussBean bean=new DiscussBean(discussReply);
				String json=JSONObject.toJSONString(bean);
				sendToCenterServer(SystemCost.PATH_SEND_DISCUSS_REPLY, json);
			}
		};
		WorkManager.getInstance().putNoticeTask(run);
	}
	/**
	 * 发送消息给中心服务器 
	 * @param servletPath 请求地址
	 * @param jsonData 发送的数据
	 */
	private void sendToCenterServer(String servletPath, String json){
		 Map<String, Object> params=new HashMap<>();
		 params.put(SystemCost.PARAM_JSON_KEY, json);
		 HttpClientUtils.sendGet(centerServerHost+servletPath, params);
	}
	public PlayerOnlineEntity findTimeSettlement(long playerId) {
		
		PlayerOnlineEntity playerOnlineEntity = playerOnlineService.load(playerId);
		long nowTime = System.currentTimeMillis();
		long onTime = nowTime - playerOnlineEntity.getLastTime();
		long deferTime = (onTime - (1 * 60 * 1000));
		if (onTime > 1 * 60 * 1000) {
			// 超过1分钟以后再次判断玩家请求是否在没有超过5秒 如果没有都按照网络延迟计算 后续继续更新在线时长
			if (deferTime > 0 && deferTime < 5000) {
				playerOnlineEntity.setOnlineTime(playerOnlineEntity.getOnlineTime() + onTime - deferTime);
				playerOnlineEntity.setLastTime(nowTime);
			} else {
				playerOnlineEntity.setLastTime(nowTime);
			}
		} else {
			playerOnlineEntity.setOnlineTime(playerOnlineEntity.getOnlineTime() + onTime);
			playerOnlineEntity.setLastTime(nowTime);
		}
		playerOnlineDao.save(playerOnlineEntity);
		return playerOnlineDao.findByID(playerId);
	}
}
