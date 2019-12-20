package com.kratos.game.herphone.signIn.service;


import java.util.Calendar;
import org.springframework.stereotype.Service;

import com.globalgame.auto.json.SignInReward_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.SignInRewardCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.signIn.entity.SignInEntity;

@Service
public class SignInService extends BaseService{ 
  
	public SignInEntity load(long playerId){
		SignInEntity entity=signInDao.findByID(playerId+"");
		if(entity!=null){
			return entity;
		}
		long currentTime=System.currentTimeMillis();
		entity=new SignInEntity(playerId+"",  0, currentTime, false);
		signInDao.save(entity);
		return entity;
	}
	/**
	 * 获取签到信息
	 * @return
	 */
	 	public SignInEntity getSignin(){
		Player player=PlayerContext.getPlayer();
		SignInEntity signin = load(player.getId());
		long currentTime=System.currentTimeMillis();
		//判断今天是否签到过
		if (isSameDay(currentTime,signin.getLastDay())) {
			return signin;
		}
		//判断与上次签到是否在同一周
		if (isSameDate(currentTime,signin.getLastDay())) {
			signin.setLastDay(currentTime);
			signin.setCurrentGeted(false);
			signInDao.save(signin);
			return signin;
		}
		//重置本周签到
		signin.setLastDay(currentTime);
		signin.setCurrentGeted(false);
		signin.setDays(0);
		signInDao.save(signin);
		return signin;
	}
	 /**
	  * 签到 
	  */
	 public SignInEntity doSignin(){
		Player player=PlayerContext.getPlayer();
		SignInEntity signin=load(player.getId());
		long currentTime=System.currentTimeMillis();
		if (!isSameDay(currentTime,signin.getLastDay())) {
			throw new BusinessException("没执行获取签到接口你执行什么签到接口？");
		}
		if (signin.isCurrentGeted() == true) {
			throw new BusinessException("你今天签到了还执行什么签到接口？");
		}
		signin.setCurrentGeted(true);
		int days = signin.getDays() + 1;
		signin.setDays(days);	
		signInDao.save(signin);
		SignInReward_Json reward_Json = JsonCacheManager.getCache(SignInRewardCache.class).getData(days);
		commonService.add(player.getId(),reward_Json.getList());
		return signin;
		 
	 } 	
	 
	 //判断两个日期是否为同一周
	 public  boolean isSameDate(long date1, long date2) { 	
	 	 Calendar cal1 = Calendar.getInstance();
	 	 Calendar cal2 = Calendar.getInstance();
	 	 cal1.setFirstDayOfWeek(Calendar.MONDAY);//西方周日为一周的第一天，将周一设为一周第一天
	 	 cal2.setFirstDayOfWeek(Calendar.MONDAY);
	 	 cal1.setTimeInMillis(date1);
	 	 cal2.setTimeInMillis(date2);
	 	 int subYear = cal1.get(Calendar.YEAR) - cal2.get(Calendar.YEAR);
	 	 if (subYear == 0){// subYear==0,说明是同一年
	 	   if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	 	    return true;
	 	  } 
	 	 	else if (subYear == 1 && cal2.get(Calendar.MONTH) == 11){ //subYear==1,说明cal比cal2大一年;java的一月用"0"标识，那么12月用"11"
	 	 		if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	 	 		return true;
	 	  }
	 	 	else if (subYear == -1 && cal1.get(Calendar.MONTH) == 11){ //subYear==-1,说明cal比cal2小一年
	 	 		if (cal1.get(Calendar.WEEK_OF_YEAR) == cal2.get(Calendar.WEEK_OF_YEAR))
	 	 			return true;
	 	  }
	 	  	return false;
	 	}
	 //判断两个日期是否为同一天
	 public  boolean isSameDay(long date1, long date2) {
		Calendar calDateA = Calendar.getInstance();
		calDateA.setTimeInMillis(date1);	 
		Calendar calDateB = Calendar.getInstance();
		calDateB.setTimeInMillis(date2);
		
		return calDateA.get(Calendar.YEAR) == calDateB.get(Calendar.YEAR)
					&& calDateA.get(Calendar.MONTH) == calDateB.get(Calendar.MONTH)
					&& calDateA.get(Calendar.DAY_OF_MONTH) == calDateB
							.get(Calendar.DAY_OF_MONTH);
		}

	 	
	 	
	 	
 	/**
	 * 签到
	 *//*
	public DoSigninRes doSignin(){
		Player player=PlayerContext.getPlayer();
		SignInEntity  signin=load(player.getId());
		long currentTime=System.currentTimeMillis();
		int currentDay=DateUtil.getCurrentDateInt(currentTime);
		if(signin.getLastDay()==currentDay){
			if(signin.isCurrentGeted()){
				throw new BusinessException("您今天已经签到过了");
			}
		}
		if(signin.getLastDay()!=currentDay){
			signin.setLastDay(currentDay);
			signin.setDays(signin.getDays()+1);
			signin.setCurrentGeted(false);
		}
		
		//增加奖励
		Calendar cal=Calendar.getInstance();
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int maxDays=cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		int addPower=0;
		if(day==10||day==15||day==maxDays){//每月10 15 月末最后一天给固定50
			addPower=50;
		}
		else{
			addPower=new Random().nextInt(45)+5;
		}
		player.addPower(addPower, true);
		//保存
		playerService.cacheAndPersist(player.getId(),player);
		//记录领取状态
		signin.setCurrentGeted(true);		
		signin.getHostoryRewardTimes().add(cal.getTimeInMillis());//记录奖励时间		
		if(signin.getHostoryRewardTimes().size()>7){
			Collections.sort(signin.getHostoryRewardTimes());//排序
			signin.getHostoryRewardTimes().remove(0);//删除最小的一个
		}
		signInDao.save(signin);		
		return new DoSigninRes(player.getPower(), addPower, signin);		
	}	*/
}
