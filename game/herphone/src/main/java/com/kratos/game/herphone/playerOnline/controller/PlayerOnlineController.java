package com.kratos.game.herphone.playerOnline.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;

@RestController
@RequestMapping("/Time")
@PrePermissions
public class PlayerOnlineController extends BaseController {

	/**
	 * 更新玩家在线时长和检测玩家消息状态
	 * 
	 * @return 返回玩家消息状态和系统时间
	 */
	@GetMapping("/updateOnlineTime")
	@PrePermissions
	public ResponseEntity<?> updateOnlineTime() {
		return new ResponseEntity<>(playerOnlineService.update(), HttpStatus.OK);
	}

}
