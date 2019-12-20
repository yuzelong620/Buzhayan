package com.kratos.game.herphone.watchVideo.service;

import org.springframework.stereotype.Service;

import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.watchVideo.bean.TakeWatchVideoRes;
import com.kratos.game.herphone.watchVideo.entity.WatchVideoEntity;
@Service
public class WatchVideoService extends BaseService{
	/**看视频奖励时间*/
	public static final long watchvideo_limit_time=1000*60*60;
	/**看视频奖励 体力值 */
	public static final int watchvideo_reward_power=10;
	
    public WatchVideoEntity load(long playerId){
    	 WatchVideoEntity entity=watchVideoDao.findByID(playerId);
    	 if(entity==null){
    		 entity=new WatchVideoEntity();
    		 entity.setPlayerId(playerId);
    		 watchVideoDao.save(entity);
    	 }
    	 return entity;
    }
	 
	public TakeWatchVideoRes takeWatchVideo() { 
      Player player = PlayerContext.getPlayer();
      WatchVideoEntity entity=load(player.getId());
      long limitTime=entity.getLastWatchTime()+watchvideo_limit_time;
      long currentTime=System.currentTimeMillis();
      if(limitTime>currentTime){
    	  throw new BusinessException("还没有到时间。");
      } 
      //增加体力
      player.addPower(watchvideo_reward_power, true);
      playerServiceImpl.cacheAndPersist(player.getId(), player);
      //保存数据
      entity.setLastWatchTime(currentTime);
      watchVideoDao.save(entity);
      //完成每日任务,调用任务更新接口
      taskService.playerScheduleUpdate(CommonCost.TackType.watchVideo.ordinal(),1);
      return new TakeWatchVideoRes(player.getPower());
	}

	public long nextTime() {
		Player player = PlayerContext.getPlayer();
	    WatchVideoEntity entity=load(player.getId());
		return entity.getLastWatchTime();
	}
}
