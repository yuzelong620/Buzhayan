package com.kratos.game.herphone.player.service;

import com.kratos.game.herphone.common.CommonService;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.globalgame.auto.json.Achievement_Json;
import com.globalgame.auto.json.CreatRole_Json;
import com.globalgame.auto.json.GameCatalog_Json;
import com.globalgame.auto.json.GameParams_Json;
import com.globalgame.auto.json.Item_Json;
import com.globalgame.auto.json.Tag_Json;
import com.kratos.engine.framework.common.CommonConstant;
import com.kratos.engine.framework.common.utils.IdGenerator;
import com.kratos.engine.framework.common.utils.JedisUtils;
import com.kratos.engine.framework.common.utils.StringHelper;
import com.kratos.engine.framework.crud.BaseCrudService;
import com.kratos.engine.framework.crud.Param;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.engine.framework.tencent.TencentManager;
import com.kratos.engine.framework.tencent.bean.ResTencentUserInfo;
import com.kratos.engine.framework.wechat.WechatManager;
import com.kratos.engine.framework.wechat.bean.ResAccessToken;
import com.kratos.engine.framework.wechat.bean.ResWechatUserInfo;
import com.kratos.game.herphone.achievement.dao.AchievementDao;
import com.kratos.game.herphone.achievement.entity.AchievementEntity;
import com.kratos.game.herphone.achievement.service.AchievementService;
import com.kratos.game.herphone.bag.dao.BagDao;
import com.kratos.game.herphone.bag.entity.BagEntity;
import com.kratos.game.herphone.bag.service.BagService;
import com.kratos.game.herphone.cache.AppCache;
import com.kratos.game.herphone.config.GlobalData;
import com.kratos.game.herphone.discuss.dao.DiscussDao;
import com.kratos.game.herphone.discuss.dao.RecommendBestDao;
import com.kratos.game.herphone.discuss.entity.DiscussEntity;
import com.kratos.game.herphone.discuss.entity.DiscussRemoveEntity;
import com.kratos.game.herphone.discuss.entity.RecommendBestEntity;
import com.kratos.game.herphone.discuss.service.DiscussRemoveService;
import com.kratos.game.herphone.discuss.service.DiscussService;
import com.kratos.game.herphone.dynamic.entity.PlayerTagsEntity;
import com.kratos.game.herphone.dynamic.service.PlayerTagsService;
import com.kratos.game.herphone.game.chose.service.ChosenOptionService;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.AchievementCache;
import com.kratos.game.herphone.json.datacache.CreatRoleCache;
import com.kratos.game.herphone.json.datacache.GameCatalogCache;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
import com.kratos.game.herphone.json.datacache.ItemCache;
import com.kratos.game.herphone.json.datacache.TagCache;
import com.kratos.game.herphone.player.PlayerContext;
import com.kratos.game.herphone.player.bean.PlayerPage;
import com.kratos.game.herphone.player.bean.PlayerScoreBean;
import com.kratos.game.herphone.player.bean.RanKingData;
import com.kratos.game.herphone.player.bean.ResPlayerPowerData;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ReqPlayerEdit;
import com.kratos.game.herphone.player.message.ReqPlayerTencentLogin;
import com.kratos.game.herphone.player.message.ReqPlayerWechatLogin;
import com.kratos.game.herphone.player.message.ResPlayerLogin;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.player.web.GMDataChange;
import com.kratos.game.herphone.playerDynamic.dao.PlayerDynamicDao;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;
import com.kratos.game.herphone.playerDynamic.service.PlayerDynamicService;
import com.kratos.game.herphone.report.service.ReportInfoService;
import com.kratos.game.herphone.report.service.ReportPlayerService;
import com.kratos.game.herphone.scheduled.ScheduledService;
import com.kratos.game.herphone.statisticalPlayer.bean.ResStplayerInfo;
import com.kratos.game.herphone.systemNotice.service.SystemNoticeService;
import com.kratos.game.herphone.task.service.TaskService;

import lombok.extern.log4j.Log4j;

import com.alibaba.fastjson.JSON;
import com.github.kevinsawicki.http.HttpRequest;
import java.util.Map;
import java.util.HashMap;

import com.kratos.game.herphone.common.CommonCost;
import com.kratos.game.herphone.common.CommonCost.ChannelId;
import com.kratos.game.herphone.tencent.dao.UnionIdDao;
import com.kratos.game.herphone.tencent.entity.UnionIdEntity;
import com.kratos.game.herphone.tencent.service.UnionIdService;
import com.kratos.game.herphone.user.dao.UserDao;
import com.kratos.game.herphone.user.entity.UserEntity;
import com.kratos.game.herphone.playerOnline.dao.PlayerOnlineDao;
import com.kratos.game.herphone.playerOnline.entity.PlayerOnlineEntity;
import com.kratos.game.herphone.playerOnline.service.PlayerLoginTimeService;
import com.kratos.game.herphone.player.bean.PlayerIdBean;
import com.kratos.game.herphone.player.bean.PlayerProperty;
import com.kratos.game.herphone.player.bean.PlayerAllBean;
import com.kratos.game.herphone.player.bean.PlayerAstrictBean;
import com.alibaba.fastjson.JSONObject;
@Log4j
@Component
public class PlayerServiceImpl extends BaseCrudService<Long, Player> implements PlayerService {
    private Logger loginLogger = LoggerFactory.getLogger("loginLogger");
    public static final short CMD_REQ_LOGIN = 1; // 请求微信登录

    @Autowired
    private GlobalData globalData;
    @Autowired
    private RoleIdService roleIdService;
    @Autowired
    private WechatManager wechatManager;
    @Autowired
    private TencentManager tencentManager;
    @Autowired
	private ScheduledService scheduledService;
    @Autowired
    UnionIdDao unionIdDao;
    @Autowired
    UnionIdService unionIdService;
    @Autowired
    UserDao userDao;
    @Autowired
    PlayerDynamicDao playerDynamicDao;
    @Autowired
    DiscussDao discussDao;
    @Autowired
    DiscussRemoveService discussRemoveService;
    @Autowired
    DiscussService discussService;
    @Autowired
    PlayerDynamicService playerDynamicService;
    @Autowired
    ReportPlayerService reportPlayerService;
    @Autowired
    ReportInfoService reportInfoService;
    @Autowired
    private PlayerOnlineDao playerOnlineDao;
    @Autowired
    private PlayerLoginTimeService playerLoginTimeService;
    @Autowired
    RecommendBestDao recommendBestDao;
    @Autowired
    TaskService taskService;
    @Autowired
    private BagService bagService;
    @Autowired
    private BagDao bagDao;
    @Autowired
    private PlayerTagsService playerTagsService;
    @Autowired
    private AchievementService achievementService;
    @Autowired
    private SystemNoticeService systemNoticeService;
    public static final String SystemNotice = "内测奖励发放通知:感谢您参加了内测版不眨眼，官方赠送您内测专属头像框，感谢支持！（请在我的荣誉里查收）";
    
    
    @Value("${spring.profiles.active}")
    private String profile;
    @Lazy
    @Autowired
    private ChosenOptionService chosenOptionService;
    /**
     * 封号
     * @param playerId
     * @return
     */
    public boolean setIsBlock(long playerId){
    	Player player=get(playerId);
        player.setIsBlock(1);
        this.cacheAndPersist(playerId,player);
        GMDataChange.recordChange("通过玩家ID封号\t玩家ID为",playerId);
    	return true;
    }
    /**
     * @param playerId
     * @param time  禁言时间
     * @return
     */
    public boolean setNoSpeak(long playerId,long time){
    	Player player=get(playerId);
        player.setNoSpeakTime(time+System.currentTimeMillis());
        this.cacheAndPersist(playerId,player);
        GMDataChange.recordChange("禁言ID为" + playerId + "\t禁言时长(毫秒)",time);
    	return true;
    }
    
    /**
     * 取消封号
     * */
	@Override
	public boolean callIsBlock(long playerId) {
		Player player=get(playerId);
		player.setIsBlock(0);
		this.cacheAndPersist(playerId,player);
		GMDataChange.recordChange("通过玩家ID取消封号\t玩家ID为",playerId);
		return true;
	}
	
	/**
	 * 取消禁言
	 * */
	@Override
	public boolean callNoSpeak(long playerId) {
		Player player=get(playerId);
		player.setNoSpeakTime(0);
		this.cacheAndPersist(playerId, player);
		GMDataChange.recordChange("通过玩家ID取消禁言\t玩家ID为",playerId);
		return false;
	}

    @Override
    public ResPlayerLogin wechatLogin(String code) {
        String openId;
        ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
        ResAccessToken resAccessToken = wechatManager.getWechatService().getAppAccessToken(code);
        openId = resAccessToken.getOpenid();
        String accessToken = resAccessToken.getAccess_token();
        if (accessToken == null) {
        	log.error("错误码："+resAccessToken.getErrcode()+",错误信息:"+resAccessToken.getErrmsg());
			throw new BusinessException("微信登录失败");
		}
        Player player = findByWechatOpenId(openId);
        if (player == null) {
            player = this.wechatRegister(accessToken, openId);
            resPlayerLogin.setCreate(true);
        }else if (player.getIsGuest() == 1) {
				player.setIsGuest(0);
				this.cacheAndPersist(player.getId(), player);	
		}     
        loginExecute(player);
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
 
    private ResTencentUserInfo getUserInfoNew(String channelId,String accessToken, String openid) {
        Map<String, String> params = new HashMap<>();
        params.put("access_token", accessToken);
        params.put("openid", openid);
        String appId=ChannelId.findByName(channelId).getAppId();
        params.put("oauth_consumer_key",appId);
        String result = HttpRequest.get("https://graph.qq.com/user/get_user_info", params, true).accept("application/json").body();
        return JSON.parseObject(result, ResTencentUserInfo.class);
    }

//    @Override 
    public ResPlayerProfile bindTencent2(ReqPlayerTencentLogin request) {
    	checkUnionidParams(request);
        Player player = PlayerContext.getPlayer();
        Player playerOld = findByTencentOpenId(request.getOpenId());
        if(playerOld != null){
            throw new BusinessException("该QQ已经绑定，请使用qq登录");
        }
        UnionIdEntity entity=unionIdDao.findByUnionId(request.getUnionid());//查找映射关系
        if(entity!=null){
        	 throw new BusinessException("该QQ已经绑定，请使用qq登录");
        }
        String openid=request.getOpenId();
        String accessToken=request.getAccessToken();
        
        player.setTencentOpenid(openid);
        ResTencentUserInfo userInfo =getUserInfoNew(request.getChannelId(),accessToken,openid);
        if(!userInfo.getRet().equals("0")){
        	 throw new BusinessException("获取qq信息失败");
        }
        player.setGender(userInfo.getGender().equals("男") ? "1" : "2");
        if (userInfo.getNickname() != null) {
           try {
               player.setNickName(new String(Base64.getEncoder().encode(userInfo.getNickname().getBytes()), CommonConstant.UTF8));
           } catch (UnsupportedEncodingException e) {
               log.error("", e);
           }
       }
       player.setAvatarUrl(userInfo.getFigureurl_qq());
       player.setIsGuest(0);
       playerDynamicService.synchdata(player);
       this.cacheAndPersist(player.getId(), player);
       unionIdService.save(request.getUnionid(),player.getId());//保存unionid 映射关系
       return new ResPlayerProfile(player);
    } 
    /**检查 unionid ,如果不存在调用 qq接口获取 unionid*/
    public void checkUnionidParams(ReqPlayerTencentLogin req){
    	if(req.getUnionid()!=null){
    		return;
    	}
    	try{
	    	String url=getUnionUrl(req.getAccessToken());
	    	String result = HttpRequest.get(url).accept("application/json").body();
	    	if(result==null){
	    	   log.error("获取unionid 失败，accessToken:"+req.getAccessToken());
	    	   return;
	    	}
	    	if(result.contains("callback( ")){
	    	   result=result.replace("callback( ", "");
	    	}
	    	if(result.contains(" );")){
	    		result=result.replace(" );", "");
	    	}
	    	JSONObject json=JSONObject.parseObject(result);
	    	if(json==null){
	    		log.error("获取unionid 失败，accessToken:"+req.getAccessToken());
	    	    return;
	    	}
	    	String unionid=json.getString("unionid");
	    	req.setUnionid(unionid);//设置 unionid
    	}
    	catch(Exception e){
    		log.error("",e);
    	}
    }    
    @Override
    public ResPlayerLogin tencentLogin2(ReqPlayerTencentLogin request) {
        ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
        checkUnionidParams(request);//检查unionid
        String openId = request.getOpenId();
        Player player=null;
        //根据 unionid 获取player
        UnionIdEntity unionidEntity=unionIdDao.findByUnionId(request.getUnionid());
        if(unionidEntity!=null){
        	player=get(unionidEntity.getPlayerId());
        }
        //根据openid获取player
        if(player==null){
            player = findByTencentOpenId(openId);
        }
        if (player == null) {
        	 player = new Player();
             player.setRoleId(roleIdService.getNextRoleId()); 
             //
             player.setTencentOpenid(openId);
             ResTencentUserInfo userInfo =getUserInfoNew(request.getChannelId(),request.getAccessToken(),openId);
             if(!userInfo.getRet().equals("0")){
             	 throw new BusinessException("获取qq信息失败");
             }
             player.setGender(userInfo.getGender().equals("男") ? "1" : "2");
             if (userInfo.getNickname() != null) {
                 try {
                     player.setNickName(new String(Base64.getEncoder().encode(userInfo.getNickname().getBytes()), CommonConstant.UTF8));
                 } catch (UnsupportedEncodingException e) {
                     log.error("", e);
                 }
             }
             player.setAvatarUrl(userInfo.getFigureurl_qq());
             player.setIsGuest(0);
             player=this.register(player);
             resPlayerLogin.setCreate(true);
        }else if (player.getIsGuest() == 1) {
			player.setIsGuest(0);
			this.cacheAndPersist(player.getId(), player);	
        }
        //如果第一次使用unionid
        if(unionidEntity==null){
        	unionIdService.save(request.getUnionid(),player.getId());
        }
        loginExecute(player);
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
    
    @Override
    public ResPlayerLogin tencentLogin(ReqPlayerTencentLogin request) {
        ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
        String openId = request.getOpenId();
        Player player = findByTencentOpenId(openId);
        if (player == null) {
            player = this.tencentRegister(request.getAccessToken(), openId);
            resPlayerLogin.setCreate(true);
        }else if (player.getIsGuest() == 1) {
			player.setIsGuest(0);
			this.cacheAndPersist(player.getId(), player);	
        }
        playerDynamicService.synchdata(player);
        resPlayerLogin.setToken(player.getToken());
        resPlayerLogin.setPlayer(new ResPlayerProfile(player));
        return resPlayerLogin;
    }

    @Override
    public ResPlayerLogin guestLogin() {
        Player player = this.guestRegister();
        ResPlayerLogin resPlayerLogin = new ResPlayerLogin();
        resPlayerLogin.setCreate(true);
//        OnFire.fire(new PlayerLoginEvent(player));
        resPlayerLogin.setToken(player.getToken());
        resPlayerLogin.setPlayer(new ResPlayerProfile(player));
        return resPlayerLogin;
    }

    @Transactional(readOnly = true)
    private Player findByWechatOpenId(String openId) {
        List<Player> resultList = this.findByParams(Param.equal("wechatOpenid", openId));
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }

    @Transactional(readOnly = true)
    private Player findByTencentOpenId(String openId) {
        List<Player> resultList = this.findByParams(Param.equal("tencentOpenid", openId));
        if (resultList == null || resultList.isEmpty()) {
            return null;
        }
        return resultList.get(0);
    }
   private String getUnionUrl(String accessToken){
//	   String url="https://graph.qq.com/oauth2.0/me?access_token=DGKFDJGJDF8346GFNF34BDF8DDF4&unionid=1"; 
		   StringBuffer sb=new StringBuffer();
		   sb.append("https://graph.qq.com/oauth2.0/me?access_token=").append(accessToken).append("&unionid=1");
		return sb.toString();
	   
   }

   
   @Autowired
   CommonService commonService;
   @Transactional(readOnly = true)
   public Player findByToken(String token) {
       Player player = JedisUtils.getInstance().get(token, Player.class);
       if(player == null) {
           List<Player> players = findByParams(Param.equal("token", token));
           if(players != null && !players.isEmpty()) {
               JedisUtils.getInstance().set(token, players.get(0));
               //重新设置  玩家属性加成
               player=players.get(0);
               PlayerDynamicEntity playerDynamic=playerDynamicService.load(player);
               commonService.resetPlayerExtra(player, playerDynamic);
               return players.get(0);
           }
       }
       return player;
   }

    @Override
    public void cache(Long key, Player player) {
        super.cache(key, player);
        JedisUtils.getInstance().set(player.getToken(), player);
    }

    @Override
    public void checkDailyReset(Player player) {
        long resetTimestamp = globalData.dailyResetTimestamp;
        if (player.getLastDailyReset() < resetTimestamp) {
            player.setLastDailyReset(globalData.dailyResetTimestamp);
            cache(player.getId(), player);
            onDailyReset(player);
        }
    }

    @Override
    public void index() {
        Player player = PlayerContext.getPlayer();
        player.setLastLoginTime(System.currentTimeMillis());
        loginLogger.info(player.getId() + ":" + player.getLastLoginTime());
        this.cacheAndPersist(player.getId(), player);
    }

    @Override
    public Boolean releaseAchievement(int id) {
        Player player = PlayerContext.getPlayer();
        Achievement_Json achievementConfig =  JsonCacheManager.getCache( AchievementCache.class).getData(id);
        if(achievementConfig==null){
        	log.error("获得成就 ，发送的id错误："+id);
        	return false;
        }
        List<Integer> releasedAchievementIds = new ArrayList<>();
        if (StringHelper.isNotBlank(player.getReleasedAchievements())) {
            String[] ids = player.getReleasedAchievements().split(",");
            for (String achievementId : ids) {
            	Integer intId=Integer.parseInt(achievementId);
            	if(achievementConfig.getId().equals(intId)){//已经获得
            		return true;
            	}
                releasedAchievementIds.add(intId);
            }
        }
        releasedAchievementIds.add(achievementConfig.getId());
        player.setReleasedAchievements(StringHelper.join(releasedAchievementIds, ","));
        player.addAchievement(achievementConfig.getAchievement());
        player.setLastAddAchievementTime(System.currentTimeMillis());
        this.cacheAndPersist(player.getId(), player);
        return false;
    }
    
    /**
     * 检查修正
     * @param player
     */
    public void checkAndAmendmentAchievementScore(Player player){
    	//获取player的所有 成就id
    	List<Integer> releasedAchievementIds = new ArrayList<>();
    	AchievementCache cache=JsonCacheManager.getCache( AchievementCache.class);
    	int sumAchievementScore=0;
        if (StringHelper.isNotBlank(player.getReleasedAchievements())) {
            String[] ids = player.getReleasedAchievements().split(",");
            for (String achievementId : ids) {
            	Integer id=Integer.parseInt(achievementId);
            	Achievement_Json json=cache.getData(id);
            	if(json==null){
            		log.error("修正成就数据，不存在的成就id:"+id);
            		continue;
            	}
                releasedAchievementIds.add(id);
                sumAchievementScore+=json.getAchievement();
            }
        }
        player.setAchievement(sumAchievementScore);
        player.setReleasedAchievements(StringHelper.join(releasedAchievementIds, ","));
    }

    @Override
    public List<Achievement_Json> getReleasedAchievements(int gameId) {
        Player player = PlayerContext.getPlayer();
//        List<Achievement_Json> configListSource = configResourceRegistry.getConfig(AchievementConfig.class).getList();
        List<Achievement_Json> configListSource =JsonCacheManager.getCache(AchievementCache.class).getList();
        List<Achievement_Json> achievementConfigList = configListSource
                .stream()
                .filter(achievementConfig -> achievementConfig.getGameid().equals(gameId))
                .map(achievementConfig -> {
                	Achievement_Json achievementConfigTemp = new Achievement_Json();
                    achievementConfigTemp.setId(achievementConfig.getId());
                    achievementConfigTemp.setAchieved(0);
                    achievementConfigTemp.setTitle(achievementConfig.getTitle());
                    achievementConfigTemp.setDesc(achievementConfig.getDesc());
                    achievementConfigTemp.setAchievementId(achievementConfig.getAchievementId());
                    achievementConfigTemp.setAchievement(achievementConfig.getAchievement());
//                    achievementConfigTemp.setRelAchievementId(achievementConfig.getRelAchievementId());
                    achievementConfigTemp.setGameid(achievementConfig.getGameid());
                    achievementConfigTemp.setChatid(achievementConfig.getChatid());
                    achievementConfigTemp.setCid(achievementConfig.getCid());
                    return achievementConfigTemp;
                })
                .collect(Collectors.toList());
        List<Integer> releasedAchievementIds = new ArrayList<>();
        if (StringHelper.isNotBlank(player.getReleasedAchievements())) {
            String[] ids = player.getReleasedAchievements().split(",");
            for (String id : ids) {
                releasedAchievementIds.add(Integer.parseInt(id));
            }
        }

        for (Achievement_Json achievementConfig : achievementConfigList) {
            if (releasedAchievementIds.contains(achievementConfig.getId())) {
                achievementConfig.setAchieved(1);
            }
        }
        return achievementConfigList;
    }

    @Override
    public ResPlayerProfile costPower() {
        Player player = PlayerContext.getPlayer();
        this.recoverPower(player, false);
        if (player.getPower() < 1) {
            throw new BusinessException("当前电量不足，请耐心等待");
        }
        return this.costPower(1);
    }

    @Override
    public ResPlayerProfile setPower(Integer power) {
        Player player = PlayerContext.getPlayer();
//        if (player.getPower() != null) {
//            return new ResPlayerProfile(player);
//        }
        player.setLastRecoverPowerTime(System.currentTimeMillis());
        player.setPower(100);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile costPower(Integer power) {
        Player player = PlayerContext.getPlayer();
        this.recoverPower(player, false);
//        if("test".equals(profile)) {
//            power = 0;
//        }
        player.costPower(power);
        this.cacheAndPersist(player.getId(), player);
        taskService.playerScheduleUpdate(CommonCost.TackType.consumeElectric.ordinal(),power); //任务接口(任务类型,完成次数)
        playerDynamicDao.addExp(player.getId(), power * 20); //修改经验值
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile onShared() {
        Player player = PlayerContext.getPlayer();
        int sharedCount = 0;
        if (player.getSharedCount() != null) {
            sharedCount = player.getSharedCount();
        }
        player.setSharedCount(sharedCount + 1);
        //分享,更新每日任务进度,调用任务进度接口
        taskService.playerScheduleUpdate(CommonCost.TackType.dailyShare.ordinal(),1);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile takeShare() {
        Player player = PlayerContext.getPlayer();
        if (player.getTakenShare() != null && player.getTakenShare()) {
            throw new BusinessException("您已经领取过分享奖励");
        }
        if (player.getSharedCount() == null || player.getSharedCount() < 1) {
            throw new BusinessException("分享次数不足，不能领取");
        }
        player.setTakenShare(true);
        player.addPower(20, true);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile takeCostPower20() {
        Player player = PlayerContext.getPlayer();
        if (player.getTakenCostPower20() != null && player.getTakenCostPower20()) {
            throw new BusinessException("您已经领取过奖励");
        }
        if (player.getCostedPower() == null || player.getCostedPower() <= 20) {
            throw new BusinessException("花费电量不足");
        }
        player.setTakenCostPower20(true);
        player.addPower(10, true);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile takeCostPower100() {
        Player player = PlayerContext.getPlayer();
        if (player.getTakenCostPower100() != null && player.getTakenCostPower100()) {
            throw new BusinessException("您已经领取过奖励");
        }
        if (player.getCostedPower() == null || player.getCostedPower() <= 130) {
            throw new BusinessException("花费电量不足");
        }
        player.setTakenCostPower100(true);
        player.addPower(20, true);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile gmSetPower(String name, Integer power) {
        List<Player> players = findByParams(Param.equal("roleId", name));
        if (players != null && players.size() > 0) {
            Player player = players.get(0);
            player.setLastRecoverPowerTime(System.currentTimeMillis());
            player.setPower(power);
            this.cacheAndPersist(player.getId(), player);
            return new ResPlayerProfile(player);
        }
        return null;
    }

    @Override
    public List<ResRankPlayer> getRank() {
        List<ResRankPlayer> resRankPlayerList = JedisUtils.getInstance().getList(AppCache.achievementList, ResRankPlayer.class);
        if (resRankPlayerList != null && !resRankPlayerList.isEmpty()) {
            return resRankPlayerList;
        }
        log.error("getRank()方法未从redis里获取到对象");
      
        return scheduledService.getRank();
    }

    @Override
    public List<ResRankPlayer> getExploration() {
        List<ResRankPlayer> resRankPlayerList = JedisUtils.getInstance().getList(AppCache.explorationList, ResRankPlayer.class);
        if (resRankPlayerList != null && !resRankPlayerList.isEmpty()) {
            return resRankPlayerList;
        }
        log.error("getExploration()方法未从redis里获取到对象");    
        return scheduledService.getExploration();
    }

    @Override
    public ResPlayerProfile bindWechat(ReqPlayerWechatLogin request) {
        Player player = PlayerContext.getPlayer();
        ResAccessToken resAccessToken = wechatManager.getWechatService().getAppAccessToken(request.getCode());
        Player playerOld = findByWechatOpenId(resAccessToken.getOpenid());
        if(playerOld != null) {
           // remove(playerOld.getId());
        	throw new BusinessException("该微信已经绑定，请使用微信登录");
        }
        player.setIsGuest(0);
        this.setWechatInfo(player, resAccessToken.getAccess_token(), resAccessToken.getOpenid());
        playerDynamicService.synchdata(player);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile bindTencent(ReqPlayerTencentLogin request) {
        Player player = PlayerContext.getPlayer();
        Player playerOld = findByTencentOpenId(request.getOpenId());
        if(playerOld != null) {
            //remove(playerOld.getId());
            throw new BusinessException("该QQ已经绑定，请使用QQ登录");
        }
        player.setIsGuest(0);
        this.setTencentInfo(player, request.getAccessToken(), request.getOpenId());
        playerDynamicService.synchdata(player);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile saveRecordCostPower() {
        Player player = PlayerContext.getPlayer();
        player.costPower(5);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile gmAddPower(String roleId, Integer power) {
        List<Player> players = findByParams(Param.equal("roleId", roleId));
        if (players != null && players.size() > 0) {
            Player player = players.get(0);
            player.addPower(power, true);
            this.cacheAndPersist(player.getId(), player);
            return new ResPlayerProfile(player);
        }
        return null;
    }

    @Override
    public ResPlayerProfile watchAd() {
        Player player = PlayerContext.getPlayer();
        if(player.getWatchAdTask() >= 3) {
            throw new BusinessException("看广告次数达到上限");
        }
        player.addWatchAd(1);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }

    @Override
    public ResPlayerProfile takeWatchAd() {
        Player player = PlayerContext.getPlayer();
        if(player.getWatchAdTask() >= 3) {
            throw new BusinessException("领取次数已达上限");
        }
        player.addTakenWatchAd(1);
        player.addPower(10, true);
        this.cacheAndPersist(player.getId(), player);
        return new ResPlayerProfile(player);
    }
    /** 结算时间 */
    static final long settleTime=1000L * 60 * 10;
    /** 最大体力 */
    static final int max_power=100;
    
    private void recoverPower(Player player, boolean persist) {
        Long lastTime = player.getLastRecoverPowerTime();
        long currentTime=System.currentTimeMillis();
        if (lastTime == null) {
            lastTime = currentTime;
        }
        int maxPower=player.getExtraPowerLimit()+max_power;
//        if(player.getPower()==null){//判空
//        	player.setPower(0);
//        }
        if(player.getPower()>=maxPower){//已经是上限不需要恢复
        	player.setLastRecoverPowerTime(currentTime);
        	if (persist) {
                this.cacheAndPersist(player.getId(), player);
            }
        	return;
        }
	    //恢复速度
	    long mySettleTime=settleTime;
	    if(player.getExtraRecoverRote()!=0){//如果有恢复加成，就减少恢复时间
	      	mySettleTime-=mySettleTime*player.getExtraRecoverRote();
	    }
	    //时间差
	    long sub=currentTime - lastTime;
	    if(sub<mySettleTime){
	        return;
	    }
	    int value=(int)(sub/mySettleTime)*10;
	    player.addPower(value);
	    long newTime=currentTime;
//	    if(player.getPower()<maxPower){//没有恢复满，等待下次恢复 
//	        newTime=mySettleTime*value+lastTime;
//	    }
	    player.setLastRecoverPowerTime(newTime);
        if (persist) {
	        this.cacheAndPersist(player.getId(), player);
	    }
        return;
    }
    
    public void recoverPower(Player player){
    	this.recoverPower(player,true);
    }
//     private void recoverPower(Player player, boolean persist) {
//        Long time = player.getLastRecoverPowerTime();
//        if (time == null) {
//            time = System.currentTimeMillis();
//        }
//        Long timeLeft = ((1000 * 60 * 4) - (System.currentTimeMillis() - time)) / 1000;
//
//        if (timeLeft <= 0) {
//            // 判断需要回复多少点电量
//            Long power = (System.currentTimeMillis() - time) / (1000 * 60 * 4);
//            player.addPower(power.intValue());
//            player.setLastRecoverPowerTime(System.currentTimeMillis());
//        }
//        if (persist) {
//            this.cacheAndPersist(player.getId(), player);
//        }
//    } 

    @Override
    public Player edit(ReqPlayerEdit reqPlayerEdit) {
        Player player = PlayerContext.getPlayer();
        if (StringHelper.isNoneBlank(reqPlayerEdit.getName())) {
            player.setName(reqPlayerEdit.getName());
        }
        if (StringHelper.isNoneBlank(reqPlayerEdit.getAvatarUrl())) {
            player.setAvatarUrl(reqPlayerEdit.getAvatarUrl());
        }
        if (StringHelper.isNoneBlank(reqPlayerEdit.getGender())) {
            player.setGender(reqPlayerEdit.getGender());
        }
        cacheAndPersist(player.getId(), player);
        return player;
    }

    @Override
    public ResPlayerProfile getProfile() {
        Player player = PlayerContext.getPlayer();
        if(StringHelper.isBlank(player.getRoleId())) {
            player.setRoleId(roleIdService.getNextRoleId());
            this.cacheAndPersist(player.getId(), player);
        }
        this.recoverPower(player, true);
        this.checkDailyReset(player);
        return new ResPlayerProfile(player);
    }

    private void setTencentInfo(Player player, String accessToken, String openid) {
        player.setTencentOpenid(openid);
        ResTencentUserInfo userInfo = tencentManager.getTencentService().getUserInfo(accessToken, openid);
        player.setGender(userInfo.getGender().equals("男") ? "1" : "2");
        if (userInfo.getNickname() != null) {
            try {
                player.setNickName(new String(Base64.getEncoder().encode(userInfo.getNickname().getBytes()), CommonConstant.UTF8));
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
            }
        }
        player.setAvatarUrl(userInfo.getFigureurl_qq());
    }

    private Player tencentRegister(String accessToken, String openid) {
        Player player = new Player();
        player.setRoleId(roleIdService.getNextRoleId());
        player.setIsGuest(0);
        this.setTencentInfo(player, accessToken, openid);
        return this.register(player);
    }

    private void setWechatInfo(Player player, String accessToken, String openid) {
        player.setWechatOpenid(openid);
        ResWechatUserInfo userInfo = wechatManager.getWechatService().getUserInfo(accessToken, openid);
        player.setGender(userInfo.getSex());
        if (userInfo.getNickname() != null) {
            try {
                player.setNickName(new String(Base64.getEncoder().encode(userInfo.getNickname().getBytes()), CommonConstant.UTF8));
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
            }
        }
        player.setAvatarUrl(userInfo.getHeadimgurl());
    }

    private Player wechatRegister(String accessToken, String openid) {
        Player player = new Player();
        player.setRoleId(roleIdService.getNextRoleId());
        player.setIsGuest(0);
        this.setWechatInfo(player, accessToken, openid);
        return this.register(player);
    }

    private Player guestRegister() {
        Player player = new Player();
        player.setRoleId(roleIdService.getNextRoleId());
        try {
            player.setNickName(new String(Base64.getEncoder().encode(("guest" + player.getRoleId()).getBytes()), CommonConstant.UTF8));
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        player.setGender("1");
        return this.register(player);
    }
    
    public Player guestRegisterByPhone() {
        Player player = new Player();
        player.setRoleId(roleIdService.getNextRoleId());
        player.setIsGuest(0);
        try {
            player.setNickName(new String(Base64.getEncoder().encode(("guest" + player.getRoleId()).getBytes()), CommonConstant.UTF8));
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        }
        player.setGender("1");
        return this.register(player,100);
    }
    
    private Player register(Player player) {
    	return register(player,100);
    	
    }
    public Player register(Player player,int power) {

        player.setId(IdGenerator.getNextId());
        player.setToken(UUID.randomUUID().toString());
        player.setLastRecoverPowerTime(System.currentTimeMillis());
        player.setPower(power);
        player.setExploration(0);  
        player.setAchievement(0);
        player.setRegister(System.currentTimeMillis());
        this.cacheAndPersist(player.getId(), player);
        return player;
    }
    @Override
    public Player auth(ReqPlayerEdit request) {
        Player player = PlayerContext.getPlayer();
        if (request.getName() != null) {
            try {
                player.setNickName(new String(Base64.getEncoder().encode(request.getName().getBytes()), CommonConstant.UTF8));
            } catch (UnsupportedEncodingException e) {
                log.error("", e);
            }
        }
        player.setAvatarUrl(request.getAvatarUrl());
        player.setGender(request.getGender());
        cacheAndPersist(player.getId(), player);
        return player;
    }

    /**
     * 各个模块的业务日重置
     *
     * @param player 玩家
     */
    private void onDailyReset(Player player) {
        player.setCostedPower(0);
        player.setTakenCostPower20(false);
        player.setTakenCostPower100(false);
        player.setTakenShare(false);
        player.setSharedCount(0);
        player.setWatchAdTask(0);
        player.setTakenWatchAdTask(0);
        this.cacheAndPersist(player.getId(), player);
    }
    
    /**
     * 根据roleid查找playerid
     * */
	@Override
	public PlayerIdBean findplayeridbyroleid(String roleid) {
		Query query = em.createNativeQuery("SELECT id,role_id,is_block,no_speak_time FROM player WHERE role_id = ? LIMIT 1");
		query.setParameter(1, roleid);
		List<PlayerIdBean> list = query.getResultList();
		PlayerIdBean play = null;
		
		for(Object item:list){
			
			Object[] obj = (Object[])item;
			
			for(int i = 0;i<obj.length;i++){				
				play = new PlayerIdBean();
				play.setPlayerid(obj[0].toString());
				play.setRoleid(obj[1].toString());
				play.setIs_block(Integer.parseInt(obj[2].toString()));
				play.setNo_speak_time(Long.valueOf(obj[3].toString()));
			}
		}
		return play;
	}

	/**
	 *根据playerid查询玩家信息
	 * */
    @Override
    public PlayerProperty findmessageBylayerId(long PlayerId) {
    	Query query = em.createNativeQuery("SELECT achievement,is_guest,avatar_url,nick_name,exploration,gender,is_block,no_speak_time FROM player WHERE id =? LIMIT 1");
		query.setParameter(1, PlayerId);
		List<PlayerProperty> resultList = query.getResultList();
		PlayerProperty bean = null;
		for (Object item : resultList) {
		    
			Object[] obj = (Object[])item;
			
			for(int i = 0;i<obj.length;i++){
				
				

					bean = new PlayerProperty();
					bean.setAchievement(Integer.parseInt(obj[0].toString()));
					
					if(obj[1]==null){
						obj[1]=1;
					}else{
						bean.setIs_guest(Integer.parseInt(obj[1].toString()));
					}
					
					if(obj[2]==null){
						obj[2]=null;
					}else{
						bean.setAvatar_url(obj[2].toString());
					}
				
					if(obj[3]==null){
						obj[3]=null;
					}else{
						bean.setNickName(obj[3].toString());
					}
					
					if(obj[5]==null){
						obj[5]=null;
					}else{
						bean.setGender(obj[5].toString());
					}					
					bean.setExploration(Integer.parseInt(obj[4].toString()));
					
					if(obj[6]==null){
						obj[6]=0;
					}else{
						bean.setIs_block(Integer.parseInt(obj[6].toString()));
					}
					
					if(obj[7]==null){
						obj[7]=0;
					}else{
						bean.setNo_speak_time(Long.valueOf(obj[7].toString()));
					}
			}
		}
		return bean;
    }
    
    /**
	 *根据ID查询信息并展示
	 * */
	@Override
	public PlayerProperty selectByRoleId(String roleId) {
		PlayerProperty bean = new PlayerProperty();
		PlayerIdBean playeridbean = findplayeridbyroleid(roleId);
		if(playeridbean==null){
			bean.setPlayerId("");
			bean.setRoleid("");
			bean.setIs_block(0);
			bean.setNo_speak_time(0);
		}else{
			 bean.setPlayerId(playeridbean.getPlayerid());
	    	 bean.setRoleid(playeridbean.getRoleid());
	    	 bean.setIs_block(playeridbean.getIs_block());
	    	 bean.setNo_speak_time(playeridbean.getNo_speak_time());
		}
		String PlayerId = playeridbean.getPlayerid();
		PlayerDynamicEntity load = playerDynamicService.load(Long.valueOf(PlayerId));
        UserEntity phone = userDao.findByPlayerId(Long.valueOf(PlayerId));
		PlayerOnlineEntity online = playerOnlineDao.findByID(Long.valueOf(PlayerId));
		PlayerProperty showbean = findmessageBylayerId(Long.valueOf(PlayerId));
		PlayerDynamicEntity fans = playerDynamicDao.findByID(PlayerId);
		
		
                if(fans==null){
       	             bean.setFansCount(0);
       	             bean.setAttentionCount(0);
                }else{
       	             bean.setFansCount(fans.getFansCount());
       	             bean.setAttentionCount(fans.getAttentionCount());
                }

	    	    if(load==null){
	    	    	bean.setNickName("");
	    	    	
	    	    }else{
	    	    	bean.setNickName(load.getNickName());
	    	    }
	    	    	    
	    	    if(phone==null){
	    	    	bean.setPhone("");
	    	    }else{
	    	    	bean.setPhone(phone.getPhone());
	    	    }
	    		
	    	    if(online==null){
	    	    	bean.setOnlineTime(0);
	    	    }else{
	    	    	bean.setOnlineTime(online.getOnlineTime());
	    	    }

	    	    if(showbean==null){
	    			bean.setAchievement(0);
	    			bean.setIs_guest(1);
	    			bean.setNickName("");
	    			bean.setAvatar_url("");
	    			bean.setGender("");
	    			bean.setExploration(0);
	    			
	    		}else{
	    			bean.setIs_guest(showbean.getIs_guest());
	    	    	bean.setAchievement(showbean.getAchievement());
	    	    	
	    	    	bean.setAvatar_url(showbean.getAvatar_url());
	    	    	bean.setGender(showbean.getGender());
	    	    	bean.setExploration(showbean.getExploration());
	    		}	
		return bean;
	}
	
    /**
     * 根据手机号查询信息并展示
     * */
	@Override
	public PlayerProperty selectByphone(String date) {
		PlayerProperty bean = new PlayerProperty();
		
        RanKingData byPhoneAchievement = getByPhoneAchievement(date);//调用根据手机号查询信息
        if(byPhoneAchievement==null){
        	bean.setPlayerId("");
        	bean.setAvatar_url("");
        	bean.setRoleid("");
        	bean.setNickName("");
        	bean.setPhone("");
        }else{
        	 bean.setPlayerId(String.valueOf(byPhoneAchievement.getId()));
             bean.setAvatar_url(byPhoneAchievement.getAvatar_url());
             bean.setRoleid(byPhoneAchievement.getRole_id());
             bean.setNickName(byPhoneAchievement.getNick_name());
             bean.setPhone(byPhoneAchievement.getPhone());
        }
 
        long id = Long.valueOf(byPhoneAchievement.getId());//获取playerid
        
        PlayerProperty player = findmessageBylayerId(id);//调用mysql查询信息
        if(player==null){
        	bean.setIs_guest(1);
        	bean.setIs_block(0);
        	bean.setNo_speak_time(0);
        }else{
        	bean.setIs_guest(player.getIs_guest());
            bean.setGender(player.getGender());
            bean.setIs_block(player.getIs_block());
            bean.setNo_speak_time(player.getNo_speak_time());
            
        } 
        PlayerOnlineEntity findByID = playerOnlineDao.findByID(id);//获取上线下线时间
        if(findByID==null){
        	bean.setOnlineTime(0);
        }else{       
            bean.setOnlineTime(findByID.getOnlineTime());
        }
        
        PlayerDynamicEntity fans = playerDynamicDao.findByID(id);
        if(fans==null){
        	bean.setFansCount(0);
        	bean.setAttentionCount(0);
        }else{
        	bean.setFansCount(fans.getFansCount());
        	bean.setAttentionCount(fans.getAttentionCount());
        }
        
        
       
		return bean;
	}
	
	/**
	 *根据昵称查询玩家信息
	 * */
	@Override
	public List<PlayerProperty> selectmessages(String nickname,PlayerPage playerPage) {
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		if(startQuery<0){
			startQuery=1;
		}
		List<PlayerProperty> playerProperty = new ArrayList<>();
		PlayerProperty p =  new PlayerProperty();
		Player player = new Player();
		Query query = em.createNativeQuery("SELECT achievement,is_guest,avatar_url,nick_name,exploration,gender,id,role_id,is_block,no_speak_time FROM player  WHERE nick_name = ? ORDER BY achievement DESC LIMIT ?,?");
		query.setParameter(1,player.setCodeName(nickname));
		query.setParameter(2,startQuery);
		query.setParameter(3,playerPage.getPage());
		List<Object[]> list = query.getResultList();
		
		int achievement = 0;
		int is_guest = 0;
		String avatar_url = "";
		String nick_name = "";
		int exploration = 0;
		String gender = "";
		String id = "";
		String role_id = "";
		int is_block = 0;
		long no_speak_time = 0;
		
       for (Object[] obj:list) {
    	   
    	   if(obj==null){
    		   break;
    	   }
    	   
    	   if(obj[0]==null){
    		   achievement = 0;
    	   }else{
    		   achievement = Integer.parseInt(obj[0].toString());
    	   }
    	   
    	   if(obj[1]==null){
    		   is_guest = 1;
    	   }else{
    		   is_guest = Integer.parseInt(obj[1].toString());
    	   }
    	   
    	   if(obj[2]==null){
    		   avatar_url = "";
    	   }else{
    		   avatar_url = obj[2].toString();
    	   }
    	   
    	   if(obj[3]==null){
    		   nick_name = "";
    	   }else{
    		   nick_name = player.decodeName(obj[3].toString());
    	   }
    	   
    	   if(obj[4]==null){
    		   exploration = 0;
    	   }else{
    		   exploration = Integer.parseInt(obj[4].toString());
    	   }
    	   
    	   if(obj[5]==null){
    		   gender = "";
    	   }else{
    		   gender = obj[5].toString();
    	   }
    	   
    	   if(obj[6]==null){
    		   id = "";
    	   }else{
    		   id = obj[6].toString();
    	   }
    	   
    	   if(obj[7]==null){
    		   role_id = "";
    	   }else{
    		   role_id = obj[7].toString();
    	   }
    	   
    	   if(obj[8]==null){
    		   is_block = 0;
    	   }else{
    		   is_block = Integer.parseInt(obj[8].toString());
    	   }
    	   
    	   if(obj[9]==null){
    		   no_speak_time = 0;
    	   }else{
    		   no_speak_time = Long.valueOf(obj[9].toString());
    	   }
    	   p = new PlayerProperty(id,nick_name,gender,is_guest,achievement,avatar_url,exploration,role_id,is_block,no_speak_time);

    	   playerProperty.add(p);
       }
	return playerProperty;	
}
	/**
	 *根据昵称查询玩家信息并展示
	 * */
	@Override
	public List<PlayerProperty> selectBynickname(String nickname,PlayerPage playerPage) {
		PlayerProperty bean = new PlayerProperty();		
		List<PlayerProperty> list = new ArrayList<>();
		List<PlayerProperty> selectByNickname = selectmessages(nickname,playerPage);
		if(selectByNickname==null){
             return null;
		}else{		
			
     	     for (int i = 0; i < selectByNickname.size(); i++) {
     	    	 
     	    	 bean =  new PlayerProperty(selectByNickname.get(i).getPlayerId(),selectByNickname.get(i).getNickName(),
     	    			selectByNickname.get(i).getGender(),selectByNickname.get(i).getIs_guest(),selectByNickname.get(i).getAchievement(),
     	    			selectByNickname.get(i).getAvatar_url(),selectByNickname.get(i).getExploration(),selectByNickname.get(i).getRoleid(),
     	    			selectByNickname.get(i).getIs_block(),selectByNickname.get(i).getNo_speak_time());

     	    	Long id = Long.valueOf(bean.getPlayerId());
    			 UserEntity phone = userDao.findByPlayerId(id);//电话信息
    			 if(phone==null){
    				 bean.setPhone("");
    			 }else{
    				 bean.setPhone(phone.getPhone());
    			 }
    			 
    			 PlayerOnlineEntity findByID = playerOnlineDao.findByID(id);//获取上线下线时间
    	              if(findByID==null){
    	            	  bean.setOnlineTime(0);
    	              }else{
    				 bean.setOnlineTime(findByID.getOnlineTime());
    	           }
    	              
    	              PlayerDynamicEntity fans = playerDynamicDao.findByID(id);
    	              if(fans==null){
    	            	  bean.setFansCount(0);
    	            	  bean.setAttentionCount(0);
    	              }else{
    	            	  bean.setFansCount(fans.getFansCount());
    	            	  bean.setAttentionCount(fans.getAttentionCount());
    	              }
    	              
    	              list.add(bean);
			}    	
    }
		return list;
 }
	
	 /**
     * 查询所有玩家信息
     * */
	@Override
	@Deprecated
	public List<PlayerAllBean> findall(PlayerPage playerPage) {
	
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		Player player = new Player();
		List<Long> arrayplayerid = new ArrayList<>();
		PlayerAllBean bean = null;
		if(startQuery<0){
			startQuery=1;
		}
		List<PlayerAllBean> arrayplayerAllBean = new ArrayList<>();

		Query query = em.createNativeQuery("SELECT nick_name,role_id,id,is_block,no_speak_time FROM player ORDER BY achievement DESC  LIMIT ?,?");
		query.setParameter(1,startQuery);
		query.setParameter(2,playerPage.getPage());
		List list = query.getResultList();
		 
		
		for (Object item : list) {
			
			Object[] obj = (Object[])item;
            
			for(int i = 0;i<obj.length;i++){
				 bean = new PlayerAllBean(obj[1].toString(),player.decodeName(obj[0].toString()),obj[2].toString(),Integer.parseInt(obj[3].toString()),Long.valueOf(obj[4].toString()));
				
				arrayplayerid.add(Long.valueOf(obj[2].toString()));
			}
			
			arrayplayerAllBean.add(bean);
		}
		

		List<UserEntity> arrUserEntity = userDao.findAllByPlayerIds(arrayplayerid);
		for (UserEntity userEntity : arrUserEntity) {		
			for (PlayerAllBean playerAllBean : arrayplayerAllBean){				
				if(userEntity.getPlayerId()==Long.valueOf(playerAllBean.getPlayerid())){
					playerAllBean.setPhone(userEntity.getPhone());
				}
			}
	
		}
		
		List<PlayerDynamicEntity> arrayPlayerDynamicEntity = playerDynamicDao.findByIds(arrayplayerid);		
		for (PlayerDynamicEntity arrayplayerDynamicEntity : arrayPlayerDynamicEntity) {
			for (PlayerAllBean playerAllBean : arrayplayerAllBean) {
				if(arrayplayerDynamicEntity.getPlayerId()==Long.valueOf(playerAllBean.getPlayerid())){
					playerAllBean.setAttentionCount(arrayplayerDynamicEntity.getAttentionCount());
					playerAllBean.setFansCount(arrayplayerDynamicEntity.getFansCount());					
				}
				
			}
		}
		
	
		List<PlayerOnlineEntity> arrayPlayerOnlineEntity = playerOnlineDao.findByIdOnlineTime(arrayplayerid);
		for (PlayerOnlineEntity playerOnlineEntity : arrayPlayerOnlineEntity) {
			for (PlayerAllBean playerAllBean : arrayplayerAllBean) {
				if(playerOnlineEntity.getPlayerId()==Long.valueOf(playerAllBean.getPlayerid())){
					playerAllBean.setOnlineTime(playerOnlineEntity.getOnlineTime());
				}
			}
		}
		

		List<PlayerProperty> arrayisPlayercount = findisPlayerById(arrayplayerid);
		for (PlayerProperty playerProperty : arrayisPlayercount) {
			for (PlayerAllBean playerAllBean : arrayplayerAllBean) {
					playerAllBean.setIsplayernum(playerProperty.getIsplayercount());									
			}
		}
		return arrayplayerAllBean;
	}
		 
    /**
     * 查询所有剧本的编号
     */
	@Override
	public List<Integer> getScenarioAll() {
		 Query query = em.createNativeQuery("SELECT DISTINCT GAME_ID FROM SCORE");
		 List<Object[]> resultList = query.getResultList();
		 List<Integer> scenarioAll = new ArrayList<Integer>();
		 for (Object obj : resultList) {
			 if(obj == null) {
				 return null ;
			 }
			 scenarioAll.add(Integer.valueOf(obj.toString()));
		}
		 return scenarioAll;
	}
	
	/**
	 * 查询所有剧本的对应玩家数量
	 */
	@Override
	public Map<String,Integer> getPlayerNum() {
		List<Integer> scenarioAll = getScenarioAll();
		Map<String,Integer> eachIdAll = new HashMap<String,Integer>();
		GameCatalogCache cache = JsonCacheManager.getCache(GameCatalogCache.class);
		List<GameCatalog_Json> jsonlist =  cache.getList();
		for (Integer scenarioId : scenarioAll) {
			Query query = em.createNativeQuery("SELECT COUNT(*) FROM SCORE WHERE GAME_ID=?");
			query.setParameter(1, scenarioId);
			 List<Object[]> resultList = query.getResultList();
			 for (Object obj : resultList) {
				 for (GameCatalog_Json gameCatalog_Json : jsonlist) {
					 if(scenarioId.equals(gameCatalog_Json.getId())) {
						eachIdAll.put(gameCatalog_Json.getName(),Integer.valueOf(obj.toString()));
					 }
				 }
			 }
		}
		 return eachIdAll;
	}
	
	/**
	 * 查询已评分的玩家
	 */
	@Override
	public Map<Integer,Integer> getGradePlayer() {
		List<Integer> scenarioAll = getScenarioAll();
		Map<Integer,Integer> gradePlayer = new HashMap<Integer, Integer>();
		for (Integer scenarioId : scenarioAll) {
			Query queryGradePlayer = em.createNativeQuery("SELECT COUNT(GAME_ID) FROM SCORE WHERE GAME_ID=? AND SCORE != 0");
			queryGradePlayer.setParameter(1, scenarioId);
			List<Object[]> numResultList = queryGradePlayer.getResultList();
			for (Object obj : numResultList) {
				if(obj == null) {
					gradePlayer.put(scenarioId,0);
					break;
				}
				gradePlayer.put(scenarioId,Integer.valueOf(obj.toString())) ;
			}
		}
		return gradePlayer;
	}
	
	/**
	 * 查询所有剧本的对应玩家平均分
	 */
	@Override
	public List<PlayerScoreBean> getAverageScore() {
		List<Integer> scenarioAll = getScenarioAll();
		Map<Integer,Integer> playerNum = getGradePlayer();
		List<PlayerScoreBean> averageValue = new ArrayList<PlayerScoreBean>();
		GameCatalogCache cache = JsonCacheManager.getCache(GameCatalogCache.class);
		List<GameCatalog_Json> jsonlist =  cache.getList();
		PlayerScoreBean playerScoreBean = new PlayerScoreBean();
		for (Integer scenarioId : scenarioAll) {
			Query playerTotal = em.createNativeQuery("SELECT SUM(SCORE) FROM SCORE WHERE SCORE != 0 AND GAME_ID = ?");
			playerTotal.setParameter(1, scenarioId);
			List<Object[]> TotalResultList = playerTotal.getResultList();
			for (Object obj : TotalResultList) {
				for (GameCatalog_Json gameCatalog_Json : jsonlist) {
				if(obj == null) {
					if(scenarioId.equals(gameCatalog_Json.getId())) {
						playerScoreBean = new PlayerScoreBean(gameCatalog_Json.getName(),0.0,playerNum.get(scenarioId));
						averageValue.add(playerScoreBean);
						break;
					}
				}
				if(playerNum.get(scenarioId) == null) {
					if(scenarioId.equals(gameCatalog_Json.getId())) {
						playerScoreBean = new PlayerScoreBean(gameCatalog_Json.getName(),0.0,playerNum.get(scenarioId));
						averageValue.add(playerScoreBean);
						break;
					}
				}
				if(playerNum.get(scenarioId) == 0) {
					if(scenarioId == gameCatalog_Json.getId()) {
						playerScoreBean = new PlayerScoreBean(gameCatalog_Json.getName(),0.0,playerNum.get(scenarioId));
						averageValue.add(playerScoreBean);
						break;
					}
				}
				if(scenarioId.equals(gameCatalog_Json.getId())) {
					playerScoreBean = new PlayerScoreBean(gameCatalog_Json.getName(),Integer.valueOf(obj.toString())/(double)playerNum.get(scenarioId),playerNum.get(scenarioId));
					averageValue.add(playerScoreBean);
				}
			}
			}
		}
		return averageValue;
	}


	/**
	 * 分页查询成就排行榜玩家
	 */
	@Override
	public List<RanKingData> getAchievement(PlayerPage playerPage) {
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query AchievementId = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,ACHIEVEMENT FROM PLAYER ACHIEVEMENT ORDER BY ACHIEVEMENT DESC LIMIT ?,?");
			AchievementId.setParameter(1, startQuery);
			AchievementId.setParameter(2, playerPage.getPage());
			List<Object[]> idResultList  = AchievementId.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : idResultList) {
				if(obj == null) {
					break;
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	
	/**
	 * 分页查询探索排行榜玩家
	 */
	@Override
	public List<RanKingData> getExploration(PlayerPage playerPage) {
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query ExplorationtId = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,EXPLORATION FROM PLAYER ORDER BY EXPLORATION DESC LIMIT ?,?");
			ExplorationtId.setParameter(1, startQuery);
			ExplorationtId.setParameter(2, playerPage.getPage());
			List<Object[]> idResultList  = ExplorationtId.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : idResultList) {
				if(obj == null) {
					break;
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	/**
	 * 分页查询探索排行榜已绑定手机号的玩家
	 */
	@Override
	public List<RanKingData> getExplorationPhone(PlayerPage playerPage) {
		List<RanKingData> kingDataList  = getExploration(playerPage);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData data : kingDataList) {
			ranKingDataIdList.add(data.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData data : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(data.getId())) {
					ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}
	public  void checkGameCatalog(){
		GameCatalogCache gameCatalogCache = JsonCacheManager.getCache(GameCatalogCache.class);
		CreatRoleCache creatRoleCache = JsonCacheManager.getCache(CreatRoleCache.class);
		 
		for(GameCatalog_Json json:gameCatalogCache.getList()){
			Integer playerId=json.getAuthorid();
			if(creatRoleCache.getData(playerId)==null){
				throw new RuntimeException("gamecatalog 表作者id 不存在！       id:"+json.getId()+",authorId:"+json.getAuthorid());
			}
		}
	}
	@Override
	public void creatRobot() {
		CreatRoleCache creatRoleCache = JsonCacheManager.getCache(CreatRoleCache.class);
		List<CreatRole_Json> creatRole_Jsons = creatRoleCache.getList();
		Player player = null;
		PlayerDynamicEntity playerDynamicEntity = null;
		long currentTime = System.currentTimeMillis();
		for (CreatRole_Json creatRole_Json : creatRole_Jsons) {
			player =get(Long.valueOf(creatRole_Json.getId()));
			if (player != null) {
				//修改机器人成就和探索度
				 player.setRoleId(creatRole_Json.getRoleId());
				 player.setExploration(creatRole_Json.getExploration());  
			     player.setAchievement(creatRole_Json.getAchievement());
			     player.setUpdate();
			     playerDynamicEntity = playerDynamicService.load(player);
			     playerDynamicEntity.setRoleId(creatRole_Json.getRoleId());
			     playerDynamicDao.save(playerDynamicEntity);
			     this.persist(player);
				continue;
			}
			player = new Player();
			player.setId(Long.valueOf(creatRole_Json.getId()));
			player.setRoleId(creatRole_Json.getRoleId());
			try {
	            player.setNickName(new String(Base64.getEncoder().encode(creatRole_Json.getNickName().getBytes()), CommonConstant.UTF8));
	        } catch (UnsupportedEncodingException e) {
	            log.error("", e);
	        }
			player.setIsGuest(0);
			player.setToken(UUID.randomUUID().toString());
			player.setAvatarUrl(creatRole_Json.getAvatarUrl());
			if ("男".equals(creatRole_Json.getGender())) {
				 player.setGender("1");
			}else {
				 player.setGender("2");
			}       
	        player.setLastRecoverPowerTime(currentTime);
	        player.setPower(100);
	        player.setExploration(creatRole_Json.getExploration());  
	        player.setAchievement(creatRole_Json.getAchievement());
	        playerDynamicEntity = new PlayerDynamicEntity(player);
	        this.persist(player);
	        playerDynamicDao.save(playerDynamicEntity);
		}
	}	
	/**
	 * 分页查询成就排行榜已绑定手机号的玩家
	 */
	@Override
	public List<RanKingData> getAchievementPhone(PlayerPage playerPage) {
		List<RanKingData>  kingDataList = getAchievement(playerPage);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData data : kingDataList) {
			ranKingDataIdList.add(data.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData data : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(data.getId())) {
					ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(data.getId(),data.getAvatar_url(),data.getRole_id(),data.getNick_name(),data.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}
	
	/**
     * 根据成就昵称查找用户信息
     */
	@Override
	public List<RanKingData> getByNameAchievementData(PlayerPage playerPage,String name) {
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query query = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,ACHIEVEMENT FROM PLAYER WHERE NICK_NAME=?LIMIT ?,?");
			query.setParameter(1, player.setCodeName(name));
			query.setParameter(2, startQuery);
			query.setParameter(3, playerPage.getPage());
			List<Object[]> resultList = query.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : resultList) {
				if(obj == null) { 
				  break; 
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	
	/**
	 * 根据成就指定昵称处理成就榜是否绑定手机号
	 */
	@Override
	public List<RanKingData> getByNameAchievement(PlayerPage playerPage,String name) {
		List<RanKingData> kingDataList  = getByNameAchievementData(playerPage,name);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData kingData : kingDataList) {
			ranKingDataIdList.add(kingData.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData kingData : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(kingData.getId())) {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}

	/**
     * 根据成就指定角色ID查找用户信息
     */
	@Override
	public List<RanKingData> getByIdAchievementData(String roleId) {
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query query = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,ACHIEVEMENT FROM PLAYER WHERE ROLE_ID=?");
			query.setParameter(1, roleId);
			List<Object[]> resultList = query.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : resultList) {
				if(obj == null) { 
				  break; 
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	
	/**
	 * 根据成就指定ID处理成就榜是否绑定手机号
	 */
	@Override
	public List<RanKingData> getByIdAchievement(String roleId) {
		List<RanKingData> kingDataList  = getByIdAchievementData(roleId);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData kingData : kingDataList) {
			ranKingDataIdList.add(kingData.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData kingData : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(kingData.getId())) {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}

	/**
     * 根据成就手机号码查找用户信息
     */
	@Override
	public RanKingData getByPhoneAchievement(String phone) {
		UserEntity userEntity = userDao.findDataByPhone(phone);
		RanKingData ranKingData = new RanKingData();
		if(userEntity == null) {
			return ranKingData;
		}
		Player player = this.get(userEntity.getPlayerId());
		ranKingData = new RanKingData(String.valueOf(player.getId()),player.getAvatarUrl(),player.getRoleId(),player.decodeName(player.getNickName()),String.valueOf(player.getAchievement()),phone);
		return ranKingData;
	}

	/**
     * 根据探索昵称查找用户信息
     */
	@Override
	public List<RanKingData> getByNameExplorationData(PlayerPage playerPage, String name) {
		int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query query = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,EXPLORATION FROM PLAYER WHERE NICK_NAME=? LIMIT ?,?");
			query.setParameter(1, player.setCodeName(name));
			query.setParameter(2, startQuery);
			query.setParameter(3, playerPage.getPage());
			List<Object[]> resultList = query.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : resultList) {
				if(obj == null) { 
				  break; 
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	
	/**
	 * 根据指定昵称处理探索榜是否绑定手机号
	 */
	@Override
	public List<RanKingData> getByNameExploration(PlayerPage playerPage, String name) {
		List<RanKingData> kingDataList  = getByNameExplorationData(playerPage,name);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData kingData : kingDataList) {
			ranKingDataIdList.add(kingData.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData kingData : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(kingData.getId())) {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}

	/**
     * 根据角色ID查找用户信息
     */
	@Override
	public List<RanKingData> getByIdExplorationData(String roleId) {
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		Player player = new Player();
		Query query = em.createNativeQuery("SELECT ID,AVATAR_URL,ROLE_ID,NICK_NAME,EXPLORATION FROM PLAYER WHERE ROLE_ID=?");
			query.setParameter(1, roleId);
			List<Object[]> resultList = query.getResultList();
			String avatar_url = "";
			String role_id = "";
			String nick_name = "";
			for (Object[] obj : resultList) {
				if(obj == null) { 
				  break; 
				}
				if(obj[1] == null) {
					avatar_url = "玩家没有上传头像";
				} else {
					avatar_url = obj[1].toString();
				}
				if(obj[2] == null) {
					role_id = "玩家没有角色ID";
				} else {
					role_id = obj[2].toString();
				}
				if(obj[3] == null) {
					nick_name = "玩家没有角色昵称";
				} else {
					nick_name = player.decodeName(obj[3].toString());
				}
				ranKingData = new RanKingData(obj[0].toString(),avatar_url,role_id,nick_name,obj[4].toString());
				ranKingDataList.add(ranKingData);
			}
		return ranKingDataList;
	}
	
	/**
	 * 根据指定ID处理探索榜是否绑定手机号
	 */
	@Override
	public List<RanKingData> getByIdExploration(String roleId) {
		List<RanKingData> kingDataList  = getByIdExplorationData(roleId);
		List<String> ranKingDataIdList = new ArrayList<String>();
		for (RanKingData kingData : kingDataList) {
			ranKingDataIdList.add(kingData.getId());
		}
		List<RanKingData> ranKingDataList = new ArrayList<RanKingData>();
		RanKingData ranKingData = new RanKingData();
		List<UserEntity> userEntity = userDao.findAllByPlayerId(ranKingDataIdList);
		for (RanKingData kingData : kingDataList) {
			for(UserEntity RanData : userEntity) {
				if(RanData.getPlayerId() == Long.valueOf(kingData.getId())) {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),RanData.getPhone());
					ranKingDataList.add(ranKingData);
					break;
				} else {
					ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
					ranKingDataList.add(ranKingData);
					break;
				}
			}
		ranKingData = new RanKingData(kingData.getId(),kingData.getAvatar_url(),kingData.getRole_id(),kingData.getNick_name(),kingData.getTopScore(),"玩家没有绑定手机号");
		ranKingDataList.add(ranKingData);
		}
		return ranKingDataList;
	}
	
	/**
     * 根据手机号码查找用户信息
     */
	@Override
	public RanKingData getByPhoneExploration(String phone) {
		UserEntity userEntity = userDao.findDataByPhone(phone);
		RanKingData ranKingData = new RanKingData();
		if(userEntity == null) {
			return ranKingData;
		}
		Player player = get(userEntity.getPlayerId());
		ranKingData = new RanKingData(String.valueOf(player.getId()),player.getAvatarUrl(),player.getRoleId(),player.decodeName(player.getNickName()),String.valueOf(player.getAchievement()),phone);
		return ranKingData;
	}
	
	@Override
	public Long getPlayerByRoleId(String roleId) {
		Query query = em.createNativeQuery("SELECT ID FROM PLAYER WHERE ROLE_ID=?");
		query.setParameter(1, roleId);
		List<Object> resultList = query.getResultList();
		if (resultList == null||resultList.size() == 0) {
			return null;
		}
		return Long.valueOf(resultList.get(0).toString());
	}
	@Override
	public long getPlayerCountDB() {
		Query playerNum = em.createNativeQuery("SELECT COUNT(*) FROM PLAYER");
		List<Object[]> idResultList = playerNum.getResultList();
		int num = 0;
		for (Object obj : idResultList) {
			num = Integer.valueOf(obj.toString());
		}
		JedisUtils ju = JedisUtils.getInstance();
		ju.set("playerNum", num);
		return num;
	}
	/**
	 * 缓存中查询玩家总人数缓存中没有去DB查找
	 */
	@Override
	public long getPlayerCount() {
		JedisUtils ju = JedisUtils.getInstance();
		Integer num = ju.get("playerNum", Integer.class);
		if(num == null) {
			return getPlayerCountDB();
		}
		return num;
	}
	
	/**
	 * 查询指定字段玩家总人数
	 */
	@Override
	public int getAssignPlayerCount(String fields,String data) {
		Query playerNum = em.createNativeQuery(createSqlWord(fields,data));
		List<Object[]> idResultList = playerNum.getResultList();
		int num = 0;
		for (Object obj : idResultList) {
			num = Integer.valueOf(obj.toString());
		}
		return num;
	}
	/**
	 * 根据参数生成sql语句
	 */
	@Override
	public String createSqlWord(String fields, String data) {
		if("NICK_NAME".equals(fields)) {
			Player player = new Player();
			return "SELECT COUNT(*) FROM PLAYER WHERE "+fields+" = '"+player.setCodeName(data)+"'";
		}else {
			return "SELECT COUNT(*) FROM PLAYER WHERE "+fields+" = "+data+"";
		}
	}
	@Override
	public List<ResStplayerInfo> ListPlayerByAch(int page ,int count) {
		Query query = em.createNativeQuery("SELECT ID,ROLE_ID,ACHIEVEMENT,EXPLORATION FROM PLAYER WHERE IS_GUEST = 0  ORDER BY ACHIEVEMENT DESC LIMIT ?,?");
		query.setParameter(1, page);
		query.setParameter(2, count);
		List<Object[]> list = query.getResultList();
		List<ResStplayerInfo> ResultList = new ArrayList<>();
		if (list == null ||list.size()==0) {
			return ResultList;
		}
		for (Object[] obj : list) {
			ResStplayerInfo resStplayerInfo = new ResStplayerInfo();
			resStplayerInfo.setPlayerId(obj[0].toString());
			resStplayerInfo.setRoleId(obj[1].toString());
			resStplayerInfo.setAchievement(Integer.parseInt(obj[2].toString()));
			resStplayerInfo.setClues(Integer.parseInt(obj[3].toString()));
			ResultList.add(resStplayerInfo);
		}
		return ResultList;
	}
	
	 /**
     * 查询违规玩家信息
     * */
	@Override
	public List<PlayerProperty> findFoul(PlayerPage playerPage) {
		Player player = new Player();
	    int startQuery = (playerPage.getStart()-1)*playerPage.getPage(); //第几条开始
		Query query = em.createNativeQuery("SELECT role_id,id,is_block,nick_name,no_speak_time FROM player WHERE is_block = 1 OR no_speak_time !=0 LIMIT ?,?");
		query.setParameter(1, startQuery);
		query.setParameter(2, playerPage.getPage());
		List<Object[]> resultList = query.getResultList();
		if(startQuery<0){
			startQuery=1;
		}
		List<PlayerProperty> list = new ArrayList<>();
		List<Long> arrayplayerid = new ArrayList<>();
		for (Object[] object : resultList) {
			PlayerProperty bean = new PlayerProperty();
			bean.setRoleid(object[0].toString());
			bean.setPlayerId(object[1].toString());
			bean.setIs_block(Integer.parseInt(object[2].toString()));
			bean.setNickName(player.decodeName(object[3].toString()));
			bean.setNo_speak_time(Long.valueOf(object[4].toString()));
			list.add(bean);
			arrayplayerid.add(Long.valueOf(object[1].toString()));
			
		}
		
		List<DiscussEntity> arrayfindByPlayerids = discussDao.findByPlayerids(arrayplayerid);
		for (DiscussEntity discussEntity : arrayfindByPlayerids) {
			for (PlayerProperty bean : list){
				if(Long.toString(discussEntity.getFromPlayerId()).equals(bean.getPlayerId())){
					bean.setDistance(discussEntity.getContent());
				}
			}		
		}
		return list;
	}
	
	/**
	 * 查询玩家玩过剧本个数
	 * */
	@Override
	public List<PlayerProperty> findisPlayerById(List<Long> playerid){
		Query query = em.createNativeQuery("select count(game_id) from score where player_id = ?");
		query.setParameter(1, playerid);
		List<PlayerProperty> list = new ArrayList<>();
		List<BigInteger> resultList = query.getResultList();
        for (BigInteger bigInteger : resultList) {
        	PlayerProperty bean = new PlayerProperty();
			bean.setIsplayercount(Integer.parseInt(bigInteger.toString()));
			list.add(bean);
		}	
		return list;
	}
	
	/**
	 * 查询某个昵称的总条数
	 * */
	@Override
	@Deprecated
	public long findnicknamecount(String nickname){
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM player  WHERE nick_name = ? ");
		Player player = new Player();
		query.setParameter(1,player.setCodeName(nickname));
		List<Object> resultList = query.getResultList();	
		int num = 0;
		for (Object obj : resultList) {
			num = Integer.valueOf(obj.toString());
		}
		JedisUtils ju = JedisUtils.getInstance();
		ju.set("playerNum", num);
		return num;
	}
	
	/**
	 * 查询数据库违规总数
	 * */
	@Override
	@Deprecated
	public long badcount() {
		Query query = em.createNativeQuery("SELECT COUNT(*) FROM player WHERE is_block = 1 OR no_speak_time !=0 ");
		Player player = new Player();
		List<Object> resultList = query.getResultList();	
		int num = 0;
		for (Object obj : resultList) {
			num = Integer.valueOf(obj.toString());
		}
		JedisUtils ju = JedisUtils.getInstance();
		ju.set("playerNum", num);
		return num;
	}
	@Override
	public List<Player> listInitPlayer(int page) {
		int index = (page - 1 )* 500;
		Query query = em.createQuery("from Player where isGuest = 1");		
		query.setFirstResult(index);//起始查找位置
		query.setMaxResults(500);//查询数量
		List<Player> list = query.getResultList();
		return list;
	}
	
	@Override
	public Map<String,PlayerAstrictBean> getPlayerAstrictData(List<Long> playerIds) {
		Map<String,PlayerAstrictBean> map = new HashMap<String,PlayerAstrictBean>();
		if(playerIds.size() == 0) {
			return map;
		} else {
			Query query = em.createNativeQuery("SELECT ID,NO_SPEAK_TIME,IS_BLOCK FROM PLAYER WHERE ID IN (:PLAYERIDS)");
	        query.setParameter("PLAYERIDS", playerIds);
	         List<Object[]> resultList = query.getResultList();
	         if (resultList == null) {
				return map;
			}
	         for (Object[] objects : resultList) {
	        	 PlayerAstrictBean playerAstrictBean = new PlayerAstrictBean();
	        	 playerAstrictBean.setPlayerId(objects[0].toString());
	        	 playerAstrictBean.setNo_speak_time(objects[1].toString());
	        	 playerAstrictBean.setIs_block(objects[2].toString());
	        	 map.put(objects[0].toString(), playerAstrictBean);
			}
			return map;
		}
	}
	@Override
	public PlayerAstrictBean getPlayerAstrictData(long playerId) {
		PlayerAstrictBean playerAstrictBean = new PlayerAstrictBean();
		Query query = em.createNativeQuery("SELECT ID,NO_SPEAK_TIME,IS_BLOCK FROM PLAYER WHERE ID = ?");
        query.setParameter(1, playerId);
         List<Object[]> resultList = query.getResultList();
         if (resultList == null) {
			return playerAstrictBean;
		}
         for (Object[] objects : resultList) {
        	 playerAstrictBean.setPlayerId(objects[0].toString());
        	 playerAstrictBean.setNo_speak_time(objects[1].toString());
        	 playerAstrictBean.setIs_block(objects[2].toString());
		}
		return playerAstrictBean;
	}
	@Override
	public Map<String, PlayerAstrictBean> findAtFoulPlayAll(int page,int count) {
		Map<String,PlayerAstrictBean> map = new HashMap<String,PlayerAstrictBean>();
		Query query = em.createNativeQuery("SELECT ID,NO_SPEAK_TIME,IS_BLOCK FROM PLAYER WHERE (? - NO_SPEAK_TIME < 0) OR IS_BLOCK = 1 LIMIT ?,?");
		int qlPage = (page - 1) * count;
		long atTime = System.currentTimeMillis();
		query.setParameter(1, atTime);
		query.setParameter(2, qlPage);
		query.setParameter(3, count);
		List<Object[]> resultList = query.getResultList();
        if (resultList == null) {
			return map;
		}
        for (Object[] objects : resultList) {
        	 PlayerAstrictBean playerAstrictBean = new PlayerAstrictBean();
	       	 playerAstrictBean.setPlayerId(objects[0].toString());
	       	 playerAstrictBean.setNo_speak_time(objects[1].toString());
	       	 playerAstrictBean.setIs_block(objects[2].toString());
	       	 map.put(objects[0].toString(), playerAstrictBean);
		}
		return map;
	}
	@Override
	public long findAtFoulPlayCount() {
		long foulPlayCount = 0;
		Query query = em.createNativeQuery("SELECT COUNT(ID) FROM PLAYER WHERE (? - NO_SPEAK_TIME < 0) OR IS_BLOCK = 1");
		long atTime = System.currentTimeMillis();
		query.setParameter(1, atTime);
		List<Object[]> resultList = query.getResultList();
		if(resultList.size() == 0){
			return foulPlayCount;
		}
		Object obj = resultList.get(0);
		foulPlayCount = Long.valueOf(obj.toString());
		return foulPlayCount;
	}
	public void loginExecute(Player player) {
		//同步player和playerdynamic数据
		  playerDynamicService.synchdata(player);
		  //更新登录时间
		  playerLoginTimeService.updatePlayerLogin(player.getId());
		  GameParams_Json cache=JsonCacheManager.getCache(GameParamsCache.class).getGameParams_Json();
		  //判断是否为老用户
		  if (player.getRegister() == Long.valueOf(cache.getDefaultRegisteredTime())) {
			  BagEntity bagEntity = bagService.load(player.getId());
			  if (!bagEntity.getBagItems().containsKey(cache.getBetaUserRewardId())) {
				  //添加奖励
				  bagEntity.getBagItems().put(cache.getBetaUserRewardId(), 1);
				  bagDao.save(bagEntity);
				  //解锁广场				
				 playerTagsService.add(cache.getRewardTagId(), player);
				 //解锁标签
				achievementService.addTag(player.getId(), cache.getRewardTagId());	
				systemNoticeService.sendSystemNotice(SystemNotice, 0, player.getId());
			  }
		}
	}
	
	/**
	 * 查询玩家玩过的剧本个数 并记录玩过剧本个数
	 */
	@Override
	public int getPlayerNumAlsoRecord() {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		if(playerDynamicEntity != null){ //不是游客 
			int num = playerDynamicEntity.getScenarioNum();
			num += 1;
			playerDynamicDao.updateScenarioNum(player.getId(),num);
		}
		return 0;
	}
	
	/**
	 * 查询指定玩家货币数量
	 */
	@Override
	public int getCurrencyNum() {
		Player player = PlayerContext.getPlayer();
		PlayerDynamicEntity playerDynamicEntity = playerDynamicDao.findByID(player.getId());
		if(playerDynamicEntity != null){ //不是游客
			return playerDynamicEntity.getCurrency();
		}
		return 0;
	}
	
	/**
	 * 查询玩家电量上限
	 */
	@Override
	public int getPowerLimit() {
		Player player = PlayerContext.getPlayer();
		//获取额外上限值
		int extra = player.getExtraPowerLimit();
		//返回上限值 默认100上限+额外上限值
		return extra + 100;
	}
	
	/**
	 * 查询玩家当前电量
	 */
	@Override
	public ResPlayerPowerData getPower() {
		Player player = PlayerContext.getPlayer();
		//获取玩家电量
		ResPlayerPowerData resPlayerPowerData = new ResPlayerPowerData();
		resPlayerPowerData.setPower(player.getPower());
		resPlayerPowerData.setLimit(player.getExtraPowerLimit() + 100);
		return resPlayerPowerData;
	}
}
