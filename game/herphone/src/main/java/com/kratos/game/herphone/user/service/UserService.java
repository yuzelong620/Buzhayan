package com.kratos.game.herphone.user.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResPlayerLogin;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.user.entity.UserEntity;

@Component
public class UserService extends BaseService{
	
	//手机绑定用户
	public Player phoneBinding(String phone){
		Player player = PlayerContext.getPlayer();
		UserEntity userEntity = userDao.findByPlayerId(player.getId());
		if (userEntity != null) {
			throw new BusinessException("您已经绑定其他手机");
		}
		long timeStemp = System.currentTimeMillis();
		userEntity = userDao.findByID(phone);
		if (userEntity != null) {
			throw new BusinessException("该手机已绑定其他用户");
		}
		userEntity = new UserEntity(player.getId(), phone, timeStemp);
		player.addPower(50, true);
		if (player.getIsGuest() == 1) {
			player.setIsGuest(0);
		}
		playerServiceImpl.cacheAndPersist(player.getId(), player);
		playerDynamicService.synchdata(player);
		userDao.save(userEntity);
		return player;
	}
	
	//手机登录
	public ResPlayerLogin validationBinding(String phone){
		UserEntity userEntity = userDao.findByID(phone);
		if (userEntity == null ) {
			throw new BusinessException("用户不存在,请前往注册");
		}
		long playerId = userEntity.getPlayerId();
		Player player = playerServiceImpl.get(playerId);
		if (player.getIsGuest() == 1) {
			player.setIsGuest(0);
			playerServiceImpl.cacheAndPersist(playerId, player);
		}
		playerServiceImpl.loginExecute(player);
	    ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
	    resPlayerLogin.setCreate(false);
//	    OnFire.fire(new PlayerLoginEvent(player));
	    resPlayerLogin.setToken(player.getToken());
	    resPlayerLogin.setPlayer(new ResPlayerProfile(player));
	    PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
        if(playerDynamicEntity != null){
        	ResPlayerProfile resPlayerProfile = resPlayerLogin.getPlayer();
        	resPlayerProfile.setAchievementTags(playerDynamicEntity.getAchievementTags());
        	resPlayerProfile.setAvatarFrame(playerDynamicEntity.getAvatarFrame());
        }
		return resPlayerLogin;
	}
	//手机注册用户
	public ResPlayerLogin registerByPhone(String phone){
		UserEntity userEntity = userDao.findByID(phone);
		
		if (userEntity != null ) {
			throw new BusinessException("用户已存在,请直接登录");
		}
		Player player = playerServiceImpl.guestRegisterByPhone();
		ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
	    resPlayerLogin.setCreate(true);
//	    OnFire.fire(new PlayerLoginEvent(player));
	    resPlayerLogin.setToken(player.getToken());
	    resPlayerLogin.setPlayer(new ResPlayerProfile(player));
	    long timeStemp = System.currentTimeMillis();
	    userEntity = new UserEntity(player.getId(), phone, timeStemp);
	    userDao.save(userEntity);
	    playerDynamicService.synchdata(player);
	    playerLoginTimeService.updatePlayerLogin(player.getId());
	    return resPlayerLogin;
	}
	//验证用户绑定状态
	public String  GetUser() {
		Player player = PlayerContext.getPlayer();
		UserEntity userEntity = userDao.findByPlayerId(player.getId());
		if (userEntity == null) {
			return "";
		}
		return userEntity.getPhone();
	}
	
	//------------------------------------------------------------------

    /**
     * GM为机器人绑定手机号
     */
	public void gmAddPhone(String phone,long playerId){
		UserEntity userEntity = userDao.findByID(phone);
		if(userEntity == null) {
			userEntity = new UserEntity(playerId, phone, System.currentTimeMillis());
			userDao.save(userEntity);
			GMDataChange.recordChange("为机器人ID" + playerId + "绑定手机号\t绑定的手机号码为",phone);
		} else {
			throw new BusinessException("该手机号已经绑定过剧本");
		}
		
	}

	/**
	 * 删除机器人绑定的手机号
	 */
	public void gmDeletePhone(long playerId) {
		userDao.gmDeletePhone(playerId);
		GMDataChange.recordChange("删除机器人绑定的手机号\t机器人ID为",playerId);
	}

	/**
	 * 修改机器人绑定的手机号
	 */
	public void gmUpdatePhone(long playerId, String phone) {
		UserEntity userEntity = userDao.findByID(phone);
		if(userEntity == null) {
			userDao.gmDeletePhone(playerId);
			userEntity = new UserEntity(playerId, phone, System.currentTimeMillis());
			userDao.save(userEntity);
			GMDataChange.recordChange("修改ID:"+ playerId +"的玩家\t手机号为",phone);
		} else {
			throw new BusinessException("该手机号已经绑定过剧本");
		}
	}
	
	/**
	 * 通过playerId查询玩家手机号
	 */
	public String findOneByPlayerPhone(long playerId) {
		UserEntity userEntity = userDao.findByPlayerId(playerId);
		if(userEntity != null) {
			return userEntity.getPhone();
		}
		return "玩家未绑定手机号";
	}
	
	/**
	 * 通过playerId查询玩家手机号
	 */
	public Map<Long,String> findByPlayerPhone(List<Long> playerId) {
		List<UserEntity> userEntityList = userDao.findAllByPlayerIds(playerId);
		Map<Long,String> map = new HashMap<Long, String>();
		for (UserEntity userEntity : userEntityList) {
			map.put(userEntity.getPlayerId(), userEntity.getPhone());
		}
		return map;
	}
	
	/**
	 * 根据手机号查询绑定信息
	 */
	public UserEntity findByPhoneData(String phone) {
		return userDao.findByID(phone);
	}
}
