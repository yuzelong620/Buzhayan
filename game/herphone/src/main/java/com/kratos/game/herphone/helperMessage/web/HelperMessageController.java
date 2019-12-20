package com.kratos.game.herphone.helperMessage.web;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kratos.game.herphone.aop.PrePermissions;
import com.kratos.game.herphone.common.BaseController;
import com.kratos.game.herphone.helperMessage.bean.ReqOptionAnswer;

@RestController
@RequestMapping("/Helper")
@PrePermissions
public class HelperMessageController extends BaseController {
	
	/**
	 * 小罗助手推送
	 * @return 返回小罗助手消息
	 */
	@GetMapping("/getInformation")
	@PrePermissions
	public ResponseEntity<?> getInformation() {
		return  new ResponseEntity<>(helperMessageService.getInformation(),HttpStatus.OK);
	}

	/**
	 * 玩家视频观看完成回复小罗助手消息
	 */
	@PostMapping("/recordOption")
	@PrePermissions
	public ResponseEntity<?> recordOption(@RequestBody ReqOptionAnswer reqOptionAnswer) {
		return new ResponseEntity<>(helperMessageService.recordOption(reqOptionAnswer),HttpStatus.OK);
	}
}
