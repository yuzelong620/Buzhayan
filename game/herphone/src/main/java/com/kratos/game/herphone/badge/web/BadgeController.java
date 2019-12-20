package com.kratos.game.herphone.badge.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;


@RequestMapping("/badge")
@PrePermissions
@RestController 
public class BadgeController extends BaseController{
	/**获取徽章获得进度*/
	@GetMapping("/BadgeProgress/{tagId}")
	@PrePermissions
	public ResponseEntity<?> BadgeProgress(@PathVariable String tagId) {		
		return new ResponseEntity<>(badgeService.BadgeProgress(Integer.valueOf(tagId)),HttpStatus.OK);
	}
	/**领取解锁徽章奖励*/
	@GetMapping("/receiveTagReward/{tagId}")
	@PrePermissions
	public ResponseEntity<?> receiveTagReward(@PathVariable String tagId) {		
		badgeService.receiveTagReward(Integer.valueOf(tagId));
		return new ResponseEntity<>(HttpStatus.OK);
	}
	/**成就徽章领取状态*/
	@GetMapping("/receiveState")
	@PrePermissions
	public ResponseEntity<?> receiveState() {		
		return new ResponseEntity<>(badgeService.receiveState(),HttpStatus.OK);
	}
}
