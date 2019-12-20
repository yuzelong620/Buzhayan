package com.kratos.game.herphone.playerDynamic.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.playerDynamic.bean.ReqPlayerDynamic;
import com.kratos.game.herphone.playerDynamic.bean.ResCompleteBoolean;
import com.kratos.game.herphone.playerDynamic.bean.ResGameOverBean;
import com.kratos.game.herphone.playerDynamic.bean.ResPlayerDynamic;
import com.kratos.game.herphone.playerDynamic.bean.ResTitleAchieved;
import com.kratos.game.herphone.playerDynamic.entity.PlayerDynamicEntity;

@RestController
@RequestMapping("/playerdynamic")
@PrePermissions
public class PlayerDynamicController extends BaseController{
	/**
	 * 获取自己资料
	 */
	@GetMapping("/myself")
	@PrePermissions
	  public ResponseEntity<ResPlayerDynamic> getMyself() {
        return new ResponseEntity<>(playerDynamicService.getPlayerDeByMySelf(),HttpStatus.OK);
    }
	/**
	 * 获取别人资料
	 */
	@GetMapping("/{pid}")
	@PrePermissions
	  public ResponseEntity<ResPlayerDynamic> getById(@PathVariable String pid) {
		long playerId = Long.valueOf(pid);
        return new ResponseEntity<>(playerDynamicService.getPlayerDeById(playerId),HttpStatus.OK);
    }
	/**
	 * 修改个人资料
	 */
	@PostMapping("/updatePlayerDynamic")
	@PrePermissions
	  public ResponseEntity<PlayerDynamicEntity> updatePlayerDynamic(@RequestBody ReqPlayerDynamic request) {
        return new ResponseEntity<>(playerDynamicService.updatePlayerDynamic(request),HttpStatus.OK);
    }
	/**
	 * 佩戴成就徽章
	 */
	@GetMapping("/wearAchievementBadges/{achievementBadgeId}")
	@PrePermissions
	  public ResponseEntity<PlayerDynamicEntity> wearAchievementBadges(@PathVariable String achievementBadgeId) {
		playerDynamicService.wearAchievementBadges(Integer.valueOf(achievementBadgeId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
	/**
	 * 玩家升级
	 */
	@GetMapping("/upgradeLevel")
	@PrePermissions
	 public ResponseEntity<?> upgradeLevel() {
		playerDynamicService.upgradeLevel();
        return new ResponseEntity<>(HttpStatus.OK);
    }
	/**
	 * 佩戴头像框
	 */
	@GetMapping("/wearAvatarFrame/{avatarFrameId}")
	@PrePermissions
	  public ResponseEntity<PlayerDynamicEntity> wearAvatarFrame(@PathVariable String avatarFrameId) {
		playerDynamicService.wearAvatarFrame(Integer.valueOf(avatarFrameId));
        return new ResponseEntity<>(HttpStatus.OK);
    }
	/**
	 * 获取玩家是否达成护眼大队称号
	 * 
	 */
	@Deprecated
	@GetMapping("/getTitleAchieved")
	@PrePermissions
	  public ResponseEntity<ResTitleAchieved> getTitleAchieved() {	
        return new ResponseEntity<>(playerDynamicService.getTitleAchieved(),HttpStatus.OK);
    }
	/**
	 * 查看玩家是否可以申请护眼大队
	 * 
	 */
	@GetMapping("/getApplyStatusByBrigade")
	@PrePermissions
	  public ResponseEntity<ResCompleteBoolean> getApplyStatusByBrigade() {	
        return new ResponseEntity<>(playerDynamicService.getApplyStatusByBrigade(),HttpStatus.OK);
   }
	/**
	 * 根据角色Id搜索玩家
	 * 
	 */
	@GetMapping("/getPlayerDynamicEntityByRoleId/{roleId}")
	@PrePermissions
	  public ResponseEntity<?> getPlayerDynamicEntityByRoleId(@PathVariable String roleId) {	
        return new ResponseEntity<>(playerDynamicService.getPlayerDynamicEntityByRoleId(roleId),HttpStatus.OK);
   }
	/**
	 * 查看玩家是否可以申请护眼先锋
	 * 
	 */
	@GetMapping("/getApplyStatusByPioneer")
	@PrePermissions
	  public ResponseEntity<ResCompleteBoolean> getApplyStatusByPioneer() {	
        return new ResponseEntity<>(playerDynamicService.getApplyStatusByPioneer(),HttpStatus.OK);
   }
	
	/**
	 * 玩家结束游戏，返回玩家游戏次数，玩家获得奖励
	 */
	@GetMapping("/gameOver")
	@PrePermissions
	 public ResponseEntity<?> getGameOver(){
		playerDynamicService.playGameOver();
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
