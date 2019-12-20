package com.kratos.game.herphone.playerDynamic.service;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.globalgame.auto.json.GameParams_Json;
import com.globalgame.auto.json.LevelReward_Json;
import com.globalgame.auto.json.Level_Json;
import com.globalgame.auto.json.Title_Json;
import com.kratos.engine.framework.common.CommonConstant;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.attention.entity.AttentionEntity;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.clue.entity.PlayerClueEntity;
import com.kratos.game.herphone.common.BaseService;
import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.common.bean.PlayerExtraBean;
import com.kratos.game.herphone.event.WorkManager;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.json.datacache.LevelCache;
import com.kratos.game.herphone.json.datacache.LevelRewardCache;
import com.kratos.game.herphone.json.datacache.TitleCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.bean.PlayerAstrictBean;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.bean.GMPlayerDataBean;
import com.kratos.game.herphone.playerDynamic.bean.ReqPlayerDynamic;
import com.kratos.game.herphone.playerDynamic.bean.ResCompleteBoolean;
import com.kratos.game.herphone.playerDynamic.bean.ResGameOverBean;
import com.kratos.game.herphone.playerDynamic.bean.ResItemTitleBean;
import com.kratos.game.herphone.playerDynamic.bean.ResPlayerDynamic;
import com.kratos.game.herphone.playerDynamic.bean.ResTitleAchieved;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.playerOnline.entity.PlayerLoginTimeEntity;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;
import com.kratos.game.herphone.user.entity.UserEntity;


@Component
public class PlayerDynamicService extends BaseService{
	private  static PlayerDynamicService instance;
	private static int upgrade = 10; //普通升级奖励10眨眼星币
	public PlayerDynamicService() {
		instance = this;
	}
	
	public static PlayerDynamicService getInstance() {
		return instance;
	}
	/**从player表转换数据到playerDynamic */
	@Deprecated
	public void init() {
		int page = 1;
		int size = 0;
		do {
			List<Player> list = playerServiceImpl.listInitPlayer(page);	
			size = list.size();			
			for (Player player : list) {
				PlayerDynamicEntity playerDynamicEntity = PlayerDynamicService.instance.load(player);
				recalculate(player,playerDynamicEntity);
			}
			page++;
		}while(size < 500);
	}
	/**重新计算*/
	public void recalculate(Player player,PlayerDynamicEntity playerDynamicEntity) {
		 int oldclue = player.getExploration(); 
	   	 double newclue = (oldclue  * 0.075 * 20)/ 61.54 ;
	   	 int sumClue = (int) Math.round(newclue);	
	   	 PlayerClueEntity playerClueEntity = playerClueService.load(player.getId());
	   	 sumClue = sumClue +playerClueService.getClueValue(playerClueEntity);
	   	 double oldAch = player.getAchievement() * 1.0; 
		 double newAch = (oldAch  * 0.031 * 10)/ 150.19 ;
		 int achievement = (int) Math.round(newAch);
		 achievement = achievementService.getAchievementNum(player.getId()) + achievement;
		 playerDynamicDao.recalculate(player.getId(),achievement,sumClue);
		 
	}
	/**
	 * 是否包含一个 称号（护眼大队等）
	 * @param player
	 * @param titleItemId
	 * @return
	 */
	public boolean containsTitleItemId(Player player,int titleItemId){
		PlayerDynamicEntity obj=load(player);
		return obj.getItemTitle().contains(titleItemId);
	}
	/**玩家手动升级并领取奖励*/
	public void upgradeLevel(){
		Player player = PlayerContext.getPlayer();	
		PlayerDynamicEntity playerDynamicEntity = load(player);
		Level_Json level_Json = JsonCacheManager.getCache(LevelCache.class).getData(playerDynamicEntity.getLevel()+1);	
		if (level_Json == null) {
			throw new BusinessException("您的等级已达上限");
		}
		if (playerDynamicEntity.getExp() < level_Json.getExp()) {
			throw new BusinessException("经验不足");
		}
		//升级并扣除升级所需的经验	
		playerDynamicDao.upgrade(player.getId(), playerDynamicEntity.getExp() - level_Json.getExp(), playerDynamicEntity.getLevel()+1);
		//获取奖励(是否包含特殊奖励)
		List<LevelReward_Json> levelReward_Jsons = JsonCacheManager.getCache(LevelRewardCache.class).getList();
		for (LevelReward_Json levelReward_Json : levelReward_Jsons) {
			if (levelReward_Json.getLevel() == playerDynamicEntity.getLevel()) {
				commonService.add(player.getId(), levelReward_Json.getList());
				return;
			}
		}
		playerDynamicDao.updateCurrency(player.getId(),upgrade);
	}
	/**佩戴成就徽章*/
	public void wearAchievementBadges(int achievementBadgeId) {
		Player player = PlayerContext.getPlayer();
		BagEntity bagEntity = bagService.load(player.getId());
		if (!bagEntity.getBagItems().containsKey(achievementBadgeId)&&achievementBadgeId!=0) {
			throw new BusinessException("参数错误");
		}
		PlayerDynamicEntity playerDynamicEntity =load(player.getId());
		if (playerDynamicEntity.getAchievementTags() == achievementBadgeId) {			
			return;
		}
		playerDynamicDao.wearAchievementBadges(achievementBadgeId, player.getId());
	}
	/**佩戴头像框*/
	public void wearAvatarFrame(int avatarFrameId) {
		Player player = PlayerContext.getPlayer();
		BagEntity bagEntity = bagService.load(player.getId());
		if (!bagEntity.getBagItems().containsKey(avatarFrameId)&& avatarFrameId!=0) {
			throw new BusinessException("参数错误");
		}
		PlayerDynamicEntity playerDynamicEntity =load(player.getId());
		if (playerDynamicEntity.getAvatarFrame() == avatarFrameId) {
			return;
		}
		playerDynamicDao.wearAvatarFrame(avatarFrameId, player.getId());
	}
	/** 
	 * 获取自己的个人资料
	 */
	public ResPlayerDynamic getPlayerDeByMySelf() {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity playerDynamicEntity = load(player);
		return new ResPlayerDynamic(playerDynamicEntity);
	}
	/** 
	 * 根据playerId获取资料
	 */
	public ResPlayerDynamic getPlayerDeById(long otherPlayerId) { 
		Player otherplayer = playerServiceImpl.get(otherPlayerId);
		if (otherplayer == null) {
			throw new BusinessException("参数错误");
		}
		PlayerDynamicEntity playerDynamicEntity =load(otherPlayerId);
		//获取玩家之间关注状态
		Player player = PlayerContext.getPlayer();
		ArrayList<String> ids = new ArrayList<String>();
		String myAndOther= attentionService.createId(player.getId(), otherPlayerId);
		String otherAndMy =attentionService.createId(otherPlayerId, player.getId());
		ids.add(myAndOther);		
		ids.add(otherAndMy);
		List<AttentionEntity> attentionEntities = attentionDao.seachAttentions(ids);
		int state =0;
		state = getState(attentionEntities, otherPlayerId);
		return new ResPlayerDynamic(playerDynamicEntity,state);
	}
	
	public PlayerDynamicEntity load(Player player) {		
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		if (playerDynamicEntity == null) {
			playerDynamicEntity = new PlayerDynamicEntity(player);			
			playerDynamicDao.save(playerDynamicEntity);
		}
		return playerDynamicEntity;
	}
	public PlayerDynamicEntity load(Long playerId) {		
		PlayerDynamicEntity playerDynamicEntity =playerDynamicDao.findByID(playerId);
		if (playerDynamicEntity == null) {
			Player player = playerServiceImpl.get(playerId);
			playerDynamicEntity = new PlayerDynamicEntity(player);	
			playerDynamicDao.save(playerDynamicEntity);
		}				
		return playerDynamicEntity;
	}
	/** 
	 * 修改个人资料
	 */
	public PlayerDynamicEntity updatePlayerDynamic(ReqPlayerDynamic reqPlayerDynamic) {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity playerDynamicEntity = load(player);		
		playerDynamicEntity.setReqPlayerDynamic(playerDynamicEntity, reqPlayerDynamic);
		player.setGender(playerDynamicEntity.getSex());
		player.setAvatarUrl(playerDynamicEntity.getHeadImgUrl());
		try {
			player.setNickName(new String(Base64.getEncoder().encode(playerDynamicEntity.getNickName().getBytes("utf-8")), CommonConstant.UTF8));	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
		playerServiceImpl.cacheAndPersist(player.getId(), player);
		playerDynamicDao.updatePlayerDms(playerDynamicEntity);
		// 异步修改个性签名、昵称、头像的方法
		Runnable runnable = new Runnable() {			
			@Override
			public void run() {
				attentionService.updatePlayerInfo(playerDynamicEntity);
			}
			@Override
			public int hashCode(){			
				return Long.hashCode(playerDynamicEntity.getPlayerId());
			}
		};
		WorkManager.getInstance().putEvent(runnable);
		return playerDynamicEntity;	
	}
	
	public PlayerExtraBean getExtra(PlayerDynamicEntity playerDynamic) { 
		TitleCache cache=JsonCacheManager.getCache(TitleCache.class);
		List<Integer> list = playerDynamic.getItemTitle();
		
		double extra_recover_rote=0;
		int    extra_power_limit=0;
		for (Integer titleId : list) {
			Title_Json title_Json=cache.getData(titleId);
			if (title_Json != null) {
				if (title_Json.getType() == CommonCost.TitleType.addPowerMax.ordinal()) {
					extra_power_limit += title_Json.getValue();
				}
			}
		}
		return new PlayerExtraBean(extra_power_limit,extra_recover_rote);
	}
	/**判断是否可以申请护眼大队*/
	public ResCompleteBoolean getApplyStatusByBrigade() {
		Player player = PlayerContext.getPlayer();	
		long now = System.currentTimeMillis();
		PlayerLoginTimeEntity playerLoginTimeEntity = playerLoginTimeService.load(player.getId());
		int days = (int) ((now - player.getRegister())/(1000 * 3600 * 24));
		ResCompleteBoolean resCompleteBoolean = new ResCompleteBoolean();
		if (days >= 30) { //注册时间超过1个月
			resCompleteBoolean.setRegister(true);
		}
		if (playerLoginTimeEntity.getMaxLogin() >= 7 ) {		//连续登录7天以上
			resCompleteBoolean.setContinuouLogin(true);
		}
		if (player.getNoSpeakTime() !=0) {			//近30天没有被禁言
			if ((now - player.getNoSpeakTime()) < (30 * 24 * 1000 * 3600)) {
				resCompleteBoolean.setNoSpeakTime(false);
			}
		}
		return resCompleteBoolean;
	}
	/**判断是否可以申请护眼先锋*/
	public ResCompleteBoolean getApplyStatusByPioneer() {
		Player player = PlayerContext.getPlayer();	
		long now = System.currentTimeMillis();
		PlayerLoginTimeEntity playerLoginTimeEntity = playerLoginTimeService.load(player.getId());
		int days = (int) ((now - player.getRegister())/(1000 * 3600 * 24));
		ResCompleteBoolean resCompleteBoolean = new ResCompleteBoolean();
		if (days >= 60) { //注册时间超过2个月
			resCompleteBoolean.setRegister(true);
		}
		if (playerLoginTimeEntity.getMaxLogin() >= 7 ) {		//连续登录7天以上
			resCompleteBoolean.setContinuouLogin(true);
		}
		if (player.getNoSpeakTime() !=0) {			//近45天没有被禁言
			if ((now - player.getNoSpeakTime()) < (45 * 24 * 1000 * 3600)) {
				resCompleteBoolean.setNoSpeakTime(false);
			}
		}
		return resCompleteBoolean;
	}
	//获取玩家护眼大队称号进度信息   
	@Deprecated
	public ResTitleAchieved getTitleAchieved() {
		Player player = PlayerContext.getPlayer();
		ResTitleAchieved resTitleAchieved = new ResTitleAchieved();
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		//获取探索度前五十玩家id
		List<ResRankPlayer> resRankPlayerList = JedisUtils.getInstance().getList(AppCache.explorationList, ResRankPlayer.class);
		for (ResRankPlayer resRankPlayer : resRankPlayerList) {
			if (Long.valueOf(resRankPlayer.getPlayerId()).equals(player.getId())) {
				resTitleAchieved.setExplore(true);
				break;
			}
		}
		//获取成就值前五十玩家id
		List<ResRankPlayer> resRankPlayerList1 = JedisUtils.getInstance().getList(AppCache.achievementList, ResRankPlayer.class);		
		for (ResRankPlayer resRankPlayer : resRankPlayerList1) {
			if (Long.valueOf(resRankPlayer.getPlayerId()).equals(player.getId()) ) {
				resTitleAchieved.setAchievement(true);
				break;
			}
		}		
		//获取粉丝数量
		PlayerDynamicEntity playerDynamicEntity = load(player.getId());
		if (playerDynamicEntity.getFansCount() >= gameParams_Json.getTitleFansCount().intValue()) {
			resTitleAchieved.setFansCount(true);
		}
		//获取热门评论数量
//		int discussCount = discussDao.hotDiscussCount(player.getId());
		int discussCount = playerDynamicEntity.getHotDiscussCount();
		if (discussCount >= gameParams_Json.getTitleHotDiscussCount().intValue()) {
				resTitleAchieved.setHotComments(true);
		}
		//获取玩家是否完成10个游戏
		int gameCount = playerExplorationService.getGameCount(player.getId());
		if (gameCount >= gameParams_Json.getTitleGameCount().intValue()) {
			resTitleAchieved.setGameCount(true);
		}
		//获取在线时间
		PlayerOnlineEntity playerOnlineEntity = playerOnlineService.load(player.getId());
		if (playerOnlineEntity.getOnlineTime()> gameParams_Json.getTitleOnline().intValue() * 60 * 60 * 1000) {
			resTitleAchieved.setOnlineTime(true);
		}
		resTitleAchieved.setCumulativeTime(playerOnlineEntity.getOnlineTime());
		if (playerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {//1001为护眼大队称号ID
			resTitleAchieved.setHaveTitle(true);
		}
		return resTitleAchieved;
	}
	/**
	 * 获取成员列表的称号
	 * @param list
	 * @return
	 */
	public Map<Long, PlayerDynamicEntity> getItmeTitle(List<Long> playerIds){
		List<PlayerDynamicEntity> playerDynamicEntities = playerDynamicDao.getItemTitle(playerIds);
		Map<Long,PlayerDynamicEntity> map = new HashMap<Long, PlayerDynamicEntity>();
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntities) {
			map.put(playerDynamicEntity.getPlayerId(), playerDynamicEntity);
		}
		return map;
	}
	private int getState(List<AttentionEntity> attentionEntities,long playerId) {
		if (attentionEntities.size() == 2) {
			return 3;
		}
		if (attentionEntities.size() == 0) {
			return 0;
		}
		for (AttentionEntity attentionEntity : attentionEntities) {
			if (attentionEntity.getToPlayerId() == playerId) {
				return 1;
			}	
		}
		return 2;
	}
	
	/**
	 * 设置护眼大队 0设置失败 1设置成功 2没有此玩家
	 */
	public int setItemTitle(String roleId) {
		Long playerId = playerServiceImpl.getPlayerByRoleId(roleId);
		if (playerId == null) {
			throw new BusinessException("未找到玩家");
		}
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		PlayerDynamicEntity playerDynamicEntity = load(playerId);
		List<Integer> titleList = new ArrayList<Integer>();		
		titleList = playerDynamicEntity.getItemTitle();
		if(titleList.contains(gameParams_Json.getProtectEyesTitleId())) {
			return 0;
		}
		titleList.add(gameParams_Json.getProtectEyesTitleId());
		playerDynamicDao.setItemTitle(playerId,titleList,System.currentTimeMillis());
		commonService.resetPlayerExtra(playerService.get(playerDynamicEntity.getPlayerId()), playerDynamicEntity);
		GMDataChange.recordChange("通过ID设置护眼大队\tID为",roleId);
		return 1;	
	
	}
	/**
	 * 设置护眼大队 0设置失败 1设置成功 2没有此玩家
	 */
	public int setItemTitleByPhone(String phone) {
		UserEntity userEntity = userDao.findDataByPhone(phone);
		if(userEntity == null) {
			throw new BusinessException("未找到玩家");
		}
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		PlayerDynamicEntity playerDynamicEntity = load(userEntity.getPlayerId());
		List<Integer> titleList = new ArrayList<Integer>();
		titleList = playerDynamicEntity.getItemTitle();
		if(titleList.contains(gameParams_Json.getProtectEyesTitleId())) {
			return 0;
		}
		titleList.add(gameParams_Json.getProtectEyesTitleId());
		playerDynamicDao.setItemTitle(userEntity.getPlayerId(),titleList,System.currentTimeMillis());
		commonService.resetPlayerExtra(playerService.get(playerDynamicEntity.getPlayerId()), playerDynamicEntity);
		GMDataChange.recordChange("通过手机号设置护眼大队\t手机号码为",phone);
		return 1;
	}
	/**
	 * 查询护眼大队玩家
	 */
	public List<ResItemTitleBean> getItemTitle(int page,int count){
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		List<PlayerDynamicEntity> list = playerDynamicDao.getItemTitlePlayer(page,count,gameParams_Json.getProtectEyesTitleId());
		List<ResItemTitleBean> reslist = new ArrayList<>();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (PlayerDynamicEntity playerDynamicEntity : list) {
			ResItemTitleBean resItemTitleBean = new ResItemTitleBean();
			String playerId = String.valueOf(playerDynamicEntity.getPlayerId());
			resItemTitleBean.setPlayerId(playerId);
			resItemTitleBean.setRoleId(playerDynamicEntity.getRoleId());
			resItemTitleBean.setSetTitleTime(simpleDateFormat.format(playerDynamicEntity.getSetTitleTime()));
			resItemTitleBean.setNickName(playerDynamicEntity.getNickName());
			resItemTitleBean.setSex(playerDynamicEntity.getSex());
			resItemTitleBean.setFansCount(playerDynamicEntity.getFansCount());
			resItemTitleBean.setSendDiscussNum(playerDynamicEntity.getSendDiscussNum());
			resItemTitleBean.setSendGodDiscuss(playerDynamicEntity.getRecommendBestNum());
			PlayerLoginTimeEntity playerLoginTimeEntity = playerLoginTimeDao.findByID(playerDynamicEntity.getPlayerId());
			if (playerLoginTimeEntity == null) {
				resItemTitleBean.setOnline(0);
			}else {
				resItemTitleBean.setOnline(playerLoginTimeEntity.getTotalLogin());
			}
			PlayerOnlineEntity playerOnlineEntity = playerOnlineService.load(playerDynamicEntity.getPlayerId());
			resItemTitleBean.setOnline(playerOnlineEntity.getOnlineTime()/(1000 * 60 *60 ));
			reslist.add(resItemTitleBean);
		}
		return reslist;
	}
	
	/**
	 * 取消护眼大队
	 */
	public void removeProTitle(long playerId) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		PlayerDynamicEntity playerDynamicEntity = load(playerId);
		if (!playerDynamicEntity.getItemTitle().contains(gameParams_Json.getProtectEyesTitleId())) {
			return;
		}
		playerDynamicDao.removeTitle(Integer.valueOf(gameParams_Json.getProtectEyesTitleId()), playerId);
		reportInfoService.updateIsTitle(playerId);
		reportPlayerService.updateIsTitle(playerId);
		commonService.resetPlayerExtra(playerService.get(playerDynamicEntity.getPlayerId()), playerDynamicEntity);
		GMDataChange.recordChange("通过玩家ID移除护眼大队\t玩家ID为",playerId);
	}
	/**
	 * 根据角色Id搜索玩家
	 */
	public ResPlayerDynamic getPlayerDynamicEntityByRoleId(String roleId) {
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.getPlayerDynamicEntityByRoleId(roleId);
		if (playerDynamicEntity == null) {
			return null;
		}	
		ResPlayerDynamic resPlayerDynamic = new ResPlayerDynamic(playerDynamicEntity);
		return resPlayerDynamic;
	}
	/**
	   * 查询护眼大队玩家总人数
	 */
	public long getItemTitleCount() {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		return playerDynamicDao.getItemTitleCount(gameParams_Json.getProtectEyesTitleId());
	}
	
	/**
	 * 查询被举报玩家是否成成为护眼大队
	 */
	public int getToPlayerIdentity(long toPlayerId) {
		GameParams_Json gameParams_Json = GameParamsCache.getGameParams_Json();
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.getToPlayerIdentity(toPlayerId,gameParams_Json.getProtectEyesTitleId());
		if(playerDynamicEntity == null) {
			return 0;
		}
		return 1;
	}
	/**
	 * 查询所有玩家信息
	 */
	public List<GMPlayerDataBean> getPlayerAllData(int page, int count) {
		List<PlayerDynamicEntity> playerDynamicEntityList = playerDynamicDao.getPlayerAllData(page,count);
		List<GMPlayerDataBean> gMPlayerDataBeanList = new ArrayList<GMPlayerDataBean>(); 
		List<Long> playerIdAll = disposePlayerId(playerDynamicEntityList);
		Map<Long, Integer> playNum = playerExplorationService.getGameCount(playerIdAll);
		Map<Long, String> playerPhone = userService.findByPlayerPhone(playerIdAll);
		Map<Long, String> playerOnlineTime = playerOnlineService.findByPlayerIdOnlineTime(playerIdAll);
		Map<String,PlayerAstrictBean> playerAstrictBeanList = playerService.getPlayerAstrictData(playerIdAll);
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntityList) {
			GMPlayerDataBean gMPlayerDataBean = new GMPlayerDataBean();
			PlayerAstrictBean playerAstrictBean = playerAstrictBeanList.get(String.valueOf(playerDynamicEntity.getPlayerId()));
			gMPlayerDataBean.setPlayerId(String.valueOf(playerDynamicEntity.getPlayerId()));
			gMPlayerDataBean.setRoleId(playerDynamicEntity.getRoleId());
			gMPlayerDataBean.setNickName(playerDynamicEntity.getNickName());
			gMPlayerDataBean.setPhone(playerPhone.get(playerDynamicEntity.getPlayerId()));
			gMPlayerDataBean.setAchievementDebris(String.valueOf(playerDynamicEntity.getAchievementDebris()));
			gMPlayerDataBean.setClue(String.valueOf(playerDynamicEntity.getClue()));
			gMPlayerDataBean.setAttentionCount(String.valueOf(playerDynamicEntity.getAttentionCount()));
			gMPlayerDataBean.setFansCount(String.valueOf(playerDynamicEntity.getFansCount()));
			gMPlayerDataBean.setPlayNum(String.valueOf(playNum.get(playerDynamicEntity.getPlayerId())));
			gMPlayerDataBean.setPlayDuration(String.valueOf(playerOnlineTime.get(playerDynamicEntity.getPlayerId())));
			if(playerAstrictBean == null) {
				gMPlayerDataBean.setNoSpeakTime("0");
				gMPlayerDataBean.setIsBlock("0");
			} else {
				gMPlayerDataBean.setNoSpeakTime(playerAstrictBean.getNo_speak_time());
				gMPlayerDataBean.setIsBlock(playerAstrictBean.getIs_block());
			}
			gMPlayerDataBeanList.add(gMPlayerDataBean);
		}
		return gMPlayerDataBeanList;
	}
	
	
	
	/**
	 * 查询所有玩家总数
	 */
	public long findPlayerCount() {
		return playerDynamicDao.findPlayerCount();
	}
	/**
	 * 根据ID查询玩家信息
	 */
	public GMPlayerDataBean findByIdPlayerData(String roleId) {
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByIdPlayerData(roleId);
		GMPlayerDataBean playerDataBean = new GMPlayerDataBean();
		if(playerDynamicEntity != null) {
			PlayerAstrictBean playerAstrictBean = playerService.getPlayerAstrictData(playerDynamicEntity.getPlayerId());
			playerDataBean.setPlayerId(String.valueOf(playerDynamicEntity.getPlayerId()));
			playerDataBean.setRoleId(playerDynamicEntity.getRoleId());
			playerDataBean.setNickName(playerDynamicEntity.getNickName());
			playerDataBean.setPhone(userService.findOneByPlayerPhone(playerDynamicEntity.getPlayerId()));
			playerDataBean.setAchievementDebris(String.valueOf(playerDynamicEntity.getAchievementDebris()));
			playerDataBean.setClue(String.valueOf(playerDynamicEntity.getClue()));
			playerDataBean.setAttentionCount(String.valueOf(playerDynamicEntity.getAttentionCount()));
			playerDataBean.setFansCount(String.valueOf(playerDynamicEntity.getFansCount()));
			playerDataBean.setPlayNum(String.valueOf(playerExplorationService.getGameCount(playerDynamicEntity.getPlayerId())));
			playerDataBean.setPlayDuration(String.valueOf(playerOnlineService.findOneByPlayerIdOnlineTime(playerDynamicEntity.getPlayerId())));
			if(playerAstrictBean == null) {
				playerDataBean.setNoSpeakTime("0");
				playerDataBean.setIsBlock("0");
			} else {
				playerDataBean.setNoSpeakTime(playerAstrictBean.getNo_speak_time());
				playerDataBean.setIsBlock(playerAstrictBean.getIs_block());
			}
		}
		return playerDataBean;
	}
	
	/**
	 * 根据手机号查询玩家信息
	 */
	public GMPlayerDataBean findByPhonePlayerData(String phone) {
		UserEntity userEntity = userService.findByPhoneData(phone);
		GMPlayerDataBean playerDataBean = new GMPlayerDataBean();
		if(userEntity != null) {
			PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(userEntity.getPlayerId());
			if(playerDynamicEntity != null) {
				PlayerAstrictBean playerAstrictBean = playerService.getPlayerAstrictData(playerDynamicEntity.getPlayerId());
				playerDataBean.setPlayerId(String.valueOf(playerDynamicEntity.getPlayerId()));
				playerDataBean.setRoleId(playerDynamicEntity.getRoleId());
				playerDataBean.setNickName(playerDynamicEntity.getNickName());
				playerDataBean.setPhone(userEntity.getPhone());
				playerDataBean.setAchievementDebris(String.valueOf(playerDynamicEntity.getAchievementDebris()));
				playerDataBean.setClue(String.valueOf(playerDynamicEntity.getClue()));
				playerDataBean.setAttentionCount(String.valueOf(playerDynamicEntity.getAttentionCount()));
				playerDataBean.setFansCount(String.valueOf(playerDynamicEntity.getFansCount()));
				playerDataBean.setPlayNum(String.valueOf(playerExplorationService.getGameCount(playerDynamicEntity.getPlayerId())));
				playerDataBean.setPlayDuration(String.valueOf(playerOnlineService.findOneByPlayerIdOnlineTime(playerDynamicEntity.getPlayerId())));
				if(playerAstrictBean == null) {
					playerDataBean.setNoSpeakTime("0");
					playerDataBean.setIsBlock("0");
				} else {
					playerDataBean.setNoSpeakTime(playerAstrictBean.getNo_speak_time());
					playerDataBean.setIsBlock(playerAstrictBean.getIs_block());
				}
			}
		}
		return playerDataBean;
	}
	
	/**
	 * 根据昵称查询玩家信息
	 */
	public List<GMPlayerDataBean> findByNamePlayerData(String nickName,int page,int count) {
		List<PlayerDynamicEntity> playerDynamicEntityList = playerDynamicDao.findByNickNameData(nickName,page,count);
		List<GMPlayerDataBean> gMPlayerDataBeanList = new ArrayList<GMPlayerDataBean>();
		List<Long> playerIdAll = disposePlayerId(playerDynamicEntityList);
		Map<Long, Integer> playNum = playerExplorationService.getGameCount(playerIdAll);
		Map<Long, String> playerPhone = userService.findByPlayerPhone(playerIdAll);
		Map<Long, String> playerOnlineTime = playerOnlineService.findByPlayerIdOnlineTime(playerIdAll);
		Map<String,PlayerAstrictBean> playerAstrictBeanList = playerService.getPlayerAstrictData(playerIdAll);
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntityList) {
			GMPlayerDataBean playerDataBean = new GMPlayerDataBean();
			PlayerAstrictBean playerAstrictBean = playerAstrictBeanList.get(String.valueOf(playerDynamicEntity.getPlayerId()));
			playerDataBean.setPlayerId(String.valueOf(playerDynamicEntity.getPlayerId()));
			playerDataBean.setRoleId(playerDynamicEntity.getRoleId());
			playerDataBean.setNickName(playerDynamicEntity.getNickName());
			playerDataBean.setPhone(playerPhone.get(playerDynamicEntity.getPlayerId()));
			playerDataBean.setAchievementDebris(String.valueOf(playerDynamicEntity.getAchievementDebris()));
			playerDataBean.setClue(String.valueOf(playerDynamicEntity.getClue()));
			playerDataBean.setAttentionCount(String.valueOf(playerDynamicEntity.getAttentionCount()));
			playerDataBean.setFansCount(String.valueOf(playerDynamicEntity.getFansCount()));
			playerDataBean.setPlayNum(String.valueOf(playNum.get(playerDynamicEntity.getPlayerId())));
			playerDataBean.setPlayDuration(String.valueOf(playerOnlineTime.get(playerDynamicEntity.getPlayerId())));
			if(playerAstrictBean == null) {
				playerDataBean.setNoSpeakTime("0");
				playerDataBean.setIsBlock("0");
			} else {
				playerDataBean.setNoSpeakTime(playerAstrictBean.getNo_speak_time());
				playerDataBean.setIsBlock(playerAstrictBean.getIs_block());
			}
			gMPlayerDataBeanList.add(playerDataBean);
		}
		return gMPlayerDataBeanList;
	}
	
	
	/**
	 * 处理playerId
	 */
	private List<Long> disposePlayerId(List<PlayerDynamicEntity> playerDynamicEntityList){
		List<Long> playerIdAll = new ArrayList<Long>();
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntityList) {
			playerIdAll.add(playerDynamicEntity.getPlayerId());
		}
		return playerIdAll;
	}
	
	/**
	 * 查询指定昵称玩家总数
	 */
	public long findByNamePlayerCount(String nickName) {
		return playerDynamicDao.findByNamePlayerCount(nickName);
	}
	/**同步player和playerdynamic数据*/
	public void synchdata(Player player) {
		load(player);
		playerDynamicDao.synchdata(player);
	}
	
	/**
	 * 查询当前所有违规人员
	 */
	public List<GMPlayerDataBean> findAtFoulPlayAll(int page,int count) {
		Map<String,PlayerAstrictBean> playerAstrictBeanList = playerService.findAtFoulPlayAll(page,count);
		List<GMPlayerDataBean> gMPlayerDataBeanList = new ArrayList<GMPlayerDataBean>();
		List<Long> playerIdAll = new ArrayList<Long>();
		Map<Long, Integer> playNum = playerExplorationService.getGameCount(playerIdAll);
		Map<Long, String> playerPhone = userService.findByPlayerPhone(playerIdAll);
		Map<Long, String> playerOnlineTime = playerOnlineService.findByPlayerIdOnlineTime(playerIdAll);
		for (String key : playerAstrictBeanList.keySet()) {
			playerIdAll.add(Long.valueOf(key));
		}
		List<PlayerDynamicEntity> playerDynamicEntityList = playerDynamicDao.findByIds(playerIdAll);
		for (PlayerDynamicEntity playerDynamicEntity : playerDynamicEntityList) {
			GMPlayerDataBean playerDataBean = new GMPlayerDataBean();
			PlayerAstrictBean playerAstrictBean = playerAstrictBeanList.get(String.valueOf(playerDynamicEntity.getPlayerId()));
			playerDataBean.setPlayerId(String.valueOf(playerDynamicEntity.getPlayerId()));
			playerDataBean.setRoleId(playerDynamicEntity.getRoleId());
			playerDataBean.setNickName(playerDynamicEntity.getNickName());
			playerDataBean.setPhone(playerPhone.get(playerDynamicEntity.getPlayerId()));
			playerDataBean.setAchievementDebris(String.valueOf(playerDynamicEntity.getAchievementDebris()));
			playerDataBean.setClue(String.valueOf(playerDynamicEntity.getClue()));
			playerDataBean.setAttentionCount(String.valueOf(playerDynamicEntity.getAttentionCount()));
			playerDataBean.setFansCount(String.valueOf(playerDynamicEntity.getFansCount()));
			playerDataBean.setPlayNum(String.valueOf(playNum.get(playerDynamicEntity.getPlayerId())));
			playerDataBean.setPlayDuration(String.valueOf(playerOnlineTime.get(playerDynamicEntity.getPlayerId())));
			if(playerAstrictBean == null) {
				playerDataBean.setNoSpeakTime("0");
				playerDataBean.setIsBlock("0");
			} else {
				playerDataBean.setNoSpeakTime(playerAstrictBean.getNo_speak_time());
				playerDataBean.setIsBlock(playerAstrictBean.getIs_block());
			}
			gMPlayerDataBeanList.add(playerDataBean);
		}
		return gMPlayerDataBeanList;
	}
	
	/**
	 * 查询当前违规人员总数
	 */
	public long findAtFoulPlayCount() {
		return playerService.findAtFoulPlayCount();
	}
	
	
	
	public void test() {
		
		playerDynamicDao.test();
	}
	/**
	 * 玩家结束游戏，返回玩家游戏次数，玩家获得奖励
	 * @return
	 */
	public ResGameOverBean playGameOver() {
		//获取用户信息
		 Player player=PlayerContext.getPlayer();
		//查询该玩家信息，获得玩家玩剧本次数
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		int playNum;
		return null;
	}
	
}
