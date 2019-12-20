package com.kratos.game.herphone.player.web;

import java.util.List;
import java.util.Map;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.globalgame.auto.json.Achievement_Json;
import com.kratos.engine.framework.net.socket.exception.BusinessException;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
//import com.kratos.game.herphone.auto.server.AchievementConfig;
import com.kratos.game.herphone.player.domain.Player;
import com.kratos.game.herphone.player.message.ReqPlayerEdit;
import com.kratos.game.herphone.player.message.ReqPlayerTencentLogin;
import com.kratos.game.herphone.player.message.ReqPlayerWechatLogin;
import com.kratos.game.herphone.player.message.ResPlayerLogin;
import com.kratos.game.herphone.player.message.ResPlayerProfile;
import com.kratos.game.herphone.player.message.ResRankPlayer;
import com.kratos.game.herphone.sms.message.ReqSmsCode;

import com.globalgame.auto.json.GameParams_Json;
import com.kratos.game.herphone.json.JsonCacheManager;
import com.kratos.game.herphone.json.datacache.GameParamsCache;
@RestController
@RequestMapping("/player")
@PrePermissions
public class PlayerController extends BaseController{


    /**
     * 微信登陆
     */
    @PostMapping("/wechat/login")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> wechatLogin(@RequestBody ReqPlayerWechatLogin request) {
        return new ResponseEntity<>(playerService.wechatLogin(request.getCode()), HttpStatus.OK);
    }

    /**
     * QQ登陆
     */
    @PostMapping("/tencent/login")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> tencentLogin(@RequestBody ReqPlayerTencentLogin request) {
        request.setChannelId(null);//老接口 没有channelId
    	return new ResponseEntity<>(playerService.tencentLogin2(request), HttpStatus.OK);
    }

    /**
     * QQ登陆2
     */
    @PostMapping("/tencent/login2")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> tencentLogin2(@RequestBody ReqPlayerTencentLogin request) {
        return new ResponseEntity<>(playerService.tencentLogin2(request), HttpStatus.OK);
    }
    
    /**
     * 游客登陆
     */
    @PostMapping("/guest/login")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> guestLogin() {
        return new ResponseEntity<>(playerService.guestLogin(), HttpStatus.OK);
    }
    
    /**
     * 手机登录  
     */
    @PostMapping("/mobileLogin")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> mobileLogin(@RequestBody ReqSmsCode param){	
    	GameParams_Json cache=JsonCacheManager.getCache(GameParamsCache.class).getGameParams_Json();
     	//
        if(cache.getIos_assessor_phone().contains(param.getMobile())){
        	return new ResponseEntity<ResPlayerLogin>(userService.validationBinding(param.getMobile()),HttpStatus.OK);
        }
    	boolean b = smsService.validateVerifyCode(param.getMobile(), "", param.getCode());
    	if (!b) {
			throw new BusinessException("验证码错误或验证码已失效");
		}
    	 return new ResponseEntity<ResPlayerLogin>(userService.validationBinding(param.getMobile()),HttpStatus.OK);
    }
    /**
     * 手机注册
     */
    @PostMapping("/mobileRegister")
    @PrePermissions(required = false)
    public ResponseEntity<ResPlayerLogin> mobileRegister(@RequestBody ReqSmsCode param){
    	GameParams_Json cache=JsonCacheManager.getCache(GameParamsCache.class).getGameParams_Json();
     	//
        if(cache.getIos_assessor_phone().contains(param.getMobile())){
        	 return new ResponseEntity<ResPlayerLogin>(userService.registerByPhone((param.getMobile())),HttpStatus.OK);
        }
    	boolean b = smsService.validateVerifyCode(param.getMobile(), "", param.getCode());
    	if (!b) {
			throw new BusinessException("验证码错误或验证码已失效");
		}
    	 return new ResponseEntity<ResPlayerLogin>(userService.registerByPhone((param.getMobile())),HttpStatus.OK);
    }
    /**
     * 修改用户
     */
    @PostMapping
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> edit(@RequestBody ReqPlayerEdit request) {
        Player player = playerService.edit(request);
        return new ResponseEntity<>(new ResPlayerProfile(player), HttpStatus.OK);
    }

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> profile() {
        return new ResponseEntity<>(playerService.getProfile(), HttpStatus.OK);
    }

    /**
     * 用户认证
     */
    @PostMapping("/auth")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> auth(@RequestBody ReqPlayerEdit request) {
        Player player = playerService.auth(request);
        return new ResponseEntity<>(new ResPlayerProfile(player), HttpStatus.OK);
    }

    /**
     * 访问首页
     */
    @PostMapping("/index")
    @PrePermissions
    public ResponseEntity<?> index() {
        playerService.index();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 解锁成就
     */
    @Deprecated
    @PostMapping("/release/achievement/{id}")
    @PrePermissions
    public ResponseEntity<Boolean> releaseAchievement(@PathVariable String id) {
        return new ResponseEntity<>(playerService.releaseAchievement(Integer.parseInt(id)), HttpStatus.OK);
    }
   

    /**
     * 查询成就
     */
    @GetMapping("/released/achievement/{gameId}")
    @PrePermissions
    public ResponseEntity<List<Achievement_Json>> getReleasedAchievements(@PathVariable String gameId) {
        return new ResponseEntity<>(playerService.getReleasedAchievements(Integer.parseInt(gameId)), HttpStatus.OK);
    }

    /**
     * 查询排行榜
     */
    @GetMapping("/rank")
    @PrePermissions
    public ResponseEntity<List<ResRankPlayer>> getRank() {
        return new ResponseEntity<>(playerService.getRank(), HttpStatus.OK);
    }

    /**
     * 扣除电量
     */
    @PostMapping("/costPower")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> costPower() {
        return new ResponseEntity<>(playerService.costPower(), HttpStatus.OK);
    }

    /**
     * 根据数量扣除电量
     */
    @PostMapping("/costPowerByNum")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> costPowerByNum(@RequestBody Map<String, Integer> param) {
        return new ResponseEntity<>(playerService.costPower(param.get("power")), HttpStatus.OK);
    }

    /**
     * 设置电量
     */
    @PostMapping("/setPower")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> setPower(@RequestBody Map<String, Integer> param) {
        return new ResponseEntity<>(playerService.setPower(param.get("power")), HttpStatus.OK);
    }

    /**
     * 分享成功
     */
    @PostMapping("/shared")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> shared() {
        return new ResponseEntity<>(playerService.onShared(), HttpStatus.OK);
    }

    /**
     * 领取分享
     */
    @PostMapping("/takeShare")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> takeShare() {
        return new ResponseEntity<>(playerService.takeShare(), HttpStatus.OK);
    }

    /**
     * 领取消耗20体力
     */
    @PostMapping("/takeCostPower20")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> takeCostPower20() {
        return new ResponseEntity<>(playerService.takeCostPower20(), HttpStatus.OK);
    }

    /**
     * 领取消耗100体力
     */
    @PostMapping("/takeCostPower100")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> takeCostPower100() {
        return new ResponseEntity<>(playerService.takeCostPower100(), HttpStatus.OK);
    }

    /**
     * 查看探索度
     */
    @GetMapping("/exploration")
    @PrePermissions
    public ResponseEntity<List<ResRankPlayer>> getExploration() {
        return new ResponseEntity<>(playerService.getExploration(), HttpStatus.OK);
    }

    /**
     * 绑定微信
     */
    @PostMapping("/bindWechat")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> bindWechat(@RequestBody ReqPlayerWechatLogin request) {
        return new ResponseEntity<>(playerService.bindWechat(request), HttpStatus.OK);
    }

    /**
     * 绑定QQ
     */
    @PostMapping("/bindTencent")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> bindTencent(@RequestBody ReqPlayerTencentLogin request) {
        return new ResponseEntity<>(playerService.bindTencent(request), HttpStatus.OK);
    }
    /**
     * 绑定QQ
     */
    @PostMapping("/bindTencent2")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> bindTencent2(@RequestBody ReqPlayerTencentLogin request) {
        return new ResponseEntity<>(playerService.bindTencent2(request), HttpStatus.OK);
    }

    /**
     * 消耗体力存档
     */
    @PostMapping("/saveRecord/costPower")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> saveRecordCostPower() {
        return new ResponseEntity<>(playerService.saveRecordCostPower(), HttpStatus.OK);
    }

    /**
     * 扣除电量
     */
    @PostMapping("/costPower/{power}")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> costPower(@PathVariable String power) {
        return new ResponseEntity<>(playerService.costPower(Integer.parseInt(power)), HttpStatus.OK);
    }

    /**
     * 完成查看广告
     */
    @Deprecated
    @PostMapping("/watchAd")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> watchAd() {
        return new ResponseEntity<>(playerService.watchAd(), HttpStatus.OK);
    }

    /**
     * 领取查看广告
     */
    @Deprecated
    @PostMapping("/takeWatchAd")
    @PrePermissions
    public ResponseEntity<ResPlayerProfile> takeWatchAd() {
        return new ResponseEntity<>(playerService.takeWatchAd(), HttpStatus.OK);
    }
    
    /**
	 * 查询并更新玩家玩过的剧本总数
	 */
	@GetMapping("/getPlayerNumAlsoRecord")
	@PrePermissions
	public ResponseEntity<?> getPlayerNumAlsoRecord() {
		return new ResponseEntity<>(playerService.getPlayerNumAlsoRecord(), HttpStatus.OK);
	}
	
	/**
	 * 查询指定玩家货币数量
	 */
	@GetMapping("/getCurrencyNum")
	@PrePermissions
	public ResponseEntity<?> getCurrencyNum() {
		return new ResponseEntity<>(playerService.getCurrencyNum(), HttpStatus.OK);
	}
	
	/**
	 * 查询玩家电量额外上限值
	 */
	@GetMapping("/getPowerLimit")
	@PrePermissions
	public ResponseEntity<?> getPowerLimit(){
		return new ResponseEntity<>(playerService.getPowerLimit(), HttpStatus.OK);
	}
	
	/**
	 * 查询当前玩家电量
	 */
	@GetMapping("/getPower")
	@PrePermissions
	public ResponseEntity<?> getPower(){
		return new ResponseEntity<>(playerService.getPower(),HttpStatus.OK);
	}
}
